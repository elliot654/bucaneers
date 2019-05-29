/*
* @(#) Board.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;
import uk.ac.aber.cs221.group16.game.Battle;
import uk.ac.aber.cs221.group16.game.Controller;
import uk.ac.aber.cs221.group16.game.Game;
import uk.ac.aber.cs221.group16.game.GraphicLoader;
import uk.ac.aber.cs221.group16.game.container.FlatIslandContainer;
import uk.ac.aber.cs221.group16.game.container.PirateIslandContainer;
import uk.ac.aber.cs221.group16.game.container.TreasureIslandContainer;
import uk.ac.aber.cs221.group16.game.items.cards.ChanceCard;
import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.map.tiles.*;
import uk.ac.aber.cs221.group16.game.players.Player;
import uk.ac.aber.cs221.group16.game.states.State;
import uk.ac.aber.cs221.group16.game.utils.Calculations;
import uk.ac.aber.cs221.group16.game.utils.MoveAssistance;
import uk.ac.aber.cs221.group16.game.windows.Choice;
import uk.ac.aber.cs221.group16.game.windows.GenericMsg;
import uk.ac.aber.cs221.group16.game.windows.TradeWindow;

import java.util.*;

/**
 * This class is used for managing the board and all ports, islands.
 * <p>
 *
 * @author Josh Smith
 * @version 1.2 Move and rotate features Beta.
 */
public class Board {

   // Constants
   public static final int gridWidth = 20;
   public static final int gridHeight = 20;
   private static final int treasureValueToWin = 20;


   // Instance Variables
   private Tile[][] grid;

   private Player currentPlayer;

   private ArrayList<StackPane> rotationPanes;

   private Game gameReference;
   private TreasureIslandContainer treasureIslandContainer;
   private FlatIslandContainer flatIslandContainer;
   private PirateIslandContainer pirateIslandContainer;

   private PortTile cadizTile;
   private PortTile londonTile;
   private PortTile veniceTile;
   private PortTile amsterdamTile;
   private PortTile genoaTile;
   private PortTile marseillesTile;

   private BayTile cliffCreekTile;
   private BayTile anchorBayTile;
   private BayTile mudBayTile;


   // Constructors

   /**
    * This constructor creates a Board with starting treasures, crew cards and chance cards to distribute across Islands.
    *
    * @param treasures   Treasures to place onto the board.
    * @param crewCards   Crew cards to place onto the board.
    * @param chanceCards Chance cards to place onto the board.
    * @param g           The game instance.
    */
   public Board(List<Treasure> treasures, Queue<CrewCard> crewCards,
                Queue<ChanceCard> chanceCards, Game g) {
      grid = newMap("res/config/CSVBUCCMAP.csv", treasures, crewCards, chanceCards);
      gameReference = g;
      rotationPanes = new ArrayList<>();
      initRotatePanes();
   }


   // Public Methods


   /**
    * Translates an x coordinate for the grid into the appropriate coordinate based on the brief. e.g. bottom left is (1,1)
    *
    * @param x The y coordinate to translate
    * @return An x coordinate appropriate for display to the user.
    */
   public int getOutputX(int x) {
      return x + 1;
   }

   /**
    * Translates a y coordinate for the grid into the appropriate coordinate based on the brief. e.g. bottom left is (1,1)
    *
    * @param y The y coordinate to translate
    * @return A y coordinate appropriate for display to the user.
    */
   public int getOutputY(int y) {
      return gridHeight - y;
   }

   /**
    * Sets the current player.
    *
    * @param newPlayer The player to set as the current player
    */
   public void setCurrentPlayer(Player newPlayer) {
      if (this.currentPlayer != null) this.currentPlayer.unhighlight();
      this.currentPlayer = newPlayer;
      this.currentPlayer.highlight();

      // highlight possible moves for each player
      MoveAssistance.getValidMoves(newPlayer, gameReference.getState(), grid).forEach(Tile::highlight);
   }

