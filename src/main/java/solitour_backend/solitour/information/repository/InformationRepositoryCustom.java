package solitour_backend.solitour.information.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import solitour_backend.solitour.information.dto.response.InformationBriefResponse;
import solitour_backend.solitour.information.dto.response.InformationMainResponse;
import solitour_backend.solitour.information.dto.response.InformationRankResponse;

import java.util.List;


@NoRepositoryBean
public interface InformationRepositoryCustom {

    Page<InformationBriefResponse> getInformationByParentCategory(Pageable pageable, Long categoryId, Long userId);

    Page<InformationBriefResponse> getInformationByChildCategory(Pageable pageable, Long categoryId, Long userId);

    Page<InformationBriefResponse> getInformationByParentCategoryFilterZoneCategoryLikeCount(Pageable pageable, Long categoryId, Long userId, Long zoneCategoryId);

    Page<InformationBriefResponse> getInformationByChildCategoryFilterLikeCount(Pageable pageable, Long categoryId, Long userId);

    Page<InformationBriefResponse> getInformationByParentCategoryFilterViewCount(Pageable pageable, Long categoryId, Long userId);

    Page<InformationBriefResponse> getInformationByChildCategoryFilterViewCount(Pageable pageable, Long categoryId, Long userId);

    Page<InformationBriefResponse> getInformationByParentCategoryFilterZoneCategory(Pageable pageable, Long parentCategoryId, Long userId, Long zoneCategoryId);

    Page<InformationBriefResponse> getInformationByChildCategoryFilterZoneCategory(Pageable pageable, Long childCategoryId, Long userId, Long zoneCategoryId);

    List<InformationRankResponse> getInformationRank();

    List<InformationMainResponse> getInformationLikeCountFromCreatedIn3(Long userId);

    List<InformationBriefResponse> getInformationRecommend(Long informationId, Long childCategoryId, Long userId);
}
