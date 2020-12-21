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

import escape.board.Location;

/**
 * Nodes used in pathfinding to store values
 * 
 * @version Nov 29, 2020
 */
class LocationNode
{
	private Location location;
	private Location parent;
	private int distanceSoFar;
	private int estimatedDistanceLeft;

	/**
	 * Creates a Node to represent a location with no parent
	 * 
	 * @param location
	 *            represented
	 * @param distanceSoFar
	 *            distanceTraveled so far in pathfinding
	 * @param estimatedDistanceLeft
	 *            distanceTo from current location to dest
	 */
	public LocationNode(Location location, int distanceSoFar,
			int estimatedDistanceLeft)
	{
		this.location = location;
		this.distanceSoFar = distanceSoFar;
		this.estimatedDistanceLeft = estimatedDistanceLeft;
		this.parent = null;
	}

	/**
	 * Creates a node to represent a location
	 * 
	 * @param location
	 *            represented
	 * @param distanceSoFar
	 *            distanceTraveled so far in pathfinding
	 * @param estimatedDistanceLeft
	 *            distanceTo from current location to dest
	 * @param parent
	 *            parent location
	 */
	public LocationNode(Location location, int distanceSoFar,
			int estimatedDistanceLeft, Location parent)
	{
		this.location = location;
		this.distanceSoFar = distanceSoFar;
		this.estimatedDistanceLeft = estimatedDistanceLeft;
		this.parent = parent;
	}

	/**
	 * combines the estimated distanceLeft with distanceSoFar for total path length
	 * (estimate)
	 * 
	 * @return total length of estimated path
	 */
	public int getTotal()
	{
		return estimatedDistanceLeft + distanceSoFar;
	}

	/**
	 * @return Location represetned
	 */
	public Location getLocation()
	{
		return location;
	}

	/**
	 * @return returns distanceSoFar
	 */
	public int getDistanceSoFar()
	{
		return distanceSoFar;
	}

	/**
	 * @return parent of location in pathfinding
	 */
	public Location getParent()
	{
		return parent;
	}

}