   /**
    * Gets the references to 4 home ports on the board.
    *
    * @return An array of PortTile objects representing the tiles which are valid home ports.
    */
   public PortTile[] getHomePorts() {
      PortTile[] homePorts = new PortTile[4];
      for (Tile[] row : grid) {
         for (Tile t : row) {
            if (t instanceof PortTile) {
               PortTile pt = (PortTile) t;
               switch (pt.getName()) {
                  case "London":
                     homePorts[0] = pt;
                     break;
                  case "Genoa":
                     homePorts[1] = pt;
                     break;
                  case "Marseilles":
                     homePorts[2] = pt;
                     break;
                  case "Cadiz":
                     homePorts[3] = pt;
                     break;
               }
            }
         }
      }
      return homePorts;
   }


   /**
    * Gets an adjacent TreasureIslandTile from the given coordinates, if none are within 1 square return null
    *
    * @param x The x coordinate.
    * @param y The y coordinate.
    * @return The adjacent island tile or null if none are within 1 square of the coordinates passed.
    */
   public TreasureIslandTile nextToTreasureIsland(int x, int y) {
      TreasureIslandTile found = null;
      ArrayList<IslandTile> islandTiles = MoveAssistance.getSurroundingIslandTiles(x, y, grid);
      for (IslandTile islandTile : islandTiles) {
         if (islandTile instanceof TreasureIslandTile) {
            found = (TreasureIslandTile) islandTile;
            break;
         }
      }
      return found;
   }

   /**
    * Initializes the board to work with the UI including pairing each tile and adding the onclick action.
    *
    * @param boardPane The GridPane used for the grid.
    */
   public void pairToUI(GridPane boardPane) {
      for (int x = 0; x < 20; x++) {
         for (int y = 0; y < 20; y++) {
            final Tile t = grid[x][y];
            grid[x][y].pairToUI(boardPane);
            grid[x][y].getTilePane().setOnMouseClicked((e) -> {
               if (gameReference.getState() == State.Move || gameReference.getState() == State.PostCombatMove) {
                  handleClickMove(t);
               }
            });
         }
      }
      /* for debugging
      for (Tile[] tiles : grid) {
         for (Tile tile : tiles) {
            tile.getTilePane().setOpacity(1);
         }
      }*/
   }

   /**
    * Gets the current player.
    *
    * @return The current Player.
    */
   public Player getCurrentPlayer() {
      return currentPlayer;
   }

   /**
    * Gets the grid from the Board.
    *
    * @return The two dimensional Array of tiles.
    */
   public Tile[][] getGrid() {
      return grid;
   }

   /**
    * Gets the players who are participating in the game.
    *
    * @return The array of players playing the game.
    */
   public Player[] getPlayers() {
      return gameReference.getPlayers();
   }

   /**
    * Gets a reference to the container used for treasure island.
    *
    * @return The container used to store things on Treasure island.
    */
   public TreasureIslandContainer getTreasureIslandContainer() {
      return treasureIslandContainer;
   }

   /**
    * Gets a reference to the container used for Flat island.
    *
    * @return The container used to store things on Flat island.
    */
   public FlatIslandContainer getFlatIslandContainer() {
      return flatIslandContainer;
   }

   /**
    * Gets a reference to the container used for Pirate island.
    *
    * @return The container used to store things on Pirate island.
    */
   public PirateIslandContainer getPirateIslandContainer() {
      return pirateIslandContainer;
   }

   /**
    * Gets the port tile for Cadiz
    *
    * @return Cadiz port tile
    */
   public PortTile getCadizTile() {
      return cadizTile;
   }

   /**
    * Gets the port tile for London
    *
    * @return London port tile
    */
   public PortTile getLondonTile() {
      return londonTile;
   }

   /**
    * Gets the port tile for Venice
    *
    * @return Venice port tile
    */
   public PortTile getVeniceTile() {
      return veniceTile;
   }

   /**
    * Gets the port tile for Amsterdam
    *
    * @return Amsterdam port tile
    */
   public PortTile getAmsterdamTile() {
      return amsterdamTile;
   }

   /**
    * Gets the port tile for Genoa
    *
    * @return Genoa port tile
    */
   public PortTile getGenoaTile() {
      return genoaTile;
   }

   /**
    * Gets the port tile for Marseilles
    *
    * @return Marseilles port tile
    */
   public PortTile getMarseillesTile() {
      return marseillesTile;
   }

