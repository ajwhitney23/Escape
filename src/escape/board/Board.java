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

package escape.board;

import java.util.HashMap;
import com.google.inject.Inject;
import escape.board.coordinates.EscapeCoordinate;
import escape.board.pathfind.*;
import escape.exception.EscapeException;
import escape.pieces.*;
import escape.pieces.EscapePiece.MovementPattern;
import escape.players.Player;
import escape.rules.RuleHandler;
import escape.util.*;

/**
 * Board holds a HashMap of Locations which the EscapeGameManager interacts with
 * 
 * @version Nov 11, 2020
 */
public class Board<C extends EscapeCoordinate>
{

	private HashMap<Integer, Location> board;
	private int xMax;
	private int yMax;
	private boolean isInfinite;
	private EscapePieceFactory pieceFactory;
	private Pathfinder pathfinder;
	private RuleHandler handler;

	/**
	 * Creates the board based on the x,y and preplaced locations
	 * 
	 * @param initializer
	 *            Locations to be initialized
	 * @param xMax
	 *            the x size of the board
	 * @param yMax
	 *            the y size of the board
	 */
	public Board(LocationInitializer[] initializer, int xMax, int yMax,
			PieceTypeDescriptor[] pieceInitailizer, RuleHandler handler)
	{
		this.handler = handler;
		this.board = new HashMap<>();
		this.xMax = xMax;
		this.yMax = yMax;
		this.pieceFactory = new EscapePieceFactory(pieceInitailizer);
		initalizeBoard(initializer);
		initializePathfinder();
	}

	/**
	 * creates the correct pathfinder based on which coordinate types are being used
	 */
	private void initializePathfinder()
	{
		switch (VersionProvider.getCoordinateType()) {
			case SQUARE:
				pathfinder = new SquarePathfinder();
				break;
			case TRIANGLE:
				pathfinder = new TrianglePathfinder();
				break;
		}
	}

	/**
	 * Sets up the board after the game is initialized, called in constructor
	 * 
	 * @param initializer
	 *            Locations on initialize
	 */
	private void initalizeBoard(LocationInitializer[] initializer)
	{
		for (LocationInitializer init : initializer) {
			Location location = new Location(init.x, init.y, init.locationType,
					pieceFactory.createPiece(init.pieceName, init.player));
			if(location.getEscapePiece() != null) {
				if(location.getEscapePiece().getPlayer() == Player.PLAYER1) {
					handler.addP1Piece();
				} else {
					handler.addP2Piece();
				}
			}
			board.put(key(location.getX(), location.getY()), location);
		}
		if (xMax == 0 && yMax == 0) {
			this.isInfinite = true;
		} else {
			this.isInfinite = false;
			for (int i = 1; i <= xMax; i++) {
				for (int j = 1; j <= yMax; j++) {
					Location location = new Location(i, j, LocationType.CLEAR, null);
					if (board.get(key(location.getX(), location.getY())) == null) {
						board.put(key(location.getX(), location.getY()), location);
					}
				}
			}
		}
	}

	/**
	 * Creates a key for the HashMap based on the x and y values of the board
	 * 
	 * @param x
	 *            the x value
	 * @param y
	 *            the y value
	 * @return the key to access that location on the board
	 */
	private int key(int x, int y)
	{
		return KeyCreator.createKey(x, y);
	}

	/**
	 * Gets a piece from a Location on the board. Will return null if either there is no
	 * piece or the location is not valid
	 * 
	 * @param x
	 *            the x location of the searched piece
	 * @param y
	 *            the y location of the searched piece
	 * @return the EscapePiece or null if there is none
	 */
	public EscapePiece getPiece(C cord)
	{
		if (cord != null) {
			Location location = board.get(key(cord.getX(), cord.getY()));
			if (location != null) {
				return location.getEscapePiece();
			} else {
				return null;
			}
		} else {
			throw new EscapeException("coordinate provided is null");
		}
	}

