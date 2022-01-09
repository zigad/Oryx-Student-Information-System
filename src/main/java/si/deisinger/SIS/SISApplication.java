package si.deisinger.SIS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class SISApplication {

	public static void main(String... args) {
		SpringApplication.run(SISApplication.class, args);
	}
}
