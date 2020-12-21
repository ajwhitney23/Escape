# My Escape Game Blog
### By: Andrew Whitney (ajwhitney)

# Alpha

## Nov. 10
- Read Over Requirements for Alpha
	- create a new game from a spec file (only SQUARE coordinates used)
	- Invoke the getPieceAt() (in EscapeGameManager) method and get theEscapePieceat that location on the board. This method does depend upon the board for the game configuration. This means that if an invalid coordinate is given that is not on the game board, 	you will return null.
	- Call the makeCoordinate() (in EscapeGameManager) method to create a valid SQUARE coordinate.  This does not depend upon the board.
	- Move a piece from one location to another.  You do not have to obey any constraints for the move, just move from coordinate c1 to c2.
	
NOTE: Documentation has a table (Table 5) that goes over the Alpha release results of calling move(). this function will not throw exceptions, it should catch them and then return false.  Assumption is that false means move is not valid while true means move is valid.  false does not switch players.
 
- Read over the Escape Project Overview and watched all related videos ( office hours / Intro to Escape / Approaching Coordinates )

We are keeping track of which players turn it is

## Nov. 11
- for coordinate factory, only implementing SQUARE since that is all in alpha requirements
- trying to figure out where to store where pieces are, thinking either hash map or 2D array. 

- Creating a Board class, which will have a hash map of Locations.  These locations will keep track of x,y,type,player,pieceName.  For finite boards, all locations within the board will be created

- makeCoordinate ensure it is a valid x,y.

- Look into injecting the board into the manager, right now it creates it in the Manager constructor
	- not sure if needed yet
	
- Location will create a new EscapePieceImpl which will keep track of which player owns it, it player or pieceName is null, it will not create one

- getPieceAt() now works

## Nov. 12

- adjusted coordinate function based on slack conversation saying makeCoordinate should be able to make coordinates not on the board

- created more tests to address some code missed

## Nov. 15

- starting work on move() function

	for true:
		- legal move
			- destination is exit
			- destination is opponents piece
			- destination is an empty location	
	for false:
		- illegal move
		- player stays the same

- thinking of make a getLocation in GameManagerImpl or just doing the entire move in Board then turning the true/false

## Nov. 16

- move() mostly finished,  thinking move() to same location would return false since "A moving player's piece on the destination"
	UPDATE: professor said for alpha, this returns true, refactor move to return true in this scenario
	
## Nov. 17

- added javadoc for all private methods

## Nov. 18

- cleanup & submit today, ensure all requirements are met

- equals(Object o) function not used in this version, keeping for hashmap

```java
	/**
	 * @return true if the locations have the same x,y or are the same locations
	 */
	@Override
	public boolean equals(Object o)
	{
		if (this == o) {
			return true;
		}
		if (!(o instanceof Location)) {
			return false;
		}
		Location location = (Location) o;
		return x == location.x && y == location.y;
	}
```

- moved all of the EscapeGameManager tests out of the EscapeGameBuilderTest file

# Beta

## Nov. 23

- reading over beta requirements, taking note of changes to be made, initial thoughts below: 
- first thing to add may be triangle coordinates and distanceTo, that way testing movement patterns can be tested on square and triangle
- next is infinite boards now possible
- movement patterns now being used (2.4.1)
- update EscapePieceImpl to have traits and movement qualities (2.4.2) (might use builder pattern to build each piece, or factory would be enough, not sure yet)
- look into A* and using modified version of this to find shortest paths

```
attributes notes:
(each attribute and if needed for beta)
- VALUE (not needed)
- DISTANCE (needed)
- JUMP (needed)
- UNBLOCK (needed)
- FLY (needed)
```

- adding an EscapePieceTest for testing the pieces are being created correctly


## Nov. 24

- updated game rules so now if to and from location are the same, returns false, updated move and test to reflect that

- worked on triangleDistance, triangle to figure out if x,y is row,col or col, row... will look for office hours tomorrow.  used table from beta discussion to help create triangleDistanceTo 

## Nov. 25

