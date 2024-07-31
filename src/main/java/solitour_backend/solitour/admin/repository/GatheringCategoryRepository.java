package solitour_backend.solitour.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.admin.entity.GatheringCategory;

import java.util.List;

public interface GatheringCategoryRepository extends JpaRepository<GatheringCategory, Long> {

    List<GatheringCategory> findAllByParentCategoryId(Long parentCategoryId);
}
