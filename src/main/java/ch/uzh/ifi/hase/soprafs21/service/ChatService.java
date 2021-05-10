package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.ChatRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public Chat getChat(Long chat_id, LocalDateTime timeStamp) {

        //get all chats
        List<Chat> all_chats = this.chatRepository.findAll();

        Chat chat_found = null;
        for(Chat i : all_chats) {
            if (chat_id == i.getId()) {
                chat_found = i;
            }
        }

        int index = 0;
        while(chat_found.getTimeStamps().get(index).isBefore(timeStamp)) {
            index++;
        }

        ArrayList<String> new_messages = new ArrayList<String>(chat_found.getMessageList().subList(index,chat_found.getMessageList().size()-1));
        ArrayList<String> new_writers = new ArrayList<String>(chat_found.getWriterList().subList(index, chat_found.getWriterList().size()-1));
        Chat value = new Chat();
        value.setMessageList(new_messages);
        value.setWriterList(new_writers);
        return value;
    }

    

}
