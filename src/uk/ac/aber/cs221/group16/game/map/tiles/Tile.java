/*
* @(#) Tile.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import uk.ac.aber.cs221.group16.game.GraphicLoader;
import uk.ac.aber.cs221.group16.game.players.Ship;

import java.util.ArrayList;

/**
 * An Abstract Class to be extended by all Tile classes.
 *
 * @author Josh Smith
 * @version 1.1 JavaFX First Implementation.
 */
public abstract class Tile {

   //Instance Variables

   protected int x, y;
   protected boolean sailable;
   protected Image image;
   private StackPane tilePane;
   private boolean highlighted;
   private ArrayList<Ship> occupiedBy;

   // Constructors

   /**
    * A constructor which takes board coordinates x and y for the Tile. This also takes a file path to the image to display
    *
    * @param x     The x coordinate on the board.
    * @param y     The y coordinate on the board.
    * @param image The image to use for the tile.
    */
   public Tile(int x, int y, Image image) {
      this.x = x;
      this.y = y;
      tilePane = new StackPane();
      tilePane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
              BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
              new BackgroundSize(100, 100, true, true, false, true))));
      occupiedBy = new ArrayList<>();
   }

   // Public Methods

   /**
    * Gets the x coordinate of the Tile.
    *
    * @return The x coordinate of the Tile.
    */
   public int getX() {
      return x;
   }

   /**
    * Gets the y coordinate of the Tile.
    *
    * @return The y coordinate of the Tile.
    */
   public int getY() {
      return y;
   }

   /**
    * Checks whether or not the Tile is valid to sail over.
    *
    * @return A boolean value representing whether or not the Tile is valid to sail over.
    */
   public boolean isSailable() {
      return sailable;
   }

   /**
    * Gets the pane which displays the tile.
    *
    * @return The StackPane used to display the Tile.
    */
   public StackPane getTilePane() {
      return tilePane;
   }

   /**
    * Places a ship onto the Tile.
    *
    * @param s The ship to add to the Tile.
    */
   public void putShip(Ship s) {
      occupiedBy.add(s);
      tilePane.getChildren().add(s.getShipPane());
   }

   /**
    * Removes the ship from the Tile.
    *
    * @param s The ship to remove from the Tile.
    */
   public void removeShip(Ship s) {
      occupiedBy.remove(s);
      tilePane.getChildren().remove(s.getShipPane());
   }

   /**
    * Initializes the Tile to work with the UI, adding the tilePane to the boardPane passed in at the coordinates known
    * by the tile.
    *
    * @param boardPane The gridpane to add the tile to.
    */
   public final void pairToUI(GridPane boardPane) {
      boardPane.add(this.tilePane, this.x + 1, this.y + 1);
   }

   /**
    * Checks whether the tile is occupied by a Ship.
    *
    * @return True if the Tile has a ship drawn onto it's pane, false otherwise.
    */
   public boolean isOccupied() {
      return occupiedBy.size() > 0;
   }

   // Protected Methods

   /**
    * Highlights the Tile
    * Sets the appropriate cursor for highlighted tile
    */
   public void highlight() {
      Pane redShift = new Pane();

      // sets a slight tint over the tile
      redShift.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");
      tilePane.getChildren().add(0, redShift);
      highlighted = true;
      // change the cursor
      redShift.setCursor(new ImageCursor(
              GraphicLoader.getInstance().getFocusCursor()
      ));
   }

   /**
    * If the tile is highlighted, unhighlights it.
    * Sets the appropriate cursor for unhighlighted tile
    */
   public void unhighlight() {
      if (highlighted) {
         tilePane.getChildren().remove(0);
         highlighted = false;
         // reset the cursor back to default when unhighliting
         tilePane.setCursor(new ImageCursor(
                 GraphicLoader.getInstance().getDefaultCursor()
         ));
      }
   }


}
