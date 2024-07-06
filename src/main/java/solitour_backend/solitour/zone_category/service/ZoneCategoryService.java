package solitour_backend.solitour.zone_category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.zone_category.dto.mapper.ZoneCategoryMapper;
import solitour_backend.solitour.zone_category.dto.request.ZoneCategoryModifyRequest;
import solitour_backend.solitour.zone_category.dto.request.ZoneCategoryRegisterRequest;
import solitour_backend.solitour.zone_category.dto.response.ZoneCategoryResponse;
import solitour_backend.solitour.zone_category.entity.ZoneCategory;
import solitour_backend.solitour.zone_category.exception.ZoneCategoryAlreadyExistsException;
import solitour_backend.solitour.zone_category.exception.ZoneCategoryNotExistsException;
import solitour_backend.solitour.zone_category.repository.ZoneCategoryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ZoneCategoryService {
    private final ZoneCategoryRepository zoneCategoryRepository;
    private final ZoneCategoryMapper zoneCategoryMapper;

    @Transactional(readOnly = true)
    public ZoneCategoryResponse getZoneCategoryById(Integer id) {
        ZoneCategory zoneCategory = zoneCategoryRepository.findById(id)
                .orElseThrow(
                        () -> new ZoneCategoryNotExistsException("해당 하는 id의 ZoneCategory가 존재하지 않습니다"));
        return zoneCategoryMapper.mapToZoneCategoryResponse(zoneCategory);
    }

    public ZoneCategoryResponse registerZoneCategory(ZoneCategoryRegisterRequest zoneCategoryRegisterRequest) {
        if (zoneCategoryRepository.existsById(zoneCategoryRegisterRequest.getId())) {
            throw new ZoneCategoryAlreadyExistsException("해당 하는 id의 데이터가 있습니다");
        }

        ZoneCategory zoneCategory = new ZoneCategory(zoneCategoryRegisterRequest.getId(), zoneCategoryRegisterRequest.getName());
        ZoneCategory savedZoneCategory = zoneCategoryRepository.save(zoneCategory);

        return zoneCategoryMapper.mapToZoneCategoryResponse(savedZoneCategory);
    }

    public ZoneCategoryResponse modifyZoneCategory(Integer id, ZoneCategoryModifyRequest zoneCategoryModifyRequest) {
        ZoneCategory zoneCategory = zoneCategoryRepository.findById(id)
                .orElseThrow(
                        () -> new ZoneCategoryNotExistsException("해당 하는 id의 ZoneCategory가 존재하지 않습니다"));
        zoneCategory.setName(zoneCategoryModifyRequest.getName());

        return zoneCategoryMapper.mapToZoneCategoryResponse(zoneCategory);
    }

    public void deleteZoneCategory(Integer id) {
        if (zoneCategoryRepository.existsById(id)) {
            zoneCategoryRepository.deleteById(id);
        } else {
            throw new ZoneCategoryNotExistsException("해당 하는 id의 ZoneCategory가 존재하지 않습니다");
        }
    }
}
