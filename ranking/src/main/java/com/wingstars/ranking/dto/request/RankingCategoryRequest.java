package com.wingstars.ranking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingCategoryRequest {
    @NotBlank(message = "Category name must not be blank")
    private String name;

    private String typeCode;
    private Boolean status;
}
