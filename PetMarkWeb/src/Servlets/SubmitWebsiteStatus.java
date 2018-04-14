package Servlets;

import Products.Product;

import java.util.List;

/**
 * Created by Guy on 27/08/2017.
 */
public class SubmitWebsiteStatus {

    private List<Product> m_ListOfProducts;
    private String m_ErrorMessages;
    private int m_NumOfProducts = 0;
    private List<String> m_StringsOfProducts;
    private String m_ExceptionMsg;
    private boolean m_isExceptionOccurred = false;

    public SubmitWebsiteStatus(List<Product> i_ListOfProducts, String i_ErrorMessages, List<String> i_StringOfProducts){
        this.m_ListOfProducts = i_ListOfProducts;
        this.m_ErrorMessages = i_ErrorMessages;
        if(i_ListOfProducts != null){
            this.m_NumOfProducts = i_ListOfProducts.size();
            this.m_StringsOfProducts = i_StringOfProducts;
        }
    }

    public SubmitWebsiteStatus(String i_Exception){
        this.m_ExceptionMsg = i_Exception;
        this.m_isExceptionOccurred = true;
    }
}
