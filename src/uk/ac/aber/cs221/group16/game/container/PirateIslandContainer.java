/*
* @(#) PirateIslandContainer.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.container;

import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;

import java.util.ArrayList;
import java.util.Queue;

/**
 * A class to contain the data relating to a Pirate Island group of tiles.
 * Each tile should hold a reference to this container.
 *
 * @author Josh Smith
 * @version 1.0 Initial Design
 * @see uk.ac.aber.cs221.group16.game.map.tiles.PirateIslandTile
 */
public class PirateIslandContainer {

   // Instance Variables
   private ArrayList<Treasure> treasure;
   private Queue<CrewCard> crewCards;

   // Constructors

   /**
    * This default constructor initialises the treasure collection.
    *
    * @param crewCards The crew cards to store in the container.
    */
   public PirateIslandContainer(Queue<CrewCard> crewCards) {
      this.treasure = new ArrayList<>();
      this.crewCards = crewCards;
   }


   // Public Methods

   /**
    * Gets the Treasure collection from the container.
    *
    * @return An ArrayList of Treasure.
    */
   public ArrayList<Treasure> getTreasure() {
      return treasure;
   }

   /**
    * Sets the Treasure collection to the passed ArrayList of Treasure.
    * <p>
    * DELETES ANY CURRENT TREASURES STORED.
    *
    * @param treasure The desired ArrayList of Treasure to be referenced by the container.
    */
   public void setTreasure(ArrayList<Treasure> treasure) {
      this.treasure = treasure;
   }

   /**
    * Gets the Crew cards stored here.
    *
    * @return The Queue of Crew cards.
    */
   public Queue<CrewCard> getCrewCards() {
      return crewCards;
   }

   /**
    * Sets the Crew cards stored here.
    * <p>
    * WILL DELETE ANY CREW CARDS ALREADY STORED HERE.
    *
    * @param crewCards The queue of crew cards to set.
    */
   public void setCrewCards(Queue<CrewCard> crewCards) {
      this.crewCards = crewCards;
   }

   /**
    * Adds a Treasure to the collection of Treasures within the container.
    *
    * @param treasure The Treasure to add.
    */
   public void addTreasure(Treasure treasure) {
      this.treasure.add(treasure);
   }

   /**
    * Takes a single CrewCard from the pack.
    *
    * @return A CrewCard from the top of the pack, or null if the pack is empty.
    */
   public CrewCard takeCrewCard() {
      return crewCards.poll();
   }

   /**
    * Adds a crew card to the Island container.
    *
    * @param c The card to add to the container.
    */
   public void addCrewCard(CrewCard c) {
      crewCards.add(c);
   }
}
