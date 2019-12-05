package com.capgemini.tdd.services.game;

import com.capgemini.tdd.dao.entities.BoardBE;
import com.capgemini.tdd.dao.entities.MoveBE;
import com.capgemini.tdd.services.game.BoardService;
import com.capgemini.tdd.services.game.MoveService;
import com.capgemini.tdd.services.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoveServiceIntegrationTest
{

    @Autowired
    private MoveService moveService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Test
    public void shouldMakeMove()
    {
        //given
        int moveSize = moveService.findAll().size();
        BoardBE boardBE = new BoardBE(userService.findByName("Adam"), userService.findByName("Kasztan"));
        boardBE = boardService.save(boardBE);

        //when
        MoveBE move= moveService.makeMove(boardBE.getId(), 0L, 0L, "Adam", "O");

        //then
        Assert.assertNotNull(move.getId());
        Assert.assertEquals(moveSize+1, moveService.findAll().size());
    }

}
