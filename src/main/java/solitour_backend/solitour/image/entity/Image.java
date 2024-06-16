package solitour_backend.solitour.image.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import solitour_backend.solitour.gathering.entity.Gathering;
import solitour_backend.solitour.image_status.entity.ImageStatus;
import solitour_backend.solitour.information.entity.Information;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor
public class Image {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_status_id")
    private ImageStatus imageStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "information_id")
    private Information information;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @Column(name = "image_address")
    private String address;

    @Column(name = "image_created_date")
    private LocalDate createdDate;
}
