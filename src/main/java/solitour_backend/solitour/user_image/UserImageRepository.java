package solitour_backend.solitour.user_image;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import solitour_backend.solitour.auth.entity.Token;

public interface UserImageRepository extends Repository<UserImage, Long> {

  UserImage save(UserImage userImage);

}
