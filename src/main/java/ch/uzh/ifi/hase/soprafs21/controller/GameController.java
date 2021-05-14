package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.helper.TimeStamp;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;

import ch.uzh.ifi.hase.soprafs21.rest.mapper.*;
import ch.uzh.ifi.hase.soprafs21.service.DrawingService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.RoundService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;
    private final RoundService roundService;
    private final DrawingService drawingService;
    private final LobbyService lobbyService;

    GameController(GameService gameService, RoundService roundService, DrawingService drawingService, LobbyService lobbyService) {
        this.gameService = gameService;
        this.roundService = roundService;
        this.drawingService = drawingService;
        this.lobbyService = lobbyService;
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
    public void addBrushStrokes(@RequestBody BrushStrokePutDTO brushStrokeEditDTO, @PathVariable long gameId) {
        // convert API brush stroke to an internal representation
        BrushStroke brushStroke = BrushStrokeDTOMapper.INSTANCE.convertBrushStrokePutDTOtoEntity(brushStrokeEditDTO);

        // add the current time to the given brush stroke
        DateTimeFormatter formatter = new Standard().getDateTimeFormatter();
        String currentTime = LocalDateTime.now().format(formatter);
        brushStroke.setTimeStamp(currentTime);

        // save the newly created brush stroke in the repository and in the drawing
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());
        drawingService.addStroke(round.getCurrentDrawing(),brushStroke);
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
/*
    // TODO #51 test and refine mapping API-call for sending a guess of what the word might be
    @PutMapping("/games/{gameId}/guess")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void makeGuess(@RequestBody GuessPutDTO guessPutDTO, @PathVariable Long gameId) {
        Guess guess = GuessDTOMapper.INSTANCE.convertGuessPutDTOToEntity(guessPutDTO);
        Game game = gameService.getGame(gameId);
        //game.makeGuess(guess);
    }
*/
    // TODO #53 test and refine the mapping for this API-call requesting the score
    @GetMapping("/games/{gameId}/score")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ScoreBoardGetDTO getScore(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        //ScoreBoard score = game.getScoreBoard();
        return null;//ScoreBoardDTOMapper.INSTANCE.convertEntityToScoreBoardGetDTO(score);
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
=======

     */

    /** (Issue #35) API call to get the current options the drawer can chose from right now has right now. Checks if
     * the game is currently in the selection phase and checks if the person sending the request is the drawer at this
     * point in time. Returns nothing if either requirements do not match.
     * @param gameId = the id of the game we would like to get the options for
     * @param username = the username of the user who requested it
     * @return a list of three strings from which the current drawer can pick one
     */
    @GetMapping("/games/{gameId}/choices/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getChoices(@PathVariable Long gameId, @PathVariable String username) {
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());
        return roundService.getChoices(round,username);
    }

    /** (Issue #39) function handling the selection of a word. The function checks if the request was send by the
     * drawer and if the phase the round is in right now is correct and allows for choices.
     * @param gameId = the id of the game the round is associated with
     * @param username = the name of the user sending this request
     * @param choiceId = the choice the user would like to make
     * @return a DTO object that contains all the information
     */
    @PutMapping("/games/{gameId}/choices/{username}/{choiceId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void makeChoice(@PathVariable Long gameId, @PathVariable String username, @PathVariable Integer choiceId) {
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());
        int choice = choiceId.intValue();
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
        Game game = gameService.getGame(gameId);
        Round round = roundService.getRound(game.getRoundId());
        return RoundDTOMapper.INSTANCE.convertEntityToRoundGetDTO(round);
    }

}

