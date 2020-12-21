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

import escape.pieces.*;
import escape.pieces.EscapePiece.*;
import escape.players.Player;

/**
 * A place on the board, stores its own x,y,type, and an escape piece (can be null)
 * 
 * @version Nov 11, 2020
 */
public class Location
{
	private int x;
	private int y;
	private LocationType locationType;
	private EscapePiece escapePiece;

	/**
	 * Location stores the x,y value of the location, its type, and a piece if one exits
	 * 
	 * @param x the x location
	 * @param y the y location
	 * @param locationType type of location
	 * @param player which player owns the piece
	 * @param pieceName the name of the piece
	 */
	public Location(int x, int y, LocationType locationType, EscapePiece piece)
	{
		this.x = x;
		this.y = y;
		this.locationType = locationType;
		this.escapePiece = piece;
	}

	/**
	 * @return the x value of the location
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return the y value of the location
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @return the locationType of the location
	 */
	public LocationType getLocationType()
	{
		return locationType;
	}

	/**
	 * @return the escapePiece at the location
	 */
	public EscapePiece getEscapePiece()
	{
		return escapePiece;
	}
	
	/**
	 * sets the location's piece to a new piece
	 * @param piece
	 */
	public void setEscapePiece(EscapePiece piece) {
		this.escapePiece = piece;
	}
		

}
