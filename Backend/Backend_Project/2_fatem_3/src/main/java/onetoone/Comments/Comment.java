package onetoone.Comments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import onetoone.Users.User;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int movieId;
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private int userid;  // Additional field if you want it available directly

    public Comment() {
    }

    public Comment(int movieId, String text, User user) {
        this.movieId = movieId;
        this.text = text;
        this.user = user;
    }

    // Constructor that can accept userid and movieid
    public Comment(int userid, int movieId, String text) {
        this.userid = userid;
        this.movieId = movieId;
        this.text = text;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}

//test1
