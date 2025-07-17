package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {

    MALE("male"),
    FEMALE("female"),
    INVALID_GENDER("invalid-gender"),
    EMPTY("");

    private final String name;
}
