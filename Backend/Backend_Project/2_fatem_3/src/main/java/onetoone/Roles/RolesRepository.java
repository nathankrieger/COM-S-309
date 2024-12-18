package onetoone.Roles;

import onetoone.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface RolesRepository extends JpaRepository<Roles, Long> {
    Roles findById(int id);
    Roles findByUser(User user);
    @Transactional
    void deleteById(int id);
}
