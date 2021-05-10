package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.ChatRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(@Qualifier("chatRepository") ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat getChat(Long chatId) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        Chat value = null;

        if (optionalChat.isEmpty()) { // if not found
            String nonExistingChat = "The chat you have been looking for does not exist.";
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingChat));
        } else { // if found
            value = optionalChat.get();
        }

        return value;
    }
/*
    public void addNewMessage(Long chatId, Message newMessage) {
        Chat chat = getChat(chatId);
        chat.addMessage(newMessage);
    }

    public ArrayList<Message> getNewMessages(Long chat_id, LocalDateTime timeStamp) {
        Chat chat = getChat(chat_id);
        int index = 0;

        // search for newer messages
        while(chat.getMessage().get(index).getTimeStampObject().isBefore(timeStamp)) {
            index++;
        }
         // send back new Messages or Chat?
        ArrayList<Message> newMessages = new ArrayList<>(chat.getMessage().subList(index, chat.getMessage().size()-1));
        return newMessages;
    }
*/
}
