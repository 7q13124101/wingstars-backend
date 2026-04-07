package com.wingstars.media.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MediaUploadResponse {
    private Long mediaId;
    private String fileUrl;
    private String moduleSource;
    private String title;
}
