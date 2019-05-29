/*
* @(#) Player.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.players;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uk.ac.aber.cs221.group16.game.Controller;
import uk.ac.aber.cs221.group16.game.GraphicLoader;
import uk.ac.aber.cs221.group16.game.items.Tradable;
import uk.ac.aber.cs221.group16.game.items.cards.ChanceCard;
import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.map.tiles.PortTile;
import uk.ac.aber.cs221.group16.game.windows.GenericMsg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class manages Players and functions relating to player hands, while storing necessary information regarding the player,
 * such as homePort tile, player name and color.
 *
 * @author Luke Revill
 * @author Josh Smith
 * @author Kamyab Sherafat
 * @author Alex Toop
 * @version 1.1 JavaFX First Implementation.
 */
public class Player {

   // Static Variables
   private static final Color defaultPaneCol = Color.SILVER;

   // Instance Variables
   private String name;
   private Color color;
   private Ship ship;
   private PortTile homePort;
   private Pane highlightPane, chanceCard, crewCard;
   private ArrayList<CrewCard> crew;
   private ArrayList<ChanceCard> chanceCards;

   // Constructors

   /**
    * This constructor creates a players object with the name, color and homeport specified by arguments, and also
    * initializes a Ship at the home port.
    *
    * @param id       The ID of the player.
    * @param name     The name of the player.
    * @param homePort The Home port of the player.
    * @param col      The color to use for the player.
    * @param board    The board the player is playing on.
    */
   public Player(int id, String name, PortTile homePort, Color col, Board board) {
      this.name = name;
      this.color = col;
      this.homePort = homePort;
      homePort.setOwner(this);
      ship = new Ship(homePort, board, GraphicLoader.getInstance().getShips()[id]);
      crew = new ArrayList<>();
      chanceCards = new ArrayList<>();

      // for debugging
//      ship.addTreasure(new DiamondTreasure());
//      ship.addTreasure(new PearlTreasure());
//      ship.addTreasure(new RumTreasure());
//      ship.addTreasure(new RubyTreasure());
//
//      homePort.addTreasure(new PearlTreasure());
//      homePort.addTreasure(new RubyTreasure());
//      homePort.addTreasure(new RumTreasure());
//      homePort.addTreasure(new DiamondTreasure());
//      homePort.addTreasure(new GoldTreasure());
   }

   // Public Methods

   /**
    * Gets the Ship associated with this player.
    *
    * @return A reference to the players Ship object.
    */
   public Ship getShip() {
      return ship;
   }

   /**
    * Displays the Hand ??
    */
   public void displayHand() {
      // code to display hand somewhere
   }


   /**
    * Calculates and returns the players fighting strength.
    *
    * @return The fighting strength of the players.
    */
   public int getFightingStrength() {
      int redTotal = 0;
      int blackTotal = 0;
      for (CrewCard card : crew) {
         if (card.getColor() == Color.RED) redTotal += card.getValue();
         if (card.getColor() == Color.BLACK) blackTotal += card.getValue();
      }
      return Math.abs(redTotal - blackTotal);
   }

   /**
    * Gets the maximum movement distance for the players Ship.
    *
    * @return The Ship's maximum movement distance in number of tiles from the current tile.
    */
   public int getMovementDistance() {
      int moveDist = 0;
      for (CrewCard card : crew) {
         moveDist += card.getValue();
      }
      return moveDist > 0 ? moveDist : 1;
   }

   /**
    * Gets the name of the player.
    *
    * @return The name.
    */
   public String getName() {
      return name;
   }

   /**
    * Gets the Home port of the player.
    *
    * @return The home port of the ship.
    */
   public PortTile getHomePort() {
      return homePort;
   }


