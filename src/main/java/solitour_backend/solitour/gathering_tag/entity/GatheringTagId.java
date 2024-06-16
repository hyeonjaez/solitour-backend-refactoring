package solitour_backend.solitour.gathering_tag.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GatheringTagId implements Serializable {
    private Long tag;
    private Long gathering;
}
