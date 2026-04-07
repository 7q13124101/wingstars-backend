package com.wingstars.ranking.entity;

import com.wingstars.cheerleader.entity.Cheerleader;
import com.wingstars.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "cheerleader_ranking_entries",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_category_cheerleader", columnNames = {"ranking_category_id", "cheerleader_id"})
        },
        indexes = {
                @Index(name = "idx_category", columnList = "ranking_category_id"),
                @Index(name = "idx_cheerleader", columnList = "cheerleader_id")
        }
)
public class CheerleaderRankingEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ranking_category_id", nullable = false)
    private CheerleaderRankingCategory rankingCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cheerleader_id", nullable = false)
    private Cheerleader cheerleader;

    @Column(name = "rank_position")
    private Integer rankPosition;

    @Column(name = "cheerleader_image_url", length = 500)
    private String cheerleaderImageUrl;

    @Builder.Default
    @Column(nullable = false)
    private Integer score = 0;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted = false;
}
