package solitour_backend.solitour.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import solitour_backend.solitour.user.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserGatheringResponse mapToUserGatheringResponse(User user);
}