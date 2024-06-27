package solitour_backend.solitour.information.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.category.entity.Category;
import solitour_backend.solitour.category.repository.CategoryRepository;
import solitour_backend.solitour.image.service.ImageService;
import solitour_backend.solitour.info_tag.entity.InfoTag;
import solitour_backend.solitour.info_tag.repository.InfoTagRepository;
import solitour_backend.solitour.information.dto.mapper.InformationMapper;
import solitour_backend.solitour.information.dto.request.InformationRegisterRequest;
import solitour_backend.solitour.information.dto.response.InformationResponse;
import solitour_backend.solitour.information.entity.Information;
import solitour_backend.solitour.information.repository.InformationRepository;
import solitour_backend.solitour.place.entity.Place;
import solitour_backend.solitour.place.repository.PlaceRepository;
import solitour_backend.solitour.tag.dto.mapper.TagMapper;
import solitour_backend.solitour.tag.entity.Tag;
import solitour_backend.solitour.tag.repository.TagRepository;
import solitour_backend.solitour.zone_category.entity.ZoneCategory;
import solitour_backend.solitour.zone_category.repository.ZoneCategoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InformationService {
    private final InformationRepository informationRepository;
    private final CategoryRepository categoryRepository;
    private final ZoneCategoryRepository zoneCategoryRepository;
    private final PlaceRepository placeRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final InfoTagRepository infoTagRepository;
    private final InformationMapper informationMapper;

    private final ImageService imageService;


    @Transactional
    public InformationResponse registerInformation(InformationRegisterRequest informationRegisterRequest) {
        Category category = categoryRepository.findById(informationRegisterRequest.getCategoryId()).orElseThrow();
        ZoneCategory zoneCategory = zoneCategoryRepository.findById(informationRegisterRequest.getZoneCategoryId()).orElseThrow();

        Place savePlace = placeRepository.save(
                new Place(
                        informationRegisterRequest.getPlaceRegisterRequest().getSearchId(),
                        informationRegisterRequest.getPlaceRegisterRequest().getName(),
                        informationRegisterRequest.getPlaceRegisterRequest().getXAxis(),
                        informationRegisterRequest.getPlaceRegisterRequest().getYAxis(),
                        informationRegisterRequest.getPlaceRegisterRequest().getAddress())
        );

        //TODO user 해야함
        //TODO image는 어떻게 해야할지

        Information information =
                new Information(
                        category,
                        zoneCategory,
                        null,
                        savePlace,
                        informationRegisterRequest.getInformationTitle(),
                        informationRegisterRequest.getInformationAddress(),
                        LocalDateTime.now(),
                        0,
                        informationRegisterRequest.getInformationContent(),
                        informationRegisterRequest.getInformationTips()
                );

        Information saveInformation = informationRepository.save(information);

        List<Tag> tags = tagMapper.mapToTags(informationRegisterRequest.getTagRegisterRequests());
        List<Tag> saveTags = tagRepository.saveAll(tags);

        for (Tag tag : saveTags) {
            infoTagRepository.save(new InfoTag(tag, saveInformation));
        }

        return informationMapper.mapToInformationResponse(information);
    }
}
