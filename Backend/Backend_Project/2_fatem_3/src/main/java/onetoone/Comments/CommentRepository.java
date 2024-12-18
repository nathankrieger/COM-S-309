package onetoone.Comments;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByMovieId(int movieId);
    List<Comment> findByUserId(int userId);
}
