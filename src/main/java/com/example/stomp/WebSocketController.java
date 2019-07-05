package com.example.stomp;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
public class WebSocketController {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/chat.sendMessage")
  @SendTo("/topic/publicChatRoom")
  public ChatMessage sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    log.info("headerAccessor = {}", headerAccessor.getSessionId());
    return chatMessage;
  }

  @MessageMapping("/chat.sendMessage.own")
//  @SendToUser("/topic/privateMessage/")
  public ChatMessage sendMessageOwn(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    log.info("headerAccessor = {}", headerAccessor.getSessionId());
    messagingTemplate.convertAndSend("/topic/privateMessage/" + chatMessage.getSender(), chatMessage);
//    messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/topic/privateMessage/" + headerAccessor.getSessionId(), chatMessage);
//    messagingTemplate.convertAndSendToUser(headerAccessor.getSessionAttributes().get("sessionId").toString(), "/topic/privateMessage/" + headerAccessor.getSessionAttributes().get("sessionId").toString(), chatMessage);
//    messagingTemplate.convertAndSendToUser(headerAccessor.getSessionAttributes().get("username").toString(), "/topic/privateMessage/" + headerAccessor.getSessionAttributes().get("username").toString(), chatMessage);
//    messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/topic/privateMessage/" + headerAccessor.getSessionId(), chatMessage);
    return chatMessage;
  }

  @MessageMapping("/chat.addUser")
  @SendTo("/topic/publicChatRoom")
  public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    // Add username in web socket session
    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    return chatMessage;
  }

  @KafkaListener(topics = "chat")
  public void notification(String message) {

    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setType(ChatMessage.MessageType.NOTIFICATION);
    chatMessage.setSender("Admin");
    chatMessage.setContent(message);

    messagingTemplate.convertAndSend("/topic/publicChatRoom", chatMessage);
  }

  @KafkaListener(topics = "weather-station")
  public void weatherStation(String message) {

    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setType(ChatMessage.MessageType.NOTIFICATION);
    chatMessage.setSender("Weather station");
    chatMessage.setContent(message);

    messagingTemplate.convertAndSend("/topic/weatherStation", chatMessage);
  }

  @KafkaListener(topics = "traffic-logger")
  public void trafficLogger(String message) {

    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setType(ChatMessage.MessageType.NOTIFICATION);
    chatMessage.setSender("Traffic logger");
    chatMessage.setContent(message);

    messagingTemplate.convertAndSend("/topic/trafficLogger", chatMessage);
  }
}
