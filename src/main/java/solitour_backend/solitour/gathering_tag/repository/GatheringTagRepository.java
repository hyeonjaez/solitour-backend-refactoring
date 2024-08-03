package solitour_backend.solitour.gathering_tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.gathering_tag.entity.GatheringTag;

public interface GatheringTagRepository extends JpaRepository<GatheringTag, Long> {
}
