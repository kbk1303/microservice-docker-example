package local.lyngberg.microservice.docker.interfaceadapters.web;



import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class TestConfig {

  @Bean
  MessageSource messageSource() {
    var ms = new ReloadableResourceBundleMessageSource();
    ms.setBasenames("classpath:i18n/messages"); // test resources below
    ms.setDefaultEncoding("UTF-8");
    ms.setFallbackToSystemLocale(false);
    return ms;
  }
}

