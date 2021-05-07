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
        Game createdGame = gameService.createGame(lobby);

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

    // TODO #44 test and refine mapping API-call for requesting the letter-count
    @GetMapping("/games/{gameId}/length")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public WordCountGetDTO lengthRequest(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        //int value = game.getLength();
        //return WordCountDTOMapper.INSTANCE.convertEntityToWordCountGetDTO(value);
        return null;
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

     */
}
