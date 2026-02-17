package local.lyngberg.microservice.docker.interfaceadapters.web.i18nconfig;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;


public class I18nAutoConfigurationTest {
     @Test
    void messageSourceBeanExists() {
        try (var ctx = new AnnotationConfigApplicationContext(I18nAutoConfiguration.class)) {
        var ms = ctx.getBean("messageSource");
        assertNotNull(ms);
        }
    }

}
