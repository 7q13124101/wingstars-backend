package wingstars.backend.entities;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum Role {
    /**
     * temp disable role CONTRIBUTOR and MODERATOR
     */
    USER("Basic", "user"),
    CONTRIBUTOR("Contributor", "contr"),
    CONTENT("Content", "content"),
    ADMINISTRATOR("Administrator", "admin");

    public final String displayValue;
    @Getter
    public final String shortName; // arbitrary max length = 13

    public static final Map<String, Role> ROLE_MAP;

    Role(String display, String shortName) {
        this.displayValue = display;
        this.shortName = shortName;
    }

    static {
        ROLE_MAP = Arrays.stream(Role.values()).collect(Collectors.toMap(Role::getShortName, r -> r, (k1,k2) -> k1, HashMap::new));
    }

    public static Role getByShortName(String shortName) {
        return ROLE_MAP.get(shortName);
    }

    public static Role fromShortName(String shortName) {

        return switch (shortName) {
            case "user" -> USER;
            case "contr" -> CONTRIBUTOR;
            case "content" -> CONTENT;
            case "admin" -> ADMINISTRATOR;
            default -> throw new IllegalArgumentException("Short[" + shortName + "] not supported.");
        };
    }
}