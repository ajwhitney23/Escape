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

/**
 * Implements template functions for pathfinding on triangle coordinates
 * 
 * @version Nov 27, 2020
 * @param <C>
 */
public class TrianglePathfinder<C extends EscapeCoordinate> extends Pathfinder<C>
{
	protected static BiFunction<Location, Location, Integer> distanceTo = (from,
			to) -> {
		int rowChange = from.getX() - to.getX();
		int colChange = from.getY() - to.getY();
		int fromOrientation = Math.abs(from.getX() + from.getY()) % 2;
		int toOrientation = Math.abs(to.getX() + to.getY()) % 2;
		boolean sameDir = false;
		if (fromOrientation == toOrientation) {
			sameDir = true;
		}
		if (Math.abs(colChange) == 0) {
			int result = 2 * Math.abs(rowChange);
			if (sameDir) {
				return result;
			} else if ((fromOrientation == 0 && rowChange > 0)
					|| (fromOrientation == 1 && rowChange < 0)) {
				return result + 1;
			} else if ((fromOrientation == 0 && rowChange < 0)
					|| (fromOrientation == 1 && rowChange > 0)) {
				return result - 1;
			}
		} else if (Math.abs(rowChange) < Math.abs(colChange)) {
			return Math.abs(rowChange) + Math.abs(colChange);
		} else if (Math.abs(rowChange) >= Math.abs(colChange)) {
			int result = 2 * Math.abs(rowChange);
			if (sameDir) {
				return result;
			} else {
				return result - 1;
			}
		}
		return 0;
	};
	
	private Location linearDest;

	/**
	 * sets the distanceTo calculation to be as if locations are triangles
	 */
	public TrianglePathfinder()
	{
		setDistanceTo(distanceTo);
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
		int xModifier = 0;
		int orientation = Math.abs(location.getX() + location.getY()) % 2;
		if (orientation == 1) {
			xModifier = -1;
		} else {
			xModifier = 1;
		}
		tempList.add(board.getLocation(locationX, locationY + 1));
		tempList.add(board.getLocation(locationX, locationY - 1));
		tempList.add(board.getLocation(locationX + xModifier, locationY));

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
		ArrayList<LocationNode> neighbors = new ArrayList<LocationNode>();
		Location location = board.getLocation(dest.getX(), dest.getY());
		neighbors.add(new LocationNode(location, distanceTo.apply(dest, location), 0));
		
		return neighbors;
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
					neighbors
							.add(new LocationNode(loc, parent.getDistanceSoFar() + 1,
									distanceTo.apply(loc, dest)));
				} else if (loc.getX() == dest.getX() && loc.getY() == dest.getY()) {
					neighbors
							.add(new LocationNode(loc, parent.getDistanceSoFar() + 1,
									distanceTo.apply(loc, dest)));
				} else if (loc.getEscapePiece() != null && getCanJump()) {
					int x = loc.getX() - locationX;
					int y = loc.getY() - locationY;
					y = y * 2;
					int xModifier = 0;
					int locOrientation = Math.abs(loc.getX() + loc.getY()) % 2;
					if (locOrientation == 1) {
						xModifier = -1;
					} else {
						xModifier = 1;
					}
					if (x == 0) {
						Location temp = getValidPiece(board, loc.getX() + xModifier,
								loc.getY());
						if (temp != null) {
							neighbors.add(new LocationNode(temp,
									parent.getDistanceSoFar() + 2,
									distanceTo.apply(temp, dest)));
						}
						temp = getValidPiece(board, loc.getX(), locationY + y);
						if (temp != null) {
							neighbors.add(new LocationNode(temp,
									parent.getDistanceSoFar() + 2,
									distanceTo.apply(temp, dest)));
						}
					} else {
						Location temp = getValidPiece(board, loc.getX(),
								loc.getY() - 1);
						if (temp != null) {
							neighbors.add(new LocationNode(temp,
									parent.getDistanceSoFar() + 2,
									distanceTo.apply(temp, dest)));
						}
						temp = getValidPiece(board, loc.getX(), loc.getY() + 1);
						if (temp != null) {
							neighbors.add(new LocationNode(temp,
									parent.getDistanceSoFar() + 2,
									distanceTo.apply(temp, dest)));
						}
					}
				} else if (loc.getLocationType() == LocationType.CLEAR
						&& loc.getEscapePiece() == null) {
					neighbors
							.add(new LocationNode(loc, parent.getDistanceSoFar() + 1,
									distanceTo.apply(loc, dest)));
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
		int fromOrientation;
		int toOrientation;
		int changeX;
		int changeY;
		if(parent == null) {
			fromOrientation = Math.abs(child.getX() + linearDest.getY()) % 2;
			toOrientation = Math.abs(child.getX() + linearDest.getY()) % 2;
			changeX = linearDest.getX() - child.getX();
			changeY = linearDest.getY() - child.getY();
		} else {
			fromOrientation = Math.abs(parent.getX() + parent.getY()) % 2;
			toOrientation = Math.abs(child.getX() + child.getY()) % 2;
			changeX = child.getX() - parent.getX();
			
			changeY = child.getY() - parent.getY();
		}
		if(changeX == 0) {
			if(changeY < 0) {
				return LinearDirection.W;
			} else {
				return LinearDirection.E;
			}
		} if(fromOrientation == 1) {
			if(changeX > 0) {
				if(toOrientation == 1) {
					if(changeX == changeY) { 
						return LinearDirection.NE;
					} else if(changeX + changeY == 0) { 
						return LinearDirection.NW;
					}
				} else {
					if(changeY - changeX == 1) {
						return LinearDirection.NE;
					} else if(changeX + changeY == -1) {
						return LinearDirection.NW;
					}
				}
			}
		} else {
			if(changeX < 0) {
				if(toOrientation == 0) {
					if(changeX + changeY == 0) {
						return LinearDirection.SE;
					} else if(changeY - changeX == 0) {
						return LinearDirection.SW;
					}
				} else {
					if(changeY + changeX == 1) {
						return LinearDirection.SE;
					} else if(changeY - changeX == -1) {
						return LinearDirection.SW;
					}
				}
			}
		}
		return null;
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
		if(getFly() == false) {
			return false;
		}
		this.linearDest = to;
		if(getDirection(from,to) != null) {
			return true;
		}
		return false;
		
	}

}
