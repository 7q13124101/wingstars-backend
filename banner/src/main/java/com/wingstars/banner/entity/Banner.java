package com.wingstars.banner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import com.wingstars.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "banners")
public class Banner extends BaseEntity {

    @Setter
    @Getter
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "position_code", nullable = false, length = 50)
    private BannerPosition position;

    @Setter
    @Getter
    @Column(name = "duration_ms")
    private Integer durationMs;

    @Setter
    @Getter
    private Integer status;

    @Column(name = "display_order")
    private Short displayOrder;

    @Setter
    @Getter
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Getter
    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<BannerImage> images = new ArrayList<>();

    public Banner() {
    }

    public BannerPosition getPositionCode() {
        return position;
    }

    public void setPositionCode(BannerPosition position) {
        this.position = position;
    }

    public Short getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Short displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setImages(List<BannerImage> images) {
        this.images.clear();
        if (images == null) {
            return;
        }
        for (BannerImage image : images) {
            addImage(image);
        }
    }

    public void addImage(BannerImage image) {
        image.setBanner(this);
        this.images.add(image);
    }
}
