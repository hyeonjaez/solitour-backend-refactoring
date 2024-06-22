package solitour_backend.solitour.image.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import solitour_backend.solitour.image.dto.response.ImageResponse;
import solitour_backend.solitour.image.entity.Image;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {

    @Mapping(source = "information.id", target = "informationId")
    @Mapping(source = "user.id", target = "userId")
    ImageResponse toImageResponse(Image image);


    List<ImageResponse> toImageResponseList(List<Image> images);
}