	/**
	 * Make the move in the current game.
	 * 
	 * @param from
	 *            starting location
	 * @param to
	 *            ending location
	 * @return true if the move was legal, false otherwise
	 */
	public boolean move(C from, C to, boolean isPlayerOneTurn)
	{
		if (from == null || to == null) {
			throw new EscapeException("Both coordinates need to be provided");
		}
		if (validFromLocation(from, isPlayerOneTurn)
				&& validToLocation(to, isPlayerOneTurn)) {
			if (pathfinder.isValidPath(this, from, to)) {
				updateBoard(from, to, isPlayerOneTurn);
				return true;
			}
		}
		return false;
	}

	/**
	 * updates the board when a move is validated
	 * 
	 * @param from
	 *            the source location
	 * @param to
	 *            the destination location
	 */
	private void updateBoard(C from, C to, boolean isP1Turn)
	{
		Location source = board.get(key(from.getX(), from.getY()));
		Location destination = board.get(key(to.getX(), to.getY()));
		EscapePiece piece = source.getEscapePiece();
		source.setEscapePiece(null);
		if (isExit(destination)) {
			if(isP1Turn) {
				handler.addP1Score(((EscapePieceImpl) piece).getValue());
				handler.removeP1Piece();
			} else {
				handler.addP2Score(((EscapePieceImpl) piece).getValue());
				handler.removeP2Piece();
			}
			destination.setEscapePiece(null);
		} else {
			destination.setEscapePiece(piece);
		}
	}

	/**
	 * checks if location is an exit
	 * 
	 * @param location
	 *            the location to check
	 * @return true if location is exit, else false
	 */
	private boolean isExit(Location location)
	{
		return location.getLocationType() == LocationType.EXIT;
	}

	/**
	 * Checks if the 'to' location is valid in move
	 * 
	 * @param cord
	 *            the to coordinate
	 * @param isPlayerOneTurn
	 *            specifies which players turn it is
	 * @return true if location is valid
	 */
	private boolean validToLocation(C cord, boolean isPlayerOneTurn)
	{
		if (isOpenLocation(cord)) {
			return true;
		}
		throw new EscapeException("To Coordinate Provided is not valid");
	}

	/**
	 * checks if the location from the coordinate is open
	 * 
	 * @param cord
	 *            the coordinate to check
	 * @return true if coordinate is valid, else return false
	 */
	private boolean isOpenLocation(C cord)
	{
		int x = cord.getX();
		int y = cord.getY();
		Location location = board.get(key(x, y));
		if (location != null) {
			if (location.getLocationType() != LocationType.BLOCK
					&& location.getEscapePiece() == null) {
				return true;
			}
		} else if (isInfinite) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the 'from' location is valid in move
	 * 
	 * @param cord
	 *            the from coordinate
	 * @param isPlayerOneTurn
	 *            specifies which players turn it is
	 * @return true if location is valid
	 */
	private boolean validFromLocation(C cord, boolean isPlayerOneTurn)
	{
		if (currentPlayerPiece(cord, isPlayerOneTurn)) {
			return true;
		}
		throw new EscapeException("From Coordinate provided is not valid");
	}

	/**
	 * returns if the piece is owned by the player who's turn it is
	 * 
	 * @param cord
	 *            the location to check
	 * @param isPlayerOneTurn
	 *            which players turn it is
	 * @return true if it is owned by the player making the move
	 */
	private boolean currentPlayerPiece(C cord, boolean isPlayerOneTurn)
	{
		if (isPiece(cord)) {
			Player piecePlayer = getPiece(cord).getPlayer();
			if (isPlayerOneTurn) {
				return piecePlayer == Player.PLAYER1;
			} else {
				return piecePlayer == Player.PLAYER2;
			}
		} else {
			return false;
		}
	}

	/**
	 * checks if the coordinates point to a piece
	 * 
	 * @param cord
	 *            the coordinate to check
	 * @return true if there is a piece at the location, otherwise return false
	 */
	private boolean isPiece(C cord)
	{
		EscapePiece piece = getPiece(cord);
		if (piece == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * returns the correct location based on the row,col provided
	 * 
	 * @param row
	 *            x location
	 * @param col
	 *            y location
	 * @return Location, if infinite board, creates a new location
	 */
	public Location getLocation(int row, int col)
	{
		Location loc = board.get(key(row, col));
		if (loc == null && isInfinite) {
			board.put(key(row, col),
					new Location(row, col, LocationType.CLEAR, null));
		}
		return board.get(key(row, col));
	}
}
