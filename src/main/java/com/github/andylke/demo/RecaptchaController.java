package com.github.andylke.demo;

import java.util.Map;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@EnableConfigurationProperties(RecaptchaProperties.class)
@Slf4j
@AllArgsConstructor
public class RecaptchaController {

  private final RestTemplateBuilder restTemplateBuilder;

  private final RecaptchaProperties recaptchaProperties;

  @GetMapping
  public String load(Model model) {
    model.addAttribute("siteKey", recaptchaProperties.getSiteKey());
    return "index";
  }

  @PostMapping
  public String verify(
      Model model, @RequestParam("g-recaptcha-response") String recaptchaResponse) {
    
    log.info("Recaptcha response = {}", recaptchaResponse);
    Map<String, Object> result =
        restTemplateBuilder
            .build()
            .exchange(
                "https://www.google.com/recaptcha/api/siteverify?secret={secret}&response={response}",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {},
                recaptchaProperties.getSecretKey(),
                recaptchaResponse)
            .getBody();
    log.info("Result = {}", result);

    model.addAttribute("siteKey", recaptchaProperties.getSiteKey());
    model.addAttribute("success", result.get("success"));
    model.addAttribute("errors", result.get("error-codes"));
    return "index";
  }
}
