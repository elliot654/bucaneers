/*
* @(#) Calculations.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.utils;

import uk.ac.aber.cs221.group16.game.map.Orientation;
import uk.ac.aber.cs221.group16.game.map.tiles.Tile;

/**
 * This class will be full of public static methods to help seperate out calculations from the rest of the code base.
 *
 * @author Josh Smith
 * @version 1.0
 */
public class Calculations {

   /**
    * Calculates the direction a ship would be in to travel between two tiles.
    *
    * @param from The tile sailing from.
    * @param to   The tile sailing to.
    * @return The orientation which the ship would sail in to reach a destination tile from a given source tile.
    * Returns null if angle doesn't match a valid game Orientation.
    */
   public static Orientation calculateDirection(Tile from, Tile to) {
      int deltaX = to.getX() - from.getX();
      int deltaY = to.getY() - from.getY();

      int angleDeg = (int) (Math.toDegrees(Math.atan2(deltaY, deltaX)));

      if (angleDeg < 0)
         angleDeg = 360 + angleDeg;
      return Orientation.fromAngle((angleDeg + 90) % 360); // need to rotate calculation 90 degrees due to ship image facing east
   }
}
