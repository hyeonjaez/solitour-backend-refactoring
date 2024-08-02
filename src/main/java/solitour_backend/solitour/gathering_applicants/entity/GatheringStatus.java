package solitour_backend.solitour.gathering_applicants.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GatheringStatus {
    WAIT("대기"),
    CONSENT("승낙"),
    REFUSE("거절");

    private final String name;

    GatheringStatus(String name) {
        this.name = name;
    }

    public static GatheringStatus fromName(String name) {
        return Arrays.stream(GatheringStatus.values())
                .filter(e -> e.name.equals(name))
                .findAny()
                .orElse(null);
    }
}
