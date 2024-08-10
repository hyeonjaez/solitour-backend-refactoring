package solitour_backend.solitour.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.admin.dto.mapper.GatheringCategoryMapper;
import solitour_backend.solitour.gathering_category.entity.GatheringCategory;
import solitour_backend.solitour.gathering_category.repository.GatheringCategoryRepository;
import solitour_backend.solitour.category.dto.request.CategoryModifyRequest;
import solitour_backend.solitour.category.dto.request.CategoryRegisterRequest;
import solitour_backend.solitour.category.dto.response.CategoryGetResponse;
import solitour_backend.solitour.category.dto.response.CategoryResponse;
import solitour_backend.solitour.category.exception.CategoryNotExistsException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GatheringCategoryService {

    private final GatheringCategoryRepository gatheringCategoryRepository;
    private final GatheringCategoryMapper gatheringCategoryMapper;

    @Transactional
    public CategoryResponse registerCategory(CategoryRegisterRequest categoryRegisterRequest) {
        GatheringCategory parentCategoryEntity;
        if (Objects.isNull(categoryRegisterRequest.getParentCategory())) {
            parentCategoryEntity = null;
        } else {
            parentCategoryEntity = gatheringCategoryRepository.findById(
                            categoryRegisterRequest.getParentCategory())
                    .orElseThrow(
                            () -> new CategoryNotExistsException("Parent category not found"));
        }

        GatheringCategory category = new GatheringCategory(parentCategoryEntity, categoryRegisterRequest.getName());
        GatheringCategory saveCategory = gatheringCategoryRepository.save(category);

        return gatheringCategoryMapper.mapToCategoryResponse(saveCategory);
    }


    public CategoryResponse getCategory(Long id) {
        GatheringCategory category = gatheringCategoryRepository.findById(id)
                .orElseThrow(
                        () -> new CategoryNotExistsException("category not found"));

        return gatheringCategoryMapper.mapToCategoryResponse(category);
    }


    public List<CategoryResponse> getChildrenCategories(Long id) {
        List<GatheringCategory> childrenCategories = gatheringCategoryRepository.findAllByParentCategoryId(id);

        return gatheringCategoryMapper.mapToCategoryResponses(childrenCategories);
    }

    public List<CategoryGetResponse> getParentCategories() {
        List<GatheringCategory> parentCategories = gatheringCategoryRepository.findAllByParentCategoryId(null);
        List<CategoryGetResponse> categoryGetResponses = new ArrayList<>();

        for (GatheringCategory category : parentCategories) {
            List<CategoryResponse> childrenCategories = getChildrenCategories(category.getId());
            categoryGetResponses.add(
                    new CategoryGetResponse(category.getId(), category.getName(), childrenCategories));
        }

        return categoryGetResponses;
    }

    @Transactional
    public CategoryResponse modifyCategory(Long id, CategoryModifyRequest categoryModifyRequest) {
        GatheringCategory parentCategoryEntity;
        if (Objects.isNull(categoryModifyRequest.getParentCategory())) {
            parentCategoryEntity = null;
        } else {
            parentCategoryEntity = gatheringCategoryRepository.findById(
                            categoryModifyRequest.getParentCategory())
                    .orElseThrow(
                            () -> new CategoryNotExistsException("Parent category not found"));
        }

        GatheringCategory category = gatheringCategoryRepository.findById(id).orElseThrow();

        category.setName(categoryModifyRequest.getName());
        category.setParentCategory(parentCategoryEntity);

        return gatheringCategoryMapper.mapToCategoryResponse(category);
    }

}
