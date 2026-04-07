package com.wingstars.ranking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class CheerleaderRankingCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "type_code", length = 100, unique = true)
    private String typeCode;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean status = true;

    @Builder.Default
    @OneToMany(mappedBy = "rankingCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheerleaderRankingEntry> entries = new ArrayList<>();

    public void addEntry(CheerleaderRankingEntry entry) {
        entries.add(entry);
        entry.setRankingCategory(this);
    }
}
