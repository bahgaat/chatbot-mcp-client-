package com.example.chatbot;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Scanner;


@SpringBootApplication
public class ChatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatbotApplication.class, args);
    }


    @Bean
    public CommandLineRunner cli(@Value("classpath:Deep Nutrition.pdf") Resource docs,
                                 ChatClient.Builder chatClientBuilder, VectorStore vectorStore,
                                 List<McpSyncClient> mcpSyncClients) {
        return args -> {

            vectorStore.add(new TokenTextSplitter().split(new PagePdfDocumentReader(docs).read()));


            ChatMemory chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(new InMemoryChatMemoryRepository())
                    .build();


            MessageChatMemoryAdvisor memoryAdvisor =
                    MessageChatMemoryAdvisor
                            .builder(chatMemory)
                            .build();
            var chatClient = chatClientBuilder
                    .defaultSystem("You are a helpful and knowledgeable nutrition assistant with deep expertise in the book *Deep Nutrition* by Dr. Cate Shanahan." +
                            "Your answers should be based primarily on this book. When you provide advice or information, clearly refer to the relevant concepts or ideas from the book." +
                            "You also have access to external tools via the MCP server, and you can invoke any available methods from those tools when needed to answer the user's questions." +
                            "Always respond in a clear, friendly, and informative way.")

                    .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
                    .defaultAdvisors(memoryAdvisor)// Enable chat memory
                    .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))// Enable RAG
                    .build();

            System.out.println("\nI am your assistant who is expert in nutrition.\n");
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("\nUSER: ");
                    System.out.println("\nASSISTANT: " +
                            chatClient.prompt(scanner.nextLine())
                                    .call()
                                    .content());

                }
            }
        };
    }


}
            
            
