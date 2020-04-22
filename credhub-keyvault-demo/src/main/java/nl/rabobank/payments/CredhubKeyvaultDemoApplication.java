package nl.rabobank.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CredhubKeyvaultDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CredhubKeyvaultDemoApplication.class, args);
	}

}
