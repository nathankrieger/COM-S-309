package onetoone.MovieViewCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface MovieViewCountRepository extends JpaRepository<MovieViewCount, Long> {
    MovieViewCount findById(int id);

    MovieViewCount findByApiID(int apiID);

    boolean existsByApiID(int apiID);
    MovieViewCount getReferenceByApiID(int apiID);

    @Transactional
    void deleteById(int id);
}
