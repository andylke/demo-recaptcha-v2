package com.github.andylke.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties(prefix = "google.recaptcha")
@Data
public class RecaptchaProperties {

  private String siteKey;

  private String secretKey;
}
