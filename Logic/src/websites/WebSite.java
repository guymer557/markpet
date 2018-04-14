package websites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by davej on 4/13/2017.
 */
@Entity
public class WebSite implements Serializable {
    private static final long serialVersionUID = 1L;

    // Persistent Fields:
    @Id
    @GeneratedValue
    Long id;
    private String URL;
    List<String> productsIdentifiersList = new ArrayList<>();
    private Date signingDate;

    // Constructors:
    public WebSite() {
    }

    public WebSite(String URL, String productsIdentidiers) {
        this.URL = URL;
        this.signingDate = new Date(System.currentTimeMillis());
        setProductsIdentifiersList(productsIdentidiers);
    }

    public String getURL(){
        return this.URL;
    }

    //
    public void setProductsIdentifiersList(String productsIdentifiers) {
        //productsIdentifier look like this: = "productsIdentifier: "textBox1" "textBox2" ...."
        String[] result = productsIdentifiers.split("\"");
        for (String string : result) {
            //pick only the words instead of marks like "{" ";" ...
            if (Arrays.asList(result).indexOf(string) % 2 != 0){
                this.productsIdentifiersList.add(string);
            }
        }
        this.productsIdentifiersList.remove(0);
    }

    public List<String> getProductsIdentifiersList() {
        return productsIdentifiersList;
    }



    // String Representation:
    @Override
    public String toString() {
        return this.URL + " (signed on " + signingDate + ")";
    }
}


