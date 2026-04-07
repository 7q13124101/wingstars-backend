package com.wingstars.media.enums;

public enum ModuleSource {
    CHEERLEADER_AVATAR("Anh dai dien cua Idol"),
    CHEERLEADER_AUDIO("File am thanh doc quyen cua Idol"),
    CHEERLEADER_GALLERY("Anh trong bo suu tap cua Idol"),
    BANNER_HOME("Banner hien thi tren trang chu"),
    BANNER_PROMO("Banner cho cac chuong trinh khuyen mai"),
    USER_AVATAR("Anh dai dien cua nguoi dung hoac Admin"),
    SYSTEM_ASSET("Cac file tai nguyen chung cua he thong"),
    UNKNOWN("Khong xac dinh");

    private final String description;

    ModuleSource(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
