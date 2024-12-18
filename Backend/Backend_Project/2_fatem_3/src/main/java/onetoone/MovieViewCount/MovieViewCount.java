package onetoone.MovieViewCount;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MovieViewCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int viewCount;
    private int apiID;
    public MovieViewCount(int apiID, int viewCount) {
        this.apiID = apiID;
        this.viewCount = viewCount;
    }

    public MovieViewCount(){
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setApiId(int apiID){
        this.apiID = apiID;
    }

    public int getApiId(){
        return apiID;
    }

    public void setViewCount(int viewCount){
        this.viewCount = viewCount;
    }

    public int getViewCount(){
        return viewCount;
    }
}