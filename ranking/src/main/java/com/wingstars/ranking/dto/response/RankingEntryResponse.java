package com.wingstars.ranking.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RankingEntryResponse {
    private Long id;
    private Integer rankPosition;
    private Integer score;
    private String rankingImageUrl;
    private Long cheerleaderId;
    private String cheerleaderName;
    private String defaultAvatarUrl;
}
