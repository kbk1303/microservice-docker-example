
package local.lyngberg.microservice.docker.auditlog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AuditlogStartApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(AuditlogStartApplication.class, args);
    }
}
