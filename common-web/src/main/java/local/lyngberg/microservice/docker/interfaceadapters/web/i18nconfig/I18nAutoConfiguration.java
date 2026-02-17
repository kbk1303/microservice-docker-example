package local.lyngberg.microservice.docker.interfaceadapters.web.i18nconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@AutoConfiguration
public class I18nAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(MessageSource.class)
  public MessageSource messageSource() {
    var ms = new ReloadableResourceBundleMessageSource();
    ms.setBasenames("classpath:i18n/messages");
    ms.setDefaultEncoding("UTF-8");
    ms.setFallbackToSystemLocale(false);
    return ms;
  }
}
