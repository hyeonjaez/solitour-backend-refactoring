package solitour_backend.solitour.info_tag.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InfoTagId implements Serializable {
    private Long tag;
    private Long information;
}
