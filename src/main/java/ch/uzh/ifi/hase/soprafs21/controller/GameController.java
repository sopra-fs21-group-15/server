package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.helper.TimeStamp;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;

import ch.uzh.ifi.hase.soprafs21.rest.mapper.*;
import ch.uzh.ifi.hase.soprafs21.service.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ch.uzh.ifi.hase.soprafs21.constant.RoundStatus.DRAWING;
import static ch.uzh.ifi.hase.soprafs21.constant.RoundStatus.SELECTING;

@RestController
public class GameController {

    private final GameService gameService;
    private final RoundService roundService;
    private final DrawingService drawingService;
    private final LobbyService lobbyService;
    private final ChatService chatService;
    private final TimerService timerService;

    GameController(GameService gameService, RoundService roundService, DrawingService drawingService, LobbyService lobbyService, ChatService chatService, TimerService timerService) {
        this.gameService = gameService;
        this.roundService = roundService;
        this.drawingService = drawingService;
        this.lobbyService = lobbyService;
        this.chatService = chatService;
        this.timerService = timerService;

    }

    // API call to create a game from the lobby (requires to be in a lobby first, lobby owner only)
    @PostMapping("/games/{lobbyId}/start")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@PathVariable Long lobbyId) {
        // copy the input into a game visible for all players through the repository
        Lobby lobby = lobbyService.getLobby(lobbyId);
        Long gameId = gameService.createGame(lobby);
        Game createdGame = gameService.getGame(gameId);

        // convert internal representation of game back to API for client
        return GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }
/*
    // API call to join a created game from the lobby (requires to be in a lobby first)
    @GetMapping("/games/{lobbyId}/convert")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO convertLobbyToGame(@PathVariable Long lobbyId) {
        // copy the input into a game visible for all players threw the repository
        Game foundGame = gameService.getGameFromLobbyId(lobbyId);

        // convert internal representation of game back to API for client
        return GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(foundGame);
    }
*/
    // API call to get a newer version of the game that you are in
    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable Long gameId) {
        // copy the input into a game visible for all players threw the repository
        Game foundGame = gameService.getGame(gameId);

        // convert internal representation of game back to API for client
        return GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(foundGame);
    }

    // TODO #40 test and refine mapping for sending drawing information
    // pass information to the right picture
    @PutMapping("/games/{gameId}/drawing")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void addBrushStrokes(@RequestBody List<BrushStrokePutDTO> brushStrokeListDTO, @PathVariable long gameId) {
        // get the correct game and drawing in which we need to save all the brushstrokes
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());

        // convert each and every API brush stroke to an internal representation
        BrushStroke temp;
        for (BrushStrokePutDTO preBrushStroke : brushStrokeListDTO) {
            temp = BrushStrokeDTOMapper.INSTANCE.convertBrushStrokePutDTOtoEntity(preBrushStroke);
            drawingService.addStroke(round.getCurrentDrawing(),temp);
        }
        System.out.println(round.getCurrentDrawing().getBrushStrokes().size());
        drawingService.save(round.getCurrentDrawing());

    }

    // TODO #42 test and refine mapping for API-calls requesting the drawing
    @PostMapping("/games/{gameId}/drawing")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ArrayList<DrawingGetDTO> drawingRequest(@RequestBody TimeStringGetDTO timeStringGetDTO, @PathVariable Long gameId) {
        TimeStamp timeStamp = TimeDTOMapper.INSTANCE.convertTimeStringGeTDTOtoEntity(timeStringGetDTO);

        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());
        List<BrushStroke> drawings = drawingService.getDrawing(round.getCurrentDrawing(),timeStamp.getTimeObject());
        ArrayList<DrawingGetDTO> value = new ArrayList<>();
        for(BrushStroke i : drawings){
            value.add(DrawingDTOMapper.INSTANCE.convertEntityToDrawingGetDTO(i));
        }
        System.out.println("length of Brushstrockes"+value.size());
        return value;
    }

    // (Issue #44) API-call for requesting the letter-count
    @GetMapping("/games/{gameId}/length")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int lengthRequest(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());
        return roundService.getLength(round);
    }

    /** (Issue 53) API-call for the current score of the round right now
     * @param gameId = the id of the game we would like have the score as it stands
     * @return a DTO object with the sorted list of players, their respective ranking and score
     */
    @GetMapping("/games/{gameId}/score")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ScoreBoardGetDTO getScore(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        ScoreBoard score = game.getScoreBoard();
        return ScoreBoardDTOMapper.INSTANCE.convertEntityToScoreBoardGetDTO(score);
    }

  
