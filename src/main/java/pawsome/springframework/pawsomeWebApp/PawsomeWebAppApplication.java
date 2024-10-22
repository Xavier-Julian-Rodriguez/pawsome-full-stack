package pawsome.springframework.pawsomeWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pawsome.springframework.pawsomeWebApp")
public class PawsomeWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PawsomeWebAppApplication.class, args);
	}

}
