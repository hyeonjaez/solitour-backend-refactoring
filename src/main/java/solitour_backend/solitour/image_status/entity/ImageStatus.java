package solitour_backend.solitour.image_status.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "image_status")
@NoArgsConstructor
public class ImageStatus {
    @Id
    @Column(name = "image_status_id")
    private String id;
}
