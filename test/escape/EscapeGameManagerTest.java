/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Andrew Whitney
 *******************************************************************************/

package escape;

//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertThrows;
//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import escape.board.coordinates.*;
import escape.board.coordinates.Coordinate.CoordinateType;
import escape.exception.EscapeException;
import escape.observers.GameObserver;
import escape.pieces.*;
import escape.pieces.EscapePiece.MovementPattern;
import escape.players.Player;

/**
 * Description
 * 
 * @version Nov 18, 2020
 */
class EscapeGameManagerTest
{

	private static EscapeGameManager manager;

	/**
	 * Description
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test1.egc");
		manager = egb.makeGameManager();
		assertNotNull(manager);
	}

	// Test 1
	@Test
	void testMakeGame() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test1.egc");
		manager = egb.makeGameManager();
		assertNotNull(manager);
	}

	// Test 2
	@Test
	void testMakingCoordinate()
	{
		Coordinate c1 = manager.makeCoordinate(3, 5);
		assertNotNull(c1);
	}

	// Test 3
	@Test
	void testMakingNonValidCoordinate() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/test2.egc");
		manager = egb2.makeGameManager();
		Coordinate c1 = manager.makeCoordinate(3, 5);
		assertNull(c1);
	}

	// Test 4
	@Test
	void testSquareDistanceToVertical()
	{
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(1, 2);
		assertEquals(1, c1.DistanceTo(c2));
	}

	// Test 5
	@Test
	void testSquareDistanceToHorizontal()
	{
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(2, 1);
		assertEquals(1, c1.DistanceTo(c2));
	}

	// Test 6
	@Test
	void testSquareDistanceToDiagonal()
	{
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(3, 3);
		assertEquals(2, c2.DistanceTo(c1));
	}

	// Test 7
	@Test
	void testSquareDistanceToMixed()
	{
		Coordinate c1 = manager.makeCoordinate(3, 5);
		Coordinate c2 = manager.makeCoordinate(8, 1);
		assertEquals(5, c2.DistanceTo(c1));
	}

	// Test 8
	@Test
	void testGetPieceAt()
	{
		Coordinate c1 = manager.makeCoordinate(4, 4);
		EscapePiece piece = manager.getPieceAt(c1);
		assertEquals(EscapePiece.PieceName.SNAIL, piece.getName());
	}

	// Test 9
	@Test
	void testGetPieceAtCorrectPlayer()
	{
		Coordinate c1 = manager.makeCoordinate(4, 4);
		EscapePiece piece = manager.getPieceAt(c1);
		assertEquals(Player.PLAYER1, piece.getPlayer());
	}

	// Test 10
	@Test
	void testGetPieceAt2()
	{
		Coordinate c1 = manager.makeCoordinate(4, 5);
		EscapePiece piece = manager.getPieceAt(c1);
		assertNull(piece);
	}

	// Test 11
	@Test
	void testInvalidCoordinate2()
	{
		Coordinate c1 = manager.makeCoordinate(3, 30);
		EscapePiece piece = manager.getPieceAt(c1);
		assertNull(piece);
	}

	// Test 12
	@Test
	void testFalseInvalidFromMove()
	{
		boolean result = manager.move(manager.makeCoordinate(4, 3),
				manager.makeCoordinate(5, 3));
		assertFalse(result);
	}

	// Test 13
	@Test
	void testFalseInvalidToMove()
	{
		boolean result = manager.move(manager.makeCoordinate(4, 4),
				manager.makeCoordinate(30, 30));
		assertFalse(result);
	}

	// Test 14
	@Test
	void testFalseIncorrectPlayerPieceMove()
	{
		boolean result = manager.move(manager.makeCoordinate(10, 12),
				manager.makeCoordinate(4, 4));
		assertFalse(result);
	}

	// Test 15
	@Test
	void testFalseMoveToBlock()
	{
		boolean result = manager.move(manager.makeCoordinate(4, 4),
				manager.makeCoordinate(3, 5));
		assertFalse(result);
	}

	// Test 16
	@Test
	void testTrueMoveToClear()
	{
		boolean result = manager.move(manager.makeCoordinate(4, 4),
				manager.makeCoordinate(4, 3));
		assertTrue(result);
	}

	// Test 17
	@Test
	void testTrueMoveToExit() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb.makeGameManager();
		boolean result = manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(2, 1));
		assertTrue(result);
		assertNull(manager.getPieceAt(manager.makeCoordinate(1, 1)));
		assertNull(manager.getPieceAt(manager.makeCoordinate(2, 1)));
	}

	// Test 18
	@Test
	void testTrueMoveToOtherPlayerPiece() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb.makeGameManager();
		boolean result = manager.move(manager.makeCoordinate(3, 7),
				manager.makeCoordinate(7, 7));
		assertFalse(result);
	}

	// Test 19
	@Test
	void testTrueSwitchesPlayers()
	{
		boolean result;
		result = manager.move(manager.makeCoordinate(4, 4),
				manager.makeCoordinate(4, 3));
		assertTrue(result);
		result = manager.move(manager.makeCoordinate(10, 12),
				manager.makeCoordinate(7, 9));
		assertTrue(result);
	}

	// Test 20
	@Test
	void testFalseMoveToOwnPiece() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/test3.egc");
		manager = egb2.makeGameManager();
		boolean result = manager.move(manager.makeCoordinate(4, 4),
				manager.makeCoordinate(5, 5));
		assertFalse(result);
	}

	// Test 21	
	@Test
	void testTrueBothCordsSamePiece()
	{
		boolean result = manager.move(manager.makeCoordinate(4, 4),
				manager.makeCoordinate(4, 4));
		assertFalse(result);
	}
	
	// Test 21
	@Test
	void testFalseToCordNull()
	{
		boolean result = manager.move(manager.makeCoordinate(4, 4), null);
		assertFalse(result);
	}
	
	// Test 22
	@Test
	void testFalseFromCordNull() 
	{
		boolean result = manager.move(null, manager.makeCoordinate(4,4));
		assertFalse(result);
	}
	
	// Test 23
	@Test
	void testNullCoordinate() 
	{
		assertNull(manager.getPieceAt(null));
	}
	
	// Test 36
	@Test
	void testInfiniteTriangleBoard() throws Exception 
	{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/testinfiniteboard.egc");
		manager = egb.makeGameManager();
		Coordinate c1 = manager.makeCoordinate(-1, -1);
		assertNotNull(manager.getPieceAt(c1));
	}
	
	// Test 37
	@Test
	void testPiecesHaveMovementPatterns()
	{
		EscapePieceImpl piece = (EscapePieceImpl) manager.getPieceAt(manager.makeCoordinate(4, 4));
		assertEquals(MovementPattern.OMNI, piece.getMovementPattern());
	}
	
	// Test 38
	@Test
	void testPieceHaveDistanceAttribute()
	{
		EscapePieceImpl piece = (EscapePieceImpl) manager.getPieceAt(manager.makeCoordinate(4, 4));
		assertEquals(1, piece.getMaxDistance());
	}
	
	// Test 39
	@Test
	void testPieceCantJump()
	{
		EscapePieceImpl piece = (EscapePieceImpl) manager.getPieceAt(manager.makeCoordinate(4, 4));
		assertFalse(piece.canJump());
	}
	
	// Test 40
	@Test
	void testPieceCantUnblock()
	{
		EscapePieceImpl piece = (EscapePieceImpl) manager.getPieceAt(manager.makeCoordinate(4, 4));
		assertFalse(piece.canUnblock());
	}
	
	// Test 41
	@Test
	void testPieceHasValue()
	{
		EscapePieceImpl piece = (EscapePieceImpl) manager.getPieceAt(manager.makeCoordinate(10, 12));
		assertEquals(1, piece.getValue());
	}
	
	// Test 42
	@Test
	void testPieceCanUnblock() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test3.egc");
		manager = egb.makeGameManager();
		EscapePieceImpl piece = (EscapePieceImpl) manager.getPieceAt(manager.makeCoordinate(4, 4));
		assertTrue(piece.canUnblock());
	}
	
	// Test 43
	@Test
	void testPieceCanJump() 
	{
		EscapePieceImpl piece = (EscapePieceImpl) manager.getPieceAt(manager.makeCoordinate(10, 12));
		assertTrue(piece.canJump());
	}
	
	// Test 44
	@Test
	void testFalseTooShortDistance()
	{
		assertFalse(manager.move(manager.makeCoordinate(4, 4), manager.makeCoordinate(4, 2)));
	}
	
	// Test 45
	@Test
	void testTrueJumpOverPiece() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		boolean result = manager.move(manager.makeCoordinate(10, 10),
				manager.makeCoordinate(3, 3));
		assertTrue(result);
	}
	
	// Test 46
	@Test
	void testTrueFindsPathTriangle() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testalgotriangle.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(3, 1)));
	}
	
	// Test 47
	@Test
	void testTrueFlyPath() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testalgotriangle.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(1, 10),
				manager.makeCoordinate(1, 13)));
	}
	
	// Test 48
	@Test
	void testFalseNoPathShortEnoughDueToObstruction() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testalgotriangle.egc");
		manager = egb2.makeGameManager();
		assertFalse(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(4, 2)));
	}
	
	// Test 49
	@Test
	void testTrueJumpTriangle() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testalgotriangle.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(1, 6),
				manager.makeCoordinate(2, 6)));
	}
	
	// Test 50
	@Test
	void testTrueOrthoMovement() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		boolean result = manager.move(manager.makeCoordinate(10, 10),
				manager.makeCoordinate(3, 3));
		assertTrue(result);
		result = manager.move(manager.makeCoordinate(21, 21), manager.makeCoordinate(20, 20));
		assertTrue(result);
	}
	
	// Test 51
	@Test
	void testNoPossiblePath() throws Exception 
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		boolean result = manager.move(manager.makeCoordinate(30, 30),
				manager.makeCoordinate(30, 32));
		assertFalse(result);
	}
	
	// Test 52
	@Test
	void testTrueLinearPathNE() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(40, 40), manager.makeCoordinate(43, 43)));
	}
	
	// Test 53
	@Test
	void testFalseNotLinear() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertFalse(manager.move(manager.makeCoordinate(40, 40), manager.makeCoordinate(41, 43)));
	}
	
	// Test 54
	@Test
	void testTrueLinearPathN() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(40, 40),
				manager.makeCoordinate(42, 40)));
	}
	
	// Test 55
	@Test
	void testTrueLinearPathNW() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(40, 40),
				manager.makeCoordinate(42, 38)));
	}
	
	// Test 56
	@Test
	void testTrueLinearPathW() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(40, 40),
				manager.makeCoordinate(40, 38)));
	}
	
	// Test 57
	@Test
	void testTrueLinearPathSW() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(40, 40),
				manager.makeCoordinate(38, 38)));
	}
	
	// Test 58
	@Test
	void testTrueLinearPathS() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(40, 40),
				manager.makeCoordinate(38, 40)));
	}
	
	// Test 59
	@Test
	void testTrueLinearPathSE() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(40, 40),
				manager.makeCoordinate(38, 42)));
	}
	
	// Test 60
	@Test
	void testTrueLinearPathE() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/testalgo.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(40, 40),
				manager.makeCoordinate(40, 42)));
	}
	
	// Test 61
	@Test
	void testBlockStopsPath() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/squareblocks.egc");
		manager = egb2.makeGameManager();
		assertFalse(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(3, 3)));
	}
	
	// Test 62
	@Test
	void testFlyOverBlockandPiece() throws Exception 
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/squareblocks.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(5, 5),
				manager.makeCoordinate(5, 9)));
	}
	
	// Test 63
	@Test
	void testUnblock() throws Exception 
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder("config/egc/squareblocks.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(5, 6),
				manager.makeCoordinate(5, 8)));
	}
	
	// Test 64
	@Test
	void testRuleHandlerTurnLimit() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/turnlimitone.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(3, 1)));
		manager.move(manager.makeCoordinate(10, 10), manager.makeCoordinate(10, 11));
		assertFalse(manager.move(manager.makeCoordinate(5, 5), manager.makeCoordinate(5, 8)));
	}
	
	// Test 65
	@Test
	void testRuleHandlerScoreLimit() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/scorelimitone.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(3, 1)));
		assertFalse(manager.move(manager.makeCoordinate(10, 10),
				manager.makeCoordinate(10, 11)));
	}
	
	// Test 66
	@Test
	void testRuleHandlerScoreLimitPlayer2Wins() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/scorelimitone.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(2, 1)));
		assertTrue(manager.move(manager.makeCoordinate(10, 10),
				manager.makeCoordinate(11, 10)));
		assertFalse(manager.move(manager.makeCoordinate(2, 1),
				manager.makeCoordinate(3, 1)));
	}
	
	// Test 67
	@Test
	void testGameObserverPlayerOneWins() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/scorelimitone.egc");
		manager = egb2.makeGameManager();
		GameObserver observer = new TestGameObserver();
		manager.addObserver(observer);
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(3, 1)));
		TestGameObserver tgo = (TestGameObserver) observer;
		assertTrue(tgo.hasMessage("PLAYER1 wins"));
	}
	
	// Test 68
	@Test
	void testGameObserverPlayerOneHasAlreadyWon() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/scorelimitone.egc");
		manager = egb2.makeGameManager();
		GameObserver observer = new TestGameObserver();
		manager.addObserver(observer);
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(3, 1)));
		assertFalse(manager.move(manager.makeCoordinate(10, 11),
				manager.makeCoordinate(11, 10)));
		TestGameObserver tgo = (TestGameObserver) observer;
		assertTrue(tgo.hasMessage("PLAYER1 wins"));
		assertEquals(tgo.getLastMessage(), "Game is already won by PLAYER1");
	}
	
	// Test 69
	@Test
	void testGameObserverRemove()
	{
		GameObserver observer = new TestGameObserver();
		assertEquals(observer, manager.addObserver(observer));
		assertEquals(observer, manager.removeObserver(observer));
	}
	
	// Test 70
	@Test
	void testNoPiecesLeftPlayerOneWins() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/onepieceeach.egc");
		manager = egb2.makeGameManager();
		GameObserver observer = new TestGameObserver();
		manager.addObserver(observer);
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(2, 1)));
		assertTrue(manager.move(manager.makeCoordinate(10, 10),
				manager.makeCoordinate(11, 10)));
		assertTrue(manager.move(manager.makeCoordinate(2, 1), manager.makeCoordinate(1, 1)));
		assertFalse(manager.move(manager.makeCoordinate(4, 3), manager.makeCoordinate(3, 6)));
		TestGameObserver tgo = (TestGameObserver) observer;
		assertTrue(tgo.hasMessage("PLAYER1 wins"));
	}
	
	// Test 71
	@Test
	void testNoPiecesLeftPlayerTwoWins() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/onepieceeach.egc");
		manager = egb2.makeGameManager();
		GameObserver observer = new TestGameObserver();
		manager.addObserver(observer);
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(3, 1)));
		assertTrue(manager.move(manager.makeCoordinate(10, 10),
				manager.makeCoordinate(10, 11)));
		assertFalse(manager.move(manager.makeCoordinate(3, 3), manager.makeCoordinate(5, 6)));
		TestGameObserver tgo = (TestGameObserver) observer;
		assertEquals(tgo.getLastMessage(), "PLAYER2 wins");
		assertFalse(manager.move(manager.makeCoordinate(3, 3), manager.makeCoordinate(5, 6)));
		assertEquals(tgo.getLastMessage(), "Game is already won by PLAYER2");
	}
	
	// Test 72
	@Test
	void testTriangleLinearNESame() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(3, 4),
				manager.makeCoordinate(4, 5)));
	}
	
	// Test 73
	@Test
	void testTriangleLinearNEDiff() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(3, 4),
				manager.makeCoordinate(4, 6)));
	}
	
	// Test 74
	@Test
	void testTriangleLinearNWSame() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(3, 4),
				manager.makeCoordinate(4, 3)));
	}
	
	// Test 75
	@Test
	void testTriangleLinearNWDiff() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(3, 4),
				manager.makeCoordinate(4, 2)));
	}
	
	// Test 76
	@Test
	void testTriangleLinearE() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(3, 4),
				manager.makeCoordinate(3, 5)));
	}
	
	// Test 77
	@Test
	void testTriangleLinearW() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(3, 4),
				manager.makeCoordinate(3, 3)));
	}
	
	// Test 78
	@Test
	void testTriangleLinearSWSame() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(2, 6),
				manager.makeCoordinate(1, 5)));
	}
	
	// Test 79
	@Test
	void testTriangleLinearSESame() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(2, 6),
				manager.makeCoordinate(1, 7)));
	}
	
	// Test 80
	@Test
	void testTriangleLinearSWDiff() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(2, 6),
				manager.makeCoordinate(1, 4)));
	}
	
	// Test 81
	@Test
	void testTriangleLinearSEDiff() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/testtrianglelinear.egc");
		manager = egb2.makeGameManager();
		assertTrue(manager.move(manager.makeCoordinate(2, 6),
				manager.makeCoordinate(1, 8)));
	}
	
	// Test 82
	@Test
	void testGameObserverDraw() throws Exception
	{
		EscapeGameBuilder egb2 = new EscapeGameBuilder(
				"config/egc/turnlimitone.egc");
		manager = egb2.makeGameManager();
		GameObserver observer = new TestGameObserver();
		manager.addObserver(observer);
		assertTrue(manager.move(manager.makeCoordinate(1, 1),
				manager.makeCoordinate(3, 1)));
		assertTrue(manager.move(manager.makeCoordinate(10, 10),
				manager.makeCoordinate(11, 10)));
		TestGameObserver tgo = (TestGameObserver) observer;
		assertTrue(tgo.hasMessage("Game is over and results in a draw"));
	}
}

