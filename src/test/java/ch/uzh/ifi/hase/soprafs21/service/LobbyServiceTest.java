package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

//I commented this test out, since I could not fix it and it was not required for this milestone!
public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private UserRepository userRepository;



    @InjectMocks
    private LobbyService lobbyService;
    private Lobby testLobby;
    private User testUser;
    @InjectMocks
    private UserService userService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testLobby = new Lobby();
        testLobby.setId(2L);
        testLobby.setPassword("testPassword");
        testLobby.setLobbyname("test");
        testLobby.setTimer(60);
        testLobby.setSize(8);
        testLobby.setRounds(5);
        testLobby.setToken("2");


        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");

        // when -> any object is being save in the userRepository -> return the dummy testUser and the dummy testlobby
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        Mockito.when(lobbyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testLobby));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn((testUser));
    }

    @Test
     void createLobby_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser

        Lobby createdLobby = lobbyService.createLobby(testLobby,testUser.getId());

        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getPassword(), createdLobby.getPassword());
        assertEquals(testLobby.getLobbyname(), createdLobby.getLobbyname());
        assertEquals(testLobby.getToken(), createdLobby.getToken());
    }


    @Test
     void createLobby_duplicateLobbyname_throwsException() {
        // given -> a first user has already been created
        Lobby createdLobby = lobbyService.createLobby(testLobby, testUser.getId());

        // when -> setup additional mocks for UserRepository
        Mockito.when(lobbyRepository.findByLobbyname(Mockito.any())).thenReturn(testLobby);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby, 1L));
    }
    @Test
     void update_Lobby_success(){
        Lobby createdLobby = lobbyService.createLobby(testLobby,testUser.getId());

        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());
        Lobby changeslobby = new Lobby();
        changeslobby.setRounds(4);
        changeslobby.setTimer(75);
        changeslobby.setLobbyname("change");
        changeslobby.setSize(10);
        changeslobby.setPassword("null");

        lobbyService.update_lobby(createdLobby.getId(), changeslobby );


        assertEquals(testLobby.getSize(), changeslobby.getSize());
        assertEquals(testLobby.getRounds(), changeslobby.getRounds());
        assertEquals(testLobby.getTimer(), changeslobby.getTimer());
        assertEquals(testLobby.getLobbyname(), changeslobby.getLobbyname());
        assertEquals(testLobby.getPassword(), changeslobby.getPassword());


    }

    @Test
      void joinaPrivateLobby_withPassword_success(){

        Lobby user = new Lobby();
        user.setPassword("testPassword");
        user.setLobbyname(testUser.getUsername());

        lobbyService.addLobbyMembers(testLobby.getId(), user);


        assertEquals(testLobby.getMembers().size(), 1);
        assertEquals(testLobby.getMembers().contains(user.getLobbyname()), true);

    }

    @Test
      void joinaPrivateLobby_withwrongPassword_fail(){
        Lobby user = new Lobby();
        user.setPassword("testPassword1");
        user.setLobbyname(testUser.getUsername());

        assertThrows(ResponseStatusException.class, ()->lobbyService.addLobbyMembers(testLobby.getId(), user));

    }
    @Test
      void joinaPrivateLobby_withoutPassword_fail(){
        Lobby user = new Lobby();
        user.setPassword(null);
        user.setLobbyname(testUser.getUsername());

        assertThrows(ResponseStatusException.class, ()->lobbyService.addLobbyMembers(testLobby.getId(), user));

    }
    @Test
      void joinaFullLobby_fail(){
        Lobby user = new Lobby();
        user.setPassword("testPassword1");
        user.setLobbyname(testUser.getUsername());
        testLobby.setStatus(LobbyStatus.FULL);
        assertThrows(ResponseStatusException.class, ()->lobbyService.addLobbyMembers(testLobby.getId(), user));

    }


    @Test
      void removeMembers_fromLobby_success(){
        testLobby.setSize(3);
        Lobby createdLobby = lobbyService.createLobby(testLobby,testUser.getId());
        createdLobby.setMembers("testUser2");
        createdLobby.setMembers("delete");


        lobbyService.removeLobbyMembers(testLobby.getId(), "delete", false);
        Mockito.verify(userRepository, Mockito.times(2)).save(Mockito.any());

        assertEquals(LobbyStatus.OPEN, createdLobby.getStatus());
        assertEquals(2, createdLobby.getMembers().size());
        assertEquals(testUser.getUsername(), createdLobby.getMembers().get(0));


    }
    @Test
      void removeaUser_whichissnt_in_theLobby(){
        testLobby.setSize(3);
        Lobby createdLobby = lobbyService.createLobby(testLobby,testUser.getId());
        createdLobby.setMembers("testUser2");

        assertThrows(ResponseStatusException.class, ()->lobbyService.removeLobbyMembers(testLobby.getId(), "user", false));

    }

    @Test
    void getnoUser(){
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(null));
        assertThrows(ResponseStatusException.class, ()->lobbyService.getUser(5L));

    }
    @Test
    void getLobbybyID_failed(){
        Mockito.when(lobbyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(null));
        assertThrows(ResponseStatusException.class, ()->lobbyService.getLobby(5L));

    }
    @Test
    void updateLobby_failed(){
        testLobby.setMembers("1");
        testLobby.setMembers("2");
        testLobby.setMembers("3");

        Lobby changeslobby = new Lobby();
        changeslobby.setRounds(null);
        changeslobby.setTimer(null);
        changeslobby.setLobbyname(null);
        changeslobby.setSize(2);
        changeslobby.setPassword(null);
        assertThrows(ResponseStatusException.class, ()->lobbyService.update_lobby(testLobby.getId(),changeslobby));
    }
    @Test
    void smallentheLobbytotheMax(){
        testLobby.setMembers("1");
        testLobby.setMembers("2");
        testLobby.setMembers("3");

        Lobby changeslobby = new Lobby();
        changeslobby.setRounds(null);
        changeslobby.setTimer(null);
        changeslobby.setLobbyname(null);
        changeslobby.setSize(3);
        changeslobby.setPassword(null);
        lobbyService.update_lobby(testLobby.getId(),changeslobby);

        assertEquals(testLobby.getStatus(),LobbyStatus.FULL);
        assertEquals(testLobby.getSize(),changeslobby.getSize());
    }
    @Test
    void updateLobby_onlyLobbyname() {
        testLobby.setMembers("1");
        testLobby.setMembers("2");
        testLobby.setMembers("3");

        Lobby changeslobby = new Lobby();
        changeslobby.setRounds(null);
        changeslobby.setTimer(null);
        changeslobby.setLobbyname("null");
        changeslobby.setSize(null);
        changeslobby.setPassword(null);

        Lobby beforeupdate= new Lobby();
        beforeupdate.setId(2L);
        beforeupdate.setPassword("testPassword");
        beforeupdate.setLobbyname("test");
        beforeupdate.setTimer(60);
        beforeupdate.setSize(8);
        beforeupdate.setRounds(5);
        beforeupdate.setToken("2");
        lobbyService.update_lobby(testLobby.getId(), changeslobby);

        assertNotEquals(testLobby.getLobbyname(), beforeupdate.getLobbyname());
        assertEquals(testLobby.getPassword(), beforeupdate.getPassword());
        assertEquals(testLobby.getToken(), beforeupdate.getToken());
        assertEquals(testLobby.getRounds(), beforeupdate.getRounds());
        assertEquals(testLobby.getSize(), beforeupdate.getSize());
        assertEquals(testLobby.getTimer(), beforeupdate.getTimer());
    }

    @Test
      void publicLobby_lastEmptySpace_success(){
        testLobby.setPassword(null);
        testLobby.setMembers("1");
        testLobby.setMembers("2");
        testLobby.setMembers("3");
        testLobby.setSize(4);


        Lobby user = new Lobby();
        user.setPassword(null);
        user.setLobbyname(testUser.getUsername());

        lobbyService.addLobbyMembers(testLobby.getId(), user);

        assertTrue(testLobby.getMembers().contains(user.getLobbyname()));
        assertEquals(testLobby.getStatus(),LobbyStatus.FULL);

    }
    @Test
      void join_in_the_smaeLobbyagain(){
        testLobby.setPassword(null);
        testLobby.setMembers(testUser.getUsername());
        testLobby.setMembers("2");
        testLobby.setMembers("3");
        testLobby.setSize(4);


        Lobby user = new Lobby();
        user.setPassword(null);
        user.setLobbyname(testUser.getUsername());

        assertThrows(ResponseStatusException.class, ()->lobbyService.addLobbyMembers(testLobby.getId(), user));

    }
    @Test
      void removelastplayer() {
        testLobby.setPassword(null);
        testLobby.setMembers(testUser.getUsername());

        lobbyService.removeLobbyMembers(testLobby.getId(), testUser.getUsername(), true);


        Mockito.verify(lobbyRepository, Mockito.times(1)).delete(Mockito.any());


    }





    }
