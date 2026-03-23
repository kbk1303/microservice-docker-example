package local.lyngberg.microservice.docker.login.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeansConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
