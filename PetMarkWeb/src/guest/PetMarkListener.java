package guest; /**
 * Created by davej on 4/12/2017.
 */

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener()
public class PetMarkListener implements ServletContextListener {

    // Prepare the EntityManagerFactory & Enhance:
    public void contextInitialized(ServletContextEvent e) {
//        com.objectdb.Enhancer.enhance("guest.*");
//        com.objectdb.Enhancer.enhance("PetMarkProject.Logic.websites.WebSite");
        EntityManagerFactory emf =
//                Persistence.createEntityManagerFactory("$objectdb/db/PetMark.odb");
                Persistence.createEntityManagerFactory("$objectdb/db/MarkPet2.odb");
        e.getServletContext().setAttribute("emf", emf);
    }

    // Release the EntityManagerFactory:
    public void contextDestroyed(ServletContextEvent e) {
        EntityManagerFactory emf =
                (EntityManagerFactory)e.getServletContext().getAttribute("emf");
        emf.close();
    }
}