package onetoone.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import onetoone.Bookmarks.Bookmark;
import onetoone.Roles.Roles;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    private String emailId;
    private boolean ifActive;

    @Column(name = "role_name") // Persistent column for role name
    private String roleName;

    @OneToMany(mappedBy = "user")
    @JsonIgnore // Prevents recursion
    private List<Bookmark> bookmarks;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Roles role;

    public User(String name, String emailId, String password) {
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.ifActive = false;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public boolean getIsActive() {
        return ifActive;
    }

    public void setIfActive(boolean ifActive) {
        this.ifActive = ifActive;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    //@JsonProperty("role") // Include role in JSON response
    public Roles getRole() {
        return role != null ? role : new Roles("User", this); // Return default role if null
    }

    public void setRole(Roles role) {
        this.role = role;
        this.roleName = (role != null) ? role.getRoleName() : "User"; // Synchronize roleName
    }

    @JsonProperty("roleName") // Ensure roleName is part of the response
    public String getRoleName() {
        if (roleName != null) {
            this.roleName = roleName.trim().replaceAll("[\\r\\n\"]", ""); // Remove \r, \n, and " characters
        } else {
            this.roleName = "User"; // Default value if null
        }
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
