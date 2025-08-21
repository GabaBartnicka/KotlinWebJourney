package dev.gababartnicka.kotlinwebjourney;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String hello() {
        var greeting = new Greeting();
        return "Spring Boot: " + greeting.greet();
    }
}