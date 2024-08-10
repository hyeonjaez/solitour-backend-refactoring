package solitour_backend.solitour.gathering.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import solitour_backend.solitour.gathering.dto.response.GatheringBriefResponse;

@NoRepositoryBean
public interface GatheringRepositoryCustom {
    List<GatheringBriefResponse> getGatheringRecommend(Long informationId, Long gatheringCategoryId, Long userId);
}
