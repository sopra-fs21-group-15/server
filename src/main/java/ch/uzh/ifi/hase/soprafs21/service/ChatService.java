package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.repository.ChatRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatService(@Qualifier("chatRepository") ChatRepository chatRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    public Chat getChat(Long chatId) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        Chat chat = null;

        if (optionalChat.isEmpty()) { // if not found
            String nonExistingChat = "The chat you have been looking for does not exist.";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingChat));
        } else { // if found
            chat = optionalChat.get();
        }

        return chat;
    }

    public void createChat(Long chatId) {
        Chat chat = new Chat();
        chat.setChatId(chatId);

        chatRepository.save(chat);
        chatRepository.flush();
    }

    public void addNewMessage(Long chatId, Message newMessage) {
        Chat chat = getChat(chatId);
        chat.setMessage(newMessage);
        chatRepository.save(chat);
        chatRepository.flush();
    }

    public Message createMessage(Message newMessage) {
        newMessage = messageRepository.saveAndFlush(newMessage);
        return newMessage;
    }

    public Chat getNewMessages(Long chatId, String timeStamp) {
        Chat chat = getChat(chatId);
        int index = 0;

        List<Message> newMessages = new ArrayList<>();
        // check if chat is empty
        if (!(chat.getMessage().isEmpty())) {

            DateTimeFormatter formatter = new Standard().getDateTimeFormatter();
            Message message = chat.getMessage().get(index);
            LocalDateTime messageTime = LocalDateTime.parse(message.getTimeStamp(),formatter);
            LocalDateTime searchedTime = LocalDateTime.parse(timeStamp, formatter);


            // search for newer messages
            while(messageTime.isBefore(searchedTime) || messageTime.isEqual(searchedTime)) {
                if (index <= chat.getMessage().size()-1) {
                    message = chat.getMessage().get(index);
                    searchedTime = LocalDateTime.parse(message.getTimeStamp(), formatter);
                    index++;
                }
                else {
                    index = chat.getMessage().size()-1;
                    break;
                }

            }

            // send back List of Messages or Chat?
            newMessages = new ArrayList<>(chat.getMessage().subList(index, chat.getMessage().size()-1));


        // search for newer messages
        while(messageTime.isBefore(searchedTime) || messageTime.isEqual(searchedTime)) {
            index++;
            if (index >= chat.getMessage().size()) {
                break;
            }
            message = chat.getMessage().get(index);
            messageTime = LocalDateTime.parse(message.getTimeStamp(),formatter);
        }

         // send back List of Messages or Chat?
        List<Message> newMessages = new ArrayList<>(chat.getMessage().subList(index, chat.getMessage().size()));

        Chat newChat = new Chat();
        newChat.setChatId(chatId);
        newChat.setMessages(newMessages);
        return newChat;
    }

}
