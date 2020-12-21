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

package escape.rules;

import escape.players.Player;
import escape.util.RuleDescriptor;

/**
 * Class that handles the rules
 * 
 * @version Dec 4, 2020
 */
public class RuleHandler
{
	private RuleDescriptor score;
	private RuleDescriptor turns;
	private int numP1Pieces;
	private int numP2Pieces;
	private int P1Score;
	private int P2Score;

	/**
	 * Initialize the rule handler with the configurator's rules
	 * 
	 * @param rules
	 */
	public RuleHandler(RuleDescriptor... rules)
	{
		initializeRules(rules);
	}

	private void initializeRules(RuleDescriptor[] rules)
	{
		for (RuleDescriptor rule : rules) {
			switch (rule.id) {
				case SCORE:
					this.score = rule;
					break;
				case TURN_LIMIT:
					this.turns = rule;
					break;
				default:
					break;
			}
		}
	}

	/**
	 * @return score limit
	 */
	public int getScoreLimit()
	{
		if (score.value != 0) {
			return score.value;
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * @return turn limit
	 */
	public int getTurnLimit()
	{
		if (turns.value != 0) {
			return turns.value;
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * adds a piece to the number of p1 pieces
	 */
	public void addP1Piece()
	{
		numP1Pieces++;
	}

	/**
	 * adds a piece to the number of p2 pieces
	 */
	public void addP2Piece()
	{
		numP2Pieces++;
	}

	/**
	 * removes a piece to the number of p1 pieces
	 */
	public void removeP1Piece()
	{
		numP1Pieces--;
	}

	/**
	 * removes a piece to the number of p2 pieces
	 */
	public void removeP2Piece()
	{
		numP2Pieces--;
	}

	/**
	 * @return return number of p1 pieces
	 */
	public int getNumP1Pieces()
	{
		return numP1Pieces;
	}

	/**
	 * @return return number of p2 pieces
	 */
	public int getNumP2Pieces()
	{
		return numP2Pieces;
	}

	/**
	 * add P1 Score
	 */
	public void addP1Score(int value)
	{
		P1Score = P1Score + value;
	}

	/**
	 * add p2 score
	 */
	public void addP2Score(int value)
	{
		P2Score = P2Score + value;
	}

	/**
	 * @return return p1 score
	 */
	public int getP1Score()
	{
		return P1Score;
	}

	/**
	 * @return return p2 score
	 */
	public int getP2Score()
	{
		return P2Score;
	}

}
