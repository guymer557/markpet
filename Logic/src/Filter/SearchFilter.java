package Filter;

import Products.Product;
import Products.ProductCompare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davej on 7/3/2017.
 */
public class SearchFilter {

    private static SearchFilter myInstance;
    private String m_searchWord; // first check
    private String m_AnimalCategory; // second check
    private String m_Category; // third check
    private String m_Price;
//    private List<Product> m_ResultProductsList = new ArrayList<>();
//    boolean foundResults = false;

    public SearchFilter(){}
    public SearchFilter(String searchWord, String animalCategory, String Category, String Price){
        m_searchWord = searchWord;
        m_AnimalCategory = animalCategory;
        m_Category = Category;
        m_Price = Price;
    }
    public static SearchFilter getInstance() {
        if(myInstance == null) {
            myInstance = new SearchFilter(null, null, null, null) ;
        }
        return myInstance;
    }

    public void setPrice(String i_Price) {
        this.m_Price = i_Price;
    }

    public void setAnimalCategory(String i_AnimalCategory) {
        this.m_AnimalCategory = i_AnimalCategory;
    }

    public String getSearchWord(){
        return m_searchWord;
    }

    public void set_searchWord(String i_searchWord) {
        this.m_searchWord = i_searchWord;
    }

    public void setCategory(String i_Category) {
        this.m_Category = i_Category;
    }

//    public void setResultProductsList(List<Product> i_List){
//        m_ResultProductsList = i_List;
//    }
//    public void setResultProductsCompareList(List<ProductCompare> i_List){
//        m_ResultProductsCompareList = i_List;
//    }


    public String getAnimalCategory() {
        return this.m_AnimalCategory;
    }

    public String getCategory(){
        return m_Category;
    }

    public String getPrice() {
        return this.m_Price;
    }
}