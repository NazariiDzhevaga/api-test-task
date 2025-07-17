package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    SUPERVISOR("supervisor"),
    ADMIN("admin"),
    USER("user"),
    EMPTY("");

    private final String name;
}
