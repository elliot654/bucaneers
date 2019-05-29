/*
* @(#) FlatIslandContainer.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.container;

import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A class to contain the data relating to a Flat Island group of tiles.
 * Each tile should hold a reference to their container.
 *
 * @author Josh Smith
 * @version 1.0 Initial Design.
 * @see uk.ac.aber.cs221.group16.game.map.tiles.FlatIslandTile
 */
public class FlatIslandContainer {

   // Instance variables
   private ArrayList<Treasure> treasurePile;
   private Queue<CrewCard> crewCards;

   // Constructors

   /**
    * This default constructor initialises the container with empty collections of crewCards and treasures.
    */
   public FlatIslandContainer() {
      this.treasurePile = new ArrayList<>();
      this.crewCards = new LinkedList<>();
   }

   // Public methods.

   /**
    * Gets the treasure pile from the container.
    *
    * @return An ArrayList storing the treasure
    */
   public ArrayList<Treasure> getTreasurePile() {
      return treasurePile;
   }

   /**
    * Sets the treasure pile to the passed in ArrayList of Treasures
    * <p>
    * DELETES ANY CURRENT TREASURES STORED.
    *
    * @param treasurePile The new ArrayList of Treasure to be used.
    */
   public void setTreasurePile(ArrayList<Treasure> treasurePile) {
      this.treasurePile = treasurePile;
   }

   /**
    * Gets the crewCards stored within the container.
    *
    * @return A Queue of CrewCards which are stored within the container.
    */
   public Queue<CrewCard> getCrewCards() {
      return crewCards;
   }

   /**
    * Set the Queue of crewCards stored within the container.
    * <p>
    * DELETES ANY CURRENT CARDS STORED.
    *
    * @param crewCards The Queue of crewCards to replace the current crewCards.
    */
   public void setCrewCards(Queue<CrewCard> crewCards) {
      this.crewCards = crewCards;
   }

   /**
    * Adds a CrewCard to the back of the queue of crewCards.
    *
    * @param card The CrewCard to be added.
    */
   public void addCrewCard(CrewCard card) {
      this.crewCards.add(card);
   }

   /**
    * Takes all the cards from the container and returns them as an ArrayList.
    *
    * @return All the cards from the container as an ArrayList of CrewCard
    */
   public ArrayList<CrewCard> takeCards() {
      ArrayList<CrewCard> cards = new ArrayList<>(crewCards);
      crewCards.clear();
      return cards;
   }

   /**
    * Takes all the treasures from the container.
    *
    * @param n The number of treasures to take.
    * @return All of the Treasures from the container as an ArrayList of Treasure.
    */
   public ArrayList<Treasure> takeTreasure(int n) {
      ArrayList<Treasure> taken = new ArrayList<>();
      if (n > treasurePile.size()) n = treasurePile.size();
      if (n > 0) {
         Iterator<Treasure> treasures = treasurePile.stream().sorted(Treasure::valueComp).iterator();
         while (n-- > 0)
            taken.add(treasures.next());
         // remove all the taken treasures from the pile on the island.
         taken.forEach(treasurePile::remove);
      }
      return taken;
   }

   /**
    * Deposits a treasure into the container.
    *
    * @param t The treasure to deposit.
    */
   public void addTreasure(Treasure t) {
      treasurePile.add(t);
   }
}
