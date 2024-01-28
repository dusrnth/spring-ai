package org.example.springai.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatRagController {

    private final ChatRagService service;

    public ChatRagController(ChatRagService service) {
        this.service = service;
    }

    @PostMapping("/open-ai/ask")
    public String askAboutSpecificTerm(@RequestBody UserQuestion question) {
        return service.callResponse(question.question());
    }

    record UserQuestion(String question) { }
}
