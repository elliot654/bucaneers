/*
* @(#) TreasureIslandTile.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

import uk.ac.aber.cs221.group16.game.GraphicLoader;
import uk.ac.aber.cs221.group16.game.container.TreasureIslandContainer;
import uk.ac.aber.cs221.group16.game.items.cards.ChanceCard;

/**
 * A Class for tiles which make up part of a TreasureIsland.
 *
 * @author Josh Smith
 * @version 1.0
 * @see IslandTile
 * @see Tile
 */
public class TreasureIslandTile extends IslandTile {

   // Instance Variables
   private TreasureIslandContainer container;

   // Constructors

   /**
    * This constructor instantiates the Tile with coordinates and a reference to the container passed in.
    *
    * @param x         The x coordinate of the Tile.
    * @param y         The y coordinate of the Tile.
    * @param container A reference to the container used by the Island.
    * @param px        The x offset into the island left to right.
    * @param py        The y offset into the island top to bottom.
    */
   public TreasureIslandTile(int x, int y, int px, int py, TreasureIslandContainer container) {
      super("TreasureIsland", x, y,
              GraphicLoader.getInstance().getTreasureIslandTiles()[px][py]);
      this.container = container;
   }

   // Public Methods

   /**
    * Takes a Chance Card and removes it from Island.
    *
    * @return The Chance Card drawn from the top of the cards. ( LIFO )
    */
   public ChanceCard drawChanceCard() {
      return this.container.takeChanceCard();
   }

   /**
    * Places the passed card at the bottom of the deck of chance cards.
    *
    * @param card The card to add to the deck.
    */
   public void placeChanceCardToBottom(ChanceCard card) {
      this.container.putChanceCard(card);
   }


}