   /**
    * Draws the players's information to the screen e.g. Ship, UI details...
    *
    * @param pane         The pane to place the Player info onto.
    * @param primaryStage The primary stage used.
    * @param board        The board which the game is being played on.
    */
   public void pairToUI(VBox pane, Stage primaryStage, Board board) {
      highlightPane = pane;

      Pane headerPane = new GridPane();
      headerPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
      headerPane.prefHeightProperty().bind(pane.heightProperty().multiply(0.15));
      Label playerName = new Label(getName());
      Label playerLoc = new Label();
      playerLoc.textProperty().bind(ship.locationStringProperty());
      Label playerPrt = new Label(getHomePort().getName());
      headerPane.getChildren().addAll(playerName, playerLoc, playerPrt);
      GridPane.setConstraints(playerName, 0, 0);
      GridPane.setConstraints(playerLoc, 1, 0);
      GridPane.setConstraints(playerPrt, 0, 1);

      HBox playerCards = new HBox();
      playerCards.prefHeightProperty().bind(pane.heightProperty().multiply(0.35));

      GridPane leftWrapper = new GridPane();
      leftWrapper.prefWidthProperty().bind(playerCards.widthProperty().multiply(0.5));
      crewCard = new Pane();
      crewCard.setCursor(new ImageCursor(GraphicLoader.getInstance().getBrokenCursor()));
      crewCard.prefHeightProperty().bind(leftWrapper.heightProperty().multiply(0.75));
      crewCard.prefWidthProperty().bind(leftWrapper.widthProperty().multiply(0.50));
      ImageView crewCardImg = new ImageView(GraphicLoader.getInstance().getCrewCardStack());
      crewCardImg.fitWidthProperty().bind(crewCard.widthProperty());
      crewCardImg.fitHeightProperty().bind(crewCard.heightProperty());
      crewCard.getChildren().add(crewCardImg);
      // Clicks crew card stack
      crewCard.setOnMouseClicked(e -> {
         // ensure its the current player's crew card stack
         if (board.getCurrentPlayer() == this) {
            StringBuilder sBuilder = new StringBuilder();
            crew.stream().sorted(Comparator.reverseOrder())
                    .forEach(c -> sBuilder.append(c.toString()).append("\n"));
            sBuilder.append("Move: ").append(getMovementDistance()).append(" | Attack: ").append(getFightingStrength());
            new GenericMsg(Controller.stage, sBuilder.toString());
         }
      });
      leftWrapper.getChildren().add(crewCard);
      leftWrapper.setId("left-wrapper");

      GridPane rightWrapper = new GridPane();
      rightWrapper.prefWidthProperty().bind(playerCards.widthProperty().multiply(0.5));
      chanceCard = new Pane();
      chanceCard.setCursor(new ImageCursor(GraphicLoader.getInstance().getBrokenCursor()));
      chanceCard.prefHeightProperty().bind(rightWrapper.heightProperty().multiply(0.75));
      chanceCard.prefWidthProperty().bind(rightWrapper.widthProperty().multiply(0.5));
      ImageView chanceCardImg = new ImageView(GraphicLoader.getInstance().getChanceCardStack());
      chanceCardImg.fitWidthProperty().bind(chanceCard.widthProperty());
      chanceCardImg.fitHeightProperty().bind(chanceCard.heightProperty());
      chanceCard.getChildren().add(chanceCardImg);
      // Clicks crew card stack
      chanceCard.setOnMouseClicked(e -> {
         // ensure its the current player's crew card stack
         StringBuilder sBuilder = new StringBuilder();
         if (board.getCurrentPlayer() == this) {
            if (chanceCards.size() > 0)
               chanceCards.forEach(c -> sBuilder.append(c.toString()).append("\n"));
            else
               sBuilder.append("No Chance Cards in hand!");
            new GenericMsg(Controller.stage, sBuilder.toString());
         }
      });
      rightWrapper.getChildren().add(chanceCard);
      rightWrapper.setId("right-wrapper");

      playerCards.getChildren().addAll(leftWrapper, rightWrapper);
      playerCards.getStylesheets().add("/css/player_cards.css");

      HBox treasureTxt = new HBox();
      treasureTxt.setAlignment(Pos.CENTER);
      treasureTxt.prefHeightProperty().bind(pane.heightProperty().multiply(0.1));
      Label treasureTtlLabel = new Label("Treasure");
      treasureTxt.getChildren().add(treasureTtlLabel);

      GridPane treasureHmPrt = new GridPane();

      Label homePort = new Label("Home Port:");
      Label homePortVal = new Label();
      homePortVal.textProperty().bind(this.homePort.treasureStringProperty());
      treasureHmPrt.getChildren().addAll(homePort, homePortVal);
      GridPane.setConstraints(homePort, 0, 0);
      GridPane.setConstraints(homePortVal, 0, 1);
      ScrollPane scrollPaneUp = new ScrollPane(treasureHmPrt);
      scrollPaneUp.prefHeightProperty().bind(pane.heightProperty().multiply(0.2));

      GridPane treasureShip = new GridPane();
      Label ship = new Label("Ship:");
      Label shipVal = new Label();
      shipVal.textProperty().bind(this.ship.treasureStringProperty());
      shipVal.textProperty().bind(this.ship.treasureStringProperty());
      treasureShip.getChildren().addAll(ship, shipVal);
      GridPane.setConstraints(ship, 0, 0);
      GridPane.setConstraints(shipVal, 0, 1);
      ScrollPane scrollPaneDwn = new ScrollPane(treasureShip);
      scrollPaneDwn.prefHeightProperty().bind(pane.heightProperty().multiply(0.2));

      pane.getChildren().addAll(headerPane, playerCards, treasureTxt, scrollPaneUp, scrollPaneDwn);
      headerPane.getStyleClass().add("headerPane");
      treasureHmPrt.getStyleClass().add("treasureHmPrt");
      treasureShip.getStyleClass().add("treasureShip");
      scrollPaneDwn.getStyleClass().add("scrollPaneDwn");
      scrollPaneUp.getStyleClass().add("scrollPaneUp");
      treasureTxt.getStyleClass().add("treasureTxt");
      playerCards.getStyleClass().add("playerCards");
      pane.getStyleClass().add("playerPane");
      pane.getStylesheets().add("/css/player_pane.css");
   }