   /**
    * Gets the tile for CliffCreek
    *
    * @return The CliffCreek tile
    */
   public BayTile getCliffCreekTile() {
      return cliffCreekTile;
   }

   /**
    * Gets the tile for AnchorBay
    *
    * @return The AnchorBay tile
    */
   public BayTile getAnchorBayTile() {
      return anchorBayTile;
   }

   /**
    * Gets the tile for MudBay
    *
    * @return The MudBay tilev
    */
   public BayTile getMudBayTile() {
      return mudBayTile;
   }


   // Private Methods

   /**
    * Handles a click of a certain tile and what should happen, only call if the player is game is in a state of movement.
    *
    * @param to The tile clicked on.
    */
   private void handleClickMove(Tile to) {
      if (isValidMove(currentPlayer, to.getX(), to.getY())) {
         movePath(currentPlayer, to);
         if (to == currentPlayer.getShip().getTile()) {
            gameReference.changeState(State.Rotate);
            handleRotation();
         }
      }
   }

   /**
    * This function handles all aspects of a combat engagement, from checking whether an engagement has happened, to assigning a winner/loser.
    * Also sets the {@link #currentPlayer} variable to the loser or defender if a attack actually happens.
    *
    * @param t The tile you want to check and engage a attack at.
    * @return True if a attack happens, false otherwise.
    */
   private boolean handlePossibleCombat(Tile t) {
      boolean fightOccurred = false;
      for (Player player : gameReference.getPlayers()) {
         if (player != currentPlayer) {
            if (player.getShip().getTile() == t) {
               if (nextToTreasureIsland(t.getX(), t.getY()) == null && !(t instanceof PortTile)) { // if not next to treasure island.
                  fightOccurred = true;
                  Player defender = player;
                  Battle battle = new Battle(currentPlayer, defender, this);
                  gameReference.changeState(State.PostCombatMove);
                  battle.engage();
                  break; // ONE FIGHT PLS
               }
            }
         }
      }
      return fightOccurred;
   }

   /**
    * Places the rotate buttons around the currentPlayer's ship and adds the trigger function for click events.
    */
   private void handleRotation() {
      final ArrayList<Tile> tiles = MoveAssistance.getSurroundingTiles(currentPlayer.getShip().getTile().getX(),
              currentPlayer.getShip().getTile().getY(), grid);

      for (int i = 0; i < tiles.size(); i++) {
         if (tiles.get(i) != null && tiles.get(i).isSailable() && gameReference.getState() == State.Rotate) {
            final Orientation o = Orientation.getAll()[i];
            rotationPanes.get(i).setOnMouseClicked(e -> {
               // when a rotation button is clicked, first clear all the rotation buttons from the screen.
               clearRotationButtons(tiles);
               currentPlayer.getShip().setOrientation(o);
               endTurn();
               // This line is necessary to prevent the parent gridpane consuming the click event and moving the ship.
               e.consume();
            });
            tiles.get(i).getTilePane().getChildren().add(rotationPanes.get(i));
         }
      }
   }

