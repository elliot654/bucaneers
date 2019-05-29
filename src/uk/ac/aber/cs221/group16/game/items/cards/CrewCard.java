/*
* @(#) CrewCard.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.items.cards;

import javafx.scene.paint.Color;
import uk.ac.aber.cs221.group16.game.items.Tradable;

/**
 * A class for Crew cards which contains the color of the card.
 *
 * @author Josh Smith
 * @version 1.0 Initial design.
 * @see Card
 */
public class CrewCard
        implements Card, Tradable {

   // Instance variables

   /**
    * This is a JavaFX Color type as we may want to expand to other colours later on.
    */
   private Color color;
   private int value;

   // Constructors

   /**
    * This contructor initialises the CrewCard with a set value and color.
    *
    * @param color The color of the Card
    * @param value The value of the Card.
    */
   public CrewCard(Color color, int value) {
      this.color = color;
      this.value = value;
   }

   // Public Methods

   /**
    * Gets the color of the Crew Card.
    *
    * @return A JavaFX Color object representing the color of the card.
    */
   public Color getColor() {
      return color;
   }

   /**
    * Gets the value of the card.
    *
    * @return The value of the Card.
    */
   public int getValue() {
      return value;
   }

   /**
    * Gets a string representing the type (colour) of the card.
    *
    * @return Black Crew Card or Red Crew Card, UNKNOWN if something goes wrong.
    */
   public String getType() {
      if (color == Color.BLACK)
         return "Black Crew Card";
      else if (color == Color.RED)
         return "Red Crew Card";
      else return "UNKNOWN";
   }

   @Override
   public String toString() {
      return String.format("%-30s", "A " + this.getType() + " of value: ") +
              this.getValue();
   }
}
