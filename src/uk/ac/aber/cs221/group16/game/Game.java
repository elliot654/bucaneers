/*
* @(#) Game.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uk.ac.aber.cs221.group16.game.items.cards.CardFactory;
import uk.ac.aber.cs221.group16.game.items.cards.ChanceCard;
import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.TreasureFactory;
import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.map.tiles.PortTile;
import uk.ac.aber.cs221.group16.game.players.Player;
import uk.ac.aber.cs221.group16.game.states.GameState;
import uk.ac.aber.cs221.group16.game.states.State;
import uk.ac.aber.cs221.group16.game.windows.Choice;
import uk.ac.aber.cs221.group16.game.windows.GenericMsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

/**
 * This class oversees the game and its functionality in its entirety.
 *
 * @author Josh Smith
 * @author Alex Toop
 * @author Kamyab Sherafat
 * @author Luke Revill
 * @version 1.3 Random player assignment Alpha.
 */
public class Game {

   // Constants
   static final int numPlayers = 4;
   private static final int gridSize = 20;

   // Instance Variables

   private Board board;
   private Player[] players;
   private Stage primaryStage;
   private Scene gameScene;
   private SimpleBooleanProperty gameOver;
   private GameState state;

   // Constructors

   /**
    * This default constructor initialises the game including setting up the Players, Board, CardPacks and Treasures.
    *
    * @param names        The list of names of players.
    * @param primaryStage The primary stage for the JavaFX application.
    */
   public Game(ArrayList<String> names, Stage primaryStage) {
      this.primaryStage = primaryStage;
      initGame(names);
   }
   // Public Methods

   /**
    * Sets the primaryStage's scene to the gameScene, if the gameScene is not initialized {@link #initScene()} will be called.
    */
   public void start() {
      initScene();
      gameScene.setCursor(new ImageCursor(GraphicLoader.getInstance().getDefaultCursor()));
      primaryStage.setScene(gameScene);
      board.setCurrentPlayer(players[0]);
      primaryStage.show();
      primaryStage.requestFocus();
   }

   /**
    * Increments thr turnNumber in the state and returns the next player.
    *
    * @return The new current player after changing turn.
    */
   public Player nextPlayer() {
      state.incrementTurnNumber();
      Player newPlayer = players[state.getTurnNumber() % numPlayers];
      new GenericMsg(Controller.stage, "End of Turn!\n" +
              "It is now " + newPlayer.getName() + "'s turn.");
      return newPlayer;
   }

   /**
    * Changes the State of the game.
    *
    * @param newState The new state, e.g State.Move or State.Rotate
    */
   public void changeState(State newState) {
      this.state.setCurrentState(newState);
   }

   /**
    * Gets the state of the game.
    *
    * @return The State value currently held in the GameState.
    */
   public State getState() {
      return this.state.getCurrentState();
   }

   /**
    * Gets the players.
    *
    * @return The array of players.
    */
   public Player[] getPlayers() {
      return players;
   }

   /**
    * Gets the board
    *
    * @return board
    */
   public Board getBoard() {
      return board;
   }

   /**
    * Gets the primary stage which the Game runs on.
    *
    * @return The Stage which the game is running on.
    */
   public Stage getPrimaryStage() {
      return primaryStage;
   }


   /**
    * Handles ending the game and informing the players of who won. Also asks whether they want to play another game
    * or exit.
    *
    * @param winner The winner of the game.
    */
   public void endGameQuery(Player winner) {
      boolean playAgain;
      playAgain = new Choice(Controller.stage, winner.getName() + " Wins! They have obtained "
              + winner.getHomePort().getTotalValue() + " treasure in " +
              "their home port!\nWould you like to play again or quit?", new String[]{"Play Again", "Quit"}).getAnswer();

      ArrayList<String> playerNames = new ArrayList<>();
      for (Player p : players) {
         playerNames.add(p.getName());
      }
      if (playAgain) {
         initGame(playerNames);
         start();
      } else {
         primaryStage.close();
      }
   }

   /**
    * Triggers the end of the game.
    */
   public void triggerGameOver() {
      this.gameOver.set(true);
   }


   // Private Methods

