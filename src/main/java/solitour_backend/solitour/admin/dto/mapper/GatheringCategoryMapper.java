package solitour_backend.solitour.admin.dto.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import solitour_backend.solitour.admin.entity.GatheringCategory;
import solitour_backend.solitour.category.dto.response.CategoryResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GatheringCategoryMapper {

    CategoryResponse mapToCategoryResponse(GatheringCategory category);

    List<CategoryResponse> mapToCategoryResponses(List<GatheringCategory> categories);
}
