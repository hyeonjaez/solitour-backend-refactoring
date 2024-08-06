package solitour_backend.solitour.gathering_applicants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.gathering_applicants.entity.GatheringApplicants;
import solitour_backend.solitour.gathering_applicants.entity.GatheringStatus;

import java.util.List;

public interface GatheringApplicantsRepository extends JpaRepository<GatheringApplicants, Long> {
    List<GatheringApplicants> findAllByGathering_Id(Long id);

    int countAllByGathering_IdAndGatheringStatus(Long id, GatheringStatus gatheringStatus);
}
