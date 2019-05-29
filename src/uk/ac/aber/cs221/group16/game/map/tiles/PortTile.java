/*
* @(#) PortTile.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import uk.ac.aber.cs221.group16.game.items.Tradable;
import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.players.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * A class to be used to represent Port tiles, player or otherwise.
 *
 * @author Josh Smith
 * @version 1.0 Initial Design
 * @see Tile
 */
public class PortTile extends Tile {

   // Instance Variables

   private Port port;
   private Player owner;
   private ObservableSet<Treasure> treasureSet;
   private Set<CrewCard> crewCardSet;
   private StringProperty treasureString = new SimpleStringProperty();

   // Constructors

   /**
    * This constructor instantiates the PortTile as a specific port with the coordinates passed as arguments.
    *
    * @param port The enum value which this port is meant to be.
    * @param x    The x coordinate of the Tile.
    * @param y    The y coordinate of the Tile.
    */
   public PortTile(Port port, int x, int y) {
      super(x, y, port.getImage());
      this.port = port;
      this.sailable = true;
      crewCardSet = new HashSet<>();
      treasureSet = FXCollections.observableSet();
      treasureSet.addListener((SetChangeListener<Treasure>) change -> updateTreasureString());

      updateTreasureString();
   }


   // Public Methods

   /**
    * This method gets the string representation of the enum value of the port.
    *
    * @return A string representing the name of the port.
    * @see Port
    */
   public String getName() {
      return port.toString();
   }

   /**
    * Adds a treasure to the treasure set in the port.
    *
    * @param t The treasure to add.
    */
   public void addTreasure(Treasure t) {
      treasureSet.add(t);
   }

   /**
    * Adds a crew card to the set of crew cards in the port.
    *
    * @param c The crew card to add
    */
   public void addCrewCard(CrewCard c) {
      if (owner == null)
         crewCardSet.add(c);
      else
         owner.addCrewCard(c);
   }

   /**
    * Gets the set of treasures from the port.
    *
    * @return The set of Treasures in the port.
    */
   public Set<Treasure> getTreasureSet() {
      return treasureSet;
   }

   /**
    * Gets the player who's home port this is.
    *
    * @return The player who calls this port home, or null if non player owned port.
    */
   public Player getOwner() {
      return owner;
   }

   /**
    * Sets the owner of the port.
    *
    * @param owner The player who has this port as a home port.
    */
   public void setOwner(Player owner) {
      this.owner = owner;
   }

   /**
    * Gets the set of crew cards from the port.
    *
    * @return The set of crew cards in the port.
    */
   public Set<CrewCard> getCrewCardSet() {
      return crewCardSet;
   }

   public StringProperty treasureStringProperty() {
      return treasureString;
   }

   /**
    * Gets the total value of the treasure in the port.
    *
    * @return The sum of the value of all treasures within the port.
    */
   public int getTotalValue() {
      int sum = 0;
      for (Treasure treasure : treasureSet) {
         sum += treasure.getValue();
      }

      return sum;
   }

   /**
    * Adds a tradable item into the port.
    *
    * @param item The tradable item to be added into the port.
    */
   public void addTradable(Tradable item) {
      if (item instanceof Treasure)
         addTreasure((Treasure) item);
      else if (item instanceof CrewCard)
         addCrewCard((CrewCard) item);
   }

   /**
    * Removes a tradable item from the port.
    *
    * @param item The tradable item to be removed from the port.
    */
   public void removeTradable(Tradable item) {
      if (item instanceof Treasure)
         treasureSet.remove(item);
      else if (item instanceof CrewCard)
         crewCardSet.remove(item);

   }

   // Private Methods

   /**
    * Updates the treasure string property to trigger an update on the UI.
    */
   private void updateTreasureString() {
      this.treasureStringProperty().set(Treasure.setToString(treasureSet));
   }
}
