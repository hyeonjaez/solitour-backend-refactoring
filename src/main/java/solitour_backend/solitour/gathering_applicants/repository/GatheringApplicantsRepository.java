package solitour_backend.solitour.gathering_applicants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.gathering_applicants.entity.GatheringApplicants;

public interface GatheringApplicantsRepository extends JpaRepository<GatheringApplicants, Long> {
}
