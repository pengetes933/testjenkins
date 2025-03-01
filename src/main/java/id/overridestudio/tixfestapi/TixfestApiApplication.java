package id.overridestudio.tixfestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
public class TixfestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TixfestApiApplication.class, args);
    }

}
