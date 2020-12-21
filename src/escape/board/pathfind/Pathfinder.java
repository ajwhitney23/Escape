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

package escape.board.pathfind;

import java.util.ArrayList;
import java.util.function.BiFunction;
import escape.board.*;
import escape.board.coordinates.EscapeCoordinate;
import escape.exception.EscapeException;
import escape.pieces.EscapePiece.MovementPattern;
import escape.players.Player;
import escape.pieces.EscapePieceImpl;

/**
 * Pathfinder for finding locations in the board
 * 
 * @version Nov 27, 2020
 */
public abstract class Pathfinder<C extends EscapeCoordinate>
{
	private MovementPattern movementPattern;
	private boolean canJump;
	private boolean isFly;
	protected Player currentPlayer;
	private boolean linnearPossible;
	private boolean canUnblock;

	protected static BiFunction<Location, Location, Integer> distanceTo = (from,
			to) -> {
		return 0;
	};

	/**
	 * Directions to be used in pathfinding for linearity
	 * 
	 * @version Dec 1, 2020
	 */
	protected enum LinearDirection
	{
		N, S, E, W, NE, NW, SE, SW
	}

	/**
	 * empty default constructor
	 */
	public Pathfinder()
	{
	}

	/**
	 * Set distanceTo lambda
	 * 
	 * @param function
	 *            lambda to replace template lambda
	 */
	public static void setDistanceTo(
			BiFunction<Location, Location, Integer> function)
	{
		distanceTo = function;
	}

	/**
	 * Gets the path from the source to the dest and retuns if the path is valid or not,
	 * if no path is possible or can't be found returns false
	 * 
	 * @param board
	 *            board used in the game
	 * @param source
	 *            source coordinate
	 * @param dest
	 *            destination coordinate
	 * @return true if valid path, else return false
	 */
	public boolean isValidPath(Board board, C source, C dest)
	{
		EscapePieceImpl piece = (EscapePieceImpl) board.getPiece(source);
		int maxDistance = getMaxDistance(piece);
		if (source.DistanceTo(dest) > maxDistance) {
			throw new EscapeException("To Coordinate is too far away from From Piece");
		}
		this.canUnblock = piece.canUnblock();
		this.canJump = piece.canJump();
		if (isPlayer1Turn(piece)) {
			this.currentPlayer = Player.PLAYER1;
		} else {
			this.currentPlayer = Player.PLAYER2;
		}
		linnearPossible = isLinnearPossible(
				board.getLocation(source.getX(), source.getY()),
				board.getLocation(dest.getX(), dest.getY()));
		setMovementPattern(piece.getMovementPattern());
		int pathSize = getPath(board, source, dest);
		if (pathSize <= maxDistance) {
			return true;
		}
		throw new EscapeException("No valid path found between the from and to coordinates provided");
	}

	/**
	 * @param piece
	 *            source locations piece
	 * @return the max distance, also sets if piece can fly or not
	 */
	private int getMaxDistance(EscapePieceImpl piece)
	{
		if (piece.getFly() != 0) {
			isFly = true;
			return piece.getFly();
		} else {
			isFly = false;
			return piece.getMaxDistance();
		}
	}

