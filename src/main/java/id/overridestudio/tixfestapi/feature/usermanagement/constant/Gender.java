package id.overridestudio.tixfestapi.feature.usermanagement.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE ("Male"),
    FEMALE("Female");

    private final String description;

    public static Gender findByDesc(String desc) {
        for (Gender value : values()) {
            if (value.getDescription().equalsIgnoreCase(desc)) {
                return value;
            }
        }
        return null;
    }
}
