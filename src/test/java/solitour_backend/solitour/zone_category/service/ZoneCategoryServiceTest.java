//package solitour_backend.solitour.zone_category.service;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//import solitour_backend.solitour.zone_category.dto.mapper.ZoneCategoryMapper;
//import solitour_backend.solitour.zone_category.dto.request.ZoneCategoryModifyRequest;
//import solitour_backend.solitour.zone_category.dto.request.ZoneCategoryRegisterRequest;
//import solitour_backend.solitour.zone_category.dto.response.ZoneCategoryResponse;
//import solitour_backend.solitour.zone_category.entity.ZoneCategory;
//import solitour_backend.solitour.zone_category.exception.ZoneCategoryNotExistsException;
//import solitour_backend.solitour.zone_category.repository.ZoneCategoryRepository;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class ZoneCategoryServiceTest {
//    @InjectMocks
//    ZoneCategoryService zoneCategoryService;
//
//    @Mock
//    ZoneCategoryRepository zoneCategoryRepository;
//
//    @Mock
//    ZoneCategoryMapper zoneCategoryMapper;
//
//    @Test
//    @DisplayName("ZoneCategory 조회 테스트")
//    void getZoneCategoryTest() {
//        ZoneCategory parentZoneCategory = new ZoneCategory(1L, null, "대한민국");
//
//        ZoneCategory zoneCategory = new ZoneCategory(2L, parentZoneCategory, "서울");
//        ZoneCategoryResponse expected = new ZoneCategoryResponse(2L, parentZoneCategory, "서울");
//
//        when(zoneCategoryRepository.findById(any(Long.class))).thenReturn(Optional.of(zoneCategory));
//        when(zoneCategoryMapper.mapToZoneCategoryResponse(any(ZoneCategory.class))).thenReturn(expected);
//
//        ZoneCategoryResponse result = zoneCategoryService.getZoneCategoryById(2L);
//
//        assertEquals(expected, result);
//        assertEquals(expected.getId(), result.getId());
//        assertEquals(expected.getParentZoneCategory(), result.getParentZoneCategory());
//        assertEquals(expected.getName(), result.getName());
//    }
//
//    @Test
//    @DisplayName("ZoneCategory 조회 실패 테스트")
//    void getZoneCategoryFailedTest() {
//        when(zoneCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//        assertThrows(ZoneCategoryNotExistsException.class,
//                () -> zoneCategoryService.getZoneCategoryById(2L));
//    }
//
//    @Test
//    @DisplayName("ZoneCategory 등록 테스트")
//    void registerZoneCategoryTest() {
//        ZoneCategory parentZoneCategory = new ZoneCategory(1L, null, "대한민국");
//        ZoneCategory zoneCategory = new ZoneCategory(1L, parentZoneCategory, "서울");
//        ZoneCategoryResponse expected = new ZoneCategoryResponse(1L, parentZoneCategory, "서울");
//        ZoneCategoryRegisterRequest zoneCategoryRegisterRequest = new ZoneCategoryRegisterRequest();
//
//        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "parentId", 2L);
//        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "name", "서울");
//
//        when(zoneCategoryRepository.findById(any(Long.class))).thenReturn(Optional.of(zoneCategory));
//        when(zoneCategoryRepository.save(any(ZoneCategory.class))).thenReturn(zoneCategory);
//        when(zoneCategoryMapper.mapToZoneCategoryResponse(any(ZoneCategory.class))).thenReturn(expected);
//
//        ZoneCategoryResponse result = zoneCategoryService.registerZoneCategory(zoneCategoryRegisterRequest);
//
//        assertEquals(expected, result);
//        assertEquals(expected.getParentZoneCategory(), result.getParentZoneCategory());
//        assertEquals(expected.getName(), result.getName());
//    }
//
//    @Test
//    @DisplayName("ZoneCategory 등록 실패 테스트")
//    void registerZoneCategoryFailedTest() {
//        ZoneCategoryRegisterRequest zoneCategoryRegisterRequest = new ZoneCategoryRegisterRequest();
//
//        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "parentId", 2L);
//        ReflectionTestUtils.setField(zoneCategoryRegisterRequest, "name", "서울");
//
//        when(zoneCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//        assertThrows(ZoneCategoryNotExistsException.class,
//                () -> zoneCategoryService.registerZoneCategory(zoneCategoryRegisterRequest));
//    }
//
//    @Test
//    @DisplayName("ZoneCategory 수정 테스트")
//    void updateZoneCategoryTest() {
//        ZoneCategory parentZoneCategory = new ZoneCategory(1L, null, "대한민국");
//
//        ZoneCategory zoneCategory = new ZoneCategory(2L, parentZoneCategory, "서울");
//        ZoneCategoryResponse expected = new ZoneCategoryResponse(1L, parentZoneCategory, "서울");
//        ZoneCategoryModifyRequest zoneCategoryModifyRequest = new ZoneCategoryModifyRequest();
//
//        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "parentId", 2L);
//        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "name", "서울");
//
//        when(zoneCategoryRepository.findById(any(Long.class))).thenReturn(Optional.of(zoneCategory));
//        when(zoneCategoryMapper.mapToZoneCategoryResponse(any(ZoneCategory.class))).thenReturn(expected);
//
//        ZoneCategoryResponse result = zoneCategoryService.modifyZoneCategory(2L, zoneCategoryModifyRequest);
//
//        assertEquals(expected, result);
//        assertEquals(expected.getParentZoneCategory(), result.getParentZoneCategory());
//        assertEquals(expected.getName(), result.getName());
//    }
//
//    @Test
//    @DisplayName("ZoneCategory 수정 실패 테스트")
//    void updateZoneCategoryFailedTest() {
//        ZoneCategoryModifyRequest zoneCategoryModifyRequest = new ZoneCategoryModifyRequest();
//
//        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "parentId", 2L);
//        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "name", "서울");
//
//        when(zoneCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//        assertThrows(ZoneCategoryNotExistsException.class,
//                () -> zoneCategoryService.modifyZoneCategory(2L, zoneCategoryModifyRequest));
//    }
//
//    @Test
//    @DisplayName("ZoneCategory 수정 실패 테스트 - parentZoneCategory 존재하지 않음")
//    void updateZoneCategoryFailedNotExistsParentZoneCategoryTest() {
//        ZoneCategory zoneCategory = new ZoneCategory(1L, null, "대한민국");
//        ZoneCategoryModifyRequest zoneCategoryModifyRequest = new ZoneCategoryModifyRequest();
//
//        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "parentId", 2L);
//        ReflectionTestUtils.setField(zoneCategoryModifyRequest, "name", "서울");
//
//        when(zoneCategoryRepository.findById(any(Long.class))).thenReturn(Optional.of(zoneCategory));
//        when(zoneCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//        assertThrows(ZoneCategoryNotExistsException.class,
//                () -> zoneCategoryService.modifyZoneCategory(2L, zoneCategoryModifyRequest));
//    }
//
//    @Test
//    @DisplayName("ZoneCategory 삭제 테스트")
//    void deleteZoneCategoryTest() {
//        when(zoneCategoryRepository.existsById(any(Long.class))).thenReturn(true);
//        zoneCategoryService.deleteZoneCategory(2L);
//    }
//
//    @Test
//    @DisplayName("ZoneCategory 삭제 실패 테스트")
//    void deleteZoneCategoryFailedTest() {
//        when(zoneCategoryRepository.existsById(any(Long.class))).thenReturn(false);
//        assertThrows(ZoneCategoryNotExistsException.class,
//                () -> zoneCategoryService.deleteZoneCategory(2L));
//    }
//
//
//}