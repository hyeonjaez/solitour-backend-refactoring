package solitour_backend.solitour.gathering.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.gathering.dto.mapper.GatheringMapper;
import solitour_backend.solitour.gathering.dto.request.GatheringRegisterRequest;
import solitour_backend.solitour.gathering.dto.response.GatheringResponse;
import solitour_backend.solitour.gathering.entity.Gathering;
import solitour_backend.solitour.gathering.exception.GatheringCategoryNotExistsException;
import solitour_backend.solitour.gathering.repository.GatheringRepository;
import solitour_backend.solitour.gathering_category.entity.GatheringCategory;
import solitour_backend.solitour.gathering_category.repository.GatheringCategoryRepository;
import solitour_backend.solitour.gathering_tag.entity.GatheringTag;
import solitour_backend.solitour.gathering_tag.repository.GatheringTagRepository;
import solitour_backend.solitour.place.dto.request.PlaceRegisterRequest;
import solitour_backend.solitour.place.entity.Place;
import solitour_backend.solitour.place.repository.PlaceRepository;
import solitour_backend.solitour.tag.dto.mapper.TagMapper;
import solitour_backend.solitour.tag.entity.Tag;
import solitour_backend.solitour.tag.repository.TagRepository;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.entity.UserRepository;
import solitour_backend.solitour.user.exception.UserNotExistsException;
import solitour_backend.solitour.zone_category.entity.ZoneCategory;
import solitour_backend.solitour.zone_category.exception.ZoneCategoryNotExistsException;
import solitour_backend.solitour.zone_category.repository.ZoneCategoryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GatheringService {
    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final ZoneCategoryRepository zoneCategoryRepository;
    private final PlaceRepository placeRepository;
    private final GatheringCategoryRepository gatheringCategoryRepository;
    private final TagMapper tagMapper;
    private final TagRepository tagRepository;
    private final GatheringTagRepository gatheringTagRepository;
    private final GatheringMapper gatheringMapper;

    @Transactional
    public GatheringResponse registerGathering(Long userId, GatheringRegisterRequest gatheringRegisterRequest) {
        PlaceRegisterRequest placeRegisterRequest = gatheringRegisterRequest.getPlaceRegisterRequest();
        Place place = new Place(
                placeRegisterRequest.getSearchId(),
                placeRegisterRequest.getName(),
                placeRegisterRequest.getXAxis(),
                placeRegisterRequest.getYAxis(),
                placeRegisterRequest.getAddress());

        Place savePlace = placeRepository.save(place);
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotExistsException("해당하는 id의 User 가 없습니다"));
        GatheringCategory gatheringCategory = gatheringCategoryRepository.findById(
                        gatheringRegisterRequest.getGatheringCategoryId())
                .orElseThrow(
                        () -> new GatheringCategoryNotExistsException("해당하는 id의 category 가 없습니다"));

        ZoneCategory parentZoneCategory = zoneCategoryRepository.findByParentZoneCategoryIdAndName(
                        null, gatheringRegisterRequest.getZoneCategoryNameParent())
                .orElseThrow(
                        () ->
                                new ZoneCategoryNotExistsException("해당하는 name의 ZoneCategory 없습니다"));

        ZoneCategory childZoneCategory = zoneCategoryRepository.findByParentZoneCategoryIdAndName(
                        parentZoneCategory.getId(), gatheringRegisterRequest.getZoneCategoryNameChild())
                .orElseThrow(
                        () -> new ZoneCategoryNotExistsException("해당하는 name의 ZoneCategory 없습니다"));

        Gathering gathering =
                new Gathering(
                        user,
                        childZoneCategory,
                        gatheringCategory,
                        savePlace,
                        gatheringRegisterRequest.getTitle(),
                        gatheringRegisterRequest.getContent(),
                        gatheringRegisterRequest.getPersonCount(),
                        0,
                        gatheringRegisterRequest.getScheduleStartDate(),
                        gatheringRegisterRequest.getScheduleEndDate(),
                        false,
                        gatheringRegisterRequest.getDeadline(),
                        gatheringRegisterRequest.getAllowedSex(),
                        gatheringRegisterRequest.getStartAge(),
                        gatheringRegisterRequest.getEndAge()
                );
        Gathering saveGathering = gatheringRepository.save(gathering);

        List<Tag> tags = tagMapper.mapToTags(gatheringRegisterRequest.getTagRegisterRequests());
        List<Tag> saveTags = tagRepository.saveAll(tags);

        for (Tag tag : saveTags) {
            gatheringTagRepository.save(new GatheringTag(tag, saveGathering));
        }
        return gatheringMapper.mapToGatheringResponse(saveGathering);
    }

}
