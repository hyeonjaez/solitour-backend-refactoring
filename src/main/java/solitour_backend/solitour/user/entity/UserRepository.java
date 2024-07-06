package solitour_backend.solitour.user.entity;

import io.jsonwebtoken.security.Password;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByNickname(String nickname);

  Optional<User> findByEmail(String email);

}