- today I continued work on the traingleDistance, finally getting to work smoothly.  make a chart w/ the available neighbors for each movement patter/ coordinate type combo for future work the the movement algo

- created factory for creating pieces, thinking of making own interface so i can retrieve movement patterns && attributes

BELOW IS MY NOTES

```
TRIANGLE
	OMNI (coordinate DistanceTo function would always be the most optimized path)
		upwards triangle 	->	y+1, y-1, x-1
		downwards triangle 	->	y+1, y-1, x+1

SQUARE
	DIAG
		each step:
			x+1 & y+1
			x+1 & y-1
			x-1 & y+1
			x-1 & y-1
	LINEAR
		entire way:
			x+dist
			x-dist
			y+dist
			y-dist
		note: can check if to and from share a common x || y value, else not possible
	ORTH
		each step:
			x+1
			x-1
			y+1
			y-1
	OMNI (coordinate DistanceTo function would always be the most optimized path)
		each step:
			x+1
			x-1
			y+1
			y-1
			x+1 & y+1
			x+1 & y-1
			x-1 & y+1
			x-1 & y-1
```

- continued research on algorithms

- infinite triangle boards now implemented


## Nov. 27

- looking at movement patterns, easiest to implement will be the linear for square
- plan of attack, try to implement a path finding algorithm that finds a valid path, it does not have to the be the shortest it just needs to be possible, so can exit the pathfinding when a valid path is found that is <= max distance
- running into an issue, not sure how to tell the pathfinder which coordinates are being used
- IDEA: have a abstract pathfinder class, have a triangle pathfinder class, and square pathfinder class, inject the correct pathfinder.  pass in the board and the two coordinates to a static pathfinder function.  make the key function its own class with a static function createKey so that way the pathfinder can use it to create keys to get locations on the board.
- writing tests and adding attributes to all pieces, making sure information is stored correctly

## Nov. 28

- started implementing A*, using template method pattern.  Concerns about jump attributes, may need to find a way to mark a neighbor as valid only if linear from current node's parent.  

## Nov. 29

- finished basic A* algorithm.  used puesdocode from https://www.geeksforgeeks.org/a-search-algorithm/ for reference while making it.

- tests for paths w/ no attributes

- idea for jump & unblock - > change getNeighbor return to LocationNode and if tempList location is null, check 1 past in the same direction, if that spot is not null then make new LocationNode and set g to be parent + 2 instead of parent + 1

- store source and dest inside pathfinder somewhere

## Nov. 30

- jump works in square coordinates

- starting triangle and testing for triangle coordinates, triangle omni works and tested

- need to add exception test for fly (distance 3, two pieces inbetween), fly now working

- added linear movement for square

- commented out unblock code since it was removed from beta, placing in blog during cleanup before submission

- ran coverage test, still at 97%

- tomorrow is major cleanup, try to refactor if possible

## Dec. 1

- changed KeyCreator to protected since it is only used in the board package
- moved the distanceTo lambdas to their own class bc of SRP of the coordinate factory
- made distance calculator protected as well for the coordinates package

- Code related to unblock:

In Pathfinder.java:

```
//global variable
private boolean canUnblock;

//in isValidPath
		this.canUnblock = piece.canUnblock();

	/**
	 * @return true if piece can unblock
	 */
	protected boolean getCanUnblock() {
		return canUnblock;
	}
```
In SquarePathfind.java: (validate neighbor function)

```
				} else if (loc.getLocationType() == LocationType.BLOCK && getCanUnblock()) {
					int x = loc.getX() - locationX;
					int y = loc.getY() - locationY;
					x = x * 2;
					y = y * 2;
					Location temp = getValidPiece(board, locationX + x, locationY + y);
					if (temp != null) {
						neighbors.add(new LocationNode(temp, parent.getG() + 2,
								distanceTo.apply(temp, dest), parent.getLocation()));
					}
```

In TrianglePathfind.java: (validate neighbor function)

