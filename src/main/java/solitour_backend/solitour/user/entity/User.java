package solitour_backend.solitour.user.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Entity
@Getter
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@EnableJpaAuditing
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_age")
    private Integer age;

    @Column(name = "user_sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_phone_number")
    private String phoneNumber;

    @CreatedDate
    @Column(name = "user_created_at")
    private LocalDateTime createdAt;
}
