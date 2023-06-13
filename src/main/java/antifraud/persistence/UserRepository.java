package antifraud.persistence;

import antifraud.business.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByRole(String role);

    Optional<User> findUserByUsername(String username);

}
