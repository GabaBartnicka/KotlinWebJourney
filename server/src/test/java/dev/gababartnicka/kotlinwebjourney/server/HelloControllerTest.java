package dev.gababartnicka.kotlinwebjourney.server;

import dev.gababartnicka.kotlinwebjourney.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRoot() throws Exception {
        String expected = "Spring Boot: Hello, Java Server!";
        
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }
}