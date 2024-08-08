package api.poker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PokerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokerApplication.class, args);
	}

}
