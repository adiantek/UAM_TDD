package com.capgemini.tdd.services.game.impl;

import com.capgemini.tdd.core.exceptions.InvalidMoveValueException;
import com.capgemini.tdd.core.exceptions.InvalidPlayerMoveException;
import com.capgemini.tdd.core.exceptions.OccupiedPositionException;
import com.capgemini.tdd.dao.MoveDao;
import com.capgemini.tdd.dao.entities.BoardBE;
import com.capgemini.tdd.dao.entities.MoveBE;
import com.capgemini.tdd.dao.entities.UserBE;
import com.capgemini.tdd.dao.enums.MoveValueEnum;
import com.capgemini.tdd.services.game.BoardService;
import com.capgemini.tdd.services.game.MoveService;
import com.capgemini.tdd.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoveServiceImpl implements MoveService
{

    @Autowired
    private MoveDao moveDao;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Override
    public MoveBE makeMove(final Long boardId, final Long x, final Long y, final String playerName, final String value)
    {
        BoardBE boardBE = boardService.findById(boardId);
        UserBE userBE = userService.findByName(playerName);
        List<MoveBE> list = moveDao.findAll().stream().filter(m -> m.getBoard() == boardBE).collect(Collectors.toList());
        MoveValueEnum nextValidMoveValue = (list.size() % 2 == 0) ? MoveValueEnum.O : MoveValueEnum.X;
        MoveValueEnum moveValue = MoveValueEnum.fromCode(value);
        if (moveValue != nextValidMoveValue) {
            throw new InvalidMoveValueException();
        }
        if (moveValue == MoveValueEnum.O) {
            if (boardBE.getPlayerOne() != userBE) {
                throw new InvalidPlayerMoveException();
            }
        } else {
            if (boardBE.getPlayerTwo() != userBE) {
                throw new InvalidPlayerMoveException();
            }
        }
        if (list.stream().anyMatch(m -> m.getX().equals(x) && m.getY().equals(y))) {
            throw new OccupiedPositionException();
        }
        MoveBE moveBE = new MoveBE(x, y, userBE, boardBE, moveValue);
        return moveDao.save(moveBE);
    }

    @Override
    public List<MoveBE> findByBoardId(final Long boardId)
    {
        return moveDao.findByBoardId(boardId);
    }

    @Override
    public List<MoveBE> findAll()
    {
        return moveDao.findAll();
    }
}
