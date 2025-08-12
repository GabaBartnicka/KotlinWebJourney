package dev.gababartnicka.kotlinwebjourney.server;

import dev.gababartnicka.kotlinwebjourney.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String hello() {
        Greeting greeting = new Greeting();
        return "Spring Boot: " + greeting.greet();
    }
}