package solitour_backend.solitour.gathering_applicants.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solitour_backend.solitour.gathering.entity.Gathering;
import solitour_backend.solitour.user.entity.User;

@Entity
@Getter
@Setter
@Table(name = "gathering_applicants")
@NoArgsConstructor
public class GatheringApplicants {

    @Id
    @Column(name = "gathering_applicants_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "gathering_applicants_state")
    @Enumerated(EnumType.STRING)
    private GatheringStatus gatheringStatus;

    public GatheringApplicants(Gathering gathering, User user, GatheringStatus gatheringStatus) {
        this.gathering = gathering;
        this.user = user;
        this.gatheringStatus = gatheringStatus;
    }
}
