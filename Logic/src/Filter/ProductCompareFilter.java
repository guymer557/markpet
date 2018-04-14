package Filter;

import Products.ProductCompare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davej on 8/10/2017.
 */
public class ProductCompareFilter{
    private String AnimalCategory;
    private String Category;
    private String searchWord;
    private Float lowPrice;
    private Float highPrice;
    private String Price;
    private String Company;
    private Integer Shops;

//    private List<ProductCompare> m_FilteredProductCompareList = new ArrayList<>();

    public ProductCompareFilter(){}
    public ProductCompareFilter(String i_Company, String i_SearchWord, Float i_lowPrice, Float i_highPrice, Integer i_Shops, String i_Category, String i_AnimalCategory){
        this.Company = i_Company;
        this.searchWord = i_SearchWord;
        this.lowPrice = i_lowPrice;
        this.highPrice = i_highPrice;
        this.Shops = i_Shops;
        this.Category = i_Category;
        this.AnimalCategory = i_AnimalCategory;
    }
    public String getSearchWord() {
        return searchWord;
    }

    public Float getLowPrice() {
        return lowPrice;
    }

    public Float getHighPrice() {
        return highPrice;
    }

    public String getCompany() {
        return Company;
    }

    public Integer getShops() {
        return Shops;
    }

    public String getCategory() {
        return Category;
    }

    public String getAnimalCategory() {
        return AnimalCategory;
    }

    public String getPrice() {
        return Price;
    }

//    public void setFilteredProductCompareList(List<ProductCompare> m_FilteredProductCompareList) {
//            this.m_FilteredProductCompareList = m_FilteredProductCompareList;
//    }
}
