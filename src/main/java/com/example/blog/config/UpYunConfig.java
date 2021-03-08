package com.example.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("upyun")
public class UpYunConfig {

    private String bucketName;

    private String username;

    private String password;

    private String hostName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return "UpYunConfig{" +
                "bucketName='" + bucketName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", hostName='" + hostName + '\'' +
                '}';
    }
}
