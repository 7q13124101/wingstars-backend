package com.wingstars.banner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import com.wingstars.core.entity.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "banners")
public class Banner extends BaseEntity {
    
    private String title;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "link_url")
    private String linkUrl;
    
    @Column(name = "position_code")
    private String positionCode;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    private Integer status;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted;
}