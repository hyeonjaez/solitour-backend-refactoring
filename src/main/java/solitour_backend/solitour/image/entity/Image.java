package solitour_backend.solitour.image.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import solitour_backend.solitour.image.image_status.ImageStatus;
import solitour_backend.solitour.image.image_status.ImageStatusConverter;
import solitour_backend.solitour.information.entity.Information;
import solitour_backend.solitour.user.entity.User;

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

    @Convert(converter = ImageStatusConverter.class)
    private ImageStatus imageStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "information_id")
    private Information information;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "image_address")
    private String address;

    @Column(name = "image_created_date")
    private LocalDate createdDate;

    public Image(ImageStatus imageStatus, Information information, User user, String address, LocalDate createdDate) {
        this.imageStatus = imageStatus;
        this.information = information;
        this.user = user;
        this.address = address;
        this.createdDate = createdDate;
    }
}
