package org.example.springai.web;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    @GetMapping
    public String hello() {
        return "";
    }
}
