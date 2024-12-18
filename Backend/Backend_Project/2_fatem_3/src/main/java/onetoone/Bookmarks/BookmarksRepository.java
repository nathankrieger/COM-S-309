package onetoone.Bookmarks;

import onetoone.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 * @author Vivek Bengre
 *
 */

public interface BookmarksRepository extends JpaRepository<Bookmark, Long> {
    Bookmark findById(int id);
    Bookmark findByName(String username);
    List<Bookmark> findByUser(User user);
    List<Bookmark> findByUserId(int userId);

    Bookmark findByUserIdAndMovieId(int userId, int movieId);

    List<Bookmark> findByMovieId(int movieId);
    @Transactional
    void deleteById(int id);

    @Transactional
    void deleteByUserIdAndMovieId(int userId, int movieId);

    boolean existsByName(String name);
}
