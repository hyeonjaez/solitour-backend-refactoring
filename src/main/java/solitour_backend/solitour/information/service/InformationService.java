package solitour_backend.solitour.information.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import solitour_backend.solitour.book_mark_information.entity.BookMarkInformationRepository;
import solitour_backend.solitour.category.entity.Category;
import solitour_backend.solitour.category.exception.CategoryNotExistsException;
import solitour_backend.solitour.category.repository.CategoryRepository;
import solitour_backend.solitour.great_information.repository.GreatInformationRepository;
import solitour_backend.solitour.image.dto.mapper.ImageMapper;
import solitour_backend.solitour.image.dto.response.ImageResponse;
import solitour_backend.solitour.image.entity.Image;
import solitour_backend.solitour.image.image_status.ImageStatus;
import solitour_backend.solitour.image.repository.ImageRepository;
import solitour_backend.solitour.image.s3.S3Uploader;
import solitour_backend.solitour.info_tag.entity.InfoTag;
import solitour_backend.solitour.info_tag.repository.InfoTagRepository;
import solitour_backend.solitour.information.dto.mapper.InformationMapper;
import solitour_backend.solitour.information.dto.request.InformationModifyRequest;
import solitour_backend.solitour.information.dto.request.InformationRegisterRequest;
import solitour_backend.solitour.information.dto.response.InformationBriefResponse;
import solitour_backend.solitour.information.dto.response.InformationDetailResponse;
import solitour_backend.solitour.information.dto.response.InformationRankResponse;
import solitour_backend.solitour.information.dto.response.InformationResponse;
import solitour_backend.solitour.information.entity.Information;
import solitour_backend.solitour.information.exception.InformationNotExistsException;
import solitour_backend.solitour.information.repository.InformationRepository;
import solitour_backend.solitour.place.dto.mapper.PlaceMapper;
import solitour_backend.solitour.place.dto.response.PlaceResponse;
import solitour_backend.solitour.place.entity.Place;
import solitour_backend.solitour.place.repository.PlaceRepository;
import solitour_backend.solitour.tag.dto.mapper.TagMapper;
import solitour_backend.solitour.tag.dto.response.TagResponse;
import solitour_backend.solitour.tag.entity.Tag;
import solitour_backend.solitour.tag.repository.TagRepository;
import solitour_backend.solitour.user.dto.UserPostingResponse;
import solitour_backend.solitour.user.dto.mapper.UserMapper;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.entity.UserRepository;
import solitour_backend.solitour.user.exception.UserNotExistsException;
import solitour_backend.solitour.zone_category.dto.mapper.ZoneCategoryMapper;
import solitour_backend.solitour.zone_category.dto.response.ZoneCategoryResponse;
import solitour_backend.solitour.zone_category.entity.ZoneCategory;
import solitour_backend.solitour.zone_category.exception.ZoneCategoryNotExistsException;
import solitour_backend.solitour.zone_category.repository.ZoneCategoryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final PlaceMapper placeMapper;
    private final ZoneCategoryMapper zoneCategoryMapper;
    private final ImageMapper imageMapper;
    private final UserMapper userMapper;
    private final GreatInformationRepository greatInformationRepository;
    private final BookMarkInformationRepository bookMarkInformationRepository;

    public static final String IMAGE_PATH = "information";
    private final ImageRepository imageRepository;


    @Transactional
    public InformationResponse registerInformation(InformationRegisterRequest informationRegisterRequest, MultipartFile thumbnail, List<MultipartFile> contentImages) {
        Category category = categoryRepository.findById(informationRegisterRequest.getCategoryId())
                .orElseThrow(
                        () -> new CategoryNotExistsException("해당하는 id의 category 가 없습니다"));
        ZoneCategory parentZoneCategory = zoneCategoryRepository.findByName(
                        informationRegisterRequest.getZoneCategoryNameParent())
                .orElseThrow(() -> new ZoneCategoryNotExistsException("해당하는 name의 ZoneCategory 없습니다"));

        ZoneCategory childZoneCategory = zoneCategoryRepository.findByParentZoneCategoryIdAndName(
                        parentZoneCategory.getId(), informationRegisterRequest.getZoneCategoryNameChild())
                .orElseThrow(() -> new ZoneCategoryNotExistsException("해당하는 ParentZoneCategoryId 와 name의 ZoneCategory 없습니다"));

        Place savePlace = placeRepository.save(
                new Place(
                        informationRegisterRequest.getPlaceRegisterRequest().getSearchId(),
                        informationRegisterRequest.getPlaceRegisterRequest().getName(),
                        informationRegisterRequest.getPlaceRegisterRequest().getXAxis(),
                        informationRegisterRequest.getPlaceRegisterRequest().getYAxis(),
                        informationRegisterRequest.getPlaceRegisterRequest().getAddress())
        );

        User user = userRepository.findById(informationRegisterRequest.getUserId())
                .orElseThrow(
                        () -> new UserNotExistsException("해당하는 id의 User 가 없습니다"));

        Information information =
                new Information(
                        category,
                        childZoneCategory,
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
        Image thumbImage = new Image(ImageStatus.THUMBNAIL, saveInformation, thumbNailImageUrl, localDate);
        imageRepository.save(thumbImage);

        for (MultipartFile multipartFile : contentImages) {
            String upload = s3Uploader.upload(multipartFile, IMAGE_PATH, saveInformation.getId());
            Image contentImage = new Image(ImageStatus.CONTENT, saveInformation, upload, localDate);
            imageRepository.save(contentImage);
        }

        List<Tag> tags = tagMapper.mapToTags(informationRegisterRequest.getTagRegisterRequests());
        List<Tag> saveTags = tagRepository.saveAll(tags);

        for (Tag tag : saveTags) {
            infoTagRepository.save(new InfoTag(tag, saveInformation));
        }


        return informationMapper.mapToInformationResponse(information);
    }


    public InformationDetailResponse getDetailInformation(Long informationId) {
        Information information = informationRepository.findById(informationId).orElseThrow(() -> new InformationNotExistsException("해당하는 id의 information이 존재하지 않습니다."));
        List<InfoTag> infoTags = infoTagRepository.findAllByInformationId(information.getId());

        UserPostingResponse userPostingResponse = userMapper.mapToUserPostingResponse(information.getUser());

        List<TagResponse> tagResponses = new ArrayList<>();
        if (!infoTags.isEmpty()) {
            tagResponses = infoTags.stream()
                    .map(data ->
                            tagMapper.mapToTagResponse(data.getTag()))
                    .toList();
        }
        PlaceResponse placeResponse = placeMapper.mapToPlaceResponse(information.getPlace());
        ZoneCategoryResponse zoneCategoryResponse = zoneCategoryMapper.mapToZoneCategoryResponse(information.getZoneCategory());

        List<Image> images = imageRepository.findAllByInformationId(information.getId());

        List<ImageResponse> imageResponseList = imageMapper.toImageResponseList(images);

        int likeCount = greatInformationRepository.countByInformationId(information.getId());

        return new InformationDetailResponse(
                information.getTitle(),
                information.getAddress(),
                information.getCreatedDate(),
                information.getViewCount(),
                information.getContent(),
                information.getTip(),
                userPostingResponse,
                tagResponses,
                placeResponse,
                zoneCategoryResponse,
                imageResponseList,
                likeCount);
    }


    @Transactional
    public InformationResponse modifyInformation(Long id, InformationModifyRequest informationModifyRequest, MultipartFile thumbNail, List<MultipartFile> contentImages) {
        Information information = informationRepository.findById(id).orElseThrow(() -> new InformationNotExistsException("해당하는 id의 information이 존재하지 않습니다."));
        information.setTitle(informationModifyRequest.getTitle());
        information.setAddress(informationModifyRequest.getAddress());
        information.setContent(informationModifyRequest.getContent());
        information.setTip(informationModifyRequest.getTips());


        List<Image> allByInformationId = imageRepository.findAllByInformationId(information.getId());

        for (Image image : allByInformationId) {
            s3Uploader.deleteImage(image.getAddress());
        }

        imageRepository.deleteAllByInformationId(information.getId());
        LocalDate localDate = LocalDate.now();

        String thumbNailImageUrl = s3Uploader.upload(thumbNail, IMAGE_PATH, information.getId());
        Image thumbImage = new Image(ImageStatus.THUMBNAIL, information, thumbNailImageUrl, localDate);
        imageRepository.save(thumbImage);

        for (MultipartFile multipartFile : contentImages) {
            String upload = s3Uploader.upload(multipartFile, IMAGE_PATH, information.getId());
            Image contentImage = new Image(ImageStatus.CONTENT, information, upload, localDate);
            imageRepository.save(contentImage);
        }


        List<InfoTag> infoTags = infoTagRepository.findAllByInformationId(information.getId());

        infoTagRepository.deleteAllByInformationId(information.getId());

        for (InfoTag infoTag : infoTags) {
            tagRepository.deleteById(infoTag.getId());
        }

        List<Tag> saveTags = tagRepository.saveAll(
                tagMapper.mapToTags(
                        informationModifyRequest.getTagRegisterRequests()));

        for (Tag tag : saveTags) {
            infoTagRepository.save(new InfoTag(tag, information));
        }


        return informationMapper.mapToInformationResponse(information);
    }

    @Transactional
    public void deleteInformation(Long id) {
        Information information = informationRepository.findById(id).orElseThrow(
            () -> new InformationNotExistsException("해당하는 id의 information이 존재하지 않습니다."));

        List<InfoTag> infoTags = infoTagRepository.findAllByInformationId(information.getId());
        infoTagRepository.deleteAllByInformationId(information.getId());

        for (InfoTag infoTag : infoTags) {
            tagRepository.deleteById(infoTag.getTag().getTagId());
        }

        greatInformationRepository.deleteAllByInformationId(information.getId());

        bookMarkInformationRepository.deleteAllByInformationId(information.getId());

        List<Image> allByInformationId = imageRepository.findAllByInformationId(
            information.getId());

        for (Image image : allByInformationId) {
            s3Uploader.deleteImage(image.getAddress());
        }

        imageRepository.deleteAllByInformationId(information.getId());
        informationRepository.deleteById(id);

    }

    //default
    public Page<InformationBriefResponse> getBriefInformationPageByParentCategory(Pageable pageable, Long parentCategoryId, Long userId) {
        if (!categoryRepository.existsById(parentCategoryId)) {
            throw new CategoryNotExistsException("해당하는 id의 category는 없습니다");
        }
        return informationRepository.getInformationByParentCategory(pageable, parentCategoryId, userId);
    }

    public Page<InformationBriefResponse> getBriefInformationPageByChildCategory(Pageable pageable, Long childCategoryId, Long userId) {
        if (!categoryRepository.existsById(childCategoryId)) {
            throw new CategoryNotExistsException("해당하는 id의 category는 없습니다");
        }
        return informationRepository.getInformationByChildCategory(pageable, childCategoryId, userId);
    }

    //좋아요순
    public Page<InformationBriefResponse> getBriefInformationPageByParentCategoryFilterLikeCount(Pageable pageable, Long parentCategoryId, Long userId) {
        if (!categoryRepository.existsById(parentCategoryId)) {
            throw new CategoryNotExistsException("해당하는 id의 category는 없습니다");
        }

        return informationRepository.getInformationByParentCategoryFilterLikeCount(pageable, parentCategoryId, userId);
    }

    public Page<InformationBriefResponse> getBriefInformationPageByChildCategoryFilterLikeCount(Pageable pageable, Long childCategoryId, Long userId) {
        if (!categoryRepository.existsById(childCategoryId)) {
            throw new CategoryNotExistsException("해당하는 id의 category는 없습니다");
        }
        return informationRepository.getInformationByChildCategoryFilterLikeCount(pageable, childCategoryId, userId);
    }

    //조회순
    public Page<InformationBriefResponse> getBriefInformationPageByParentCategoryFilterViewCount(Pageable pageable, Long parentCategoryId, Long userId) {
        if (!categoryRepository.existsById(parentCategoryId)) {
            throw new CategoryNotExistsException("해당하는 id의 category는 없습니다");
        }
        return informationRepository.getInformationByParentCategoryFilterViewCount(pageable, parentCategoryId, userId);
    }

    public Page<InformationBriefResponse> getBriefInformationPageByChildCategoryFilterViewCount(Pageable pageable, Long childCategoryId, Long userId) {
        if (!categoryRepository.existsById(childCategoryId)) {
            throw new CategoryNotExistsException("해당하는 id의 category는 없습니다");
        }
        return informationRepository.getInformationByChildCategoryFilterViewCount(pageable, childCategoryId, userId);
    }

    //zoneCategory 별
    public Page<InformationBriefResponse> getBriefInformationPageByParentCategoryAndZoneCategory(Pageable pageable, Long parentCategoryId, Long userId, Long zoneCategoryId) {
        if (!categoryRepository.existsById(parentCategoryId)) {
            throw new CategoryNotExistsException("해당하는 id의 category는 없습니다");
        }

        if (!zoneCategoryRepository.existsById(zoneCategoryId)) {
            throw new ZoneCategoryNotExistsException("해당하는 id의 zoneCategory는 없습니다");
        }

        return informationRepository.getInformationByParentCategoryFilterZoneCategory(pageable, parentCategoryId, userId, zoneCategoryId);
    }

    public Page<InformationBriefResponse> getBriefInformationPageByChildCategoryAndZoneCategory(Pageable pageable, Long childCategoryId, Long userId, Long zoneCategoryId) {
        if (!categoryRepository.existsById(childCategoryId)) {
            throw new CategoryNotExistsException("해당하는 id의 category는 없습니다");
        }

        if (!zoneCategoryRepository.existsById(zoneCategoryId)) {
            throw new ZoneCategoryNotExistsException("해당하는 id의 zoneCategory는 없습니다");
        }

        return informationRepository.getInformationByChildCategoryFilterZoneCategory(pageable, childCategoryId, userId, zoneCategoryId);
    }


    public List<InformationRankResponse> getRankInformation() {
        return informationRepository.getInformationRank();
    }
}
