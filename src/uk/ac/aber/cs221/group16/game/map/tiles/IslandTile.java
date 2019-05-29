/*
* @(#) IslandTile.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

import javafx.scene.image.Image;

/**
 * A abstract class used for tiles which make up part of an island.
 *
 * @author Josh Smith
 * @version 1.0
 * @see Tile
 */
public abstract class IslandTile extends Tile {

   // Instance Variables
   private String name;

   // Constructors

   /**
    * This constructor sets the name and coordinates of the Tile.
    * Also sets the sailable attribute to false.
    *
    * @param name  The name of the Island.
    * @param x     The x coordinate of the Tile.
    * @param y     The y coordinate of the Tile.
    * @param image The image of this specific Island Tile
    */
   public IslandTile(String name, int x, int y, Image image) {
      super(x, y, image);
      this.name = name;
      this.sailable = false;
   }

   // Public Methods

   /**
    * Gets the name of the Island.
    *
    * @return A string representation of the Island's name.
    */
   public String getName() {
      return name;
   }
}
