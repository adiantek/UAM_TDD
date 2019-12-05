package com.capgemini.tdd.services.game;

import com.capgemini.tdd.core.exceptions.InvalidMoveValueException;
import com.capgemini.tdd.dao.entities.BoardBE;
import com.capgemini.tdd.services.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvalidMoveIntegrationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Autowired
    private MatchResultService matchResultService;

    @Test
    public void testInvalidMoveValue()
    {
        BoardBE boardBE = new BoardBE(userService.findByName("Adam"), userService.findByName("Kasztan"));
        boardBE = boardService.save(boardBE);
        long boardId = boardBE.getId();
        gameService.makeMove(boardId, 0L, 0L, "Adam", "O");
        try {
            gameService.makeMove(boardId, 1L, 0L, "Kasztan", "O");
        } catch (InvalidMoveValueException ignored) {
            return;
        }
        throw new RuntimeException("Test failed");
    }

}
