package ch.uzh.ifi.hase.soprafs21.service;



import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.repository.ChatRepository;


import ch.uzh.ifi.hase.soprafs21.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

//I commented this test out, since I could not fix it and it was not required for this milestone!
public class ChatServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChatRepository chatRepository ;



    @InjectMocks
    private ChatService chatService;
    private Chat testChat;
    private Message testMessage;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testChat = new Chat();
        testChat.setChatId(1L);

        testMessage = new Message();
        testMessage.setMessage("Test");
        testMessage.setTimeStamp("2021-05-17 14:52:10:000");

        testChat.setMessage(testMessage);

        // when -> any object is being save in the userRepository -> return the dummy testUser and the dummy testlobby
        Mockito.when(chatRepository.save(Mockito.any())).thenReturn(testChat);
        Mockito.when(chatRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testChat));
        Mockito.when(messageRepository.saveAndFlush(Mockito.any())).thenReturn(testMessage);
    }

    @Test
     void getGame_byGameID(){
        Chat foundchat = chatService.getChat(testChat.getChatId());

        assertEquals(foundchat.getMessage().get(0), testMessage);

    }
    @Test
     void getGame_byGameID_failed(){
        Mockito.when(chatRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> chatService.getChat(6L));

    }
    @Test
     void createChat_success(){
        chatService.createChat(1L);
        Mockito.verify(chatRepository,Mockito.times(1)).save(Mockito.any());

    }

    @Test
     void add_message(){
        Chat chat = chatService.getChat(testChat.getChatId());
        String time2 = LocalTime.now().toString();
        Message testMessage2 = new Message();
        testMessage2.setMessage("new Text");
        testMessage2.setTimeStamp(time2);

        chatService.addNewMessage(chat.getChatId(), testMessage2);

        assertTrue(chat.getMessage().contains(testMessage));
        assertTrue(chat.getMessage().contains(testMessage2));

    }
    @Test
     void create_newMessage(){
        Message newMessage = chatService.createMessage(testMessage);
        assertEquals(newMessage.getMessage(),testMessage.getMessage());
        assertEquals(newMessage.getTimeStamp(), testMessage.getTimeStamp());
    }

    @Test
     void getNewMessages_Test(){

        String searchedtimer = "2021-05-17 14:54:10:000";

        Message newMessage = new Message();
        newMessage.setTimeStamp("2021-05-17 14:54:10:000");
        newMessage.setMessage("Test 89789");
        chatService.addNewMessage(testChat.getChatId(), newMessage);
        Message newMessage1 = new Message();
        newMessage1.setTimeStamp("2021-05-17 14:57:10:000");
        newMessage1.setMessage("Test 897889");
        chatService.addNewMessage(testChat.getChatId(), newMessage1);


        Chat getMessages = chatService.getNewMessages(testChat.getChatId(), searchedtimer);

        assertFalse(getMessages.getMessage().contains(testMessage));
        assertFalse(getMessages.getMessage().contains(newMessage));
        assertTrue(getMessages.getMessage().contains(newMessage1));

    }


    @Test
     void getaemptyChatback_ifthereareNoMessages(){

        String searchedtimer = "2021-05-17 14:54:10:000";

        Chat emptyChat = new Chat();
        emptyChat.setChatId(2L);

        Mockito.when(chatRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(emptyChat));


        Chat getMessages = chatService.getNewMessages(emptyChat.getChatId(), searchedtimer);

        assertFalse(getMessages.getMessage().contains(testMessage));
        assertTrue(getMessages.getMessage().isEmpty());

    }


    @Test
     void getNewMessages_noMessage(){

        String searchedtimer = "2021-05-17 15:54:10:000";

        Message newMessage = new Message();
        newMessage.setTimeStamp("2021-05-17 14:56:10:000");
        newMessage.setMessage("Test 89789");
        chatService.addNewMessage(testChat.getChatId(), newMessage);
        Message newMessage1 = new Message();
        newMessage1.setTimeStamp("2021-05-17 14:57:10:000");
        newMessage1.setMessage("Test 89789");
        chatService.addNewMessage(testChat.getChatId(), newMessage1);


        Chat getMessages = chatService.getNewMessages(testChat.getChatId(), searchedtimer);

        assertFalse(getMessages.getMessage().contains(testMessage));
        assertFalse(getMessages.getMessage().contains(newMessage));
        assertFalse(getMessages.getMessage().contains(newMessage1));


    }
    @Test
     void getNewMessages_allMessage(){

        String searchedtimer = "2021-05-17 12:54:10:000";

        Message newMessage = new Message();
        newMessage.setTimeStamp("2021-05-17 14:56:10:000");
        newMessage.setMessage("Test 89789");
        chatService.addNewMessage(testChat.getChatId(), newMessage);
        Message newMessage1 = new Message();
        newMessage1.setTimeStamp("2021-05-17 14:57:10:000");
        newMessage1.setMessage("Test 89789");
        chatService.addNewMessage(testChat.getChatId(), newMessage1);


        Chat getMessages = chatService.getNewMessages(testChat.getChatId(), searchedtimer);

        assertTrue(getMessages.getMessage().contains(testMessage));
        assertTrue(getMessages.getMessage().contains(newMessage));
        assertTrue(getMessages.getMessage().contains(newMessage1));

    }
    @Test
    void test_enteringLobbyMessage(){
        List<Message> nomessage= new ArrayList<>();
        testChat.setMessages(nomessage);
        Mockito.when(chatRepository.saveAndFlush(Mockito.any())).thenReturn(testChat);


        assertTrue(testChat.getMessage().isEmpty());
        chatService.enteringLobbyMessage(testChat.getChatId(), "User1");
        assertFalse(testChat.getMessage().isEmpty());
        assertEquals("BOT",testChat.getMessage().get(0).getWriterName());
        assertTrue(testChat.getMessage().get(0).getMessage().contains("User1"));
        assertTrue(testChat.getMessage().get(0).getMessage().contains("entered"));

    }
    @Test
    void test_leavingLobbyMessage(){
        List<Message> nomessage= new ArrayList<>();
        testChat.setMessages(nomessage);
        Mockito.when(chatRepository.saveAndFlush(Mockito.any())).thenReturn(testChat);


        assertTrue(testChat.getMessage().isEmpty());
        chatService.leavingLobbyMessage(testChat.getChatId(), "User1");
        assertFalse(testChat.getMessage().isEmpty());
        assertEquals("BOT",testChat.getMessage().get(0).getWriterName());
        assertTrue(testChat.getMessage().get(0).getMessage().contains("User1"));
        assertTrue(testChat.getMessage().get(0).getMessage().contains("left the lobby"));

    }
    @Test
    void test_leavingGameMessage(){
        List<Message> nomessage= new ArrayList<>();
        testChat.setMessages(nomessage);
        Mockito.when(chatRepository.saveAndFlush(Mockito.any())).thenReturn(testChat);


        assertTrue(testChat.getMessage().isEmpty());
        chatService.leavingGameMessage(testChat.getChatId(), "User1");
        assertFalse(testChat.getMessage().isEmpty());
        assertEquals("BOT",testChat.getMessage().get(0).getWriterName());
        assertTrue(testChat.getMessage().get(0).getMessage().contains("User1"));
        assertTrue(testChat.getMessage().get(0).getMessage().contains("left the game"));

    }
    @Test
    void test_quessing_message(){
        List<Message> nomessage= new ArrayList<>();
        testChat.setMessages(nomessage);
        Mockito.when(chatRepository.saveAndFlush(Mockito.any())).thenReturn(testChat);


        assertTrue(testChat.getMessage().isEmpty());
        chatService.guessingWordMessage(testChat.getChatId(), "User1");

        assertFalse(testChat.getMessage().isEmpty());
        assertEquals("BOT",testChat.getMessage().get(0).getWriterName());
        assertTrue(testChat.getMessage().get(0).getMessage().contains("User1"));
        assertTrue(testChat.getMessage().get(0).getMessage().contains("word."));

    }

    @Test
    void get_Drawer_Message(){
        List<Message> nomessage= new ArrayList<>();
        testChat.setMessages(nomessage);
        Mockito.when(chatRepository.saveAndFlush(Mockito.any())).thenReturn(testChat);


        assertTrue(testChat.getMessage().isEmpty());
        chatService.currentDrawerMessage(testChat.getChatId(), "User1");

        assertFalse(testChat.getMessage().isEmpty());
        assertEquals("BOT",testChat.getMessage().get(0).getWriterName());
        assertTrue(testChat.getMessage().get(0).getMessage().contains("User1"));
        assertTrue(testChat.getMessage().get(0).getMessage().contains("drawer"));


    }

     @Test
     void get_Solution_Message(){
     List<Message> nomessage= new ArrayList<>();
     testChat.setMessages(nomessage);
     Mockito.when(chatRepository.saveAndFlush(Mockito.any())).thenReturn(testChat);


     assertTrue(testChat.getMessage().isEmpty());
     chatService.revealingSolutionMessage(testChat.getChatId(), "User1");

     assertFalse(testChat.getMessage().isEmpty());
     assertEquals("BOT",testChat.getMessage().get(0).getWriterName());
     assertTrue(testChat.getMessage().get(0).getMessage().contains("User1"));
     assertTrue(testChat.getMessage().get(0).getMessage().contains("correct"));


     }



}
