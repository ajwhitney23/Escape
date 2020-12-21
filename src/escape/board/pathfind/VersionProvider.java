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

import escape.board.coordinates.Coordinate.CoordinateType;

/**
 * provides coordinate version for setting correct pathfinding type
 * 
 * @version Nov 27, 2020
 */
public class VersionProvider
{
	private static CoordinateType type = null;

	/**
	 * @return type coordinateType used in implementation of game
	 */
	public static CoordinateType getCoordinateType()
	{
		return type;
	}

	/**
	 * sets the coordinate type
	 * 
	 * @param type
	 *            coordniatetype provided
	 */
	public static void setCoordinateType(CoordinateType type)
	{
		VersionProvider.type = type;
	}

}
