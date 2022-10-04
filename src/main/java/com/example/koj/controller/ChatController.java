package com.example.koj.controller;

import com.example.koj.entity.Knowledge;
import com.example.koj.model.ChatMessage;
import com.example.koj.repository.KnowledgeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChatController {
    public static final String SYSTEM_SENDER = "SYSTEM";
    KnowledgeRepository knowledgeRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    ChatMessage sendMessage(@Payload ChatMessage message) {
        if (!message.getSender().equalsIgnoreCase(SYSTEM_SENDER)) {
            String[] contentWords = message.getContent().split(" ");

            Set<Knowledge> foundKnowledges = new HashSet<>();
            for (String contentWord : contentWords) {
                String searchTerm = "%" + contentWord + "%";
                foundKnowledges.addAll(knowledgeRepository.getKnowledgeWithQuestionSearch(searchTerm));
            }

            Map<Knowledge, Integer> scoredKnowledges = new HashMap<>();
            for (Knowledge knowledge : foundKnowledges) {
                String question = knowledge.getQuestion();
                int matchCount = 0;
                for (String contentWord : contentWords) {
                    if (question.contains(contentWord)) {
                        matchCount++;
                    }
                }
                scoredKnowledges.put(knowledge, matchCount); // This overrides other matches, keeps last found match
            }

            var bestMatchCount = scoredKnowledges.values().stream().max(Comparator.naturalOrder());
            if (bestMatchCount.isPresent()) {
                var bestMatches = scoredKnowledges.entrySet().stream()
                        .filter(entry -> Objects.equals(entry.getValue(), bestMatchCount.get()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                String reply = "Sorry, I didn't understand.";
                if (bestMatches.size() > 1) {
                    reply = bestMatches.get(0).getAnswer();
                } else if (bestMatches.size() == 1) {
                    reply = bestMatches.get(0).getAnswer();
                }

                return new ChatMessage(reply, SYSTEM_SENDER);
            }
        }
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        message.setSender(SYSTEM_SENDER);
        message.setContent("Hi! How amy I help you?");
        return message;
    }
}
