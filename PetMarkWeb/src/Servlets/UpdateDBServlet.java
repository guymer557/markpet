package Servlets;

import Comments.Comment;
import DataBase.ObjectDB;
import DataBase.User;
import DataExtraction.ExtractProductsV2;
import Products.Product;
import Products.ProductCompare;
import Products.ProductModel;
import Utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 * Created by davej on 7/2/2017.
 */
@WebServlet(name = "UpdateDBServlet" , urlPatterns = "/updateDB")
public class UpdateDBServlet extends HttpServlet {
    Gson gson = new Gson();
    ObjectDB objectDB = ObjectDB.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        objectDB.setEntityManager(getServletContext());
        String Action = request.getParameter("action");

        try {
            switch (Action) {
                case"initProductModel":
                    initProductModel();
                    break;
                case "addUser":
                    signupUser(request);
                    out.println(gson.toJson(Boolean.TRUE));
                    break;
                case "checkUser":
                    User userChecked = searchUser(request);
                    if (userChecked != null){
                        request.getSession(true).setAttribute("User", userChecked);
                    }
                    out.println(gson.toJson(userChecked));
                    break;

                case "addComment":
                    addComment(request);
                    out.println(gson.toJson(new String("תגובתך התווספה")));
                    break;
                case "updateDB":
//                    String productsListSTR = request.getParameter("listOfProductToInsert");
//                    Type listType = new TypeToken<ArrayList<Product>>(){}.getType();
//
//                    List<Product> productsList = new Gson().fromJson(productsListSTR, listType);
//                    List<Product> productsList = (List<Product>) request.getAttribute("submitWebsiteProducts");
                    List<Product> productsList = (List<Product>) this.getServletConfig().getServletContext().getAttribute("submitWebsiteProducts");

                    String logo = request.getParameter("logo");
                    String shipment = request.getParameter("shipment");
                    try{
                        for (Product p : productsList){
                            p.setLogo(logo);
                            p.setShipment(Float.parseFloat(shipment));
                        }
                        objectDB.CreateEntities(productsList);
                    }catch (Exception e){

                    }

//                    ExtractProductsV2 exp = new ExtractProductsV2();
//                    List<Product> productsList = exp.ScanSiteForProducts();
//                    objectDB.CreateEntities(productsList);
                    List<Product> prodList = objectDB.getEntityManager().createQuery("SELECT p FROM Product", Product.class).getResultList();
                    break;
                case "updateProductCompare":
                    Query deleteProductCompareQuery = objectDB.getEntityManager().createQuery("DELETE FROM ProductCompare p WHERE p.AnimalCategory like :AnimalCategory and p.Category like :Category");
                    String animalCategory = request.getParameter("animalCategoryVal");
                    String category = request.getParameter("categoryVal");
                    String animalCategoryTemplate = ObjectDB.createStringTemplate(animalCategory);
                    String categoryTemplate = ObjectDB.createStringTemplate(category);

                    //delete all products compare which belongs to given category and animal category in request
                    deleteProductCompareQuery.setParameter("AnimalCategory", animalCategory );
                    deleteProductCompareQuery.setParameter("Category", category );
                    try{
                        int deletedCount = objectDB.DeleteEntities(deleteProductCompareQuery); //FOR DEBUG
                        System.out.println(deletedCount);

                    }catch(Exception e){
                        out.println(gson.toJson(e.getMessage()));
                    }

                    List<ProductModel> productModelList = objectDB.getEntityManager().createQuery("SELECT p FROM ProductModel p WHERE p.AnimalCategory LIKE " + animalCategoryTemplate + " AND p.Category LIKE " + categoryTemplate, ProductModel.class).getResultList();
                    for (ProductModel prodModel : productModelList) {

                        try {
                            String englishName = prodModel.getEnglishName().toLowerCase();
                            String hebrewName = prodModel.getHebrewName();
                            String englishNameTemplate = ObjectDB.createStringTemplate(englishName);
                            String hebrewNameTemplate = ObjectDB.createStringTemplate(hebrewName);

                            String query = "SELECT p FROM Product p WHERE p.m_AnimalCategory LIKE " + animalCategoryTemplate + " AND p.m_Category LIKE " + categoryTemplate +
                                    " AND (LOWER(p.m_Name) like " + englishNameTemplate+ " or p.m_Name like " + hebrewNameTemplate + ") ORDER BY p.m_Price";

                            List<Product> productList = objectDB.getEntityManager().createQuery(query, Product.class).getResultList();
                            if (!productList.isEmpty()) {
                                ProductCompare productCompare = createProductCompare(prodModel, productList, query);
                                objectDB.CreateEntity(productCompare);
                            }
                        } catch (Exception e) {
                            out.println(gson.toJson(e.getMessage()));
                        }
                    }
                    out.println(gson.toJson("success"));
                    break;
            }

        } finally {
//            objectDB.CloseConnection();
        }

    }

    private void initProductModel() {
        List<ProductModel> productModelList = new ArrayList<>();
        String englishName;
        String company = new String();
        HashSet<String> uniqueItems = new HashSet<>();

        try{
            TypedQuery<Product> query = objectDB.getEntityManager().createQuery(
                    "SELECT p FROM Product p WHERE p.m_Shop LIKE :shop", Product.class);
            query.setParameter("shop", "%" + "petpoint" + "%");
            List<Product> productList = query.getResultList();
            for (Product p : productList){
                ProductModel PM = new ProductModel();

                englishName = p.getName().replaceAll("[^a-zA-Z'\\s]","");
                String hebrewName = p.getName().replaceAll("[^\u0590-\u05fe\\s]","");
                hebrewName = hebrewName.trim();
                englishName = englishName.trim();
                if (englishName.length() > 0){
                    String arr[] = englishName.split(" ", 2);
                    company = arr[0];
                    if (company.equals("\'")){
                        englishName = arr[1];
                        englishName = englishName.trim();
                        String arr2[] = englishName.split(" ", 2);
                        company = arr2[0];
                    }
                    PM.setEnglishName(englishName);

                    System.out.println("company : " + company + "       name : " + englishName + "   origin: " + p.getName());
                }
                else{
                    PM.setEnglishName("no name");
                }
                if (hebrewName.length() > 0){
                    PM.setHebrewName(hebrewName);
                }
                else{
                    PM.setHebrewName("אין שם");
                }
                PM.setCompany(company);
                PM.setCategory(p.getCategory());
                PM.setAnimalCategory(p.getAnimalCategory());
                PM.setImage(p.getImage());

                if (!uniqueItems.contains(englishName)){
                    uniqueItems.add(englishName);
                    productModelList.add(PM);
                }

            }
            objectDB.CreateEntities(productModelList);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private User searchUser(HttpServletRequest request) {
        String user = request.getParameter("user");
        Gson gson = new Gson();
        User userObj = getUserFromDB(gson.fromJson(user, User.class));
        return userObj;
    }

    private void signupUser(HttpServletRequest request) {
        String user = request.getParameter("user");
        Gson gson = new Gson();
        User userObj = gson.fromJson(user, User.class);
        request.getSession(true).setAttribute("User", userObj);
        objectDB.CreateEntity(userObj);
        User userObj2 = SessionUtils.getUser(request);
        System.out.println("testing singup user");
    }

    private User getUserFromDB(User userObj) {
        TypedQuery<User> query = objectDB.getEntityManager().createQuery(
                "SELECT u FROM User u WHERE  u.email LIKE :email and u.password LIKE :password", User.class);
        query.setParameter("email", "%" + userObj.getEmail() + "%");
        query.setParameter("password", "%" + userObj.getPassword() + "%");
        List<User> userList = query.getResultList();
        if (userList.isEmpty()){
            return null; // user does not exist
        }
        return userList.get(0); // user exist
    }

    private void addComment(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        String msg = request.getParameter("comment");
        Comment commentToAdd = new Comment();
        commentToAdd.setComment(msg);
        commentToAdd.setDate(Date.valueOf(LocalDate.now()));
        commentToAdd.setRefID(id);

        commentToAdd.setUserName(SessionUtils.getUser(request).getName());
        objectDB.CreateEntity(commentToAdd);
    }


    private ProductCompare createProductCompare(ProductModel prodModel, List<Product> productsInProductModelList, String productsQuery) {


        HashSet uniqueShops = new HashSet();
        productsInProductModelList.forEach((p) -> uniqueShops.add(p.getShop()));
        uniqueShops.size();

//        String Name = productsInProductModelList.get(0).getName();
        String Name = prodModel.getEnglishName();
        Float lowPrice = productsInProductModelList.get(0).getPrice();

        Integer Shops = uniqueShops.size();
        Float highPrice = productsInProductModelList.get(productsInProductModelList.size() - 1).getPrice();
        String Company = prodModel.getCompany();
        String Image = productsInProductModelList.get(0).getImage();
        String Category = prodModel.getCategory();
        String AnimalCategory = prodModel.getAnimalCategory();
        String Query = productsQuery;

        ProductCompare prodCompare = new ProductCompare(Company, Name, lowPrice, highPrice, Shops, Image, Category, AnimalCategory, Query);
        return prodCompare;
    }


}


