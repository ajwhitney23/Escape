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

package escape;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import escape.board.coordinates.Coordinate;
import escape.pieces.EscapePiece;
import escape.players.Player;

/**
 * This is a simple test, not really a unit test, to make sure that the EscapeGameBuilder,
 * in the starting code, is actually working.
 * 
 * @version May 30, 2020
 */
class EscapeGameBuilderTest
{
	private static EscapeGameManager manager;

	@Test
	void testEscapeGameBuilder() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test1.egc");
		manager = egb.makeGameManager();
		assertNotNull(manager);
	}
}
