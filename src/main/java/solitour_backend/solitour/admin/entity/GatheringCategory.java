package solitour_backend.solitour.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "gathering_category")
@NoArgsConstructor
public class GatheringCategory {

    @Id
    @Column(name = "gathering_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_category_id")
    private GatheringCategory parentCategory;

    @Column(name = "gathering_category_name")
    private String name;

    public GatheringCategory(GatheringCategory parentCategory, String name) {
        this.parentCategory = parentCategory;
        this.name = name;
    }
}