   /**
    * UnHighlights a Player's info window.
    * Sets the appropriate cursor for player cards.
    */
   public void unhighlight() {
      highlightPane.setStyle("-fx-background-color: silver;");
      for (int i = 0; i < 2; i++) {
         ((i > 0) ? crewCard : chanceCard).setCursor(
                 new ImageCursor(GraphicLoader.getInstance().getBrokenCursor()));
      }
   }

   /**
    * Highlight's a Player's info window.
    * Sets the appropriate cursor for player cards.
    */
   public void highlight() {
      highlightPane.setStyle("-fx-background-color: white;");
      for (int i = 0; i < 2; i++) {
         ((i > 0) ? crewCard : chanceCard).setCursor(
                 new ImageCursor(GraphicLoader.getInstance().getFocusCursor()));
      }
   }

   /**
    * Adds a crew card to the players hand.
    *
    * @param card The card to add to the players hand.
    */
   public void addCrewCard(CrewCard card) {
      crew.add(card);
   }

   /**
    * Sums the number of crew cards in the players hand.
    *
    * @return The number of crew cards in the players hand.
    */
   public int numCrewCards() {
      return crew.size();
   }

   /**
    * Adds a chance card to the players hand.
    *
    * @param card The chance card to add to the players hand.
    */
   public void addChanceCard(ChanceCard card) {
      chanceCards.add(card);
   }

   /**
    * Makes the player attack another player, returns the loser or null if there is a draw.
    *
    * @param defender The defender.
    * @return The loser of the battle is returned, null if a draw.
    */
   public Player attack(Player defender) {

      if (this.getFightingStrength() > defender.getFightingStrength()) // attacker wins
         return defender;
      else if (this.getFightingStrength() < defender.getFightingStrength())  // defender wins
         return this;
      else  // draw
         return null;
   }

   /**
    * Gets the crew cards in the player's hand.
    *
    * @return A list of crew cards from the players hand.
    */
   public List<CrewCard> getCrewCards() {
      return crew;
   }

   /**
    * Removes a tradable item from the player depending on what type of tradable item it is.
    *
    * @param item A tradable item to remove from the player's possession.
    */
   public void removeTradable(Tradable item) {
      if (item instanceof Treasure)
         ship.getCargo().remove(item);
      else if (item instanceof CrewCard)
         crew.remove(item);
      else if (item instanceof ChanceCard)
         chanceCards.remove(item);
   }

   /**
    * Adds a tradable item into the players possession.
    *
    * @param item A tradable item to add to the players possession.
    */
   public void addTradable(Tradable item) {
      if (item instanceof Treasure)
         ship.getCargo().add((Treasure) item);
      else if (item instanceof CrewCard)
         crew.add((CrewCard) item);
   }

   /**
    * Sets the Crew Cards in players hand to a set hand(Used in Testing)
    *
    * @param newCards The new list of Crew cards to set.
    */
   public void setCrewCards(ArrayList<CrewCard> newCards) {
      this.crew = newCards;
   }

   /**
    * Gets the Chance Cards in the players hand.
    *
    * @return The Chance cards held by the player as an ArrayList.
    */
   public ArrayList<ChanceCard> getChanceCards() {
      return chanceCards;
   }
}
