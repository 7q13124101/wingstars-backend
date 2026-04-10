package com.wingstars.banner.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.wingstars.banner.dto.BannerDTO;
import com.wingstars.banner.entity.Banner;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    BannerDTO toDTO(Banner banner);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    Banner toEntity(BannerDTO bannerDTO);
}
