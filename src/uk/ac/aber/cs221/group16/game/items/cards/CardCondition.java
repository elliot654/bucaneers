/*
* @(#) CardCondition.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.items.cards;

import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.players.Player;

/**
 * This functional interface can be used by anonymous classes to define a
 * condition in which the CardAction can be used.
 *
 * @author Josh Smith
 * @version 1.0
 * @see CardAction
 */
@FunctionalInterface
public interface CardCondition {

   /**
    * This function should perform a check to see if the Player is currently
    * valid to use the chance card.
    *
    * @param p The player in question.
    * @param b The board the player is playing on.
    * @return True if the player is in a valid state to use the Chance Card.
    * Otherwise, False.
    * @see ChanceCard
    */
   boolean check(Player p, Board b);
}
