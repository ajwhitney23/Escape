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

import escape.exception.EscapeException;
import escape.pieces.EscapePiece.*;
import escape.players.Player;
import escape.util.*;

/**
 * factory that creates pieces based on their descriptors
 * 
 * @version Nov 24, 2020
 */
public class EscapePieceFactory
{
	private PieceTypeDescriptor birdInitializer;
	private PieceTypeDescriptor dogInitializer;
	private PieceTypeDescriptor frogInitializer;
	private PieceTypeDescriptor horseInitializer;
	private PieceTypeDescriptor snailInitializer;

	/**
	 * initializes the escape factory when the game is being initialized
	 * 
	 * @param initializer
	 *            array of piecetypedescriptors that initialize the movement patterns and
	 *            attributes for each piece
	 */
	public EscapePieceFactory(PieceTypeDescriptor[] initializer)
	{
		for (PieceTypeDescriptor init : initializer) {
			switch (init.getPieceName()) {
				case BIRD:
					this.birdInitializer = init;
					break;
				case DOG:
					this.dogInitializer = init;
					break;
				case FROG:
					this.frogInitializer = init;
					break;
				case HORSE:
					this.horseInitializer = init;
					break;
				case SNAIL:
					this.snailInitializer = init;
					break;
				default:
					throw new EscapeException("PieceName not known");
			}
		}
	}

	/**
	 * factory method that creates pieces based on the names
	 * 
	 * @param name
	 *            type of piece
	 * @param player
	 *            player that owns the piece
	 * @return new piece
	 */
	public EscapePiece createPiece(PieceName name, Player player)
	{
		EscapePiece piece = null;
		if (name == null || player == null) {
			return piece;
		}
		switch (name) {
			case BIRD:
				piece = new EscapePieceImpl(player, name,
						birdInitializer.getMovementPattern(),
						birdInitializer.getAttributes());
				break;
			case DOG:
				piece = new EscapePieceImpl(player, name,
						dogInitializer.getMovementPattern(),
						dogInitializer.getAttributes());
				break;
			case FROG:
				piece = new EscapePieceImpl(player, name,
						frogInitializer.getMovementPattern(),
						frogInitializer.getAttributes());
				break;
			case HORSE:
				piece = new EscapePieceImpl(player, name,
						horseInitializer.getMovementPattern(),
						horseInitializer.getAttributes());
				break;
			case SNAIL:
				piece = new EscapePieceImpl(player, name,
						snailInitializer.getMovementPattern(),
						snailInitializer.getAttributes());
				break;
			default:
				throw new EscapeException("PieceName not known");
		}
		return piece;
	}

}
