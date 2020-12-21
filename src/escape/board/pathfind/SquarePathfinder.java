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
import escape.board.pathfind.Pathfinder.LinearDirection;
import escape.pieces.EscapePiece.MovementPattern;

/**
 * Implements template functions for pathfinding on square coordinates
 * 
 * @version Nov 27, 2020
 * @param <C>
 */
public class SquarePathfinder<C extends EscapeCoordinate> extends Pathfinder<C>
{
	protected static BiFunction<Location, Location, Integer> distanceTo = (from,
			to) -> {
		return (Math.max(Math.abs(from.getX() - to.getX()),
				Math.abs(from.getY() - to.getY())));
	};

	/**
	 * Constructor, sets the distanceTo calculation to calculate as it locations are
	 * squares
	 */
	public SquarePathfinder()
	{
		setDistanceTo(distanceTo);
	}

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
	@Override
	protected ArrayList<LocationNode> getDiagonalNeighbors(Board board,
			LocationNode parent, Location dest)
	{
		ArrayList<Location> tempList = new ArrayList<Location>();
		Location location = parent.getLocation();
		int locationX = location.getX();
		int locationY = location.getY();
		tempList.add(board.getLocation(locationX + 1, locationY + 1));
		tempList.add(board.getLocation(locationX - 1, locationY - 1));
		tempList.add(board.getLocation(locationX + 1, locationY - 1));
		tempList.add(board.getLocation(locationX - 1, locationY + 1));

		return validateNeighbors(board, parent, dest, tempList);
	}

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
	@Override
	protected ArrayList<LocationNode> getOmniNeighbors(Board board,
			LocationNode parent, Location dest)
	{
		ArrayList<Location> tempList = new ArrayList<Location>();
		Location location = parent.getLocation();
		int locationX = location.getX();
		int locationY = location.getY();
		tempList.add(board.getLocation(locationX + 1, locationY + 1));
		tempList.add(board.getLocation(locationX - 1, locationY - 1));
		tempList.add(board.getLocation(locationX + 1, locationY - 1));
		tempList.add(board.getLocation(locationX - 1, locationY + 1));
		tempList.add(board.getLocation(locationX, locationY + 1));
		tempList.add(board.getLocation(locationX, locationY - 1));
		tempList.add(board.getLocation(locationX + 1, locationY));
		tempList.add(board.getLocation(locationX - 1, locationY));

		return validateNeighbors(board, parent, dest, tempList);
	}

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
	@Override
	protected ArrayList<LocationNode> getLinearNeighbors(Board board,
			LocationNode parent, Location dest, LinearDirection direction)
	{
		ArrayList<Location> tempList = new ArrayList<Location>();
		Location location = parent.getLocation();
		int locationX = location.getX();
		int locationY = location.getY();
		if (direction != null) {
			switch (direction) {
				case E:
					tempList.add(board.getLocation(locationX, locationY + 1));
					break;
				case N:
					tempList.add(board.getLocation(locationX + 1, locationY));
					break;
				case NE:
					tempList.add(board.getLocation(locationX + 1, locationY + 1));
					break;
				case NW:
					tempList.add(board.getLocation(locationX + 1, locationY - 1));
					break;
				case S:
					tempList.add(board.getLocation(locationX - 1, locationY));
					break;
				case SE:
					tempList.add(board.getLocation(locationX - 1, locationY + 1));
					break;
				case SW:
					tempList.add(board.getLocation(locationX - 1, locationY - 1));
					break;
				case W:
					tempList.add(board.getLocation(locationX, locationY - 1));
					break;
			}
		} else {
			tempList.add(board.getLocation(locationX + 1, locationY + 1));
			tempList.add(board.getLocation(locationX - 1, locationY - 1));
			tempList.add(board.getLocation(locationX + 1, locationY - 1));
			tempList.add(board.getLocation(locationX - 1, locationY + 1));
			tempList.add(board.getLocation(locationX, locationY + 1));
			tempList.add(board.getLocation(locationX, locationY - 1));
			tempList.add(board.getLocation(locationX + 1, locationY));
			tempList.add(board.getLocation(locationX - 1, locationY));
		}

		return validateNeighbors(board, parent, dest, tempList);
	}

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
	@Override
	protected ArrayList<LocationNode> getOrthogonalNeighbors(Board board,
			LocationNode parent, Location dest)
	{
		ArrayList<Location> tempList = new ArrayList<Location>();
		Location location = parent.getLocation();
		int locationX = location.getX();
		int locationY = location.getY();
		tempList.add(board.getLocation(locationX, locationY + 1));
		tempList.add(board.getLocation(locationX, locationY - 1));
		tempList.add(board.getLocation(locationX + 1, locationY));
		tempList.add(board.getLocation(locationX - 1, locationY));

		return validateNeighbors(board, parent, dest, tempList);
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
	@Override
	protected ArrayList<LocationNode> validateNeighbors(Board board,
			LocationNode parent, Location dest, ArrayList<Location> tempList)
	{
		ArrayList<LocationNode> neighbors = new ArrayList<LocationNode>();
		int locationX = parent.getLocation().getX();
		int locationY = parent.getLocation().getY();
		for (Location loc : tempList) {
			if (loc != null) {
				if (getFly()) {
					neighbors.add(new LocationNode(loc,
							parent.getDistanceSoFar() + 1,
							distanceTo.apply(loc, dest), parent.getLocation()));
				} else if (loc.getLocationType() == LocationType.BLOCK
						&& getCanUnblock() && !(loc.getX() == dest.getX()
						&& loc.getY() == dest.getY())) {
					neighbors.add(new LocationNode(loc, parent.getDistanceSoFar() + 1,
							distanceTo.apply(loc, dest), parent.getLocation()));
				} else if (loc.getX() == dest.getX() && loc.getY() == dest.getY()) {
					neighbors.add(new LocationNode(loc,
							parent.getDistanceSoFar() + 1,
							distanceTo.apply(loc, dest), parent.getLocation()));
				} else if (loc.getEscapePiece() != null && getCanJump()) {
					int x = loc.getX() - locationX;
					int y = loc.getY() - locationY;
					x = x * 2;
					y = y * 2;
					Location temp = getValidPiece(board, locationX + x,
							locationY + y);
					if (temp != null) {
						neighbors.add(new LocationNode(temp,
								parent.getDistanceSoFar() + 2,
								distanceTo.apply(temp, dest), parent.getLocation()));
					}
				} else if (loc.getLocationType() == LocationType.CLEAR
						&& loc.getEscapePiece() == null) {
					neighbors.add(new LocationNode(loc,
							parent.getDistanceSoFar() + 1,
							distanceTo.apply(loc, dest), parent.getLocation()));
				}
			}
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
	@Override
	protected LinearDirection getDirection(Location parent, Location child)
	{
		LinearDirection direction = null;
		if (parent == null) {
			return direction;
		} else {
			int changeX = child.getX() - parent.getX();
			int changeY = child.getY() - parent.getY();

			if (changeX == 1 && changeY == 1) {
				return LinearDirection.NE;
			} else if (changeX == 0 && changeY == 1) {
				return LinearDirection.E;
			} else if (changeX == -1 && changeY == 1) {
				return LinearDirection.SE;
			} else if (changeX == -1 && changeY == 0) {
				return LinearDirection.S;
			} else if (changeX == -1 && changeY == -1) {
				return LinearDirection.SW;
			} else if (changeX == 0 && changeY == -1) {
				return LinearDirection.W;
			} else if (changeX == 1 && changeY == -1) {
				return LinearDirection.NW;
			} else {
				return LinearDirection.N;
			}
		}
	}

	/**
	 * takes in two locations and returns true if linear is possible
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@Override
	protected boolean isLinnearPossible(Location from, Location to)
	{
		int changeX = Math.abs(from.getX() - to.getX());
		int changeY = Math.abs(from.getY() - to.getY());
		if (changeX == changeY || changeX == 0 || changeY == 0) {
			return true;
		} else {
			return false;
		}
	}
}
