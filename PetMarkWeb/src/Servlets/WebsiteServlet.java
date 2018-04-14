package Servlets;

import DataBase.ObjectDB;
import com.google.gson.Gson;
import guest.Guest;
import websites.WebSite;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by davej on 4/15/2017.
 */
@WebServlet(name = "WebsiteServlet", urlPatterns = "/WebsiteServlet")
public class WebsiteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();

        ObjectDB objectDB = ObjectDB.getInstance();
        objectDB.setEntityManager(getServletContext());
        String Action = request.getParameter("action");

        try {
            switch (Action){
                case "websitesList":
                    List<WebSite> webList = objectDB.getEntityManager().createQuery("SELECT w FROM WebSite w", WebSite.class).getResultList();
                    out.println(gson.toJson(webList));
                    break;
            }

//            }
//
        } finally {
            objectDB.CloseConnection();
        }


    }
}
