package nl.rabobank.payments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    @Value("${mysecret2:nothing}")
    private String superSecret;

    @GetMapping
    public String homepage() {
        return "The secret is [" + superSecret + "]";
    }
}
