package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;

import ch.uzh.ifi.hase.soprafs21.rest.mapper.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) { this.gameService = gameService; }

    // API call to create a game from the lobby (requires to be in a lobby first, lobby owner only)
    @PostMapping("/games/{lobbyId}/start")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@PathVariable Long lobbyId) {
        // copy the input into a game visible for all players threw the repository
        Game createdGame = gameService.createGame(lobbyId);

        // convert internal representation of game back to API for client
        return GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);

    }

    // API call to join a created game from the lobby (requires to be in a lobby first)
    @GetMapping("/games/{lobbyId}/convert")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO convertLobbyToGame(@PathVariable Long lobbyId) {
        // copy the input into a game visible for all players threw the repository
        Game foundGame = gameService.getGameFromLobby(lobbyId);

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
    @PutMapping("/game/{gameId}/drawing")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void addBrushStrokes(@RequestBody DrawInstructionPutDTO drawInstructionEditDTO, @PathVariable long gameId) {
        // convert API brush stroke to an internal representation
        //BrushStroke brushStroke = BrushStrokeDTOMapper.INSTANCE.convertBrushStrokePutDTOtoEntity(brushStrokeEditDTO);

        // System.out.println("Input");
        // System.out.println(drawInstructionEditDTO);
        DrawInstruction drawInstruction = DrawInstructionDTOMapper.INSTANCE.convertDrawInstructionPutDTOtoEntity(drawInstructionEditDTO);

        Game game = gameService.getGame(gameId);
        game.appendDrawInstruction(drawInstruction);

        // method checks on the level of the round if it is the right user
        // game.addStroke(brushStrokeEditDTO.getUser_id(), brushStroke);
        // game.addStroke(brushStrokeEditDTO.getUserName(), brushStroke);
    }

    // TODO #42 test and refine mapping for API-calls requesting the drawing
    @GetMapping("/game/{gameId}/drawing")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<DrawInstruction> drawingRequest(@RequestBody DrawingPostDTO drawingPostDTO, @PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        LocalDateTime now = LocalDateTime.now();
        ArrayList<DrawInstruction> drawInstructions = game.getDrawInstructions(now);

        System.out.println("Sending Draw-Instructions after" + now);
        System.out.println(drawInstructions);
        return drawInstructions;




        // LocalDateTime timeStamp = DrawingDTOMapper.INSTANCE.convertDrawingPostDTOtoEntity(drawingPostDTO);
        // Game game = gameService.getGame(gameId);
        // Drawing drawing = game.getDrawing(timeStamp);
        //return DrawingDTOMapper.INSTANCE.convertEntityToDrawingGetDTO(drawing);
    }

    // TODO #44 test and refine mapping API-call for requesting the letter-count
    @GetMapping("/game/{gameId}/length")
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
    @PutMapping("/game/{gameId}/guess")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void makeGuess(@RequestBody GuessPutDTO guessPutDTO, @PathVariable Long gameId) {
        Guess guess = GuessDTOMapper.INSTANCE.convertGuessPutDTOToEntity(guessPutDTO);
        Game game = gameService.getGame(gameId);
        //game.makeGuess(guess);
    }
*/
    // TODO #53 test and refine the mapping for this API-call requesting the score
    @GetMapping("/game/{gameId}/score")
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
