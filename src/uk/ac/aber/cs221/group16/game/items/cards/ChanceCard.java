/*
* @(#) ChanceCard.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.items.cards;

import uk.ac.aber.cs221.group16.game.items.Tradable;
import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.players.Player;

/**
 * A class for ChanceCards, they have a string of text to be shown to users and an action which will perform
 * the necessary changes to the board or ship.
 *
 * @author Josh Smith
 * @author Luke Revill
 * @version 1.0 Initial design.
 * @see Card
 * @see CardAction
 */
public class ChanceCard
        implements Card, Tradable {

   // Instance variables
   private String text;
   private int cardID;
   private CardAction action;
   private CardCondition condition;
   private int value;
   // Constructors

   /**
    * This constructor takes the text and CardAction which can be passed as an anonymous class implementation.
    *
    * @param id         The card ID.
    * @param text       The text to be displayed on the chance card
    * @param cardAction The action to take place when the card is activated.
    */
   public ChanceCard(int id, String text, CardAction cardAction) {
      this.cardID = id;
      this.text = text;
      this.action = cardAction;
   }

   /**
    * This constructor is for use with cards which have conditional requirements
    * prior to running the CardAction.
    *
    * @param id         The card ID.
    * @param text       The text to be displayed on the chance card
    * @param cardAction The action to take place when the card is activated.
    * @param cond       The condition which must be true for the action to be performed.
    */
   public ChanceCard(int id, String text, CardAction cardAction,
                     CardCondition cond) {
      this.cardID = id;
      this.text = text;
      this.action = cardAction;
      condition = cond;
   }

   /**
    * This constructor is for use with cards which can be traded.
    *
    * @param id    The card ID.
    * @param text  The text on the card.
    * @param value The value of the card when trading.
    */
   public ChanceCard(int id, String text, int value) {
      this.cardID = id;
      this.text = text;
      this.value = value;
   }

   // public methods

   /**
    * Gets the text displayed on the card.
    *
    * @return Text which explains what the action on the card does.
    */
   public String getText() {
      return text;
   }

   /**
    * Performs the action specified by the card on the ship and the board.
    *
    * @param p The player to perform the action on.
    * @param b The board which the ship is on.
    */

   /**
    * Gets the ID that corresponds to each chance card
    *
    * @return cardID, the id Number of each card
    */
   public int getCardID() {
      return cardID;
   }


   public void performAction(Player p, Board b) {
      if (action != null) action.perform(p, b);
   }

   /**
    * If the Chance card has a condition to it's use, this will be checked
    * against the player who holds the card.
    *
    * @param p The player who holds the card.
    * @param b The board the player is playing on.
    * @return The outcome of the conditional check, true if the player is valid
    * to use the card.
    */
   public boolean performCheck(Player p, Board b) {
      if (condition != null) {
         return condition.check(p, b);
      } else if (value > 0) {
         return false;
      } else
         return true;
   }

   @Override
   public String toString() {
      return getType() + ": " +
              this.getText() + "\n\n";
   }

   @Override
   public String getType() {
      return "Chance Card " + cardID;
   }

   @Override
   public int getValue() {
      return value;
   }
}