	/**
	 * A* Algorithm derived from geeksforgeeks website:
	 * https://www.geeksforgeeks.org/a-search-algorithm/
	 * 
	 * @param board
	 *            game board
	 * @param source
	 *            source coordinate
	 * @param dest
	 *            destination coordinate
	 * @return the length of the path found
	 */
	private int getPath(Board board, C source, C dest)
	{
		Location end = board.getLocation(dest.getX(), dest.getY());
		ArrayList<LocationNode> open = new ArrayList<LocationNode>();
		ArrayList<LocationNode> closed = new ArrayList<LocationNode>();
		open.add(new LocationNode(board.getLocation(source.getX(), source.getY()), 0,
				distanceTo.apply(board.getLocation(source.getX(), source.getY()),
						end)));
		while (!open.isEmpty()) {
			LocationNode currentNode = null;
			int currentNodeDistance = Integer.MAX_VALUE;
			int currentNodeIndex = 0;
			for (int i = open.size() - 1; i >= 0; i--) {
				LocationNode loc = open.get(i);
				if (currentNode == null) {
					currentNode = loc;
					currentNodeDistance = currentNode.getTotal();
					currentNodeIndex = i;
				} else {
					if (loc.getTotal() < currentNodeDistance) {
						currentNodeDistance = loc.getTotal();
						currentNode = loc;
						currentNodeIndex = i;
					}
				}
			}
			open.remove(currentNodeIndex);
			ArrayList<LocationNode> neighbors = getNeighbors(board, currentNode, end);
			if (neighbors != null) {
				for (LocationNode neighbor : neighbors) {
					if (sameLocation(neighbor.getLocation(), end)) {
						return currentNode.getTotal();
					} else if (!onListLower(neighbor, open)
							&& !onListLower(neighbor, closed)) {
						open.add(neighbor);
					}
				}
			}
			closed.add(currentNode);
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * takes in the list of neighbors and ensures each location is a valid neighbor that
	 * can be traveled to
	 * 
	 * @param board
	 *            board used in the game
	 * @param parent
	 *            parent location
	 * @param dest
	 *            destination of pathfind
	 * @param list
	 *            list of gathered neighbors
	 * @return list of valid gathered neighbors as nodes
	 */
	protected ArrayList<LocationNode> validateNeighbors(Board board,
			LocationNode parent, Location dest, ArrayList<Location> list)
	{
		return null;
	}

	/**
	 * searches list and finds if locations node represented same location is on the list
	 * but with a lower total distance
	 * 
	 * @param location
	 *            node of location
	 * @param list
	 *            list of either open or closed nodes
	 * @return true if lower total distance exists else false
	 */
	private boolean onListLower(LocationNode location, ArrayList<LocationNode> list)
	{
		for (LocationNode node : list) {
			if (sameLocation(location.getLocation(), node.getLocation())
					&& node.getTotal() < location.getTotal()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * compares two locations
	 * 
	 * @param a
	 *            one location
	 * @param b
	 *            two location
	 * @return true if locations are the same, else false
	 */
	private boolean sameLocation(Location a, Location b)
	{
		if (a.getX() == b.getX() && a.getY() == b.getY()) {
			return true;
		}
		return false;
	}

	/**
	 * gets neighbors of the locations specified
	 * 
	 * @param board
	 *            board used for the game
	 * @param location
	 *            current location in pathfind
	 * @param dest
	 *            destination location
	 * @return list of location nodes of neighbors
	 */
	protected ArrayList<LocationNode> getNeighbors(Board board,
			LocationNode location, Location dest)
	{
		ArrayList<LocationNode> neighbors = new ArrayList<LocationNode>();
		switch (getMovementPattern()) {
			case DIAGONAL:
				neighbors = getDiagonalNeighbors(board, location, dest);
				break;
			case LINEAR:
				if (linnearPossible) {
					neighbors = getLinearNeighbors(board, location, dest,
							getDirection(location.getParent(),
									location.getLocation()));
				} else {
					neighbors = null;
				}
				break;
			case OMNI:
				neighbors = getOmniNeighbors(board, location, dest);
				break;
			case ORTHOGONAL:
				neighbors = getOrthogonalNeighbors(board, location, dest);
				break;
			default:
				break;
		}
		return neighbors;
	}

	/**
	 * gets the direction based on the parent and child locations x&y
	 * 
	 * @param parent
	 * @param child
	 * @return the linear direction b/w the two locations
	 */
	protected LinearDirection getDirection(Location parent, Location child)
	{
		return null;
	}

	/**
	 * takes in two locations and returns true if linear is possible
	 * 
	 * @param from
	 *            location
	 * @param to
	 *            location
	 * @return true if linear path is possible, else return false
	 */
	protected boolean isLinnearPossible(Location from, Location to)
	{
		return false;
	}

	/**
	 * SUPPORTED BOARDS: SQUARE
	 */
	/**
	 * returns list of diagonal neighbors
	 * 
	 * @param board
	 *            board used
	 * @param location
	 *            current node
	 * @param dest
	 *            destination location
	 * @return list of diagonal nodes
	 */
	protected ArrayList<LocationNode> getDiagonalNeighbors(Board board,
			LocationNode location, Location dest)
	{
		return null;
	}

	/**
	 * SUPPORTED BOARDS: SQUARE, ORTHOSQUARE, HEX, TRIANGLE
	 */
	/**
	 * returns list of linear neighbors
	 * 
	 * @param board
	 *            board used
	 * @param location
	 *            current node
	 * @param dest
	 *            destination location
	 * @return list of linear nodes
	 */
	protected ArrayList<LocationNode> getLinearNeighbors(Board board,
			LocationNode location, Location dest, LinearDirection direction)
	{
		return null;
		// TODO Auto-generated method stub

	}

	/**
	 * SUPPORTED BOARDS: SQUARE, ORTHOSQUARE, HEX, TRIANGLE
	 */
	/**
	 * returns list of omni neighbors
	 * 
	 * @param board
	 *            board used
	 * @param location
	 *            current node
	 * @param dest
	 *            destination location
	 * @return list of omni nodes
	 */
	protected ArrayList<LocationNode> getOmniNeighbors(Board board,
			LocationNode location, Location dest)
	{
		return null;
		// TODO Auto-generated method stub

	}

	/**
	 * SUPPORTED BOARDS: SQUARE, ORTHOSQUARE
	 */
	/**
	 * returns list of orthogonal neighbors
	 * 
	 * @param board
	 *            board used
	 * @param location
	 *            current node
	 * @param dest
	 *            destination location
	 * @return list of orthogonal nodes
	 */
	protected ArrayList<LocationNode> getOrthogonalNeighbors(Board board,
			LocationNode location, Location dest)
	{
		return null;
		// TODO Auto-generated method stub

	}

	/**
	 * sets the movement pattern for pathfinding
	 * 
	 * @param pattern
	 *            movement pattern
	 */
	protected void setMovementPattern(MovementPattern pattern)
	{
		this.movementPattern = pattern;
	}

	/**
	 * @return the movementpattern for pathfinding
	 */
	protected MovementPattern getMovementPattern()
	{
		return movementPattern;
	}

	/**
	 * @return true if piece can jump
	 */
	protected boolean getCanJump()
	{
		return canJump;
	}

	/**
	 * @return true if piece can fly
	 */
	protected boolean getFly()
	{
		return isFly;
	}

	/**
	 * returns true if it is player ones turn else return false
	 * 
	 * @param piece
	 *            of souce location
	 * @return true if p1 turn, else false
	 */
	private boolean isPlayer1Turn(EscapePieceImpl piece)
	{
		if (piece.getPlayer() == Player.PLAYER1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * gets valid piece from location on board
	 * 
	 * @param board
	 *            board used in game
	 * @param row
	 *            x location
	 * @param col
	 *            y location
	 * @return Location on board if it is an empty location
	 */
	protected Location getValidPiece(Board board, int row, int col)
	{
		Location location = board.getLocation(row, col);
		if (location.getLocationType() == LocationType.CLEAR
				&& location.getEscapePiece() == null) {
			return location;
		} else {
			return null;
		}
	}
	
	protected boolean getCanUnblock() {
		return canUnblock;
	}

}
