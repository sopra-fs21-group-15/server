package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.ChatDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.LobbyDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LobbyController {

    private final UserService userService;
    private final LobbyService lobbyService;
    private final GameService gameService;
    private final ChatService chatService;

    LobbyController(UserService userService, LobbyService lobbyService, GameService gameService, ChatService chatService) {
        this.userService = userService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        this.chatService = chatService;
    }

    // Get mapping to /lobbies to fetch all lobbies to the frontend

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
       // fetch all lobbies in the internal representation
       List<Lobby> lobbies = lobbyService.getLobbies();
       List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();

       // convert each lobby to the API representation
       for (Lobby lobby : lobbies) {
            lobbyGetDTOs.add(LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
            }
            return lobbyGetDTOs;
        }

    @PostMapping("/lobbies/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@PathVariable Long userId, @RequestBody LobbyPostDTO lobbyPostDTO) {
        // convert API lobby to internal representation
        Lobby lobbyInput = LobbyDTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        // create lobby
        Lobby createdLobby = lobbyService.createLobby(lobbyInput, userId);

        chatService.createChat(createdLobby.getId());
        Message botMessage = chatService.enteringLobbyMessage(createdLobby.getId(),userService.getUserById(userId).getUsername());

        // convert internal representation of lobby back to API
        return LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    // Put mapping to  /lobbies/{lobbyId} to update the lobby setting in the repository

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Lobby editCurrentLobby(@PathVariable Long lobbyId, @RequestBody LobbyPostDTO lobbyEditDTO) {
        // convert API lobby to internal representation
        Lobby lobbyInput = LobbyDTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyEditDTO);
        lobbyService.update_lobby(lobbyId, lobbyInput);
        Lobby returnLobby = lobbyService.getLobby(lobbyId);
        return returnLobby;
    }

    // Get mapping to /lobbies/{lobbyId} to get the lobby by its Id

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyByID(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobby(lobbyId);
        // convert internal representation of lobby back to API
        LobbyGetDTO lobbyGetDTO = LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
        return lobbyGetDTO;
    }

    // Put mapping to add a member to the lobby & enter it

    @PutMapping("/lobbies/{lobbyId}/joiners")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO addMember(@PathVariable Long lobbyId, @RequestBody LobbyPostDTO lobbyEnterDTO) {
        Lobby lobbyInput = LobbyDTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyEnterDTO);
        lobbyService.addLobbyMembers(lobbyId, lobbyInput);
        Lobby lobby = lobbyService.getLobby(lobbyId);
        Message botMessage = chatService.enteringLobbyMessage(lobbyId,lobbyInput.getLobbyname());
        LobbyGetDTO lobbyGetDTO = LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
        return lobbyGetDTO;
    }

    // Put mapping to remove a member from the lobby

    @PutMapping("/lobbies/{lobbyId}/leavers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeMember(@PathVariable Long lobbyId, @RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        lobbyService.removeLobbyMembers(lobbyId, userInput.getUsername());
        Message botMessage = chatService.leavingLobbyMessage(lobbyId,userInput.getUsername());
    }

    // Back to the lobby
    @PutMapping("/lobbies/{lobbyId}/return")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO returnMember(@PathVariable Long lobbyId, @RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        gameService.leaveGame(lobbyId, userInput.getUsername());
        lobbyService.returnLobbyMembers(lobbyId, userInput.getUsername());

        Lobby lobby = lobbyService.getLobby(lobbyId);
        Message botMessage = chatService.enteringLobbyMessage(lobbyId,userInput.getUsername());
        LobbyGetDTO lobbyGetDTO = LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
        return lobbyGetDTO;
    }

    // Mapping for the lobby chats...

    @PostMapping("/lobbies/{lobbyId}/chats")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ChatGetDTO chatGetDTO(@PathVariable Long lobbyId, @RequestBody MessagePostDTO chatPostDTO) {
        Lobby lobby = lobbyService.getLobby(lobbyId);
        Message chatInput = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(chatPostDTO);
        Long chatId = lobby.getId();
        Chat newMessages = chatService.getNewMessages(chatId, chatInput.getTimeStamp());
        ChatGetDTO newChat = ChatDTOMapper.INSTANCE.convertEntityToChatGetDTO(newMessages);
        return newChat;
    }

    @PutMapping("/lobbies/{lobbyId}/chats")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void addChatMessage(@PathVariable Long lobbyId, @RequestBody MessagePostDTO messagePostDTO) {
        Message message = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);
        message = chatService.createMessage(message);
        chatService.addNewMessage(lobbyId, message);
    }

    @GetMapping("/lobbies/{lobbyId}/chats")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ChatGetDTO getChat(@PathVariable Long lobbyId) {
        String all = "2000-01-01 00:00:00:001";
        Chat newMessages = chatService.getNewMessages(lobbyId, all);
        ChatGetDTO newChat = ChatDTOMapper.INSTANCE.convertEntityToChatGetDTO(newMessages);
        return newChat;
    }
}
