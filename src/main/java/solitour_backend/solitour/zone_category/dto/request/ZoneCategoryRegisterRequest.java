package solitour_backend.solitour.zone_category.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ZoneCategoryRegisterRequest {
    @Min(1)
    @NotNull
    private Integer id;

    @NotBlank
    @Size(max = 20)
    private String name;
}
