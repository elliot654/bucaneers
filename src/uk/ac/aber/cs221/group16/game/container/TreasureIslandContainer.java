/*
* @(#) TreasureIslandContainer.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.container;

import uk.ac.aber.cs221.group16.game.items.cards.ChanceCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;

import java.util.*;

/**
 * A class to contain the data relating to a Treasure Island group of tiles.
 * Each tile should hold a reference to their container.
 *
 * @author Josh Smith
 * @version 1.0 Initial Design
 * @see uk.ac.aber.cs221.group16.game.map.tiles.TreasureIslandTile
 */
public class TreasureIslandContainer {

   // Instance variables
   private List<Treasure> treasures;
   private Queue<ChanceCard> chanceCards;

   // Constructors

   /**
    * This constructor initialises the Treasure and ChanceCard collections within the container as the parameters respectively.
    *
    * @param treasure    The ArrayList of treasures to be used.
    * @param chanceCards The Queue of ChanceCards to be used.
    */
   public TreasureIslandContainer(List<Treasure> treasure, Queue<ChanceCard> chanceCards) {
      this.treasures = treasure;
      this.chanceCards = chanceCards;
   }


   // Public Methods

   /**
    * Gets the Treasure Collection as an ArrayList.
    *
    * @return An ArrayList of Treasures.
    */
   public List<Treasure> getTreasures() {
      return treasures;
   }

   /**
    * Sets the ArrayList of treasures to be stored within the container.
    * <p>
    * DELETES ANY CURRENT TREASURES STORED.
    *
    * @param treasure The ArrayList of Treasure to be stored within the container.
    */
   public void setTreasure(ArrayList<Treasure> treasure) {
      this.treasures = treasure;
   }

   /**
    * Gets the Queue of ChanceCards stored within the container.
    *
    * @return The Queue of ChanceCards.
    */
   public Queue<ChanceCard> getChanceCards() {
      return chanceCards;
   }

   /**
    * Sets the Queue of ChanceCards to be stored within the container.
    * <p>
    * DELETES ANY CURRENT CARDS STORED.
    *
    * @param chanceCards The Queue of ChanceCard to be stored within the container.
    */
   public void setChanceCards(Queue<ChanceCard> chanceCards) {
      this.chanceCards = chanceCards;
   }

   /**
    * Takes a card from the front of the Queue and removes it from the Queue.
    *
    * @return The ChanceCard from the front of the Queue, or null if the Queue was empty.
    */
   public ChanceCard takeChanceCard() {
      // Queue.poll returns null if the Queue is empty.
      return this.chanceCards.poll();
   }

   /**
    * Adds the passed card to bottom of the deck.
    *
    * @param card The chance card to add to the deck.
    */
   public void putChanceCard(ChanceCard card) {
      chanceCards.add(card);
   }

   /**
    * Adds a treasure to the container.
    *
    * @param t The treasure to add.
    */
   public void addTreasure(Treasure t) {
      treasures.add(t);
   }

   /**
    * Gets treasures of a specific value from the TreasureIsland, or the closest obtainable value from the available
    * treasures.
    *
    * @param value The value to try and obtain in treasures.
    * @param shipRemCapacity The space available to fill with this treasure.
    * @param andRemoveThem whether or not to remove the treasures as well as return them.
    * @return The set of treasures of value or as close as possible to the desired value.
    */
   public Set<Treasure> getTreasuresOfValue(int value, int shipRemCapacity, boolean andRemoveThem) {
      Set<Treasure> obtained = new HashSet<>();
      treasures.sort(Comparator.reverseOrder());
      if (value == 6) {
         Treasure pleaseBe2 = treasures.remove(treasures.size() - 1);
         if (pleaseBe2.getValue() == 2) {
            obtained.add(pleaseBe2);
            value -= pleaseBe2.getValue();
            shipRemCapacity--;
         }
      }
      for (Treasure treasure : this.getTreasures()) {
         if (obtained.size() == 2) break;
         if (treasure.getValue() <= value && shipRemCapacity > 0) {
            obtained.add(treasure);
            value -= treasure.getValue();
            shipRemCapacity--;
         }
      }
      if (andRemoveThem) {
         for (Treasure treasure : obtained) {
            this.getTreasures().remove(treasure);
         }
      }
      return obtained;
   }
}