   /**
    * Handles the island interaction, aimed to be used at the end of the turn to check for island tiles in surrounding areas
    * and act on anything that should happen when you come into contact e.g. treasure island pick up a chance card and use it.
    */
   private void handleIslandInteraction() {
      ArrayList<IslandTile> islandTiles = MoveAssistance.getSurroundingIslandTiles(currentPlayer.getShip().getX(),
              currentPlayer.getShip().getY(), grid);
      if (islandTiles.size() > 0) {
         // get the first island in the surrounding tiles and checks for type
         if (islandTiles.get(0) instanceof TreasureIslandTile) {
            // if its treasure island, pick up a chance card and do the action.
            ChanceCard drawnCard = ((TreasureIslandTile) islandTiles.get(0)).drawChanceCard();
            if (drawnCard != null) {
               new GenericMsg(gameReference.getPrimaryStage(), "You drew a Chance Card (id: " + drawnCard.getCardID() + ")\n\n" + drawnCard.getText());
               if (drawnCard.performCheck(currentPlayer, this)) {
                  // uses the card if it can be used now.
                  drawnCard.performAction(currentPlayer, this);
                  // put the card back to the bottom of the deck.
                  treasureIslandContainer.putChanceCard(drawnCard);
               } else { // add the card to the player for later use
                  currentPlayer.addChanceCard(drawnCard);
               }
            }
         } else if (islandTiles.get(0) instanceof FlatIslandTile) {
            FlatIslandTile fIsland = (FlatIslandTile) islandTiles.get(0);
            StringBuilder sBuilder = new StringBuilder("You have taken the following cards from Flat Island:\n");
            // take all crew cards
            int cardsTaken = 0;
            for (CrewCard c : fIsland.takeCrewCards()) {
               currentPlayer.addCrewCard(c);
               sBuilder.append(c.toString()).append("\n");
               cardsTaken++;
            }
            if (cardsTaken > 0)
               new GenericMsg(Controller.stage, sBuilder.toString());
            sBuilder = new StringBuilder("You have taken the following Treasures from Flat Island:\n");
            // take treasures as many treasures as you can store.
            int treasuresTaken = 0;
            int numToTake = currentPlayer.getShip().getCapacity() - currentPlayer.getShip().getCargo().size();
            if (numToTake > 0) {
               for (Treasure treasure : fIsland.takeTreasure(numToTake)) {
                  currentPlayer.getShip().addTreasure(treasure);
                  sBuilder.append(treasure.getType()).append("\n");
                  treasuresTaken++;
               }
               if (treasuresTaken > 0)
                  new GenericMsg(Controller.stage, sBuilder.toString());
            }
         }
      }
   }

   /**
    * Begins a path to a destination tile. Unhighlights all valid moves, State is changed to {@link State#InMovement}.
    *
    * @param p    The player to move.
    * @param dest The tile which you intend to move to.
    */
   private void movePath(Player p, Tile dest) {
      Tile cur = p.getShip().getTile();
      if (cur != dest)
         p.getShip().setOrientation(Calculations.calculateDirection(cur, dest));
      MoveAssistance.getValidMoves(p, gameReference.getState(), grid)
              .forEach(Tile::unhighlight);

      // save state so it can be reverted to after movement.
      State startState = gameReference.getState();
      gameReference.changeState(State.InMovement);

      // start animation loop
      partialMove(p, dest, startState);
   }

   /**
    * Moves the player's ship to the next tile in the current direction and recursively calls itself if it is not at
    * the destination. The cycle can also be broken if an enemy ship is intercepted and the player chooses to engage.
    * <p>
    * The game state is returned to the returnState once the destination is reached.
    * <p>
    * {@link #handleEndOfPath(Player)} is called once the end of the path is reached.
    *
    * @param p           The player who is moving.
    * @param dest        The tile which is the intended destination.
    * @param returnState The state which the game should return to once the destination is reached.
    */
   private void partialMove(Player p, Tile dest, State returnState) {
      final int delay = 200;
      new Timeline(new KeyFrame(Duration.millis(delay), event -> {
         int dx = p.getShip().getOrientation().getDelta().getKey();
         int dy = p.getShip().getOrientation().getDelta().getValue();
         Tile cur = p.getShip().getTile();

         // don't bother moving if you're dest is the current location.
         if (cur != dest) {
            Tile next = grid[p.getShip().getX() + dx][p.getShip().getY() + dy];
            boolean sailable = MoveAssistance.isSailableCoordinate(next.getX(), next.getY(), grid);
            if (sailable)
               p.getShip().moveTo(next, this);

            // play the sound clip
            //SoundLoader.getInstance().getShipMoveSound().play();
            if (next != dest && sailable) {
               // recursion goes until reached destination
               boolean foundEnemy = false;
               // check for enemy intercept
               for (Player enemy : gameReference.getPlayers()) {
                  if (enemy != p && enemy.getShip().getTile() == p.getShip().getTile()) {
                     // check combat validity.
                     if (nextToTreasureIsland(p.getShip().getX(), p.getShip().getY()) == null &&
                             !(p.getShip().getTile() instanceof PortTile)) {
                        // ask the user whether they want to engage. Needs to be run on the GUI thread.
                        Platform.runLater(() -> handleEnemyOnPath(p, enemy, dest, returnState));
                        foundEnemy = true;
                        break;
                     }
                  }
               }
               if (!foundEnemy)
                  partialMove(p, dest, returnState);
            } else {
               // ends the cycle and tells the GUI to run this code whenever it gets chance (usually instantly)
               Platform.runLater(() -> {
                  gameReference.changeState(returnState);
                  handleEndOfPath(p);
               });
            }
         }
      })).play();
   }

