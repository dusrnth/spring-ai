package org.example.springai.web;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
public class ChatBasicController {

    private final ChatClient chatClient;
    private final StreamingChatClient streamingChatClient;

    @Autowired
    public ChatBasicController(ChatClient chatClient, StreamingChatClient streamingChatClient) {
        this.chatClient = chatClient;
        this.streamingChatClient = streamingChatClient;
    }

    @GetMapping("/open-ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/open-ai/generate/content-only")
    public Flux<String> generateStreamContentOnly(@RequestParam(defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(message);
        return streamingChatClient.stream(prompt)
                .filter(chatResponse -> !isFinished(chatResponse))
                .map(chatResponse -> chatResponse.getResult().getOutput().getContent());
    }

    /**
     * https://github.com/Hyune-s-lab/practice-spring-ai/commit/378eeab7b21e0044ac502484ee508a94e54a0d1c
     */
    private boolean isFinished(ChatResponse chatResponse) {
        return chatResponse.getResult().getOutput().getContent() == null
                || "STOP".equals(chatResponse.getResult().getMetadata().getFinishReason());
    }
}