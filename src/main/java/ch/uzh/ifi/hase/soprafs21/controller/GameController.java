package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.LobbyDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.GameDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) { this.gameService = gameService; }

    // TODO: finish provisional call by specifying mapping
    // pass information and settings from the lobby to create a game
    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<LobbyGetDTO> createGame(@RequestBody GamePostDTO gamePostDTO) {
        // convert information passed down into a usable instance of game
        Game gameInput = GameDTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // create the game for all players
        Game createdGame = gameService.createGame(gameInput);

        // convert internal representation of game back to API for client
        //return GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
        return null;
    }

}
