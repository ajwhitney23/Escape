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

import java.util.ArrayList;
import escape.board.Board;
import escape.board.coordinates.*;
import escape.board.coordinates.Coordinate.CoordinateType;
import escape.exception.EscapeException;
import escape.pieces.EscapePiece;
import escape.players.Player;
import escape.rules.RuleHandler;
import escape.util.EscapeGameInitializer;
import escape.observers.*;

/**
 * Implementation of EscapeGameManager, controls the game, where the user interacts with
 * the rest of the application
 * 
 * @version Nov 10, 2020
 */
public class EscapeGameManagerImpl<C extends EscapeCoordinate>
		implements EscapeGameManager<C>
{
	private Board board;
	private RuleHandler ruleHandler;
	private CoordinateType type;
	private int xMax;
	private int yMax;
	private boolean isPlayerOneTurn;
	private boolean P1HasWon;
	private boolean P2HasWon;
	private int moves;
	private ArrayList<GameObserver> observers;

	/**
	 * Creates an implementation of EscapeGameManager
	 * 
	 * @param escapeGameInitializer
	 *            the data needed to create the game
	 */
	public EscapeGameManagerImpl(EscapeGameInitializer escapeGameInitializer)
	{
		initializeEscapeGame(escapeGameInitializer);
		this.isPlayerOneTurn = true;
		this.P1HasWon = false;
		this.P2HasWon = false;
		this.ruleHandler = new RuleHandler(escapeGameInitializer.getRules());
		this.board = new Board(escapeGameInitializer.getLocationInitializers(),
				escapeGameInitializer.getxMax(), escapeGameInitializer.getyMax(),
				escapeGameInitializer.getPieceTypes(), ruleHandler);
		this.moves = 0;
		this.observers = new ArrayList<GameObserver>();
	}

	private void initializeEscapeGame(EscapeGameInitializer initializer)
	{
		this.type = initializer.getCoordinateType();
		this.xMax = initializer.getxMax();
		this.yMax = initializer.getyMax();
	}

	/**
	 * Add an observer to this manager. Whever the move() method returns false, the
	 * observer will be notified with a message indication the problem.
	 * 
	 * @param observer
	 * @return the observer
	 */
	@Override
	public GameObserver addObserver(GameObserver observer)
	{
		observers.add(observer);
		return observer;
	}

	/**
	 * Remove an observer from this manager. The observer will no longer receive
	 * notifications from this game manager.
	 * 
	 * @param observer
	 * @return the observer that was removed or null if it had not previously been
	 *         registered
	 */
	@Override
	public GameObserver removeObserver(GameObserver observer)
	{
		if (observers.remove(observer)) {
			return observer;
		}
		return null;
	}

	/**
	 * notify's all observers of all events in the form of a string
	 * 
	 * @param message
	 */
	private void notifyObservers(String message)
	{
		for (GameObserver observer : observers) {
			observer.notify(message);
		}
	}

	/**
	 * notify's all observers of all events in the form of a string and a cause
	 * 
	 * @param message
	 *            EscapeExcetption message
	 * @param cause
	 *            Throwable
	 */
	private void notifyObservers(String message, Throwable cause)
	{
		for (GameObserver observer : observers) {
			observer.notify(message, cause);
		}
	}

	/**
	 * checks if game is over
	 */
	private void checkGameOver()
	{
		if (P1HasWon && P2HasWon) {
			throw new EscapeException("Game is over and results in a draw");
		} else if (P1HasWon) {
			throw new EscapeException("Game is already won by PLAYER1");
		} else if (P2HasWon) {
			throw new EscapeException("Game is already won by PLAYER2");
		}
	}

	/**
	 * checks for game winner
	 */
	private void checkWinner()
	{
		int scoreLimit = ruleHandler.getScoreLimit();
		int turn = moves / 2;
		if (turn == ruleHandler.getTurnLimit()) {
			if (ruleHandler.getP1Score() > ruleHandler.getP2Score()) {
				P1HasWon = true;
				notifyObservers("PLAYER1 wins");
			} else if (ruleHandler.getP2Score() > ruleHandler.getP1Score()) {
				P2HasWon = true;
				notifyObservers("PLAYER2 wins");
			} else {
				P1HasWon = true;
				P2HasWon = true;
				notifyObservers("Game is over and results in a draw");
			}
		} else if (ruleHandler.getP1Score() >= scoreLimit) {
			P1HasWon = true;
			notifyObservers("PLAYER1 wins");
		} else if (ruleHandler.getP2Score() >= scoreLimit) {
			P2HasWon = true;
			notifyObservers("PLAYER2 wins");
		}
	}

	/**
	 * checks if player has any pieces
	 */
	private void hasPieces()
	{
		if (isPlayerOneTurn) {
			if (ruleHandler.getNumP1Pieces() == 0) {
				P2HasWon = true;
				throw new EscapeException("PLAYER2 wins");
			}
		} else {
			if (ruleHandler.getNumP2Pieces() == 0) {
				P1HasWon = true;
				throw new EscapeException("PLAYER1 wins");
			}
		}
	}

	/**
	 * checks if the player can move
	 */
	private void validMove()
	{
		checkGameOver();
		hasPieces();
	}

	/*
	 * @see escape.EscapeGameManager#move(escape.required.Coordinate,
	 * escape.required.Coordinate)
	 */
	/**
	 * Make the move in the current game.
	 * 
	 * @param from
	 *            starting location
	 * @param to
	 *            ending location
	 * @return true if the move was legal, false otherwise
	 */
	@Override
	public boolean move(C from, C to)
	{
		try {
			validMove();
			boolean result = board.move(from, to, isPlayerOneTurn);
			if (result) {
				moves++;
				checkWinner();
				isPlayerOneTurn = !isPlayerOneTurn;
			}
			return result;
		} catch (EscapeException e) {
			if (e.getCause() == null) {
				notifyObservers(e.getMessage());
			} else {
				notifyObservers(e.getMessage(), e.getCause());
			}
			return false;
		}
	}

	/*
	 * @see escape.EscapeGameManager#getPieceAt(escape.required.Coordinate)
	 */
	/**
	 * Return the piece located at the specified coordinate. If executing this method in
	 * the game instance causes an exception, then this method returns null and sets the
	 * status message appropriately. The status message is not used in the initial
	 * release(s) of the game.
	 * 
	 * @param coordinate
	 *            the location to probe
	 * @return the piece at the specified location or null if there is none
	 */
	@Override
	public EscapePiece getPieceAt(C coordinate)
	{
		try {
			return board.getPiece(coordinate);
		} catch (EscapeException e) {
			return null;
		}
	}

	/*
	 * @see escape.EscapeGameManager#makeCoordinate(int, int)
	 */
	/**
	 * Returns a coordinate of the appropriate type. If the coordinate cannot be created,
	 * then null is returned and the status message is set appropriately.
	 * 
	 * @param x
	 *            the x component
	 * @param y
	 *            the y component
	 * @return the coordinate or null if the coordinate cannot be implemented
	 */
	@Override
	public C makeCoordinate(int x, int y)
	{
		return (C) CoordinateFactory.makeCoordinate(type, x, y);
	}
}
