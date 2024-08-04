package solitour_backend.solitour.gathering.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AllowedSex {
    MALE("male"),
    FEMALE("female"),
    ALL("all");

    private final String name;

    AllowedSex(String name) {
        this.name = name;
    }

    public static AllowedSex fromName(String name) {
        return Arrays.stream(AllowedSex.values())
                .filter(e -> e.name.equals(name))
                .findAny()
                .orElse(null);
    }
}
