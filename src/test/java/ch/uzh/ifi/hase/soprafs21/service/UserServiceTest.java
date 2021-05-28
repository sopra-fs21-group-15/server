package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    private User testUser2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setPassword("testPassword2");
        testUser2.setUsername("testUsername2");


        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
    }

    @Test
     void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());


    }


    @Test
     void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
     void getUsers_byID(){
        User createdUser = userService.createUser(testUser);
        User foundUser = userService.getUserById(testUser.getId());

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());

        assertEquals(foundUser.getId(), createdUser.getId());
        assertEquals(foundUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());

    }
    @Test
     void getUsers_byID_unsuccessfull(){
        User createdUser = userService.createUser(testUser);
        User foundUser = userService.getUserById(1000L);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUserById(1L));

    }


    @Test
      void logoutUser_setUserStatus_toOFFLINE_success(){
        // given the user is created automatically ONLINE
        User createdUser = userService.createUser(testUser);

        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        userService.logout(testUser.getId());

        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());

    }
    @Test
     void  loginUser_sucessfull(){
        // given the a user is created
        User createdUser = userService.createUser(testUser);
        createdUser.setStatus(UserStatus.OFFLINE);
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
        // when --> Setup additional mocks
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        User logedinUser = userService.loginRequest(createdUser);

        assertEquals(testUser.getPassword(), logedinUser.getPassword());
        assertEquals(testUser.getUsername(), logedinUser.getUsername());
        assertNotNull(logedinUser.getToken());
        assertEquals(UserStatus.ONLINE, logedinUser.getStatus());



    }

    @Test
     void loginRequest_Usernotregistered_throwError(){

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.loginRequest(testUser));
    }
    @Test
     void loginRequest_wrongPassword_throwError(){
        User createdUser = userService.createUser(testUser);

        User fake_User = new User();
        fake_User.setId(1L);
        fake_User.setPassword("abc");
        fake_User.setUsername("testUsername");
        fake_User.setStatus(UserStatus.OFFLINE);

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(fake_User);

        assertThrows(ResponseStatusException.class, () -> userService.loginRequest(createdUser));
        assertEquals(UserStatus.OFFLINE, fake_User.getStatus());
    }
    @Test
     void updateUser_successfull(){
        User userchanges = new User();
        userchanges.setUsername("Updated");
        userchanges.setBirthDate("01.01.2000");

        assertNotEquals(testUser.getUsername(), userchanges.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
        userService.updateUser(testUser.getId(), userchanges);

        assertEquals(testUser.getUsername(), userchanges.getUsername());
        assertEquals(testUser.getBirthDate(), userchanges.getBirthDate());


    }

    @Test
     void updateUser_successfull_withNullvalues(){
        User userchanges = new User();
        userchanges.setUsername(null);
        userchanges.setBirthDate(null);

        assertNotEquals(testUser.getUsername(), userchanges.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
        userService.updateUser(testUser.getId(), userchanges);

        assertEquals(testUser.getUsername(), testUser.getUsername());
        assertEquals(testUser.getBirthDate(), testUser.getBirthDate());


    }


    @Test
     void updateUser_unsuccessfull(){
        User existinguser = new User();
        existinguser.setUsername("Updated");
        String unchanged= testUser.getUsername();

        User userchanges = new User();
        userchanges.setUsername("Updated");
        userchanges.setBirthDate("01.01.2000");

        assertNotEquals(testUser.getUsername(), userchanges.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(existinguser);

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(testUser.getId(),userchanges));
        assertEquals(testUser.getUsername(),unchanged);

}
@Test
     void addUserasFriend_success(){

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser2));
        userService.addUserToFriendRequestList(testUser2.getId(), testUser);

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser2);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        userService.addUserToFriendsList(testUser.getId(), testUser2);
        assertTrue(testUser2.getFriendsList().contains(testUser.getUsername()));
        assertTrue(testUser.getFriendsList().contains(testUser2.getUsername()));

}
@Test
     void requestFriendWhenUserIsAlreadyFriend1(){
    testUser.setFriendsList("testUsername2");

    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser2);
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
    assertThrows(ResponseStatusException.class, () -> userService.addUserToFriendRequestList(testUser.getId(),testUser2));
}
@Test
void requestFriendWhenUserIsAlreadyFriend2() {
    testUser2.setFriendsList("testUsername");

    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser2));
    assertThrows(ResponseStatusException.class, () -> userService.addUserToFriendRequestList(testUser2.getId(), testUser));
}
@Test
void requestFriendWhenUserIsAlreadyFriend3() {
    testUser2.setFriendsList("testUsername");
    testUser.setFriendsList("testUsername2");

    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser2));
    assertThrows(ResponseStatusException.class, () -> userService.addUserToFriendRequestList(testUser2.getId(), testUser));
}
@Test
void requestFriendWhenRequestWasAlreadySent(){
    testUser2.setFriendRequestList("testUsername");

    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser2);
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
    assertThrows(ResponseStatusException.class, () -> userService.addUserToFriendRequestList(testUser.getId(),testUser2));
}

@Test
     void removeUser_fromFL_success(){

    testUser.setFriendsList("testUsername2");
    testUser2.setFriendsList("testUsername");
    testUser.setFriendsList("Friend1");
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser2);
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
    userService.removeUserFromFriendsList(testUser.getId(),testUser2);
    assertTrue(testUser.getFriendsList().contains("Friend1"));
    assertFalse(testUser.getFriendsList().contains(testUser2.getUsername()));
}
@Test
     void Remove_random_person_failed(){
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser2);
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
    assertThrows(ResponseStatusException.class, () -> userService.removeUserFromFriendsList(testUser.getId(),testUser2));

    }
}
