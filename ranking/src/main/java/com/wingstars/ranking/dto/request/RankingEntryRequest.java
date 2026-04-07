package com.wingstars.ranking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingEntryRequest {
    @NotNull
    private Long cheerleaderId;

    @NotNull
    @Min(1)
    private Integer rankPosition;

    @Min(value = 0, message = "Diem so khong duoc nho hon 0")
    private Integer score = 0;

    @Size(max = 500, message = "URL anh xep hang khong duoc vuot qua 500 ky tu")
    private String cheerleaderImageUrl;
}
