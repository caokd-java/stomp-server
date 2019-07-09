package com.example.stomp;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@SpringBootApplication
@Log4j2
public class StompApplication {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  public static void main(String[] args) {
    SpringApplication.run(StompApplication.class, args);
  }

  @KafkaListener(topics = "notification")
  public void pushNotification (ChatMessageResponse chatMessageResponse) {
    log.info("Message listened from Kafka {}", chatMessageResponse);
    messagingTemplate.convertAndSendToUser(chatMessageResponse.getSessionId(), "/queue/privateMessage", chatMessageResponse, createMessageHeader(chatMessageResponse));
  }

  private MessageHeaders createMessageHeader(ChatMessageResponse chatMessageResponse) {
    SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.create();
    simpMessageHeaderAccessor.setSessionId(chatMessageResponse.getSessionId());
    return simpMessageHeaderAccessor.getMessageHeaders();
  }
}
