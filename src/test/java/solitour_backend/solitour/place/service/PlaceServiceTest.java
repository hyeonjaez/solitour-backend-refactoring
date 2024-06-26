package solitour_backend.solitour.place.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import solitour_backend.solitour.place.dto.mapper.PlaceMapper;
import solitour_backend.solitour.place.dto.request.PlaceModifyRequest;
import solitour_backend.solitour.place.dto.request.PlaceRegisterRequest;
import solitour_backend.solitour.place.dto.response.PlaceResponse;
import solitour_backend.solitour.place.entity.Place;
import solitour_backend.solitour.place.exception.PlaceNotExistsException;
import solitour_backend.solitour.place.repository.PlaceRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {
    @InjectMocks
    PlaceService placeService;

    @Mock
    PlaceRepository placeRepository;

    @Mock
    PlaceMapper placeMapper;

    @Test
    @DisplayName("place 조회 테스트")
    void getPlaceTest() {
        Place place = new Place(1L, "hi", "hyeon", new BigDecimal("1.1"), new BigDecimal("1.2"), "wnth");
        PlaceResponse expected = new PlaceResponse(1L, "hi", "hyeon", "wnth");

        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.of(place));
        when(placeMapper.mapToPlaceResponse(any(Place.class))).thenReturn(expected);

        PlaceResponse result = placeService.getPlace(1L);
        assertEquals(expected, result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getSearchId(), result.getSearchId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getAddress(), result.getAddress());
    }

    @Test
    @DisplayName("place 조회 실패 테스트")
    void getPlaceFailedTest() {
        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PlaceNotExistsException.class, () -> placeService.getPlace(1L));
    }

    @Test
    @DisplayName("place 등록 테스트")
    void savePlaceTest() {
        Place place = new Place(1L, "hi", "hyeon", new BigDecimal("1.1"), new BigDecimal("1.2"), "wnth");
        PlaceResponse expected = new PlaceResponse(1L, "hi", "hyeon", "wnth");
        PlaceRegisterRequest placeRegisterRequest = new PlaceRegisterRequest();

        ReflectionTestUtils.setField(placeRegisterRequest, "searchId", "hi");
        ReflectionTestUtils.setField(placeRegisterRequest, "name", "hyeon");
        ReflectionTestUtils.setField(placeRegisterRequest, "xAxis", new BigDecimal("1.1"));
        ReflectionTestUtils.setField(placeRegisterRequest, "yAxis", new BigDecimal("1.2"));
        ReflectionTestUtils.setField(placeRegisterRequest, "address", "wnth");
        ReflectionTestUtils.setField(placeRegisterRequest, "isCustom", true);

        when(placeRepository.save(any(Place.class))).thenReturn(place);
        when(placeMapper.mapToPlaceResponse(any(Place.class))).thenReturn(expected);

        PlaceResponse result = placeService.savePlace(placeRegisterRequest);

        assertEquals(expected, result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getSearchId(), result.getSearchId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getAddress(), result.getAddress());
    }

    @Test
    @DisplayName("place 수정 테스트")
    void updatePlaceTest() {
        Place place = new Place(1L, "hi", "hyeon", new BigDecimal("1.1"), new BigDecimal("1.2"), "wnth");
        PlaceResponse expected = new PlaceResponse(1L, "hi", "hyeon", "wnth");

        PlaceModifyRequest placeModifyRequest = new PlaceModifyRequest();

        ReflectionTestUtils.setField(placeModifyRequest, "searchId", "hi");
        ReflectionTestUtils.setField(placeModifyRequest, "name", "hyeon");
        ReflectionTestUtils.setField(placeModifyRequest, "xAxis", new BigDecimal("1.1"));
        ReflectionTestUtils.setField(placeModifyRequest, "yAxis", new BigDecimal("1.2"));
        ReflectionTestUtils.setField(placeModifyRequest, "address", "wnth");

        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.of(place));
        when(placeMapper.mapToPlaceResponse(any(Place.class))).thenReturn(expected);

        PlaceResponse result = placeService.updatePlace(1L, placeModifyRequest);

        assertEquals(expected, result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getSearchId(), result.getSearchId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getAddress(), result.getAddress());
    }

    @Test
    @DisplayName("place 수정 실패 테스트")
    void updatePlaceFailedTest() {
        PlaceModifyRequest placeModifyRequest = new PlaceModifyRequest();

        ReflectionTestUtils.setField(placeModifyRequest, "searchId", "hi");
        ReflectionTestUtils.setField(placeModifyRequest, "name", "hyeon");
        ReflectionTestUtils.setField(placeModifyRequest, "xAxis", new BigDecimal("1.1"));
        ReflectionTestUtils.setField(placeModifyRequest, "yAxis", new BigDecimal("1.2"));
        ReflectionTestUtils.setField(placeModifyRequest, "address", "wnth");

        when(placeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(PlaceNotExistsException.class,
                () -> placeService.updatePlace(1L, placeModifyRequest));
    }

    @Test
    @DisplayName("place 삭제 테스트")
    void deletePlaceTest() {
        when(placeRepository.existsById(any(Long.class))).thenReturn(true);
        placeService.deletePlace(1L);
    }

    @Test
    @DisplayName("place 삭제 실패 테스트")
    void deletePlaceFailedTest() {
        when(placeRepository.existsById(any(Long.class))).thenReturn(false);
        assertThrows(PlaceNotExistsException.class,
                () -> placeService.deletePlace(1L));
    }
}