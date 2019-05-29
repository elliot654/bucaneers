/*
* @(#) Treasure.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.items.treasures;

import javafx.scene.image.Image;
import uk.ac.aber.cs221.group16.game.items.Tradable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This interface represents a Treasure object with the relevant Value and Type of the Treasure.
 *
 * @author Josh Smith
 * @version 1.0 Initial Design.
 */
public interface Treasure extends Tradable {


   // Static Methods

   /**
    * Comparator function to sort from lowest to highest value.
    *
    * @param t1 Treasure 1
    * @param t2 Treasure 2
    * @return 1 if the first treasure is lower, otherwise -1
    */
   static int valueComp(Treasure t1, Treasure t2) {
      return t1.getValue() < t2.getValue() ? 1 : -1;

   }

   /**
    * Takes a Set of treasures and returns a formatted multiline string to summarise the contents of the set.
    *
    * @param tSet The set of treasures.
    * @return A multiline formatted string representing the contents of the treasure set.
    */
   static String setToString(Set<Treasure> tSet) {
      if (tSet.size() == 0) {
         return "Empty!";
      } else {
         StringBuilder stringBuilder = new StringBuilder();
         Map<String, Integer> numOfEach = new HashMap<>();
         Map<String, Integer> totalValueOfEach = new HashMap<>();
         for (Treasure treasure : tSet) {
            String type = treasure.getType();

            numOfEach.put(type, numOfEach.getOrDefault(type, 0) + 1);
            totalValueOfEach.put(type, totalValueOfEach.getOrDefault(type, 0) + treasure.getValue());
         }
         for (Map.Entry<String, Integer> entry : numOfEach.entrySet()) {
            stringBuilder.append(entry.getValue())
                    .append(" ").append(entry.getKey())
                    .append(": value of ").append(totalValueOfEach.get(entry.getKey()))
                    .append("\n");
         }

         return stringBuilder.toString();
      }
   }

   // Public Methods

   /**
    * Gets the Image of the Treasure.
    *
    * @return The Image representing this type of Treasure.
    */
   Image getImage();

   /**
    * Gets the value of the treasure.
    *
    * @return The value of the treasure.
    */
   int getValue();

   /**
    * Gets the type of the treasure e.g. Diamond.
    *
    * @return The string representing the Type of the treasure.
    */
   String getType();
}
