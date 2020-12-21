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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import escape.board.coordinates.Coordinate;

/**
 * Description
 * @version Nov 25, 2020
 */
class EscapeGameManagerTriangleCoordianteTest
{
	private static EscapeGameManager manager;
	
	@BeforeEach
	void setUp() throws Exception
	{
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test4.egc");
		manager = egb.makeGameManager();
	}

	// Test 25
	@Test
	void testTraingleDistanceToSameColumnOppositeDirectionMovingUp() throws Exception
	{
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(2, 1);
		assertEquals(1, c1.DistanceTo(c2));
	}

	// Test 26
	@Test
	void testTraingleDistanceToSameColumnSame() throws Exception
	{
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(3, 1);
		assertEquals(4, c1.DistanceTo(c2));
	}

	// Test 27
	@Test
	void testTraingleDistanceToSameColumnOppositeDirectionMovingDown()
			throws Exception
	{
		Coordinate c1 = manager.makeCoordinate(3, 1);
		Coordinate c2 = manager.makeCoordinate(2, 1);
		assertEquals(3, c1.DistanceTo(c2));
	}

	// Test 28
	@Test
	void testTraingleDistanceToSameColumnOppositeDirectionMovingUp2()
			throws Exception
	{
		Coordinate c1 = manager.makeCoordinate(1, 3);
		Coordinate c2 = manager.makeCoordinate(4, 3);
		assertEquals(5, c1.DistanceTo(c2));
	}

	// Test 29
	@Test
	void testTraingleDistanceToSameColumnOppositeDirectionMovingUpUpFirst()
			throws Exception
	{
		Coordinate c1 = manager.makeCoordinate(1, 4);
		Coordinate c2 = manager.makeCoordinate(4, 4);
		assertEquals(7, c1.DistanceTo(c2));
	}
	
	// Test 30
	@Test
	void testTraingleDistanceToSameColumnOppositeDirectionMovingDownUpFirst()
			throws Exception
	{
		Coordinate c1 = manager.makeCoordinate(3, 4);
		Coordinate c2 = manager.makeCoordinate(1, 4);
		assertEquals(4, c1.DistanceTo(c2));
	}
	
	// Test 31
	@Test
	void testTriangleDistanceToChangeInRowsLessThanChangeInColumns()
	{
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(2, 7);
		assertEquals(7, c1.DistanceTo(c2));
	}
	
	// Test 32
	@Test
	void testTriangleDistanceToChangeInRowsLessThanChangeInColumns2()
	{
		Coordinate c1 = manager.makeCoordinate(2, 1);
		Coordinate c2 = manager.makeCoordinate(4, 7);
		assertEquals(8, c1.DistanceTo(c2));
	}
	
	// Test 33
	@Test
	void testTriangleDistanceToSameDirectionChangeInRowsGTChangeInColumns()
	{
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(4, 2);
		assertEquals(6, c1.DistanceTo(c2));
	}
	
	// Test 34
	@Test
	void testTriangleDistanceToSameDirectionChangeInRowsEqualChangeInColumns()
	{
		Coordinate c1 = manager.makeCoordinate(2, 2);
		Coordinate c2 = manager.makeCoordinate(4, 4);
		assertEquals(4, c1.DistanceTo(c2));
	}
	
	// Test 35
	@Test
	void testTriangleDistanceToOppositeDirectionChangeInRowsGTChangeInColumns()
	{
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(3, 2);
		assertEquals(3, c1.DistanceTo(c2));
	}
	
	

}
