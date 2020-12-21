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
 * Holds the calculators for calculating distance to for different coordinate types
 * 
 * @version Nov 27, 2020
 */
class DistanceCalculator
{

	/**
	 * calculates the distanceTo for square coordinate types
	 * 
	 * @param from
	 *            from coordinates
	 * @param to
	 *            to coordinate
	 * @return distance b/w two coordinates
	 */
	protected static BiFunction<EscapeCoordinate, EscapeCoordinate, Integer> squareDistanceTo = (
			from, to) -> {
		return (Math.max(Math.abs(from.getX() - to.getX()),
				Math.abs(from.getY() - to.getY())));
	};

	/**
	 * calculates the distance to for triangle coordinate types
	 * 
	 * @param from
	 *            from coordinates
	 * @param to
	 *            to coordinate
	 * @return distance b/w two coordinates
	 */
	protected static BiFunction<EscapeCoordinate, EscapeCoordinate, Integer> triangleDistanceTo = (
			from, to) -> {
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

}
