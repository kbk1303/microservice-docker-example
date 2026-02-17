package local.lyngberg.microservice.docker.login;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LoginStartApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(LoginStartApplication.class, args);
    }

}
