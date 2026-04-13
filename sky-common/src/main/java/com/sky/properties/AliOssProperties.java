package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.alioss")
@Data
public class AliOssProperties {

    private String endpoint;      // 地域节点
    private String accessKeyId;   // 账号
    private String accessKeySecret; // 密码
    private String bucketName;    // 存储空间名

}
