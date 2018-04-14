package Products;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import javax.print.attribute.standard.MediaSize;
import java.io.Serializable;

/**
 * Created by davej on 8/8/2017.
 */
@Entity
public class ProductCompare implements Serializable{
    private static final long serialVersionUID = 1L;

    // Persistent Fields:
    @Id
    @GeneratedValue
    Long id;
    private String Name;
    private Float lowPrice;
    private Float highPrice;
    private String Company;
    private Integer Shops;
    private String Image;
    private String Category;
    private String AnimalCategory;
    private String productsQuery;

    public ProductCompare(){}
    public ProductCompare(String i_Company, String i_Name, Float i_lowPrice, Float i_highPrice, Integer i_Shops, String i_Image, String i_Category, String i_AnimalCategory, String i_productsQuery){
        this.Company = i_Company;
        this.Name = i_Name;
        this.lowPrice = i_lowPrice;
        this.highPrice = i_highPrice;
        this.Shops = i_Shops;
        this.Image = i_Image;
        this.Category = i_Category;
        this.AnimalCategory = i_AnimalCategory;
        this.productsQuery = i_productsQuery;
    }

    public String getName() {
        return Name;
    }

    public String getQuery() {
        return productsQuery;
    }
}