   /**
    * This function handles asking the player whether or not they want to engage in combat.
    *
    * @param p           The player who is currently moving.
    * @param enemy       The enemy who is on the path.
    * @param dest        The destination tile to continue moving towards if the player decides to keep moving.
    * @param returnState The state which the game should return to once the destination is reached.
    */
   private void handleEnemyOnPath(Player p, Player enemy, Tile dest, State returnState) {
      // Change this string to direct the question at the enemy player
      boolean fight = new Choice(Controller.stage, enemy.getName() + ": A player just tried to sail over you!\nWould you like to engage in combat?",
              new String[]{"yes", "no"}).getAnswer();
      if (!fight) {
         // return to sailing.
         partialMove(p, dest, returnState);
      } else {
         // if there is going to be a fight, change currentPlayer to enemy.
         p.unhighlight();
         currentPlayer = enemy;
         handlePossibleCombat(p.getShip().getTile());
      }
   }

   /**
    * Handles the logic that needs to be ran at the end of a movement path. Based on State it will perform specific tasks.
    * Combat and island interaction is available if you are not in PostCombatMove state.
    *
    * @param player The player who just moved.
    */
   private void handleEndOfPath(Player player) {
      boolean fought = false;
      boolean inAPort = false;
      Tile tileAtEndOfPath = player.getShip().getTile();
      // if the player is in a PostCombatMove state just do the rotation stuff. No other interactions
      if (gameReference.getState() != State.PostCombatMove) {
         // ensures that the player actually moved and didn't try to attack his current square.
         fought = handlePossibleCombat(player.getShip().getTile()); // This function will set the current player to the loser or defender if a attack happens.

         // filter out cards which cannot be used
         // then performAction on the ones which can be used.
         if (player.getChanceCards().size() > 0) {
            Set<ChanceCard> toRemove = new HashSet<>();
            player.getChanceCards().stream().filter(c -> c.performCheck(player, this))
                    .forEach(c -> {
                       // Alert the user to the
                       new GenericMsg(Controller.stage,
                               "Activated " + c.getType());
                       c.performAction(player, this);

                       // flags cards for removal after the ForEach
                       toRemove.add(c);
                       // return the card to the bottom of the chance cards
                       treasureIslandContainer.putChanceCard(c);
                    });
            // removes the flagged cards
            toRemove.forEach(player::removeTradable);
         }
         handleIslandInteraction();

         // This must be below handleIslandInteraction as some chance cards teleport you to ports.
         inAPort = currentPlayer.getShip().getTile() instanceof PortTile;
         if (inAPort) {
            // auto rotate if in port.
            player.getShip().portRotate();

            // If at home port, deposit all the treasures.
            if (player.getShip().getTile() == player.getHomePort()) {
               player.getShip().getCargo().forEach(t -> player.getHomePort().addTreasure(t));
               player.getShip().getCargo().clear();
            }

            handlePortInteraction(player);
         }
      }


      // if a attack has occurred, no rotation happens and the current player is changed to the loser so they can move again.
      //  if port or bay, don't rotate
      if (!fought && !(inAPort)) {
         gameReference.changeState(State.Rotate);
         handleRotation();
      } else if (inAPort) {
         endTurn();
      }

   }

   /**
    * Handles the interaction a player has with a port, checks if player is at the port then performs necessary
    * actions.
    *
    * @param player The player to check and act upon.
    */
   private void handlePortInteraction(Player player) {
      PortTile portTile = (PortTile) player.getShip().getTile();
      if (player.getShip().getTile() != player.getHomePort()) {
         // present the trading window if the port and the player both have
         // at least something to trade.
         if ((portTile.getTreasureSet().size() > 0 || portTile.getCrewCardSet().size() > 0) &&
                 (player.getCrewCards().size() > 0 || player.getShip().getCargo().size() > 0))
            new TradeWindow(player, (PortTile) player.getShip().getTile()).showAndWait();
         else
            new GenericMsg(Controller.stage, "No tradable items are present at this port!");
      }
   }

