package onetoone.Messages;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import onetoone.Users.User;

@Entity
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int sent;
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Messages() {
    }

    public Messages(User user, String content, int sent) {
        this.user = user;
        this.content = content;
        this.sent = sent;
    }

    // Getters and Setters
    public void setId(int id){ this.id = id; }
    public int getId() { return id; }
    public void setUser(User user){ this.user = user; }
    public User getUser() { return user; }
    public int getSent() {
        return sent;
    }
    public void setSent(int sent) {
        this.sent = sent;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) { this.content = content; }
}