   /**
    * Initializes the {@link #gameScene} for the game. Linking all of the necessary parts such as players and the board
    * to the UI.
    * <p>
    * // TODO: Possibly tidy up and break down this function.
    */
   private void initScene() {
      BorderPane uiRootNode = new BorderPane();
      uiRootNode.getStylesheets().add("css/border_pane_style.css");
      gameScene = new Scene(uiRootNode, 800, 600);

      // Get Side Panels
      VBox left = makeSidePane();
      VBox right = makeSidePane();

      // Add and set up side panels.
      uiRootNode.setLeft(left);
      uiRootNode.setRight(right);

      for (int i = 0; i < 4; i++) {
         this.players[i].pairToUI((i < 2) ? (VBox) left.getChildren().get((i % 2 == 0) ? 0 : 1) :
                 (VBox) right.getChildren().get((i % 2 == 0) ? 1 : 0), primaryStage, board);
      }

      GridPane center = new GridPane();
      center.getStylesheets().add("/css/center_style.css");
      //center.setGridLinesVisible(true); for debugging
      uiRootNode.setCenter(center);
      // Add constraints of width and height to each node in the grid.
      for (int i = 0; i < gridSize + 2; i++) {
         ColumnConstraints colCs = new ColumnConstraints();
         colCs.setPercentWidth(4.54545454545);
         center.getColumnConstraints().add(colCs);

         RowConstraints rowCs = new RowConstraints();
         rowCs.setPercentHeight(4.54545454545);
         center.getRowConstraints().add(rowCs);
      }

      labelBoard(center);

      // Add and set up board.
      board.pairToUI(center);
   }

   /**
    * Makes and returns the side pane containing two gridpanes which occupy the top and bottom half of the VBox.
    *
    * @return The VBox containing two grid panes, top at children index 0 and bottom at children index 1
    */
   private VBox makeSidePane() {
      VBox sidePane = new VBox();
      for (int i = 0; i < 2; i++) {
         VBox vbox = new VBox();
         vbox.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.5));
         sidePane.getChildren().add(vbox);
      }
      sidePane.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.2));
      return sidePane;
   }

   /**
    * Takes the center grid pane and places labels along the border to represent the axis. 1..gridSize
    *
    * @param gridPane The Center grid pane to label the axis.
    */
   private void labelBoard(GridPane gridPane) {
      for (Integer i = 1; i <= gridSize; i++) {
         // Create labels, For some reason we can't just use one label multiple times.
         Label label1 = new Label(i.toString());
         Label label2 = new Label(i.toString());

         // set max size of grid pane labels
         for (int j = 0; j < 2; j++) {
            ((j > 0) ? label2 : label1).maxHeightProperty().bind(
                    gridPane.heightProperty().multiply(4.54545454545));
            ((j > 0) ? label2 : label1).maxWidthProperty().bind(
                    gridPane.widthProperty().multiply(4.54545454545));
         }

         /*// Align labels
         for (int j = 0; j < 2; j++) {
            GridPane.setHalignment((j < 1) ? label1 : label2, HPos.CENTER);
            GridPane.setValignment((j < 1) ? label1 : label2, VPos.CENTER);
         }*/

         // Add labels
         gridPane.add(label1, i, gridSize + 1); // bottom
         gridPane.add(label2, 0, gridSize - i + 1); // left
      }
   }

   /**
    * This function will take a list of player names and instantiate a player
    * for each of them with a unique color and home port. The array of
    * instantiated players will then be returned in a shuffled order.
    *
    * @param names  The list of names.
    * @param ports  The array of home ports
    * @param colors The array of colors
    * @param board  The board which the players will play on.
    * @return A shuffled array of players in respect to the names that went in. * Ports/Colors of respective Players will still be in order.
    */
   private Player[] makePlayers(ArrayList<String> names, PortTile[] ports, Color[] colors, Board board) {
      Collections.shuffle(names);
      Player[] players = new Player[numPlayers];
      for (int i = 0; i < ports.length; i++) {
         players[i] = new Player(i, names.get(i), ports[i], colors[i], board);
      }
      return players;
   }

   /**
    * Initialises the game, can be ran again during the game to restart the game.
    *
    * @param names Names of the players.
    */
   private void initGame(ArrayList<String> names) {
      gameOver = new SimpleBooleanProperty(false);
      gameOver.addListener((observable, oldValue, newValue) -> {
         if (newValue) {
            endGameQuery(getPlayers()[state.getTurnNumber() % numPlayers]);
         }
      });

      CardFactory cf = new CardFactory(6); // 6 of each card
      TreasureFactory tf = new TreasureFactory(4); // 4 of each treasure

      Queue<CrewCard> crewCardPack = cf.getShuffledCrewCards();
      Queue<ChanceCard> chanceCardPack = cf.getShuffledChanceCards();
      // TODO: Initialize treasures.
      board = new Board(tf.genTreasureList(), crewCardPack, chanceCardPack, this);
      PortTile[] ports = board.getHomePorts();
      state = new GameState();
      Color[] colors = {Color.RED, Color.YELLOW, Color.LIME, Color.ORANGE};

      // Shuffles names and gives players different home ports
      players = makePlayers(names, ports, colors, board);

      for (int i = 0; i < 5; i++) { // 5 cards for each player.
         for (Player player : players) {
            player.addCrewCard(crewCardPack.poll());
         }
      }
   }
}
