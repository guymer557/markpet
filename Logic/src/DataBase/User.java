package DataBase;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by davej on 4/12/2017.
 */
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Persistent Fields:
    @Id
    @GeneratedValue
    Long id;
    String Name = new String();
    private String password;
    private String email;
    public String getName() {
        return Name;
    }

    public void setName(String i_Name){
        this.Name = i_Name;
    }

    public Long getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
