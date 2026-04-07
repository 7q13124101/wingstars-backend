package com.wingstars.media.entity;

import com.wingstars.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "media_assets",
        indexes = {
                @Index(name = "idx_module_active", columnList = "module_source, is_active")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaAsset extends BaseEntity {

    @Column(name = "file_url", nullable = false, unique = true, length = 500)
    private String fileUrl;

    @Column(name = "module_source", length = 50)
    // BANNER, CHEERLEADER, NEWS...
    private String moduleSource;

    @Column(length = 255)
    private String title;

    @Column(name = "jump_url", length = 500)
    private String jumpUrl;

    @Builder.Default
    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Builder.Default
    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted = false;
}
