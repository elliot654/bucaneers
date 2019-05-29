/*
* @(#) FlatIslandTile.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

import uk.ac.aber.cs221.group16.game.GraphicLoader;
import uk.ac.aber.cs221.group16.game.container.FlatIslandContainer;
import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;

import java.util.ArrayList;


/**
 * A Class for tiles which make up part of a FlatIsland.
 *
 * @author Josh Smith
 * @version 1.0
 * @see IslandTile
 * @see Tile
 */
public class FlatIslandTile extends IslandTile {

   // Instance Variables
   private FlatIslandContainer container;

   // Constructors

   /**
    * This constructor sets the coordinates of the Tile and sets the reference to the container.
    *
    * @param x         The x coordinate of the Tile.
    * @param y         The y coordinate of the Tile.
    * @param px        The x offset into the island left to right.
    * @param py        The y offset into the island top to bottom.
    * @param container The container for the Tile to reference.
    */
   public FlatIslandTile(int x, int y, int px, int py, FlatIslandContainer container) {
      super("FlatIsland", x, y,
              GraphicLoader.getInstance().getFlatIslandTiles()[px][py]);
      this.container = container;
   }

   // Public Methods

   /**
    * Gets all the cards from the Island and removes them from the Island.
    *
    * @return The Crew cards as an ArrayList of CrewCards
    */
   public ArrayList<CrewCard> takeCrewCards() {
      return this.container.takeCards();
   }

   /**
    * Gets all the Treasures from the Island and removes them from the Island.
    *
    * @param n The number of treasures to take.
    * @return All of the Treasures stored at the Island.
    */
   public ArrayList<Treasure> takeTreasure(int n) {
      return this.container.takeTreasure(n);
   }


}
