package com.capgemini.tdd.suits;

import com.capgemini.tdd.rest.GameRestServiceTest;
import com.capgemini.tdd.rest.UserRestServiceTest;
import com.capgemini.tdd.services.game.InvalidMoveIntegrationTest;
import com.capgemini.tdd.services.game.GameServiceIntegrationTest;
import com.capgemini.tdd.services.game.MoveServiceIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ InvalidMoveIntegrationTest.class, GameServiceIntegrationTest.class, MoveServiceIntegrationTest.class, UserRestServiceTest.class, GameRestServiceTest.class })
public class IntegrationTestsSuite
{

}
