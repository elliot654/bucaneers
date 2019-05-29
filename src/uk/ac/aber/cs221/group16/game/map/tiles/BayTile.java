/*
* @(#) BayTile.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

/**
 * A class to be used to represent Bay tiles.
 *
 * @author Josh Smith
 * @version 1.0 Initial Design
 * @see Tile
 */
public class BayTile extends Tile {

   // Instance Variables

   private Bay bay;

   // Constructors

   /**
    * This constructor instantiates the PortTile as a specific bay with the coordinates passed as arguments.
    *
    * @param bay The enum value which this bay is meant to be.
    * @param x   The x coordinate of the Tile.
    * @param y   The y coordinate of the Tile.
    */
   public BayTile(Bay bay, int x, int y) {
      super(x, y, bay.getImage());
      this.bay = bay;
      this.sailable = true;
   }


   // Public Methods

   /**
    * This method gets the string representation of the enum value of the bay.
    *
    * @return A string representing the name of the bay.
    * @see Bay
    */
   public String getName() {
      return bay.toString();
   }
}