   /**
    * Checks whether or not the players home port's total value exceeds the static constant {@link #treasureValueToWin}
    *
    * @param player The player who's value to check.
    * @return True if the game is over. False otherwise.
    */
   private boolean checkEndGame(Player player) {
      if (player.getHomePort().getTotalValue() >= treasureValueToWin)
         return true;
      else
         return false;
   }

   /**
    * Checks whether or not moving a players's ship to a new position is valid by game rules.
    *
    * @param player The player who's ship is being moved.
    * @param toX    The X coordinate to move to (internal)
    * @param toY    The Y coordinate to move to (internal)
    * @return True if the move is valid, false if not,
    */
   private boolean isValidMove(Player player, int toX, int toY) {
      Set<Tile> validMoves = MoveAssistance.getValidMoves(player, gameReference.getState(), grid);
      //validMoves.forEach(Tile::highlight);
      return validMoves.contains(grid[toX][toY]);
   }

   /**
    * Removes rotate buttons.
    *
    * @param tiles The tiles ArrayList of surrounding tiles at the time of rotation.
    */
   private void clearRotationButtons(ArrayList<Tile> tiles) {
      for (int i = 0; i < tiles.size(); i++) {
         if (tiles.get(i) != null)
            tiles.get(i).getTilePane().getChildren().remove(rotationPanes.get(i));
      }
   }

   /**
    * Initializes the rotation panes for use throughout the game, Only needs to be run at the start, once.
    */
   private void initRotatePanes() {
      rotationPanes = new ArrayList<>();
      Image im = GraphicLoader.getInstance().getRotateTriangle();

      for (Orientation dir : Orientation.getAll()) {
         StackPane pane = new StackPane();
         pane.setBackground(new Background(new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,
                 BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
         pane.setRotate(dir.getAngle());
         rotationPanes.add(pane);
      }
   }

   /**
    * Creates a new map based on a CSV provided. Also requires that the games parts be passed in e.g.
    * cards and treasures.
    *
    * @param mapCsvPath  The path to the CSV file which has the map config.
    * @param treasures   The treasures of the game.
    * @param crewCards   The crew cards of the game.
    * @param chanceCards The chance cards of the game.
    * @return A fully instantiated map of tiles with the size {@link #gridWidth} by {@link #gridHeight}
    */
   private Tile[][] newMap(String mapCsvPath, List<Treasure> treasures, Queue<CrewCard> crewCards,
                           Queue<ChanceCard> chanceCards) {
      MapBuilder mapBuilder = new MapBuilder(gridWidth, gridHeight, treasures, crewCards, chanceCards);
      mapBuilder.loadCSV(mapCsvPath);
      treasureIslandContainer = mapBuilder.getTreasureIslandContainer();
      pirateIslandContainer = mapBuilder.getPirateIslandContainer();
      flatIslandContainer = mapBuilder.getFlatIslandContainer();

      cadizTile = mapBuilder.getCadizTile();
      marseillesTile = mapBuilder.getMarseillesTile();
      veniceTile = mapBuilder.getVeniceTile();
      londonTile = mapBuilder.getLondonTile();
      amsterdamTile = mapBuilder.getAmsterdamTile();
      genoaTile = mapBuilder.getGenoaTile();

      anchorBayTile = mapBuilder.getAnchorBayTile();
      mudBayTile = mapBuilder.getMudBayTile();
      cliffCreekTile = mapBuilder.getCliffCreekTile();

      return mapBuilder.toMap();
   }

   private void endTurn() {
      // check for end game state.
      if (checkEndGame(currentPlayer)) {
         gameReference.triggerGameOver();
      } else {
         gameReference.changeState(State.Move);
         // changes player and highlights for new player.
         setCurrentPlayer(gameReference.nextPlayer());
      }
   }

}