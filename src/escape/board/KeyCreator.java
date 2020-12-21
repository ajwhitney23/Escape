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

/**
 * created the key value for retrieving locations on the board
 * 
 * @version Nov 27, 2020
 */
class KeyCreator
{
	/**
	 * this function creates the keys for each location on the hashmap
	 * 
	 * @param x,
	 *            row
	 * @param y,
	 *            column
	 * @return key used to retrive location at row,col
	 */
	public static int createKey(int x, int y)
	{
		return (x * 133807) + (y * 31);
	}

}
