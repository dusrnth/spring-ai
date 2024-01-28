package org.example.springai.web;

import org.example.springai.retriever.VectorStoreRetriever;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatRagService {

    @Value("classpath:/system-prompt.st")
    private Resource systemPrompt;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ChatRagService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String callResponse(String message) {
        List<Document> similarDocuments = new VectorStoreRetriever(vectorStore).retrieve(message);

        UserMessage userMessage = new UserMessage(message);
        Message systemMessage = getSystemMessage(similarDocuments);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse response = chatClient.call(prompt);
        return response.getResult().getOutput().getContent();
    }

    private Message getSystemMessage(List<Document> similarDocuments) {
        String documents = similarDocuments.stream()
                .map(entry -> entry.getContent())
                .collect(Collectors.joining("\n"));

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        return systemPromptTemplate.createMessage(Map.of("documents", documents));
    }
}
