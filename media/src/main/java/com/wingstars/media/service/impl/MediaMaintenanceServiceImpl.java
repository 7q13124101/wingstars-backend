package com.wingstars.media.service.impl;

import com.wingstars.media.entity.MediaAsset;
import com.wingstars.media.repository.MediaAssetRepository;
import com.wingstars.media.service.MediaMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaMaintenanceServiceImpl implements MediaMaintenanceService {

    private final MediaAssetRepository mediaAssetRepository;

    @Override
    @Transactional
    public int updateFileUrlsWithHost(String host, int execute) {
        if (execute != 1) {
            return 0;
        }

        List<MediaAsset> assets = mediaAssetRepository.findAllWithRelativePath();
        int count = 0;
        
        // Ensure host doesn't end with / if fileUrl starts with /
        String normalizedHost = host;
        if (normalizedHost.endsWith("/")) {
            normalizedHost = normalizedHost.substring(0, normalizedHost.length() - 1);
        }

        for (MediaAsset asset : assets) {
            String currentUrl = asset.getFileUrl();
            if (!currentUrl.startsWith("http")) {
                if (!currentUrl.startsWith("/")) {
                    currentUrl = "/" + currentUrl;
                }
                asset.setFileUrl(normalizedHost + currentUrl);
                mediaAssetRepository.save(asset);
                count++;
            }
        }
        
        return count;
    }
}
