package com.wingstars.media.enums;

import lombok.Getter;

@Getter
public enum ModuleSource {
    CHEERLEADER_AVATAR("Cheerleader profile avatar"),
    CHEERLEADER_AUDIO("Exclusive audio file of cheerleader"),
    CHEERLEADER_GALLERY("Images in cheerleader gallery"),
    BANNER_HOME("Banner displayed on home page"),
    BANNER_PROMO("Banner for promotional programs"),
    USER_AVATAR("Avatar of User or Admin"),
    SYSTEM_ASSET("General system resource files"),
    UNKNOWN("Unknown source");

    private final String description;

    ModuleSource(String description) {
        this.description = description;
    }

}
