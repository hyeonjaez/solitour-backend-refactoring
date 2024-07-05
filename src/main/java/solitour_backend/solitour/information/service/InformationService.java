package solitour_backend.solitour.information.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import solitour_backend.solitour.category.entity.Category;
import solitour_backend.solitour.category.repository.CategoryRepository;
import solitour_backend.solitour.image.entity.Image;
import solitour_backend.solitour.image.image_status.ImageStatus;
import solitour_backend.solitour.image.repository.ImageRepository;
import solitour_backend.solitour.image.s3.S3Uploader;
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
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.entity.UserRepository;
import solitour_backend.solitour.zone_category.entity.ZoneCategory;
import solitour_backend.solitour.zone_category.repository.ZoneCategoryRepository;

import java.time.LocalDate;
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
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public static final String IMAGE_PATH = "information";
    private final ImageRepository imageRepository;


    @Transactional
    public InformationResponse registerInformation(InformationRegisterRequest informationRegisterRequest, MultipartFile thumbnail, List<MultipartFile> contentImages) {
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

        User user = userRepository.findById(informationRegisterRequest.getUserId()).orElseThrow();

        Information information =
                new Information(
                        category,
                        zoneCategory,
                        user,
                        savePlace,
                        informationRegisterRequest.getInformationTitle(),
                        informationRegisterRequest.getInformationAddress(),
                        LocalDateTime.now(),
                        0,
                        informationRegisterRequest.getInformationContent(),
                        informationRegisterRequest.getInformationTips()
                );

        Information saveInformation = informationRepository.save(information);
        LocalDate localDate = LocalDate.now();

        String thumbNailImageUrl = s3Uploader.upload(thumbnail, IMAGE_PATH, saveInformation.getId());
        Image thumbImage = new Image(ImageStatus.THUMBNAIL, saveInformation, null, thumbNailImageUrl, localDate);
        imageRepository.save(thumbImage);

        for (MultipartFile multipartFile : contentImages) {
            String upload = s3Uploader.upload(multipartFile, IMAGE_PATH, saveInformation.getId());
            Image contentImage = new Image(ImageStatus.CONTENT, saveInformation, null, upload, localDate);
            imageRepository.save(contentImage);
        }


        List<Tag> tags = tagMapper.mapToTags(informationRegisterRequest.getTagRegisterRequests());
        List<Tag> saveTags = tagRepository.saveAll(tags);

        for (Tag tag : saveTags) {
            infoTagRepository.save(new InfoTag(tag, saveInformation));
        }


        return informationMapper.mapToInformationResponse(information);
    }
}
