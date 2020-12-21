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

package escape.board.coordinates;

import java.util.function.BiFunction;

/**
 * Implementation of EscapeCoordinate. Coordinates are used to go to different locations
 * on the board
 * 
 * @version Nov 10, 2020
 */
public class CoordinateImpl implements EscapeCoordinate
{

	private int x;
	private int y;
	private BiFunction<EscapeCoordinate, EscapeCoordinate, Integer> distanceTo;

	/**
	 * Coordinates used by the user to specify locations on the board
	 * 
	 * @param x
	 *            x value for location
	 * @param y
	 *            y value for location
	 * @param distanceTo
	 *            lambda used to calculate the distanceTo based on different square types
	 */
	public CoordinateImpl(int x, int y,
			BiFunction<EscapeCoordinate, EscapeCoordinate, Integer> distanceTo)
	{
		this.x = x;
		this.y = y;
		this.distanceTo = distanceTo;
	}

	/*
	 * @see escape.coordinates.Coordinate#DistanceTo(escape.coordinates.Coordinate)
	 */
	/**
	 * Return the distance from this coordinate to another based solely upon the
	 * coordinate type as described in the Escape Developer's Guide. This method is
	 * normally not used by a client, but is defined her in order for some tests for the
	 * alpha release.
	 * 
	 * @param c
	 *            the coordinate to find distance from
	 * @return the distance to the other coordinate
	 */
	@Override
	public int DistanceTo(Coordinate c)
	{
		int result = distanceTo.apply(this, (EscapeCoordinate) c);
		return result;
	}

	/*
	 * @see escape.coordinates.EscapeCoordinate#getX()
	 */
	/**
	 * @return the x value
	 */
	@Override
	public int getX()
	{
		return x;
	}

	/*
	 * @see escape.coordinates.EscapeCoordinate#getY()
	 */
	/**
	 * @return the y value
	 */
	@Override
	public int getY()
	{
		return y;
	}

}
