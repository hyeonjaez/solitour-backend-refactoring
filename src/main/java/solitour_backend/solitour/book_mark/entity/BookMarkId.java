package solitour_backend.solitour.book_mark.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookMarkId implements Serializable {
    private Long user;
    private Long information;
    private Long gathering;
}
