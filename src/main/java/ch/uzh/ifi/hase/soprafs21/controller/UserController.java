package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.LobbyDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    // Get mapping to /users to fetch all users to the frontend

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getAllUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    // Post mapping to /users to create a new user and save it

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    //Put Mapping to /login to compare the given input to the saved user

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO requestLogin(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        //ask userService if login request is allowed
        User userLogin = userService.loginRequest(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userLogin);
    }

    // Get mapping to /users/{userId} to get the user by its Id

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByID(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        // convert internal representation of user back to API
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        return userGetDTO;
    }

    @PutMapping("/users/userNames")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByUserName(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User user = userService.getUserByUserName(userInput);
        // convert internal representation of user back to API
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        return userGetDTO;
    }

    // Put mapping to /users/{userId} to update the user in the repository with its changes

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void editCurrentUser(@PathVariable Long userId, @RequestBody UserPostDTO userEditDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userEditDTO);
        userService.updateUser(userId, userInput);

    }

    // Put Mapping to put the logged-out user on OFFLINE

    @PutMapping("/logout/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void userLogout (@PathVariable Long userId) {

        userService.logout(userId);
    }


    // Put mapping to add a friend to a user

    @PutMapping("/users/{userId}/friends/requests")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void addFriendRequest(@PathVariable Long userId, @RequestBody UserPostDTO userPostDTO) {
        User friend = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        userService.addUserToFriendRequestList(userId, friend);
        User user = userService.getUserById(userId);
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @PutMapping("/users/{userId}/friends/confirmations")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO addFriend(@PathVariable Long userId, @RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User friend = userService.getUserByUserName(userInput);
        userService.removeUserFromFriendRequestList(userId, userInput);
        userService.addUserToFriendsList(userId, userInput);
        User user = userService.getUserById(userId);
        if (friend.getFriendRequestList().contains(user.getUsername())){
            userService.removeUserFromFriendRequestList(friend.getId(), user);
        }
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        return userGetDTO;
    }

    @PutMapping("/users/{userId}/friends/rejections")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO removeFriendRequest(@PathVariable Long userId, @RequestBody UserPostDTO userPostDTO) {
        User friend = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        userService.removeUserFromFriendRequestList(userId, friend);
        User user = userService.getUserById(userId);
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        return userGetDTO;
    }

    @PutMapping("/users/{userId}/friends/deletions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO removeFriend(@PathVariable Long userId, @RequestBody UserPostDTO userPostDTO) {
        User friend = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        userService.removeUserFromFriendsList(userId, friend);
        User user = userService.getUserById(userId);
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        return userGetDTO;
    }


}

