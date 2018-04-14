package Utils;

import DataBase.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by davej on 8/10/2017.
 */
public class SessionUtils {

    public static boolean hasSession(HttpServletRequest request) {
        return request.getSession(false) != null;
    }

    public static HttpSession getSession(HttpServletRequest request){
        return request.getSession(true);
    }


    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("User") != null;
    }

    public static User getUser(HttpServletRequest request){
        return (User) request.getSession(true).getAttribute("User");
    }

    public static void loginUser(HttpSession session, User user) {
        session.setAttribute("User", user);
    }

//    public static String getUsername (HttpServletRequest request) {
//
//        HttpSession session = request.getSession(false);
//        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
//        return sessionAttribute != null ? sessionAttribute.toString() : null;
//    }

    public static boolean isComputer(HttpSession session) {
        return ((Boolean)session.getAttribute("isComputer")).booleanValue();
    }

    public static void logoutUser(HttpSession session) {
        session.invalidate();
    }
    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static void setUser(HttpServletRequest request, User user) {
        request.getSession(true).setAttribute("User", user);
    }
}

