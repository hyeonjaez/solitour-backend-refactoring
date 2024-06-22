package solitour_backend.solitour.place.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PlaceRegisterRequest {

    @NotBlank
    @Size(min = 1, max = 30)
    private String searchId;

    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @NotNull
    @Digits(integer = 10, fraction = 6)
    private BigDecimal xAxis;

    @NotNull
    @Digits(integer = 10, fraction = 6)
    private BigDecimal yAxis;

    @NotBlank
    @Size(min = 1, max = 50)
    private String address;

    @NotNull
    private Boolean isCustom;

}
