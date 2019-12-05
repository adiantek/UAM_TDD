package com.capgemini.tdd.services.game.impl;

import com.capgemini.tdd.core.data.Board;
import com.capgemini.tdd.core.data.BoardStatusEnum;
import com.capgemini.tdd.core.enums.WinEnum;
import com.capgemini.tdd.core.service.BoardBuilder;
import com.capgemini.tdd.core.service.impl.BoardInspector;
import com.capgemini.tdd.dao.entities.BoardBE;
import com.capgemini.tdd.dao.entities.MatchResultBE;
import com.capgemini.tdd.dao.entities.UserBE;
import com.capgemini.tdd.dao.enums.MoveValueEnum;
import com.capgemini.tdd.services.game.BoardService;
import com.capgemini.tdd.services.game.GameService;
import com.capgemini.tdd.services.game.MatchResultService;
import com.capgemini.tdd.services.game.MoveService;
import com.capgemini.tdd.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService
{

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Autowired
    private MoveService moveService;

    @Autowired
    private BoardBuilder boardBuilder;

    @Autowired
    private MatchResultService matchResultService;



    @Override
    public BoardBE startNewGame(final String playerOneName, final String playerTwoName)
    {
        UserBE player1 = userService.findByName(playerOneName);
        UserBE player2 = userService.findByName(playerTwoName);

        BoardBE newBoard = new BoardBE(player1, player2);
        return boardService.save(newBoard);
    }

    @Override
    public BoardBE getById(final Long id)
    {
        return boardService.findById(id);
    }

    @Override
    public BoardBE getByPlayersNames(final String playerOne, final String playerTwo)
    {
        return boardService.findByPlayersNames(playerOne, playerTwo);
    }

    @Override
    public BoardStatusEnum makeMove(final Long boardId, final Long x, final Long y, final String playerName, final String value)
    {
        MatchResultBE existingMatchResultBE = matchResultService.findByBoardId(boardId);
        if(existingMatchResultBE != null)
        {
            return BoardStatusEnum.GAME_OVER;
        }

        moveService.makeMove(boardId, x, y, playerName, value);
        Board board = boardBuilder.buildBoard(boardId);
        WinEnum boardStatus = BoardInspector.validate(board, MoveValueEnum.fromCode(value));
        if(WinEnum.DONE == boardStatus)
        {
            BoardBE boardBE = boardService.findById(boardId);
            String playerOne = boardBE.getPlayerOne().getName();
            String playerTwo = boardBE.getPlayerTwo().getName();
            UserBE winner = userService.findByName(playerName);
            UserBE looser = playerOne.equals(playerName) ? userService.findByName(playerTwo) : userService.findByName(playerOne);
            MatchResultBE matchResultBE = new MatchResultBE(boardBE, winner, looser, winner);
            matchResultService.save(matchResultBE);
            return BoardStatusEnum.GAME_OVER;
        }
        else if(WinEnum.NONE == boardStatus && board.size() == 9)
        {
            BoardBE boardBE = boardService.findById(boardId);
            String playerOne = boardBE.getPlayerOne().getName();
            String playerTwo = boardBE.getPlayerTwo().getName();
            UserBE playerOneBE = userService.findByName(playerName);
            UserBE playerTwoBE = playerOne.equals(playerName) ? userService.findByName(playerTwo) : userService.findByName(playerOne);
            MatchResultBE matchResultBE = new MatchResultBE(boardBE, playerOneBE, playerTwoBE, null);
            matchResultService.save(matchResultBE);
            return BoardStatusEnum.DRAW;
        }
        return BoardStatusEnum.IN_PROGRESS;
    }

}