/*
    //API Call for getting the chat in the game
    @GetMapping("/game/{gameId}/messages")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ChatGetDTO getMessages (@PathVariable Long gameId, @RequestBody ChatPostDTO chatPostDTO) {
        LocalDateTime timeStamp = ChatDTOMapper.INSTANCE.convertChatPostDTOtoEntity(chatPostDTO);
        Game game = gameService.getGame(gameId);
        Chat chat = game.getChat(timeStamp);
        return ChatDTOMapper.INSTANCE.convertEntityToChatGetDTO(chat);
    }
*/
    /*
    //API Call for posting a message in the game
    @PutMapping("/game/{gameId}/messages")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean guessMessage (@PathVariable Long gameId, @RequestBody MessagePostDTO messagePostDTO) {
        Message message = MessageDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);
        Game game = gameService.getGame(gameId);
        Boolean correct = game.makeGuess(message);
        return correct;
    }
*/

    
    @PutMapping("/games/{gameId}/guess")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public boolean makeGuess(@RequestBody MessagePostDTO messagePostDTO, @PathVariable Long gameId) {
        Message message = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO); // convert to usable object
        Game game = gameService.getGame(gameId); // find game
        Round round = roundService.getRound(game.getRoundId()); // get current round
        boolean value = roundService.makeGuess(message,round); // check if the guess is valid and correct
        if (value) {
            gameService.addPoints(game,message);
        }
        return value;
    }

    /*
    /** (Issue #35) API call to get the current options the drawer can chose from right now has right now. Checks if
     * the game is currently in the selection phase and checks if the person sending the request is the drawer at this
     * point in time. Returns nothing if either requirements do not match.
     * @param gameId = the id of the game we would like to get the options for
     * @param username = the username of the user who requested it
     * @return a list of three strings from which the current drawer can pick one
     *//*
    @GetMapping("/games/{gameId}/choices/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getChoices(@PathVariable Long gameId, @PathVariable String username) {
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());
        return roundService.getChoices(round,username);
    }*/


    /** (Issue #39) function handling the selection of a word. The function checks if the request was send by the
     * drawer and if the phase the round is in right now is correct and allows for choices.
     * @param gameId = the id of the game the round is associated with
     * @param username = the name of the user sending this request
     * @param choiceId = the choice the user would like to make
     */
    @PutMapping("/games/{gameId}/choices/{username}/{choiceId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void makeChoice(@PathVariable Long gameId, @PathVariable String username, @PathVariable Integer choiceId) {
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());
        int choice = choiceId;
        roundService.makeChoice(round,username,choice);
    }

    /** (Issue #114) API call to get the current information of the round including but not limited to the current
     * drawer, the phase the round is in and the current word that was picked.
     * @param gameId = the id of the game the round is associated with
     * @return a DTO object that contains all the information
     */
    @GetMapping("/games/{gameId}/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundGetDTO update(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId); // find the right game
        Round round = roundService.getRound(game.getRoundId()); // get the currently active round

        // get and add the choices for this selection phase
        ArrayList<String> choices = roundService.getChoices(round, round.getDrawerName());
        round.setSelection(choices);

        // calculate when this current phase will end
        String end = timerService.getEnd(game.getTimer());
        round.setEndsAt(end);

        return RoundDTOMapper.INSTANCE.convertEntityToRoundGetDTO(round);
    }

    // Game Chat

    @PostMapping("/games/{gameId}/chats")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ChatGetDTO chatGetDTO(@PathVariable Long gameId, @RequestBody MessagePostDTO chatPostDTO) {
        Game game = gameService.getGame(gameId);
        Message chatInput = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(chatPostDTO);
        Long chatId = game.getId();
        Chat newMessages = chatService.getNewMessages(chatId, chatInput.getTimeStamp());
        return ChatDTOMapper.INSTANCE.convertEntityToChatGetDTO(newMessages);
    }

    @PutMapping("/games/{gameId}/chats")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean addGuess(@PathVariable Long gameId, @RequestBody MessagePostDTO messagePostDTO) {
        Message message = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);
        message = chatService.createMessage(message);
        Game game = gameService.getGame(gameId); // find game
        Round round = roundService.getRound(game.getRoundId()); // get current round
        boolean guess = false;
        if (round.getStatus().equals(DRAWING)) { // check if the phase of the round is correct
            guess = roundService.makeGuess(message,round); // check if the guess is valid and correct
        }
        if (guess == false) {
            chatService.addNewMessage(gameId, message); // add chat message
        }
        return guess;
    }
    /*
    /** (Issue 51) API-call for sending a guess of what the word might be from a user
     * @param gameId = the id of the game we would like to send the guess to
     * @param messagePostDTO = contains the player, the guess and a timestamp when it was send
     * @return true only if the player guessed the correct word for the first time
     */
    /*
    @GetMapping("/games/{gameId}/guess")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public boolean makeGuess(@RequestBody MessagePostDTO messagePostDTO, @PathVariable Long gameId) {
        Message message = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO); // convert to usable object
        Game game = gameService.getGame(gameId); // find game
        Round round = roundService.getRound(game.getRoundId()); // get current round
        boolean value = roundService.makeGuess(message,round); // check if the guess is valid and correct
        return value;
    }
*/
}

