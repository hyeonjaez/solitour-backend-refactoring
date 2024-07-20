package solitour_backend.solitour.admin.dto;

import lombok.*;
import solitour_backend.solitour.user.user_status.UserStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponseDTO {
        private Long id;
        private UserStatus userStatus;
        private String oauthId;
        private String provider;
        private String nickname;
        private String name;
        private Integer age;
        private String sex;
        private String email;
        private String phoneNumber;
        private Boolean isAdmin;
        private LocalDateTime latestLoginAt;
        private LocalDateTime createdAt;
        private LocalDateTime deletedAt;
}
