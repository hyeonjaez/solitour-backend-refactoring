package solitour_backend.solitour.great.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GreatId implements Serializable {
    private Long user;
    private Long information;
    private Long gathering;
}