```
				} else if (loc.getLocationType() == LocationType.BLOCK && getCanUnblock()) {
					int x = loc.getX() - locationX;
					int y = loc.getY() - locationY;
					y = y * 2;
					int xModifier = 0;
					int locOrientation = Math.abs(loc.getX() + loc.getY()) % 2;
					if(locOrientation == 1) {
						xModifier = -1;
					} else {
						xModifier = 1;
					}
					if(x == 0) {
						Location temp = getValidPiece(board, loc.getX() + xModifier , loc.getY());
						if (temp != null) {
							neighbors.add(new LocationNode(temp, parent.getG() + 2,
									distanceTo.apply(temp, dest)));
						}
						temp = getValidPiece(board, loc.getX(), locationY + y);
						if (temp != null) {
							neighbors.add(new LocationNode(temp, parent.getG() + 2,
									distanceTo.apply(temp, dest)));
						}
					} else {
						Location temp = getValidPiece(board, loc.getX(), loc.getY() - 1);
						if (temp != null) {
							neighbors.add(new LocationNode(temp, parent.getG() + 2,
									distanceTo.apply(temp, dest)));
						}
						temp = getValidPiece(board, loc.getX(), loc.getY() + 1);
						if (temp != null) {
							neighbors.add(new LocationNode(temp, parent.getG() + 2,
									distanceTo.apply(temp, dest)));
						}
					}
```

# Final

### requirements

x - Game Rules implemented
x - you can't move to a place w/ a piece on it
x - SQUARE BOARDS
	x - LINEAR & FLY
	x - BLOCK Locations
o - TRIANGLE
	x - only omni mp
	o - linear bonus ( linear & fly )
x - games should handle jump correctly 
x - implement addObserver() and removeObserver()
	x - when a player wins the game -> "PLAYERx wins"
	x - if the moving player won, return true
	x - move attempted after the game is over, the message should say "Game is already won by PLAYERx"

## Dec. 4

- first change after reading release notes is that the destination of a move can not contain any pieces now, checking that to location is clear and not empty is now need, update older tests as needed
	NOTE: updated config files for the pathfinder so instead of moving to player piece, moved to empty location

- add back in the unblock function for square

- implement class for handling rules

## Dec. 6

- addObserver and removeObserver added... work out how notify observer will work.  Go thorough observer list and do observer.notify()

- notify same functions as EscapeException constructors.

- at any point i'm going to return false, throw new EscapeException with the string why

	- in validFromLocation
		throw when from location is not current players piece
	- in validToLoction
		- in isOpenLocation
			throw when location.getLocationType() == LocationType.BLOCK
			throw when location.getEscapePiece() != null
	- in pathfinder.isValidPath
		throw when DistanceTo > maxDistance
		throw when pathSize > maxDistance
		
- catch these EscapeExceptions is EscapeGameManager, notify observers with the message from the exception, then return false,
now board.move() can only return true otherwise will throw an exception do to some error which will get reported and move will not occur

- beginning of turn check if the game is already over, if so throw new exception with reason and move will not occur

- end of turn check if game has been won, if so report to observer without exception just string "PLAYERx wins"
	- after make sure to return true, so don't throw exception
	
- make RuleHandler class to deal with the rules and keeping track of scores, if someone has won, etc. check with that for score keeping.  maybe move isPlayerOneTurn into RuleHandler

- refactored program based on grade from Beta, looking into triangle issues once mastertests are released to see which cases were missed.

- refactor vars in Pathfind

- implemented TestGameObserver and throw EscapeExcpetions instead of false in board when invalid to/from locations are found / when the path is not found/too long, TLDR; throw exception instead of returing false in helper methods related to move() so move() only location returning false

- work in notify(String message, Throwable cause) and EscapeException(String message, Throwable cause)? Not quite sure how yet

- need to iron out rules, refactor ways of checking winners after moves and before moves 

## Dec. 7

- unblock understood incorrectly, used to think it acted like jump, realized it treats blocks like clear as long as it isnt final destination, refactored accordingly for square, triangle still not added back yet

## Dec. 8

- added linear mp for triangle coordinates, fly only supported

- added draw condition