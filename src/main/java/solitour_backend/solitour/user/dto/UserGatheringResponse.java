package solitour_backend.solitour.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserGatheringResponse {
    private Long id;
    private String name;
    private Integer age;
    private String sex;
}