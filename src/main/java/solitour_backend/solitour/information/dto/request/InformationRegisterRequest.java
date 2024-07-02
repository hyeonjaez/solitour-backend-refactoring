package solitour_backend.solitour.information.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import solitour_backend.solitour.place.dto.request.PlaceRegisterRequest;
import solitour_backend.solitour.tag.dto.request.TagRegisterRequest;

import java.util.List;

@Getter
@NoArgsConstructor
public class InformationRegisterRequest {
    private String informationTitle;
    private String informationAddress;
    private String informationContent;
    private String informationTips;


    private Long userId;

    private PlaceRegisterRequest placeRegisterRequest;

    private Long categoryId;

    private Integer zoneCategoryId;

    private List<TagRegisterRequest> tagRegisterRequests;

    private MultipartFile thumbNailImage;

    private List<MultipartFile> contentImages;

}
