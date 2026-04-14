package com.wingstars.media.service;

public interface MediaMaintenanceService {
    int updateFileUrlsWithHost(String protocol, String host, String port, int execute);
}
