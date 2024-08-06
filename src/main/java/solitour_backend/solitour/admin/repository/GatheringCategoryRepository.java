package solitour_backend.solitour.admin.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.admin.entity.GatheringCategory;

public interface GatheringCategoryRepository extends JpaRepository<GatheringCategory, Long> {

    List<GatheringCategory> findAllByParentCategoryId(Long parentCategoryId);
}
