/*
* @(#) CardAction.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.items.cards;

import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.players.Player;

/**
 * An interface to be implemented by anonymous classes for the actions of each ChanceCard.
 *
 * @author Josh Smith
 * @version 1.0 Proof of concept
 */
@FunctionalInterface
public interface CardAction {
   /**
    * This function should perform a set of changes to the ship and board to represent the action of the Chance Card.
    *
    * @param p The Player in question.
    * @param b The board the ship is on.
    * @see ChanceCard
    */
   void perform(Player p, Board b);
}
