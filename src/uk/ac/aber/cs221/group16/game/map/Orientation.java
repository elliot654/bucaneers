/*
* @(#) Orientation.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map;

import javafx.util.Pair;
import uk.ac.aber.cs221.group16.game.map.tiles.Tile;
import uk.ac.aber.cs221.group16.game.utils.MoveAssistance;

/**
 * This Enum is for representing the direction of ships within the game.
 *
 * @author Josh Smith
 * @version 1.2 Move and rotate features Beta.
 */
public enum Orientation {
   N, NE, E, SE,
   S, SW, W, NW;

   /**
    * Directions ordered to match the results of
    * {@link MoveAssistance#getSurroundingTiles(int, int, Tile[][])} (int, int, Tile[][])}.
    */
   private static final Orientation[] directions = {Orientation.NW, Orientation.N, Orientation.NE, Orientation.W,
           Orientation.E, Orientation.SW, Orientation.S,
           Orientation.SE};

   // Static Methods

   /**
    * Gets the orientation which matches an angle in degrees.
    *
    * @param deg The angle in degrees to find the orientation for.
    * @return The Orientation which represents the angle or null if doesn't match.
    */
   public static Orientation fromAngle(int deg) {
      switch (deg) {
         case 0:
            return Orientation.N;
         case 45:
            return Orientation.NE;
         case 90:
            return Orientation.E;
         case 135:
            return Orientation.SE;
         case 180:
            return Orientation.S;
         case 225:
            return Orientation.SW;
         case 270:
            return Orientation.W;
         case 315:
            return Orientation.NW;
         default:
            return null;
      }
   }

   /**
    * Gets all of the possible Orientations in an array.
    *
    * @return An Array of getAll possible Orientations.
    */
   public static Orientation[] getAll() {
      return directions;
   }

   // Public Methods

   /**
    * Gets the angle related to the enum value.
    *
    * @return A number betweeen 0 or 360, -1 if something really goes wrong.
    */
   public int getAngle() {
      if (this.equals(Orientation.N)) {
         return 0;
      } else if (this.equals(Orientation.NE)) {
         return 45;
      } else if (this.equals(Orientation.E)) {
         return 90;
      } else if (this.equals(Orientation.SE)) {
         return 135;
      } else if (this.equals(Orientation.S)) {
         return 180;
      } else if (this.equals(Orientation.SW)) {
         return 225;
      } else if (this.equals(Orientation.W)) {
         return 270;
      } else if (this.equals(Orientation.NW)) {
         return 315;
      } else return -1;
   }

   /**
    * Gets the modifier for specific Orientations, e.g the delta for a NE move is (1, -1).
    *
    * @return The delta modifier for the Orientation. e.g. NE is 1, -1
    */
   public Pair<Integer, Integer> getDelta() {
      if (this.equals(Orientation.N)) {
         return new Pair<>(0, -1);
      } else if (this.equals(Orientation.NE)) {
         return new Pair<>(1, -1);
      } else if (this.equals(Orientation.E)) {
         return new Pair<>(1, 0);
      } else if (this.equals(Orientation.SE)) {
         return new Pair<>(1, 1);
      } else if (this.equals(Orientation.S)) {
         return new Pair<>(0, 1);
      } else if (this.equals(Orientation.SW)) {
         return new Pair<>(-1, 1);
      } else if (this.equals(Orientation.W)) {
         return new Pair<>(-1, 0);
      } else if (this.equals(Orientation.NW)) {
         return new Pair<>(-1, -1);
      } else return new Pair<>(0, 0);
   }
}
