package bit.app.user.repository;

import bit.app.user.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteAll();

}
