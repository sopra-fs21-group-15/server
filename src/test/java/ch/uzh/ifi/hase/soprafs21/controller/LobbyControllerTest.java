package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.ChatDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.LobbyDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private ChatService chatService;

    @MockBean
    private LobbyService lobbyService;

    // test the getUsers correspondence
    @Test
    public void givenLobbies_whenGetLobbies_thenReturnJsonArray() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setPassword("Firstname Lastname");
        lobby.setLobbyname("firstname@lastname");
        lobby.setTimer(50);
        lobby.setRounds(5);
        lobby.setSize(4);
        lobby.setMembers("Test");


        List<Lobby> allLobbys = Collections.singletonList(lobby);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(lobbyService.getLobbies()).willReturn(allLobbys);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON);

        // then

        
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].password", is(lobby.getPassword())))
                .andExpect(jsonPath("$[0].lobbyname", is(lobby.getLobbyname())))
                .andExpect(jsonPath("$[0].timer", is(lobby.getTimer())))
                .andExpect(jsonPath("$[0].rounds", is(lobby.getRounds())))
                .andExpect(jsonPath("$[0].size", is(lobby.getSize())));
    }

    // test the create Lobby correspondence
    // /lobby POST: Code 201 --> Correct Input
    @Test
    public void createLobby_validInput_Lobbycreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);


        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setPassword("Firstname Lastname");
        lobby.setLobbyname("firstname@lastname");
        lobby.setTimer(50);
        lobby.setRounds(5);
        lobby.setSize(4);
        lobby.setMembers("Test");


        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setPassword(lobby.getPassword());
        lobbyPostDTO.setTimer("50");
        lobbyPostDTO.setRounds("5");
        lobbyPostDTO.setSize("4");
        lobbyPostDTO.setLobbyname(lobby.getLobbyname());



        given(lobbyService.createLobby(Mockito.any(),Mockito.any())).willReturn(lobby);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                .andExpect(jsonPath("$.password", is(lobby.getPassword())))
                .andExpect(jsonPath("$.lobbyname", is(lobby.getLobbyname())))
                .andExpect(jsonPath("$.size", is(lobby.getSize())))
                .andExpect(jsonPath("$.rounds", is(lobby.getRounds())))
                .andExpect(jsonPath("$.timer", is(lobby.getTimer())));
    }
    @Test
    public void updateLobby_validInput() throws Exception {

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setSize("6");
        lobbyPostDTO.setRounds("10");
        lobbyPostDTO.setTimer("120");
        Lobby lobbyInput = LobbyDTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        doNothing().when(lobbyService).update_lobby(2L, lobbyInput);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        mockMvc.perform(putRequest).andExpect(status().isOk()); }

        @Test
        public void gettheLobby_data() throws Exception{

            // given
            User user = new User();
            user.setId(1L);
            user.setPassword("Test Password");
            user.setUsername("testUsername");
            user.setToken("1");
            user.setStatus(UserStatus.ONLINE);


            Lobby lobby = new Lobby();
            lobby.setId(2L);
            lobby.setPassword("Firstname Lastname");
            lobby.setLobbyname("firstname@lastname");
            lobby.setTimer(50);
            lobby.setRounds(5);
            lobby.setSize(4);
            lobby.setMembers("Test");


            LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
            lobbyPostDTO.setPassword(lobby.getPassword());
            lobbyPostDTO.setTimer("50");
            lobbyPostDTO.setRounds("5");
            lobbyPostDTO.setSize("4");
            lobbyPostDTO.setLobbyname(lobby.getLobbyname());



            given(lobbyService.getLobby(Mockito.any())).willReturn(lobby);


            // when/then -> do the request + validate the result
            MockHttpServletRequestBuilder getRequest = get("/lobbies/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(lobby));

            // then
            mockMvc.perform(getRequest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                    .andExpect(jsonPath("$.password", is(lobby.getPassword())))
                    .andExpect(jsonPath("$.lobbyname", is(lobby.getLobbyname())))
                    .andExpect(jsonPath("$.size", is(lobby.getSize())))
                    .andExpect(jsonPath("$.rounds", is(lobby.getRounds())))
                    .andExpect(jsonPath("$.timer", is(lobby.getTimer())));
        }

    @Test
    public void JoinaLobby() throws Exception {
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setSize("6");
        lobbyPostDTO.setRounds("10");
        lobbyPostDTO.setTimer("120");
        lobbyPostDTO.setLobbyname("Joiner");
        Lobby lobbyInput = LobbyDTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setPassword("Firstname Lastname");
        lobby.setLobbyname("firstname@lastname");
        lobby.setTimer(50);
        lobby.setRounds(5);
        lobby.setSize(4);
        lobby.setMembers("Test");

        doNothing().when(lobbyService).addLobbyMembers(2L, lobbyInput);
        MockHttpServletRequestBuilder putRequest = put("/lobbies/2/joiners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobby));

        // then addition needed
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());}


    @Test
    public void Quit_Lobby() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setPassword("Firstname Lastname");
        lobby.setLobbyname("firstname@lastname");
        lobby.setTimer(50);
        lobby.setRounds(5);
        lobby.setSize(4);
        lobby.setMembers("Test");
        lobby.setMembers("Quitboy");


        UserPostDTO quit= new UserPostDTO();
        quit.setUsername("Quitboy");
        quit.setPassword("abc");

        User quiting = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(quit);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/2/leavers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobby));


        mockMvc.perform(putRequest).andExpect(status().isNoContent()); }


    @Test
    public void chatPost() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setPassword("Firstname Lastname");
        lobby.setLobbyname("firstname@lastname");
        lobby.setTimer(50);
        lobby.setRounds(5);
        lobby.setSize(4);
        lobby.setMembers("Test");
        lobby.setMembers("Quitboy");
        given(lobbyService.getLobby(Mockito.any())).willReturn(lobby);

        MessagePostDTO messagePostDTO= new MessagePostDTO();
        messagePostDTO.setMessage("AAAAA");
        messagePostDTO.setWriterName("ABC");
        messagePostDTO.setTimeStamp("05.02.1998 12:00:00:0000");

        Message inputmessage = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);

        Chat newMessages = chatService.getNewMessages(lobby.getId(), inputmessage.getTimeStamp());
        ChatGetDTO newChat= ChatDTOMapper.INSTANCE.convertEntityToChatGetDTO(newMessages);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/2/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobby));


        mockMvc.perform(postRequest).andExpect(status().isOk()); }

        @Test
    public void chatPut() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setPassword("Firstname Lastname");
        lobby.setLobbyname("firstname@lastname");
        lobby.setTimer(50);
        lobby.setRounds(5);
        lobby.setSize(4);
        lobby.setMembers("Test");
        lobby.setMembers("Quitboy");

        MessagePostDTO messagePostDTO= new MessagePostDTO();
        messagePostDTO.setMessage("AAAAA");
        messagePostDTO.setWriterName("ABC");
        messagePostDTO.setTimeStamp("05.02.1998 12:00:00:0000");

        Message inputmessage = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);

        Message message = chatService.createMessage(inputmessage);
        chatService.addNewMessage(lobby.getId(), message );

        MockHttpServletRequestBuilder putRequest = put("/lobbies/2/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobby));


        mockMvc.perform(putRequest).andExpect(status().isOk()); }






    /**
    // /users POST: Code 409 --> Wrong Input
    @Test
    public void createUser_invalidInput_existingUserCreated() throws Exception {
        // given
        User existing_user = new User();
        existing_user.setId(1L);
        existing_user.setPassword("Test Password");
        existing_user.setUsername("testUsername");
        existing_user.setToken("1");
        existing_user.setStatus(UserStatus.OFFLINE);

        UserPostDTO userPostDTO1 = new UserPostDTO();
        userPostDTO1.setPassword("Test Password");
        userPostDTO1.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(existing_user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO1));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());

        System.out.println(userService.getAllUsers());

        User created_user = new User();
        created_user.setId(1L);
        created_user.setPassword("Test Password2");
        created_user.setUsername("testUsername");
        created_user.setToken("2");
        created_user.setStatus(UserStatus.OFFLINE);

        UserPostDTO userPostDTO2 = new UserPostDTO();
        userPostDTO2.setPassword("Test Password");
        userPostDTO2.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        MockHttpServletRequestBuilder postRequest2 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO2));

        mockMvc.perform(postRequest2)
                .andExpect(status().isConflict());

    }

    **/



    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "userpassword": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}