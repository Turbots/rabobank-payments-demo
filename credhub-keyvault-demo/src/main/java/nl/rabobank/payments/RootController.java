package nl.rabobank.payments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class RootController {

    @Value("${user.home}")
    private String homeDirectory;

    @GetMapping
    public List<String> getRoot() throws IOException {
        return Files.list(Paths.get(homeDirectory + File.separator + "certificates")).map(Path::toString).collect(Collectors.toList());
    }
}
