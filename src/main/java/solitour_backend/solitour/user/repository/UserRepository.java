package solitour_backend.solitour.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
