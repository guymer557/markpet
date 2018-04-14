package Products;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by davej on 8/8/2017.
 */
@Entity
public class ProductModel implements Serializable{
    private static final long serialVersionUID = 1L;

    // Persistent Fields:
    @Id
    @GeneratedValue
    Long id;
    private String englishName;
    private String hebrewName;
    private String Company;
    private String Image;
    private String Category;
    private String AnimalCategory;

    public ProductModel(){}
    public ProductModel(String i_englishName, String i_hebrewName, String i_Company, String i_Image, String i_Category, String i_AnimalCategory){
        this.Company = i_Company;
        this.englishName = i_englishName;
        this.Image = i_Image;
        this.Category = i_Category;
        this.AnimalCategory = i_AnimalCategory;
        this.hebrewName = i_hebrewName;
    }
    public String getEnglishName(){
        return englishName;
    }
    public String getHebrewName(){
        return hebrewName;
    }

    public String getCompany() {
        return Company;
    }

    public String getCategory(){
        return Category;
    }

    public String getAnimalCategory(){
        return AnimalCategory;
    }


    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setCompany(String company) {
        this.Company = company;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public void setAnimalCategory(String animalCategory) {
        this.AnimalCategory = animalCategory;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public void setHebrewName(String hebrewName) {
        this.hebrewName = hebrewName;
    }
}
