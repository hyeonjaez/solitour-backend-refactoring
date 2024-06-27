package solitour_backend.solitour.information.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import solitour_backend.solitour.place.dto.request.PlaceRegisterRequest;
import solitour_backend.solitour.tag.dto.request.TagRegisterRequest;

import java.util.List;

@Getter
@NoArgsConstructor
public class InformationRegisterRequest {
    //information
    private String informationTitle;
    private String informationAddress;
    private String informationContent;
    private String informationTips;

    //user
    private Long userId;

    //place
    private PlaceRegisterRequest placeRegisterRequest;

    //category
    private Long categoryId;

    //zoneCategory
    private Integer zoneCategoryId;

    //tag
    private List<TagRegisterRequest> tagRegisterRequests;

    //TODO image 어떻게 해야할지

}
