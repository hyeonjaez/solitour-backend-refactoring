package solitour_backend.solitour.information.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import solitour_backend.solitour.information.dto.response.InformationBriefResponse;
import solitour_backend.solitour.information.dto.response.InformationRankResponse;

import java.util.List;


@NoRepositoryBean
public interface InformationRepositoryCustom {
    Page<InformationBriefResponse> getInformationByParentCategory(Pageable pageable, Long categoryId, Long userId);

    Page<InformationBriefResponse> getInformationByChildCategory(Pageable pageable, Long categoryId, Long userId);

    Page<InformationBriefResponse> getInformationByParentCategoryFilterLikeCount(Pageable pageable, Long categoryId, Long userId);

    Page<InformationBriefResponse> getInformationByChildCategoryFilterLikeCount(Pageable pageable, Long categoryId, Long userId);

    List<InformationRankResponse> getInformationRank();
}
