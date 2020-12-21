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

package escape.pieces;

import escape.players.Player;
import escape.util.PieceAttribute;

/**
 * Implementation of EscapePiece, are the pieces on the locations on the board.
 * 
 * @version Nov 11, 2020
 */
public class EscapePieceImpl implements EscapePiece
{

	private Player player;
	private PieceName pieceName;
	private MovementPattern movementPattern;
	private PieceAttribute distance;
	private PieceAttribute jump;
	private PieceAttribute unblock;
	private PieceAttribute fly;

	/**
	 * Creates and implementation of an EscapePiece with an associated player and name
	 * 
	 * @param player
	 * @param pieceName
	 */
	public EscapePieceImpl(Player player, PieceName pieceName,
			MovementPattern movementPattern, PieceAttribute... attributes)
	{
		this.player = player;
		this.pieceName = pieceName;
		this.movementPattern = movementPattern;
		initializeAttributes(attributes);
	}

	/**
	 * inits the attributes of the piece
	 * 
	 * @param attributes
	 */
	private void initializeAttributes(PieceAttribute... attributes)
	{
		for (PieceAttribute attr : attributes) {
			switch (attr.getId()) {
				case DISTANCE:
					distance = attr;
					break;
				case FLY:
					fly = attr;
					break;
				case JUMP:
					jump = attr;
					break;
				case UNBLOCK:
					unblock = attr;
					break;
				default:
					break;
			}
		}
	}

	/**
	 * @return pieceName the name of the piece
	 */
	@Override
	public PieceName getName()
	{
		return pieceName;
	}

	/**
	 * @return player which player controls the piece
	 */
	@Override
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * @return movementPattern the movement pattern the piece uses
	 */
	public MovementPattern getMovementPattern()
	{
		return movementPattern;
	}

	/**
	 * @return distance.getValue() the max distance the piece can go in a turn
	 */
	public int getMaxDistance()
	{
		if (distance != null) {
			return distance.getValue();
		} else {
			return 0;
		}
	}

	/**
	 * @return the fly distance
	 */
	public int getFly()
	{
		if (fly != null) {
			return fly.getValue();
		} else {
			return 0;
		}
	}

	/**
	 * @return the value of the piece
	 */
	public int getValue()
	{
		return 1;
	}

	/**
	 * @return true if piece can jump, else false
	 */
	public boolean canJump()
	{
		if (jump != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return true if piece can unblock, else false
	 */
	public boolean canUnblock()
	{
		if (unblock != null) {
			return true;
		} else {
			return false;
		}
	}

}
