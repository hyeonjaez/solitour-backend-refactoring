package solitour_backend.solitour.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.image.entity.Image;
import solitour_backend.solitour.image.image_status.ImageStatus;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    boolean existsByInformationId(Long informationId);

    List<Image> findAllByInformationIdAndImageStatus(Long informationId, ImageStatus imageStatus);

    Image findOneByInformationIdAndImageStatus(Long informationId, ImageStatus imageStatus);

    boolean existsByUserId(Long userId);

    Image findOneByUserId(Long userId);
}
