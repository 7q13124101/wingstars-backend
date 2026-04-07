package com.wingstars.ranking.entity;

import com.wingstars.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "cheerleader_ranking_categories")
public class CheerleaderRankingCategory extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "type_code", length = 100, unique = true)
    private String typeCode;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean status = true;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted = false;

    @Builder.Default
    @OneToMany(mappedBy = "rankingCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheerleaderRankingEntry> entries = new ArrayList<>();

    public void addEntry(CheerleaderRankingEntry entry) {
        entries.add(entry);
        entry.setRankingCategory(this);
    }
}
