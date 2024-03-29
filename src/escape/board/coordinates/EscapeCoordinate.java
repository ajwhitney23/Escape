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

/**
 * Own implementation of Coordinate that includes getting the X and Y values
 * 
 * @version Nov 10, 2020
 */
public interface EscapeCoordinate extends Coordinate
{
	int getX();

	int getY();
}
