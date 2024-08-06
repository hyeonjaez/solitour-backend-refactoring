package solitour_backend.solitour.gathering.repository;

import org.springframework.data.repository.NoRepositoryBean;
import solitour_backend.solitour.gathering.dto.response.GatheringBriefResponse;

import java.util.List;

@NoRepositoryBean
public interface GatheringRepositoryCustom {
    List<GatheringBriefResponse> getGatheringRecommend(Long informationId, Long gatheringCategoryId, Long userId);
}
