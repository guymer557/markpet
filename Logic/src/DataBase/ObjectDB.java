package DataBase;

import Comments.Comment;
import Products.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by davej on 4/15/2017.
 */
public class ObjectDB {

    private static final String ENTITIY_MANAGER_FACTORY_ATTRIBUTE_NAME = "emf";
    private static ObjectDB myInstance;
    private EntityManager entityManager;
    private ObjectDB() {
    }

    public List<Product> getProductByID(Long id){
        Query query = ObjectDB.getInstance().getEntityManager().createQuery("SELECT p FROM ProductCompare p WHERE p.id in :ids");
        List<Long> ids = new LinkedList<Long>();
        ids.add(id);
        query.setParameter("ids", ids);
        List<Product> resultList = query.getResultList();
        return resultList;
    }

    public List<Comment> getCommentsByID(Long id){
        Query query = ObjectDB.getInstance().getEntityManager().createQuery("SELECT p FROM Comment p WHERE p.refID in :ids");
        List<Long> ids = new LinkedList<Long>();
        ids.add(id);
        query.setParameter("ids", ids);
        List<Comment> resultList = query.getResultList();
        return resultList;
    }



    public static ObjectDB getInstance() {
        if(myInstance == null) {
            myInstance = new ObjectDB() ;
        }
        return myInstance;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(ServletContext servletContext){
        // Obtain a database connection:
        EntityManagerFactory emf =
                (EntityManagerFactory)servletContext.getAttribute(ENTITIY_MANAGER_FACTORY_ATTRIBUTE_NAME);
        //Create entity manager to manage transaction
        this.entityManager = emf.createEntityManager();

    }

    public void CreateEntity(Object... newObjects) {
        //Operations that modify database content,such as store, update, and delete
        //should only be performed within an active transaction.
        entityManager.getTransaction().begin();
        //Making new Objects
        for (Object obj : newObjects){
            entityManager.persist(obj);
        }
        //Database updates are collected and managed in memory
        //and applied to the database when the transaction is committed:
        entityManager.getTransaction().commit();
    }

    public void CreateEntities(List<?> newObjects) {
        //Operations that modify database content,such as store, update, and delete
        //should only be performed within an active transaction.
        entityManager.getTransaction().begin();
        //Making new Objects
        for (Object obj : newObjects){
            entityManager.persist(obj);
        }
        //Database updates are collected and managed in memory
        //and applied to the database when the transaction is committed:
        entityManager.getTransaction().commit();
    }

    public int DeleteEntities(Query query){
        entityManager.getTransaction().begin();
        int deletedCount = query.executeUpdate();
        entityManager.getTransaction().commit();
        return deletedCount;
    }

    public Query CreateQuery(String query){
        return entityManager.createQuery(query);
    }

    public void CloseConnection() {
        if (entityManager.getTransaction().isActive())
            entityManager.getTransaction().rollback();
        entityManager.close();
    }

    //insert % before each word in string str and return it
    public static String createStringTemplate(String str){
        String strNew = str.concat("%\' "); // insert '%' to the end of the string
        strNew = " \'%" + strNew; // insert '%' to the beginning of the string
        return strNew;


    }
    //insert % before each word in string str and return it
    public static String createParameterTemplate(String str){
        String strNew = str.concat("%"); // insert '%' to the end of the string
        strNew = '%' + strNew; // insert '%' to the beginning of the string
        return strNew;
    }
}
