/*
* @(#) PirateIslandTile.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

import uk.ac.aber.cs221.group16.game.GraphicLoader;
import uk.ac.aber.cs221.group16.game.container.PirateIslandContainer;
import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;

/**
 * A Class for tiles which make up part of a Pirate Island.
 *
 * @author Josh Smith
 * @version 1.0
 * @see IslandTile
 * @see Tile
 */
public class PirateIslandTile extends IslandTile {

   // Instance Variables
   private PirateIslandContainer container;

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
   public PirateIslandTile(int x, int y, int px, int py, PirateIslandContainer container) {
      super("PirateIsland", x, y,
              GraphicLoader.getInstance().getPirateIslandTiles()[px][py]);
      this.container = container;
   }

   // Public Methods

   /**
    * Draws a single CrewCard from the Island's pack.
    *
    * @return A CrewCard from the pack, or null if the pack was empty.
    */
   public CrewCard drawCrewCard() {
      return this.container.takeCrewCard();
   }
}
