/*
* @(#) SeaTile.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

import uk.ac.aber.cs221.group16.game.GraphicLoader;

/**
 * A class to be used to represent Sea tiles.
 *
 * @author Josh Smith
 * @version 1.0 Initial Design
 * @see Tile
 */
public class SeaTile extends Tile {

   // Instance Variables

   // Constructors

   /**
    * This constructor instantiates the Tile with coordinates as specified by the arguments. Also sets sailable to True.
    *
    * @param x The x coordinate of the Tile.
    * @param y The y coordinate of the Tile.
    */
   public SeaTile(int x, int y) {
      super(x, y, GraphicLoader.getInstance().getSeaTile());
      this.sailable = true;
   }

   // Public Methods
}
