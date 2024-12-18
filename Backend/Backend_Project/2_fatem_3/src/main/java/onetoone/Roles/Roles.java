package onetoone.Roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
//import lombok.Getter;
import onetoone.Users.User;



import jakarta.persistence.*;
import onetoone.Users.User;

//@Getter
@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roleName;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Roles(String roleName, User user) {
        this.roleName = roleName;
        this.user = user;
    }

    public Roles() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoleName() {
        return roleName != null ? roleName.replace("\"", "").trim() : null;
    }

    public User getUser() {
        return user;
    }
}


