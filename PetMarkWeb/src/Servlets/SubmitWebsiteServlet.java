package Servlets;

import DataBase.ObjectDB;
import DataExtraction.ExtractProductsV2;
import DataExtraction.ExtractProgressInfo;
import DataExtraction.ExtractionInfo;
import Products.Product;
import Products.ProductModel;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by davej on 4/14/2017.
 */
@WebServlet(name = "SubmitWebsiteServlet", urlPatterns = "/SubmitWebsiteServlet")
public class SubmitWebsiteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectDB objectDB = ObjectDB.getInstance();
        objectDB.setEntityManager(getServletContext());
        String action = request.getParameter("action");
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        switch (action){
            case "SubmitWebsiteServlet":
                ExtractionInfo requestParameters = new ExtractionInfo(request);
                ExtractProductsV2 extractProducts = new ExtractProductsV2(requestParameters);
                List<Product> listOfProducts = null;
                try {
                    listOfProducts = extractProducts.ScanSiteForProducts();
                    request.getSession(true).setAttribute("submitWebsiteProducts", listOfProducts);
                    this.getServletConfig().getServletContext().setAttribute("submitWebsiteProducts", listOfProducts);
                }
                catch(Exception e){
                    out.print(gson.toJson(new SubmitWebsiteStatus(e.getMessage())));
                    return;
                }

                out.print(gson.toJson(new SubmitWebsiteStatus(listOfProducts,extractProducts.GetErrorMessagesBuffer(),extractProducts.GetStringsOfProducts())));
                break;
            case "UpdateProgressBar":
                int currentValueOfProgressBar =(int)ExtractProgressInfo.GetTotalPercents();
                out.print(gson.toJson(currentValueOfProgressBar));
                break;
            case "SubmitProductModel":
                String englishName = request.getParameter("englishName");
                String hebrewName = request.getParameter("hebrewName");
                String Company = request.getParameter("Company");
                String Image = request.getParameter("Image");
                String Category = request.getParameter("Category");
                String AnimalCategory = request.getParameter("AnimalCategory");
                try {
                    objectDB.CreateEntity(new ProductModel(englishName, hebrewName, Company, Image, Category, AnimalCategory));
                    List<ProductModel> mylist = objectDB.getEntityManager().createQuery("select p from ProductModel p", ProductModel.class).getResultList();
                } finally {
                    objectDB.CloseConnection();
                    /////////////////////////////////////////
                }
                break;
        }
    }
}
