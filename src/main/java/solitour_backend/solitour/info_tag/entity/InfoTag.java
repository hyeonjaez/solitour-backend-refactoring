package solitour_backend.solitour.info_tag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import solitour_backend.solitour.information.entity.Information;
import solitour_backend.solitour.tag.entity.Tag;

@Entity
@Getter
@Table(name = "info_tag")
@NoArgsConstructor
@IdClass(InfoTagId.class)
public class InfoTag {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "information_id")
    private Information information;
}
