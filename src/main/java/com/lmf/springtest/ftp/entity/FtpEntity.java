package com.lmf.springtest.ftp.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author lmf
 * @date 2022/8/2
 */
// @Data
// @Service
@Configuration
public class FtpEntity implements Serializable {
    @Value("${ftp.ftpIp}")
    private String ftpIp;
    @Value("${ftp.ftpUser}")
    private String ftpUser;
    @Value("${ftp.ftpPwd}")
    private String ftpPwd;
    @Value("${ftp.ftpPort}")
    private Integer ftpPort;

}
