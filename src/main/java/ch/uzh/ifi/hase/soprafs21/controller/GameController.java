package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;

import ch.uzh.ifi.hase.soprafs21.rest.mapper.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) { this.gameService = gameService; }

    // TODO #29 test and refine mapping for API-calls for starting a game
    // TODO get information from lobby directly
    // pass information and settings from the lobby to create a game
    @PostMapping("/lobbies/start")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@RequestBody GamePostDTO gamePostDTO) {
        // convert information passed down into a usable instance of game
        Game gameInput = GameDTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // copy the input into a game visible for all players threw the repository
        Game createdGame = gameService.createGame(gameInput);

        // convert internal representation of game back to API for client
        return GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    // TODO #40 test and refine mapping for sending drawing information
    // pass information to the right picture
    @PutMapping("/game/{gameId}/drawing")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void addBrushStrokes(@RequestBody BrushStrokePutDTO brushStrokeEditDTO, @PathVariable long gameId) {
        // convert API brush stroke to an internal representation
        BrushStroke brushStroke = BrushStrokeDTOMapper.INSTANCE.convertBrushStrokePutDTOtoEntity(brushStrokeEditDTO);

        Game game = gameService.getGame(gameId);

        // method checks on the level of the round if it is the right user
        game.addStroke(brushStrokeEditDTO.getUser_id(), brushStroke);
    }

    // TODO #42 test and refine mapping for API-calls requesting the drawing
    @GetMapping("/game/{gameId}/drawing")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DrawingGetDTO drawingRequest(@RequestBody DrawingPostDTO drawingPostDTO, @PathVariable Long gameId) {
        LocalDateTime timeStamp = DrawingDTOMapper.INSTANCE.convertDrawingPostDTOtoEntity(drawingPostDTO);
        Game game = gameService.getGame(gameId);
        Drawing drawing = game.getDrawing(timeStamp);
        return DrawingDTOMapper.INSTANCE.convertEntityToDrawingGetDTO(drawing);
    }

    // TODO #44 test and refine mapping API-call for requesting the letter-count
    @GetMapping("/game/{gameId}/length")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public WordCountGetDTO lengthRequest(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        int value = game.getLength();
        return WordCountDTOMapper.INSTANCE.convertEntityToWordCountGetDTO(value);
    }

    // TODO #51 test and refine mapping API-call for sending a guess of what the word might be
    @PutMapping("/game/{gameId}/guess")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void makeGuess(@RequestBody GuessPutDTO guessPutDTO, @PathVariable Long gameId) {
        Guess guess = GuessDTOMapper.INSTANCE.convertGuessPutDTOToEntity(guessPutDTO);
        Game game = gameService.getGame(gameId);
        game.makeGuess(guess);
    }

    // TODO #53 test and refine the mapping for this API-call requesting the score
    @GetMapping("/game/{gameId}/score")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ScoreBoardGetDTO getScore(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        ScoreBoard score = game.getScoreBoard();
        return ScoreBoardDTOMapper.INSTANCE.convertEntityToScoreBoardGetDTO(score);
    }

}
