package solitour_backend.solitour.gathering_tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.gathering_tag.entity.GatheringTag;

import java.util.List;

public interface GatheringTagRepository extends JpaRepository<GatheringTag, Long> {
    List<GatheringTag> findAllByGathering_Id(Long gatheringId);
}
