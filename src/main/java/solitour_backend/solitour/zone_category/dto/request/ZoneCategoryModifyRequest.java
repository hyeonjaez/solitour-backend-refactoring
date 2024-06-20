package solitour_backend.solitour.zone_category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ZoneCategoryModifyRequest {
    @NotBlank
    @Size(max = 20)
    private String name;
}
