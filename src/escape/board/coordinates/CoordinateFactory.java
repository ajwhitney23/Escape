/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright �2020 Andrew Whitney
 *******************************************************************************/

package escape.board.coordinates;

import escape.board.coordinates.Coordinate.CoordinateType;

/**
 * Factory that creates coordinates for the makeCoordainte() function in EscapeGameManager
 * 
 * @version Nov 11, 2020
 */
public class CoordinateFactory
{

	/**
	 * creates the coordinates to be used in the EscapeGameManager
	 * 
	 * @param type
	 *            the type of coordinate to be used
	 * @param x
	 *            the x location of the coordinate
	 * @param y
	 *            the y location of the coordinate
	 * @return coordinate created, or null if type is not implemented yet
	 */
	public static EscapeCoordinate makeCoordinate(CoordinateType type, int x, int y)
	{
		EscapeCoordinate coordinate = null;
		switch (type) {
			case SQUARE:
				coordinate = new CoordinateImpl(x, y,
						DistanceCalculator.squareDistanceTo);
				break;
			case TRIANGLE:
				coordinate = new CoordinateImpl(x, y,
						DistanceCalculator.triangleDistanceTo);
				break;
			default:
				coordinate = null;
				break;
		}
		return coordinate;
	}

}
