package solitour_backend.solitour.gathering_tag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import solitour_backend.solitour.gathering.entity.Gathering;
import solitour_backend.solitour.tag.entity.Tag;

@Entity
@Getter
@Table(name = "gathering_tag")
@NoArgsConstructor
@IdClass(GatheringTagId.class)
public class GatheringTag {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
}
