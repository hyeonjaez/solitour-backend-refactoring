package solitour_backend.solitour.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ImageResponse {
    private Long id;
    private String imageStatus;
    private Long informationId;
    private Long userId;
    private String address;
    private LocalDate createdDate;
}