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
    public int updateFileUrlsWithHost(String protocol, String host, String port, int execute) {
        if (execute != 1) {
            return 0;
        }

        List<MediaAsset> assets = mediaAssetRepository.findAllByIsDeletedFalse();
        int count = 0;

        StringBuilder prefixBuilder = new StringBuilder();
        if (host != null && !host.trim().isEmpty()) {
            String selectedProtocol = (protocol != null && !protocol.trim().isEmpty()) ? protocol.trim() : "http";
            if (!selectedProtocol.endsWith("://")) {
                prefixBuilder.append(selectedProtocol).append("://");
            } else {
                prefixBuilder.append(selectedProtocol);
            }
            
            prefixBuilder.append(host.trim());
            if (port != null && !port.trim().isEmpty()) {
                prefixBuilder.append(":").append(port.trim());
            }
        }
        String newPrefix = prefixBuilder.toString();

        for (MediaAsset asset : assets) {
            String currentUrl = asset.getFileUrl();
            if (currentUrl == null) continue;

            int uploadIndex = currentUrl.indexOf("/uploads/");
            if (uploadIndex != -1) {
                String relativePath = currentUrl.substring(uploadIndex);
                String newUrl = newPrefix + relativePath;

                if (!newUrl.equals(currentUrl)) {
                    asset.setFileUrl(newUrl);
                    mediaAssetRepository.save(asset);
                    count++;
                }
            }
        }

        return count;
    }
}
