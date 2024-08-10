package solitour_backend.solitour.gathering_category.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.gathering_category.entity.GatheringCategory;

public interface GatheringCategoryRepository extends JpaRepository<GatheringCategory, Long> {

    List<GatheringCategory> findAllByParentCategoryId(Long parentCategoryId);
}
