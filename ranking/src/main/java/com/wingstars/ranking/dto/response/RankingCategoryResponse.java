package com.wingstars.ranking.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingCategoryResponse {
    private Long id;
    private String name;
    private String typeCode;
    private Boolean status;
}
