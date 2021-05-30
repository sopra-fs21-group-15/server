package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.constant.*;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.ChatDTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.LobbyDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapping;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimerService timerService;

    @MockBean
    private ScoreBoardService scoreBoardService;

    @MockBean
    private DrawingService drawingService;

    @MockBean
    private RoundService roundService;

    @MockBean
    private GameService gameService;

    @MockBean
    private ChatService chatService;

    @MockBean
    private LobbyService lobbyService;

    // test start game API call
    @Test
    void startGame_returnGame() throws Exception {
        // given
        Lobby lobby = new Lobby();
        lobby.setPassword("Firstname Lastname");
        lobby.setLobbyname("firstname@lastname");
        lobby.setId(2L);
        lobby.setTimer(50);
        lobby.setRounds(5);
        lobby.setSize(4);
        lobby.setMembers("Player 1");
        lobby.setMembers("Player 2");
        lobby.setMembers("Player 3");
        lobby.setMembers("Player 4");
        lobby.setGameMode(GameModes.CLASSIC);

        Game game = new Game();
        game.setId(lobby.getId());
        game.setPlayers(lobby.getMembers());
        game.setNumberOfRounds(lobby.getRounds());
        game.setGameName(lobby.getLobbyname());
        game.setId(lobby.getId());
        game.setGameModes(lobby.getGameMode());
        game.setRoundId(5L);

        int timePerRound = lobby.getTimer().intValue();
        Timer timer = timerService.createTimer(timePerRound);
        game.setTimePerRound(timePerRound);
        game.setTimer(timer);
        ScoreBoard scoreBoard = scoreBoardService.createScoreBoard(lobby);
        game.setScoreBoard(scoreBoard);
        game.setRoundTracker(0);


        given(lobbyService.getLobby(Mockito.any())).willReturn(lobby);
        given(gameService.createGame(Mockito.any())).willReturn(lobby.getId());
        given(gameService.getGame(Mockito.any())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/1/start")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(game.getId().intValue())))
                .andExpect(jsonPath("$.players", is(game.getPlayers())))
                .andExpect(jsonPath("$.numberOfRounds", is(game.getNumberOfRounds())))
                .andExpect(jsonPath("$.timePerRound", is(game.getTimePerRound())))
                .andExpect(jsonPath("$.roundTracker", is(game.getRoundTracker())))
                .andExpect(jsonPath("$.roundId", is(game.getRoundId().intValue())));
    }
//Test get game API call
    @Test
    void getGame_returnGame() throws Exception {

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        Game game = new Game();
        game.setId(2L);
        game.setPlayers(players);
        game.setNumberOfRounds(5);
        game.setGameName("firstname@lastname");
        game.setGameModes(GameModes.CLASSIC);
        game.setRoundId(5L);

        Timer timer = timerService.createTimer(50);
        game.setTimePerRound(50);
        game.setTimer(timer);
        game.setRoundTracker(0);

        given(gameService.getGame(Mockito.any())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/games/2/")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(game.getId().intValue())))
                .andExpect(jsonPath("$.players", is(game.getPlayers())))
                .andExpect(jsonPath("$.numberOfRounds", is(game.getNumberOfRounds())))
                .andExpect(jsonPath("$.timePerRound", is(game.getTimePerRound())))
                .andExpect(jsonPath("$.roundTracker", is(game.getRoundTracker())))
                .andExpect(jsonPath("$.roundId", is(game.getRoundId().intValue())));

    }
    // Test the put drawing API
/*
    @Test
    void putDrawing_returnVoid() throws Exception {

        BrushStrokePutDTO brushStrokePutDTO = new BrushStrokePutDTO();
        brushStrokePutDTO.setSize(1);
        brushStrokePutDTO.setX(1);
        brushStrokePutDTO.setY(1);
        brushStrokePutDTO.setColour("red");
        brushStrokePutDTO.setTimeStamp("2021-05-05 08:00:00:000");

        List<BrushStrokePutDTO> brushStrokePutDTOList = new ArrayList<BrushStrokePutDTO>();
        brushStrokePutDTOList.add(brushStrokePutDTO);

        Game game = new Game();
        game.setId(2L);
        game.setRoundId(5L);

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        Drawing drawing = new Drawing();
        drawing.setId(6L);
        List<Drawing> drawings = new ArrayList<>();
        drawings.add(drawing);

        Round round = new Round();
        round.setId(2L);
        round.setStatus(RoundStatus.DRAWING);
        round.setPlayers(players);
        round.setDrawings(drawings);

        given(gameService.getGame(Mockito.any())).willReturn(game);
        given(roundService.getRound(Mockito.any())).willReturn(round);

        doNothing().when(drawingService).addStrokes(round.getCurrentDrawing(), Mockito.any());
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/2/drawing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(brushStrokePutDTOList));
        ;
        mockMvc.perform(putRequest).andExpect(status().isNoContent());

    }
*/

// Test chat get new messages API
    @Test
    void chatPost() throws Exception {

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        Game game = new Game();
        game.setId(2L);
        game.setPlayers(players);
        game.setNumberOfRounds(5);
        game.setGameName("firstname@lastname");
        game.setGameModes(GameModes.CLASSIC);
        game.setRoundId(5L);

        Timer timer = timerService.createTimer(50);
        game.setTimePerRound(50);
        game.setTimer(timer);
        game.setRoundTracker(0);
        given(gameService.getGame(Mockito.any())).willReturn(game);

        MessagePostDTO messagePostDTO= new MessagePostDTO();
        messagePostDTO.setMessage("AAAAA");
        messagePostDTO.setWriterName("ABC");
        messagePostDTO.setTimeStamp("05.02.1998 12:00:00:0000");

        Message inputmessage = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);

        Chat newMessages = chatService.getNewMessages(game.getId(), inputmessage.getTimeStamp());
        ChatGetDTO newChat= ChatDTOMapper.INSTANCE.convertEntityToChatGetDTO(newMessages);

        MockHttpServletRequestBuilder postRequest = post("/games/2/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(game));


        mockMvc.perform(postRequest).andExpect(status().isOk()); }

    // test guess/write API call
    @Test
    void chatPut() throws Exception {
        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        Game game = new Game();
        game.setId(2L);
        game.setPlayers(players);
        game.setNumberOfRounds(5);
        game.setGameName("firstname@lastname");
        game.setGameModes(GameModes.CLASSIC);
        game.setRoundId(5L);

        Timer timer = timerService.createTimer(50);
        game.setTimePerRound(50);
        game.setTimer(timer);
        game.setRoundTracker(0);

        Drawing drawing = new Drawing();
        drawing.setId(6L);
        List<Drawing> drawings = new ArrayList<>();
        drawings.add(drawing);

        Round round = new Round();
        round.setId(2L);
        round.setStatus(RoundStatus.DRAWING);
        round.setPlayers(players);
        round.setDrawings(drawings);

        MessagePostDTO messagePostDTO= new MessagePostDTO();
        messagePostDTO.setMessage("AAAAA");
        messagePostDTO.setWriterName("ABC");
        messagePostDTO.setTimeStamp("05.02.1998 12:00:00:0000");

        Message inputMessage = ChatDTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);

        given(gameService.getGame(Mockito.any())).willReturn(game);
        given(roundService.getRound(Mockito.any())).willReturn(round);
        given(roundService.makeGuess(Mockito.any(), Mockito.any())).willReturn(false);

        Message message = chatService.createMessage(inputMessage);
        chatService.addNewMessage(game.getId(), message );

        doNothing().when(gameService).addPoints(game, message);

        MockHttpServletRequestBuilder putRequest = put("/games/2/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(game));


        mockMvc.perform(putRequest).andExpect(status().isOk()); }

    // test leaving a game
    @Test
    void quitGame() throws Exception {

        Game game = new Game();
        game.setId(2L);
        game.setRoundId(5L);

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        game.setPlayers(players);

        UserPostDTO quit= new UserPostDTO();
        quit.setUsername("Player 4");
        quit.setPassword("abc");

        User quiting = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(quit);

        MockHttpServletRequestBuilder putRequest = put("/games/2/leavers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(game));


        mockMvc.perform(putRequest).andExpect(status().isOk()); }




    @Test
    void length_request() throws Exception {

        Game game = new Game();
        game.setId(2L);
        game.setRoundId(5L);

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        game.setPlayers(players);



        BrushStroke brushStroke= new BrushStroke();
        brushStroke.setId(3L);
        brushStroke.setY(4);
        brushStroke.setSize(15);
        brushStroke.setColour("Black");
        brushStroke.setX(5);
        brushStroke.setTimeStamp("2021-05-17 14:52:10:000");
       List<BrushStroke> brush = new ArrayList<>();
       brush.add(brushStroke);
       Drawing drawing = new Drawing();
       drawing.setDrawerName("Player1");
       drawing.setId(1L);
       drawing.setBrushStrokes(brush);

       List<Drawing> picture = new ArrayList<>();
       picture.add(drawing);

        Round round = new Round();
        round.setId(5L);
        round.setStatus(RoundStatus.DRAWING);
        round.setWord("abc");
        round.setDrawings(picture);



       given(roundService.getRound(Mockito.any())).willReturn(round);
        given(gameService.getGame(Mockito.any())).willReturn(game);


        MockHttpServletRequestBuilder putRequest = get("/games/2/length")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(round));


        mockMvc.perform(putRequest).andExpect(status().isOk()); }

    @Test
    void painting_putrequest() throws Exception {

        Game game = new Game();
        game.setId(2L);
        game.setRoundId(5L);

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        game.setPlayers(players);



        BrushStrokePutDTO brushStrokeDTO= new BrushStrokePutDTO();
        brushStrokeDTO.setY(4);
        brushStrokeDTO.setSize(15);
        brushStrokeDTO.setColour("Black");
        brushStrokeDTO.setX(5);
        brushStrokeDTO.setTimeStamp("2021-05-17 14:52:10:000");

        List<BrushStrokePutDTO> brushDTO = new ArrayList<>();
        brushDTO.add(brushStrokeDTO);

        BrushStroke brushStroke= new BrushStroke();
        brushStroke.setId(3L);
        brushStroke.setY(4);
        brushStroke.setSize(15);
        brushStroke.setColour("Black");
        brushStroke.setX(5);
        brushStroke.setTimeStamp("2021-05-17 14:52:10:000");
        List<BrushStroke> brush = new ArrayList<>();
        brush.add(brushStroke);
        Drawing drawing = new Drawing();
        drawing.setDrawerName("Player1");
        drawing.setId(1L);
        drawing.setBrushStrokes(brush);

        List<Drawing> picture = new ArrayList<>();
        picture.add(drawing);




        Round round = new Round();
        round.setId(5L);
        round.setStatus(RoundStatus.DRAWING);
        round.setWord("abc");
        round.setDrawings(picture);



        given(roundService.getRound(Mockito.any())).willReturn(round);
        given(gameService.getGame(Mockito.any())).willReturn(game);


        MockHttpServletRequestBuilder putRequest = put("/games/2/drawing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(brushDTO));


        mockMvc.perform(putRequest).andExpect(status().isNoContent()); }

    @Test
    void painting_postrequest() throws Exception {

        Game game = new Game();
        game.setId(2L);
        game.setRoundId(5L);

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        game.setPlayers(players);



        TimeStringGetDTO timeStringGetDTO = new TimeStringGetDTO();
        timeStringGetDTO.setTimeString("2021-05-17 14:52:09:000");

        BrushStroke brushStroke= new BrushStroke();
        brushStroke.setId(3L);
        brushStroke.setY(4);
        brushStroke.setSize(15);
        brushStroke.setColour("Black");
        brushStroke.setX(5);
        brushStroke.setTimeStamp("2021-05-17 14:52:10:000");
        List<BrushStroke> brush = new ArrayList<>();
        brush.add(brushStroke);
        Drawing drawing = new Drawing();
        drawing.setDrawerName("Player1");
        drawing.setId(1L);
        drawing.setBrushStrokes(brush);

        List<Drawing> picture = new ArrayList<>();
        picture.add(drawing);




        Round round = new Round();
        round.setId(5L);
        round.setStatus(RoundStatus.DRAWING);
        round.setWord("abc");
        round.setDrawings(picture);



        given(roundService.getRound(Mockito.any())).willReturn(round);
        given(gameService.getGame(Mockito.any())).willReturn(game);


        MockHttpServletRequestBuilder putRequest = post("/games/2/drawing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(timeStringGetDTO));


        mockMvc.perform(putRequest).andExpect(status().isCreated()); }

    @Test
    void scoreBoard_get_request() throws Exception {


        Game game = new Game();
        game.setId(2L);
        game.setRoundId(5L);

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        game.setPlayers(players);
        BrushStroke brushStroke= new BrushStroke();
        brushStroke.setId(3L);
        brushStroke.setY(4);
        brushStroke.setSize(15);
        brushStroke.setColour("Black");
        brushStroke.setX(5);
        brushStroke.setTimeStamp("2021-05-17 14:52:10:000");
        List<BrushStroke> brush = new ArrayList<>();
        brush.add(brushStroke);
        Drawing drawing = new Drawing();
        drawing.setDrawerName("Player1");
        drawing.setId(1L);
        drawing.setBrushStrokes(brush);

        List<Drawing> picture = new ArrayList<>();
        picture.add(drawing);




        Round round = new Round();
        round.setId(5L);
        round.setStatus(RoundStatus.DRAWING);
        round.setWord("abc");
        round.setDrawings(picture);


        given(gameService.getGame(Mockito.any())).willReturn(game);


        MockHttpServletRequestBuilder putRequest = get("/games/2/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(game));


        mockMvc.perform(putRequest).andExpect(status().isOk()); }


    @Test
    void Choice_PutRequest() throws Exception {


        Game game = new Game();
        game.setId(2L);
        game.setRoundId(5L);

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        game.setPlayers(players);



        BrushStroke brushStroke= new BrushStroke();
        brushStroke.setId(3L);
        brushStroke.setY(4);
        brushStroke.setSize(15);
        brushStroke.setColour("Black");
        brushStroke.setX(5);
        brushStroke.setTimeStamp("2021-05-17 14:52:10:000");
        List<BrushStroke> brush = new ArrayList<>();
        brush.add(brushStroke);
        Drawing drawing = new Drawing();
        drawing.setDrawerName("Player1");
        drawing.setId(1L);
        drawing.setBrushStrokes(brush);

        List<Drawing> picture = new ArrayList<>();
        picture.add(drawing);




        Round round = new Round();
        round.setId(5L);
        round.setStatus(RoundStatus.DRAWING);
        round.setWord("abc");
        round.setDrawings(picture);



        given(gameService.getGame(Mockito.any())).willReturn(game);
        given(roundService.getRound(Mockito.any())).willReturn(round);




        MockHttpServletRequestBuilder putRequest = get("/games/2/score")
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(putRequest).andExpect(status().isOk()); }



    @Test
    void Round_GET_TEST() throws Exception {


        Game game = new Game();
        game.setId(2L);
        game.setRoundId(5L);

        ArrayList<String> players = new ArrayList<String>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");

        game.setPlayers(players);



        BrushStroke brushStroke= new BrushStroke();
        brushStroke.setId(3L);
        brushStroke.setY(4);
        brushStroke.setSize(15);
        brushStroke.setColour("Black");
        brushStroke.setX(5);
        brushStroke.setTimeStamp("2021-05-17 14:52:10:000");
        List<BrushStroke> brush = new ArrayList<>();
        brush.add(brushStroke);
        Drawing drawing = new Drawing();
        drawing.setDrawerName("Player1");
        drawing.setId(1L);
        drawing.setBrushStrokes(brush);

        List<Drawing> picture = new ArrayList<>();
        picture.add(drawing);




        Round round = new Round();
        round.setId(5L);
        round.setStatus(RoundStatus.DRAWING);
        round.setWord("abc");
        round.setDrawings(picture);

        ArrayList<String> choices= new ArrayList<>();
        choices.add("a");
        choices.add("b");
        choices.add("c");


        given(gameService.getGame(Mockito.any())).willReturn(game);
        given(roundService.getRound(Mockito.any())).willReturn(round);
        given(roundService.getChoices(Mockito.any(), Mockito.any())).willReturn(choices);




        MockHttpServletRequestBuilder putRequest = get("/games/2/update")
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(putRequest).andExpect(status().isOk()); }
        



    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
