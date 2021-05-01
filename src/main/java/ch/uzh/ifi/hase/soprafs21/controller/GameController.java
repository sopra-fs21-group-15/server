package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.entity.Drawing;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;

import ch.uzh.ifi.hase.soprafs21.rest.mapper.BrushStrokeDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DrawingDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.GameDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @PutMapping("/game/{gameId}")
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
    @GetMapping("/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DrawingGetDTO drawingRequest(@RequestBody DrawingPostDTO drawingPostDTO, @PathVariable long gameId) {
        LocalDateTime timeStamp = DrawingDTOMapper.INSTANCE.convertDrawingPostDTOtoEntity(drawingPostDTO);
        Game game = gameService.getGame(gameId);
        Drawing drawing = game.getDrawing(timeStamp);
        return DrawingDTOMapper.INSTANCE.convertEntityToDrawingGetDTO(drawing);
    }

    // TODO #44 add mapping API-call for requesting the letter-count
    /*@GetMapping("/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public letterC*/

}
