package uk.ac.aber.cs221.group16.game.items;

/**
 * This interface ensures all classes which implement it have the necessary functions for a trade object.
 *
 * @author Josh Smith
 * @version 1.0
 */
public interface Tradable extends Comparable<Tradable> {

   /**
    * Gets the type of the tradeable object.
    *
    * @return The String representation of the tradeable object.
    */
   String getType();

   /**
    * Gets the value of this tradeable object.
    *
    * @return A value for the object.
    */
   int getValue();

   @Override
   default int compareTo(Tradable o) {
      return Integer.compare(this.getValue(), o.getValue());
   }
}
