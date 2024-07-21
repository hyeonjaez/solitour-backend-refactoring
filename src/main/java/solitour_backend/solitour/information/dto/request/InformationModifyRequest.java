package solitour_backend.solitour.information.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import solitour_backend.solitour.image.dto.request.ImageDeleteRequest;
import solitour_backend.solitour.image.dto.request.ImageUseRequest;
import solitour_backend.solitour.tag.dto.request.TagRegisterRequest;

import java.util.List;

@Getter
@NoArgsConstructor
public class InformationModifyRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    private List<ImageUseRequest> useImages;

    private List<ImageDeleteRequest> deleteImages;

    @NotBlank
    @Size(min = 1, max = 20)
    private String address;

    private String content;

    private String tips;

    private List<TagRegisterRequest> tagRegisterRequests;

}
