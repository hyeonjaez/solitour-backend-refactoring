package solitour_backend.solitour.zone_category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "zone_category")
@NoArgsConstructor
@AllArgsConstructor
public class ZoneCategory {

    @Id
    @Column(name = "zone_category_id")
    private Integer id;

    @Column(name = "zone_category_name")
    private String name;
}
