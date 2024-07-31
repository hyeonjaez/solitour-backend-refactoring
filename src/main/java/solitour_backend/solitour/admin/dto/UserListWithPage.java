package solitour_backend.solitour.admin.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class UserListWithPage {

    private Long count;
    private List<UserListResponseDTO> users;
}
