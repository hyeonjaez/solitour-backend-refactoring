package solitour_backend.solitour.gathering_applicants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.gathering_applicants.entity.GatheringApplicants;
import solitour_backend.solitour.gathering_applicants.entity.GatheringStatus;

import java.util.List;
import java.util.Optional;

public interface GatheringApplicantsRepository extends JpaRepository<GatheringApplicants, Long> {
    List<GatheringApplicants> findAllByGathering_Id(Long id);

    int countAllByGathering_IdAndGatheringStatus(Long id, GatheringStatus gatheringStatus);

    boolean existsByGatheringIdAndUserId(Long gatheringId, Long userId);

    Optional<GatheringApplicants> findByGatheringIdAndUserId(Long gatheringId, Long userId);
}
