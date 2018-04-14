package Servlets;

import Comments.Comment;
import DataBase.ObjectDB;
import DataBase.User;
import DataExtraction.AmazonExtraction;
import Filter.ProductCompareFilter;
import Filter.SearchFilter;
import Products.Product;
import Products.ProductCompare;
import Utils.SessionUtils;
import com.ECS.client.jax.Item;
import com.google.gson.Gson;

import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;

/**
 * Created by davej on 7/3/2017.
 */
@WebServlet(name = "SearchServlet", urlPatterns = "/SearchServlet")
public class SearchServlet extends HttpServlet {
    private static final Float PRICE_FROM = 200f;
    private static final Float PRICE_LIMIT = 50f;
    private static final Float PRICE_MAX = 10000f;

    ObjectDB objectDB = ObjectDB.getInstance();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action){
                case "updateSearchFilter":
                    updateSearchFilter(request);
                    break;
                case "updateProductCompareFilter":
                    updateProductCompareFilter(request);
                    break;
                case "addProductToCart":
                    addProductToCart(request);
                    break;
                case "deleteProductFromCart":
                    deleteProductToCart(request);
                    break;
                case "setCurrentProductCompare":
                    setCurrentProductCompare(request);
                    break;
                case "addEbayItems":
                    addEbayItems(request);
                case "initAmazonStores":
                    initAmazonStores(request);
                    break;


            }
        } finally {
//            objectDB.CloseConnection();
        }
    }




    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        SearchFilter searchFilter = new SearchFilter();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        objectDB.setEntityManager(getServletContext());
        String action = request.getParameter("action");
        List<Product> prodList = new ArrayList<>();
        String Query;
        try {
            switch (action){

                case "initStores":
                    initStores(request, response.getWriter(), gson);
                    break;

                case "getAbroadStores":
                    getAbroadStores(request, response.getWriter(), gson);
                    break;

                case "searchByQuery":

                    String queryStr = new String(request.getParameter("queryStr").getBytes("iso-8859-1"),"UTF-8");

//                    TypedQuery<Product> query2 = objectDB.getEntityManager().createQuery(queryStr , Product.class);
//                    query2.setParameter("AnimalCategory", AnimalCategory);
//                    query2.setParameter("Category", Category);
//                    query2.setParameter("productModelenglishName", productModelenglishNameTemplate);
//                    query2.setParameter("productModelhebrewName", productModelhebrewNameTemplate);
                    try{
                        prodList = objectDB.getEntityManager().createQuery(queryStr, Product.class).getResultList();
                        request.getSession(true).setAttribute("SFilterProducts", prodList);
//                        searchFilter.setResultProductsList(prodList);
                        out.println(gson.toJson(prodList));
                    }catch (Exception e){
                        out.println(gson.toJson(new String(e.getMessage())));
                    }

                    break;
                case "getProductCompareFilter":
                    ProductCompareFilter PCF = ((ProductCompareFilter)SessionUtils.getSession(request).getAttribute("ProductCompareFilter"));
                    if (PCF == null){
                        PCF = new ProductCompareFilter();
                        request.getSession(true).setAttribute("ProductCompareFilter", PCF);
                    }
                    List<ProductCompare> productCompareList = getProductCompareList(PCF);
                    request.getSession(true).setAttribute("PCFilterProducts", productCompareList);
//                    PCF.setFilteredProductCompareList(productCompareList);
                    out.println(gson.toJson(PCF));
                    break;
                case "getSearchFilter":
                    SearchFilter SF = ((SearchFilter) SessionUtils.getSession(request).getAttribute("SearchFilter"));
                    if (SF == null){
                        SF = new SearchFilter();
                        request.getSession(true).setAttribute("SearchFilter", SF);
                    }
                    List<Product> productList = getProductList(SF);
                    request.getSession(true).setAttribute("SFilterProducts", productList);
//                    SF.setResultProductsList(productList);
                    out.println(gson.toJson(SF));
                    break;
                case "getSFilterProducts":
                    List<Product> SFilterProducts = (( List<Product>) SessionUtils.getSession(request).getAttribute("SFilterProducts"));
                    out.println(gson.toJson(SFilterProducts));
                    break;
                case "getPCFilterProducts":
                    List<ProductCompare> PCFilterProducts = (( List<ProductCompare>) SessionUtils.getSession(request).getAttribute("PCFilterProducts"));
//                    AmazonExtraction AE = new AmazonExtraction();
//                    List<Item> amazonList = AE.SearchItems();
                    out.println(gson.toJson(PCFilterProducts));
                    break;

                case "getCurrentProductCompare":
                    ProductCompare product = (( ProductCompare) SessionUtils.getSession(request).getAttribute("CurrentProductCompare"));
                    out.println(gson.toJson(product));
                    break;

                case "getCartProducts":{
                    List<Product> cartProducts = (List<Product>) request.getSession(true).getAttribute("cartProducts");
                    if (cartProducts == null){
                        cartProducts = new ArrayList<>();
                        request.getSession(true).setAttribute("cartProducts", cartProducts);
                    }
                    out.println(gson.toJson(cartProducts));
                    break;
                }
                case "getComments":
                    List<Comment> commentsList = getComments(request);
                    out.println(gson.toJson(commentsList));
                    break;
                case "getUser":
                    User user = SessionUtils.getUser(request);
                    out.println(gson.toJson(user));
                    break;

            }


        } finally {
//            objectDB.CloseConnection();

        }
    }

    private void getAbroadStores(HttpServletRequest request, PrintWriter out, Gson gson) {
       List<Product> abroadProducts = new ArrayList<>();
        List<Product> ebayProducts = (List<Product>) request.getSession(true).getAttribute("ebayProducts");
        List<Product> amazonProducts = (List<Product>) request.getSession(true).getAttribute("amazonProducts");
        if (ebayProducts != null){
            abroadProducts.addAll(ebayProducts);
        }
        if (amazonProducts != null){
            abroadProducts.addAll(amazonProducts);
        }
        out.println(gson.toJson(abroadProducts));
    }

    private void initAmazonStores(HttpServletRequest request) {
        ProductCompare currentProductCompare =  (ProductCompare) request.getSession().getAttribute("CurrentProductCompare");
        AmazonExtraction AE = new AmazonExtraction();

        try{
            List<Product> amazonList = AE.SearchItems(currentProductCompare.getName());
            request.getSession(true).setAttribute("amazonProducts", amazonList);
//            out.println(gson.toJson(amazonList));
        }catch (Exception e){
//            out.println(gson.toJson(new String(e.getMessage())));
        }

    }

    private void initStores(HttpServletRequest request, PrintWriter out, Gson gson) {
        ProductCompare currentProductCompare =  (ProductCompare) request.getSession().getAttribute("CurrentProductCompare");
        String queryStr = currentProductCompare.getQuery();
        try{
           List<Product> prodList = objectDB.getEntityManager().createQuery(queryStr, Product.class).getResultList();
            request.getSession(true).setAttribute("SFilterProducts", prodList);
//                        searchFilter.setResultProductsList(prodList);
            out.println(gson.toJson(prodList));
        }catch (Exception e){
            out.println(gson.toJson(new String(e.getMessage())));
        }

    }


    private List<Product> getProductList(SearchFilter SF) {
        try{
            String searchWord = SF.getSearchWord() != null ? SF.getSearchWord() : "";
            String AnimalCategory = SF.getAnimalCategory() != null ? SF.getAnimalCategory() : "";
            String Category = SF.getCategory() != null ? SF.getCategory() : "";
            Float[] prices = getLowHighPrices(SF.getPrice());
            Float lowPrice = prices[0];
            Float highPrice = prices[1];
            TypedQuery<Product> query = objectDB.getEntityManager().createQuery(
                    "SELECT p FROM Product p WHERE LOWER(p.m_Name) LIKE :searchWord AND p.m_AnimalCategory LIKE :AnimalCategory AND p.m_Category LIKE :Category AND p.m_Price >= :lowPrice AND p.m_Price <= :highPrice  ORDER BY p.m_Price", Product.class);
            query.setParameter("searchWord", "%" + searchWord + "%");
            query.setParameter("AnimalCategory", "%" + AnimalCategory + "%");
            query.setParameter("Category", "%" + Category + "%");
            query.setParameter("lowPrice", lowPrice);
            query.setParameter("highPrice", highPrice);
            List<Product> productList = query.getResultList();
            return productList;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Float[] getLowHighPrices(String Price) {
        LinkedList<String> numbers = new LinkedList<String>();
        Float[] prices = new Float[2];
        if (Price != null){
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(Price);
            while (m.find()) {
                numbers.add(m.group());
            }
            if (numbers.size() == 1){
                float price = numbers.get(0) != null ? parseFloat(numbers.get(0)) : 0f;
                if (price == PRICE_FROM){
                    prices[0] = price;
                    prices[1] = PRICE_MAX;
                }
                else{
                    if (price == PRICE_LIMIT){
                        prices[0] = 0f;
                        prices[1] = price;
                    }
                }
            }
            else
            {
                prices[0] = numbers.get(1) != null ? parseFloat(numbers.get(1)) : 0f;
                prices[1] = numbers.get(0) != null ? parseFloat(numbers.get(0)) : PRICE_MAX;
            }

        }
        else{
            prices[0] = 0f;
            prices[1] = PRICE_MAX;

        }
        return prices;
    }

    private List<ProductCompare> getProductCompareList(ProductCompareFilter PCF) {
        try{
            String searchWord = PCF.getSearchWord() != null ? PCF.getSearchWord() : "";
            String AnimalCategory = PCF.getAnimalCategory() != null ? PCF.getAnimalCategory() : "";
            String Category = PCF.getCategory() != null ? PCF.getCategory() : "";
            String Company = PCF.getCompany() != null ? PCF.getCompany() : "";
            Float[] prices = getLowHighPrices(PCF.getPrice());
            Float lowPrice = prices[0];
            Float highPrice = prices[1];
           TypedQuery<ProductCompare> query = objectDB.getEntityManager().createQuery(
                    "SELECT p FROM ProductCompare p WHERE  p.AnimalCategory LIKE :AnimalCategory AND p.Category LIKE :Category and p.Company like :Company and LOWER(p.Name) like :searchWord and p.lowPrice >= :lowPrice and p.highPrice <= :highPrice ORDER BY p.lowPrice", ProductCompare.class);
           query.setParameter("searchWord", "%" + searchWord + "%");
           query.setParameter("AnimalCategory", "%" + AnimalCategory + "%");
           query.setParameter("Category", "%" + Category + "%");
           query.setParameter("Company", "%" + Company + "%");
           query.setParameter("lowPrice", lowPrice);
           query.setParameter("highPrice", highPrice);
           List<ProductCompare> productCompareList = query.getResultList();
            return productCompareList;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void addProductToCart(HttpServletRequest request) {
        String product = request.getParameter("product");
        Gson gson = new Gson();
        try{
            Product p = gson.fromJson(product, Product.class);
            List<Product> cartProducts = (List<Product>) request.getSession(true).getAttribute("cartProducts");
            cartProducts.add(p);
        }catch (Exception e){
            System.out.println("something wrong");
        }

    }

    private void deleteProductToCart(HttpServletRequest request) {
        String product = request.getParameter("product");
        Gson gson = new Gson();
        try{
            Product p = gson.fromJson(product, Product.class);
            List<Product> cartProducts = (List<Product>) request.getSession(true).getAttribute("cartProducts");
            Iterator<Product> iter = cartProducts.iterator();
            while (iter.hasNext()) {
                Product pItr = iter.next();
                if (pItr.getID().longValue() == p.getID().longValue()) {
                    iter.remove();
                }
            }
        }catch (Exception e){
            System.out.println("something wrong");
        }
    }

    private void updateSearchFilter(HttpServletRequest request) {
        String SFstringObj = request.getParameter("SF");
        Gson gson = new Gson();

        SearchFilter SF = gson.fromJson(SFstringObj, SearchFilter.class);
//        SearchFilter SF = new SearchFilter(searchWord, animalCategoryVal, categoryVal, priceVal);
        request.getSession(true).setAttribute("SearchFilter", SF);
    }

    private void addEbayItems(HttpServletRequest request) {
        String ebayItemsStr = request.getParameter("items");
        Gson gson = new Gson();
        List<Product> prodList = gson.fromJson(ebayItemsStr, ArrayList.class);
        request.getSession(true).setAttribute("ebayProducts", prodList);
    }

    private void updateProductCompareFilter(HttpServletRequest request) {
        String PCFstringObj = request.getParameter("PCF");
        Gson gson = new Gson();
        ProductCompareFilter PCF = gson.fromJson(PCFstringObj, ProductCompareFilter.class);
//        ProductCompareFilter PCF = new ProductCompareFilter(Company,searchWord,lowPrice ,highPrice ,Shops ,Category, AnimalCategory);
        request.getSession(true).setAttribute("ProductCompareFilter", PCF);
    }

    private void setCurrentProductCompare(HttpServletRequest request) {
        String currentProductCompare = request.getParameter("item");
        Gson gson = new Gson();
        ProductCompare product = gson.fromJson(currentProductCompare, ProductCompare.class);
        request.getSession(true).setAttribute("CurrentProductCompare", product);
    }
    private List<Comment> getComments(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        List<Comment> commentsList = objectDB.getCommentsByID(id);
        return commentsList;
    }



}
