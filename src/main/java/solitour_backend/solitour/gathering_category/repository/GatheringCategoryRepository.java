package solitour_backend.solitour.gathering_category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.gathering_category.entity.GatheringCategory;

import java.util.List;

public interface GatheringCategoryRepository extends JpaRepository<GatheringCategory, Long> {

    List<GatheringCategory> findAllByParentCategoryId(Long parentCategoryId);
}
