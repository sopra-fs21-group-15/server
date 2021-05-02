package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
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
    private LobbyService lobbyService;
    private UserService userService;

    // test the get Lobbies correspondence
    @Test
    public void returnJsonArray_of_theLobbies() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setPassword("123");
        lobby.setLobbyname("lobby1");
        lobby.setStatus(LobbyStatus.OPEN);
        lobby.setRounds(5);
        lobby.setSize(5);
        lobby.setTimer(60);
        lobby.setMembers("User");

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
                .andExpect(jsonPath("$[0].status", is(lobby.getStatus().toString())))
                .andExpect(jsonPath("$[0].rounds", is(lobby.getRounds())))
                .andExpect(jsonPath("$[0].size", is(lobby.getSize())))
                .andExpect(jsonPath("$[0].timer", is(lobby.getTimer())))
                .andExpect(jsonPath("$[0].members", is(lobby.getMembers())));
    }

    // test the createUser correspondence
    // /users POST: Code 201 --> Correct Input
    @Test
    public void createLobby_validInput_lobbycreated() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setPassword("Test Password");
        lobby.setLobbyname("testUsername");
        lobby.setSize(5);
        lobby.setRounds(5);
        lobby.setTimer(60);

        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setPassword("Test Password");
        lobbyPostDTO.setLobbyname("testUsername");
        lobbyPostDTO.setSize("5");
        lobbyPostDTO.setRounds("5");
        lobbyPostDTO.setTimer("60");
        lobbyPostDTO.setLobbyChat("60");


        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test Password");
        userPostDTO.setUsername("testUsername");

        given(lobbyService.createLobby(Mockito.any(),Mockito.eq(user.getId()))).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/"+ user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                .andExpect(jsonPath("$.password", is(lobby.getPassword())))
                .andExpect(jsonPath("$.lobbyname", is(lobby.getLobbyname())))
                .andExpect(jsonPath("$.status", is(lobby.getStatus())))
                .andExpect(jsonPath("$.size", is(lobby.getSize())))
                .andExpect(jsonPath("$.rounds", is(lobby.getRounds())))
                .andExpect(jsonPath("$.timer", is(lobby.getTimer())));
    }

    // /users POST: Code 409 --> Wrong Input
    @Test
    public void createLobby_withdublicatedName() throws Exception {
        // given
        Lobby existing_lobby = new Lobby();
        existing_lobby.setId(1L);
        existing_lobby.setPassword("Test");
        existing_lobby.setLobbyname("lobby1");
        existing_lobby.setRounds(5);
        existing_lobby.setStatus(LobbyStatus.OPEN);
        existing_lobby.setSize(5);
        existing_lobby.setTimer(60);

        LobbyPostDTO lobbyPostDTO1 = new LobbyPostDTO();
        lobbyPostDTO1.setPassword("Test");
        lobbyPostDTO1.setLobbyname("lobby1");
        lobbyPostDTO1.setSize("5");
        lobbyPostDTO1.setRounds("5");
        lobbyPostDTO1.setTimer("60");


        User existing_user = new User();
        existing_user.setId(1L);
        existing_user.setPassword("Test Password");
        existing_user.setUsername("testUsername");
        existing_user.setToken("1");
        existing_user.setStatus(UserStatus.OFFLINE);

        UserPostDTO userPostDTO1 = new UserPostDTO();
        userPostDTO1.setPassword("Test Password");
        userPostDTO1.setUsername("testUsername");



        given(lobbyService.createLobby(Mockito.any(),Mockito.eq(existing_user.getId()))).willReturn(existing_lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/"+ existing_user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO1));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());

        System.out.println(lobbyService.getLobbies());

        Lobby creating_lobby = new Lobby();
        creating_lobby.setId(1L);
        creating_lobby.setPassword("");
        creating_lobby.setLobbyname("lobby1");
        creating_lobby.setRounds(5);
        creating_lobby.setStatus(LobbyStatus.OPEN);
        creating_lobby.setSize(5);
        creating_lobby.setTimer(70);

        LobbyPostDTO lobbyPostDTO2 = new LobbyPostDTO();
        lobbyPostDTO2.setPassword("");
        lobbyPostDTO2.setLobbyname("lobby1");
        lobbyPostDTO2.setSize("5");
        lobbyPostDTO2.setRounds("5");
        lobbyPostDTO2.setTimer("70");


        User created_user = new User();
        created_user.setId(1L);
        created_user.setPassword("Test Password2");
        created_user.setUsername("testUsername2");
        created_user.setToken("2");
        created_user.setStatus(UserStatus.OFFLINE);

        UserPostDTO userPostDTO2 = new UserPostDTO();
        userPostDTO2.setPassword("Test Password2");
        userPostDTO2.setUsername("testUsername2");


        given(lobbyService.createLobby(Mockito.any(),Mockito.eq(created_user.getId()))).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        MockHttpServletRequestBuilder postRequest2 = post("/lobbies/"+ created_user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO2));

        mockMvc.perform(postRequest2)
                .andExpect(status().isConflict());

    }


    // test the loginUser correspondence
    // /login PUT: Code 204 --> correct input
    @Test
    public void joinfullLobby() throws Exception {
        // given
        Lobby lobby = new Lobby();

        lobby.setPassword("Lobby Password");
        lobby.setLobbyname("testUsername");
        lobby.setSize(5);
        lobby.setRounds(5);
        lobby.setTimer(60);
        lobby.setStatus(LobbyStatus.FULL);


        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setPassword("Lobby Password");
        lobbyPostDTO.setLobbyname("testUsername");
        lobbyPostDTO.setSize("5");
        lobbyPostDTO.setRounds("5");
        lobbyPostDTO.setTimer("60");


        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test Password");
        userPostDTO.setUsername("testUsername");

        User user2 = new User();
        user2.setId(1L);
        user2.setPassword("Lobby Password");
        user2.setUsername("testUsername2");
        user2.setToken("2");
        user2.setStatus(UserStatus.ONLINE);


        UserPostDTO userPostDTO2 = new UserPostDTO();
        userPostDTO2.setPassword("Lobby Password");
        userPostDTO2.setUsername("testUsername2");

        Lobby lobby2 = new Lobby();
        lobby2.setPassword("Lobby Password");
        lobby2.setLobbyname(user2.getUsername());

        LobbyPostDTO lobbyPostDTO2 = new LobbyPostDTO();
        lobbyPostDTO2.setPassword("Lobby Password");
        lobbyPostDTO2.setLobbyname(user2.getUsername().toString());


        given(lobbyService.add_lobby_members(Mockito.eq(lobby.getId()), Mockito.eq(lobby2))).willReturn(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/" + lobby.getId() + "/joiners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO2));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());


    }

    // /login PUT: Code 404 --> wrong input
    @Test
    public void joinLobby_invalidPassword() throws Exception {
        // given
        Lobby lobby = new Lobby();

        lobby.setPassword("Lobby Password");
        lobby.setLobbyname("testUsername");
        lobby.setSize(5);
        lobby.setRounds(5);
        lobby.setTimer(60);

        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setPassword("Lobby Password");
        lobbyPostDTO.setLobbyname("testUsername");
        lobbyPostDTO.setSize("5");
        lobbyPostDTO.setRounds("5");
        lobbyPostDTO.setTimer("60");



        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test Password");
        userPostDTO.setUsername("testUsername");

        User user2 = new User();
        user2.setId(1L);
        user2.setPassword("Test Password");
        user2.setUsername("testUsername2");
        user2.setToken("2");
        user2.setStatus(UserStatus.ONLINE);


        UserPostDTO userPostDTO2 = new UserPostDTO();
        userPostDTO2.setPassword("Test Password");
        userPostDTO2.setUsername("testUsername2");


        Lobby lobby2 = new Lobby();
        lobby2.setPassword("Lobby Password2");
        lobby2.setLobbyname(user2.getUsername());


        given(lobbyService.add_lobby_members(Mockito.eq(lobby.getId()), Mockito.eq(lobby2))).willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+ lobby.getId()+ "/joiners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized());
    }


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