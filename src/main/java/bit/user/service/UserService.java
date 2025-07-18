package bit.user.service;

import bit.user.domain.User;
import bit.user.dto.UserCreateRequest;
import java.util.Optional;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    User createUser(UserCreateRequest userCreateRequest);

    Optional<User> findById(Long userId);

    boolean isRegisteredEmail(String email);
}
