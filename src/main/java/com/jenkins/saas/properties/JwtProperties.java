package com.jenkins.saas.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String privateKeyPath;
    private String publicKeyPath;
    private String accessTokenExpiration;
    private String sigingAlgorithm;

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public String getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public String getSigingAlgorithm() {
        return sigingAlgorithm;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    public void setAccessTokenExpiration(String accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public void setSigingAlgorithm(String sigingAlgorithm) {
        this.sigingAlgorithm = sigingAlgorithm;
    }
}
