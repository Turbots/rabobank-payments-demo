package nl.rabobank.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KeyVaultSecretsApp {

    public static void main(String[] args) {
        SpringApplication.run(KeyVaultSecretsApp.class, args);
    }

}
