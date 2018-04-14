package Products;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by davej on 7/2/2017.
 */
@Entity
public class Product implements Serializable{
        private static final long serialVersionUID = 1L;

        // Persistent Fields:
        @Id
        @GeneratedValue
        Long id;
        private String m_URL;
        private String m_Name;
        private Float m_Price;
        private String m_Shop;
        private String m_Image;
        private String m_Category;
        private String m_AnimalCategory;
        private String Logo;
        private Float  Shipment;


    public Long getID(){
        return id;
    }
    public Product(String i_Url, String i_Name, Float i_Price, String i_Shop, String i_Image){
        this.m_URL = i_Url;
        this.m_Name = i_Name;
        this.m_Price = i_Price;
        this.m_Shop = i_Shop;
        this.m_Image = i_Image;
    }

    public Product(String i_Url, String i_Name, Float i_Price, String i_Shop, String i_Image, String i_Category, String i_AnimalCategory){
        this.m_URL = i_Url;
        this.m_Name = i_Name;
        this.m_Price = i_Price;
        this.m_Shop = i_Shop;
        this.m_Image = i_Image;
        this.m_Category = i_Category;
        this.m_AnimalCategory = i_AnimalCategory;
    }

    public Float getPrice(){
        return m_Price;
    }
    public String getImage(){
        return m_Image;
    }
    public String getName(){
        return m_Name;
    }

    public String getShop() {
        return m_Shop;
    }

    public void setShipment(float shipment) {
        this.Shipment = shipment;
    }

    public void setLogo(String logo) {
        this.Logo = logo;
    }

    public String getCategory() {
        return m_Category;
    }

    public String getAnimalCategory() {
        return m_AnimalCategory;
    }
}
