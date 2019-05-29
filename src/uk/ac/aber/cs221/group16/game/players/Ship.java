/*
* @(#) Ship.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.players;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.map.Orientation;
import uk.ac.aber.cs221.group16.game.map.tiles.PortTile;
import uk.ac.aber.cs221.group16.game.map.tiles.Tile;

import java.util.Set;

/**
 * This Class manages the basic operations of the ship e.g. adding and removing Treasures. It also stores the orientation and current
 * position of the ship.
 *
 * @author Josh Smith
 * @version 1.2 Move and rotate features Beta.
 */
public class Ship {

   // Instance Variables
   private Tile curTile;
   private int capacity;
   private ObservableSet<Treasure> cargo;
   private Orientation orientation;
   private Pane shipPane;
   private StringProperty locationString = new SimpleStringProperty();
   private StringProperty treasureString = new SimpleStringProperty();

   // Constructors

   /**
    * This constructor initializes a ship with coordinates passed in as arguments. This will also add the ship to the
    * tiles pane.
    *
    * @param t     The tile the Ship will start on.
    * @param board The board the ship is on.
    * @param image The image to use for the ship.
    */
   public Ship(Tile t, Board board, Image image) {
      capacity = 2;
      shipPane = new Pane();
      shipPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
              BackgroundPosition.CENTER, new BackgroundSize(128, 128, false, false, true, false))));
      this.curTile = t;
      cargo = FXCollections.observableSet();
      // listen for changes and update the string
      cargo.addListener((SetChangeListener<Treasure>) c -> updateTreasureString());
      t.putShip(this);
      shipPane.setPrefWidth(Control.USE_COMPUTED_SIZE);
      shipPane.setPrefHeight(Control.USE_COMPUTED_SIZE);
      updateLocationString(board);
      updateTreasureString();

      // initialise the rotation of the ship in its home port.
      portRotate();
   }

   // Public Methods

   /**
    * Adds the passed Treasure into the collection of Treasures kept by the Ship.
    *
    * @param t The Treasure to add.
    * @return Returns true if the treasure was successfully added. False if the ship's cargo hold was full..
    */
   public boolean addTreasure(Treasure t) {
      if (cargo.size() < capacity)
         cargo.add(t);
      else
         return false;
      return true;
   }

   /**
    * Sets the current tile of the Ship. Removing the ship from its current tile and placing it onto the new one.
    * Also updates the location string.
    *
    * @param board The board that the ship is on.
    * @param t     The tile to place the ship at.
    */
   public void moveTo(Tile t, Board board) {
      if (curTile != null) // if the curTile is not null, remove the ship from the previous tile first.
         curTile.removeShip(this);
      curTile = t;
      t.putShip(this);
      updateLocationString(board);
   }

   /**
    * Gets the Tile which the Ship is currently placed on.
    *
    * @return The tile the ship currently is placed on.
    */
   public Tile getTile() {
      return curTile;
   }

   /**
    * Gets the collection of Treasure kept by the Ship.
    *
    * @return The collection of Treasures which the ship holds.
    */
   public Set<Treasure> getCargo() {
      return cargo;
   }

   /**
    * Gets the capcity of the ship.
    *
    * @return The maximum number of Treasures this ship can hold.
    */
   public int getCapacity() {
      return capacity;
   }

   /**
    * Gets the Orientation of the Ship.
    *
    * @return The Orientation of the Ship.
    */
   public Orientation getOrientation() {
      return orientation;
   }

   /**
    * Sets the Orientation of the Ship.
    *
    * @param orientation The new Orientation for the ship to rotate towards.
    */
   public void setOrientation(Orientation orientation) {
      this.orientation = orientation;
      shipPane.setRotate(orientation.getAngle());
      if (orientation.getAngle() > 180)
         shipPane.setStyle("-fx-scale-x: -1;");
      else
         shipPane.setStyle("-fx-scale-x: 1;");
   }

   /**
    * Gets the X coordinate for the ship.
    *
    * @return The ship's current X coordinate.
    */
   public int getX() {
      return curTile.getX();
   }

   /**
    * Gets the Y coordinate for the ship.
    *
    * @return The ship's current Y coordinate.
    */
   public int getY() {
      return curTile.getY();
   }

   /**
    * Gets the pane which displays the ship.
    *
    * @return The pane which has the ship image on it.
    */
   public Pane getShipPane() {
      return shipPane;
   }

   /**
    * Gets the locationStringProperty and follows the javabeans style guide on using properties.
    *
    * @return A StringProperty object representing the location dispay string.
    */
   public StringProperty locationStringProperty() {
      return locationString;
   }

   /**
    * Gets the treasureStringProperty and follows the javabeans style guide on using properties.
    *
    * @return A StringProperty object representing the treasure display string.
    */
   public StringProperty treasureStringProperty() {
      return treasureString;
   }

// Private Methods

   /**
    * This function updates the output string for the ship position. On the grid where the bottom left is 1, 1.
    *
    * @param b The board the ship is on.
    */
   private void updateLocationString(Board b) {
      locationString.set(String.format("\t( x:%d, y:%d )", b.getOutputX(getX()), b.getOutputY(getY())));
   }

   /**
    * Updates the treasure string property to trigger an update on the UI.
    */
   private void updateTreasureString() {
      this.treasureStringProperty().set(Treasure.setToString(cargo));
   }

   public void portRotate() {
      // set ship orientation

      if (curTile instanceof PortTile) {
         switch (((PortTile) curTile).getName()) {
            case "Venice": // Purposeful fallthrough as same orientation.
            case "London":
               this.setOrientation(Orientation.E);
               break;
            case "Genoa":
               this.setOrientation(Orientation.N);
               break;
            case "Cadiz":
               this.setOrientation(Orientation.S);
               break;
            case "Amsterdam": // Purposeful fallthrough as same orientation.
            case "Marseilles":
               this.setOrientation(Orientation.W);
               break;
         }
      }
   }

   public int getRemainingCapacity() {
      return capacity - cargo.size();
   }
}
