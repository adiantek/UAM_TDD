package com.capgemini.tdd.services.game;

import com.capgemini.tdd.core.data.BoardStatusEnum;
import com.capgemini.tdd.dao.entities.BoardBE;
import com.capgemini.tdd.dao.entities.MatchResultBE;
import com.capgemini.tdd.services.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceIntegrationTest
{

    @Autowired
    private GameService gameService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Autowired
    private MatchResultService matchResultService;

    @Test
    public void shouldMakeMovesToFinishGame()
    {
        //given
        BoardBE boardBE = new BoardBE(userService.findByName("Adam"), userService.findByName("Kasztan"));
        boardBE = boardService.save(boardBE);

        //when
        long boardId = boardBE.getId();
        gameService.makeMove(boardId, 0L, 0L, "Adam", "O");
        gameService.makeMove(boardId, 2L, 0L, "Kasztan", "X");

        gameService.makeMove(boardId, 0L, 1L, "Adam", "O");
        BoardStatusEnum tempBoardStatusEnum = gameService.makeMove(boardId, 2L, 1L, "Kasztan", "X");

        BoardStatusEnum finalBoardStatusEnum = gameService.makeMove(boardId, 0L, 2L, "Adam", "O");

        //then
        Assert.assertEquals(BoardStatusEnum.IN_PROGRESS, tempBoardStatusEnum);
        Assert.assertEquals(BoardStatusEnum.GAME_OVER, finalBoardStatusEnum);
        MatchResultBE matchResult = matchResultService.findByBoardId(boardId);
        Assert.assertNotNull(matchResult);
        Assert.assertEquals("Adam", matchResult.getWinner().getName());

    }

}
