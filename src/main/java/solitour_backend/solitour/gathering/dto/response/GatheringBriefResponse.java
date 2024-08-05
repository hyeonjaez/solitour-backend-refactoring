package solitour_backend.solitour.gathering.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import solitour_backend.solitour.gathering.entity.AllowedSex;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GatheringBriefResponse {
    private Long gatheringId;
    private String title;
    private String zoneCategoryParentName;
    private String zoneCategoryChildName;
    private Integer viewCount;
    private Boolean isBookMark;
    private Integer likeCount;

    private String gatheringCategoryName;
    private String userName;

    private LocalDateTime scheduleStartDate;
    private LocalDateTime scheduleEndDate;

    private LocalDateTime deadline;

    private AllowedSex allowedSex;

    private Integer startAge;
    private Integer endAge;
    private Integer personCount;
    private Integer nowPersonCount;


}
