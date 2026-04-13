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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "banners")
public class Banner extends BaseEntity {

    private String title;
    @Column(name = "link_url")
    private String linkUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "position_code", nullable = false, length = 50)
    private BannerPosition position;

    private Integer status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<BannerImage> images = new ArrayList<>();

    public Banner() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public BannerPosition getPositionCode() {
        return position;
    }

    public void setPositionCode(BannerPosition position) {
        this.position = position;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<BannerImage> getImages() {
        return images;
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
