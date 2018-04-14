package guest;

import DataBase.ObjectDB;
import com.google.gson.Gson;
import websites.WebSite;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.objectdb.o.MVL.p;

/**
 * Created by davej on 4/12/2017.
 */
@WebServlet(name = "GuestServlet", urlPatterns = "/GuestServlet")
public class GuestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ObjectDB objectDB = ObjectDB.getInstance();
        objectDB.setEntityManager(getServletContext());
        try {
            // Handle a new guest (if any):
            String name = request.getParameter("name");
            if (name != null) {
                objectDB.CreateEntity(new Guest(name));
            }
            List<Guest> guestList = objectDB.getEntityManager().createQuery("SELECT g FROM Guest g", Guest.class).getResultList();
        } finally {
            // Close the database connection:
            objectDB.CloseConnection();
            /////////////////////////////////////////
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
