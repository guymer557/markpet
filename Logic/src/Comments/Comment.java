package Comments;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by davej on 8/24/2017.
 */
@Entity
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    // Persistent Fields:
    @Id
    @GeneratedValue
    Long id;
    private Long userID;
    private Long refID;
    private String userName;
    private String Comment;
    private Date Date;

    public Comment(){}

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }


    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public java.sql.Date getDate() {
        return Date;
    }

    public void setDate(java.sql.Date date) {
        Date = date;
    }

    public Long getRefID() {
        return refID;
    }

    public void setRefID(Long refID) {
        this.refID = refID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

