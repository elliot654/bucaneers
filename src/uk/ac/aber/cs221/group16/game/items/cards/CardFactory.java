/*
* @(#) CardFactory.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.items.cards;

import javafx.scene.paint.Color;
import uk.ac.aber.cs221.group16.game.Controller;
import uk.ac.aber.cs221.group16.game.items.Tradable;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.map.tiles.IslandTile;
import uk.ac.aber.cs221.group16.game.map.tiles.Tile;
import uk.ac.aber.cs221.group16.game.map.tiles.TreasureIslandTile;
import uk.ac.aber.cs221.group16.game.players.Player;
import uk.ac.aber.cs221.group16.game.utils.MoveAssistance;
import uk.ac.aber.cs221.group16.game.windows.Choice;
import uk.ac.aber.cs221.group16.game.windows.GenericMsg;
import uk.ac.aber.cs221.group16.game.windows.PlayerSelection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is intended to provide the game with sets of cards, Crew and Chance.
 *
 * @author Josh Smith
 * @author Alex Toop
 * @author Dylan Lewis
 * @author Filip Koziel
 * @version 1.0
 */
public class CardFactory {

   // Static Variables
   private static Color[] cols = {Color.RED, Color.BLACK};

   // Instance Variables
   private int numRepeatsCrewCards;

   // Constructors

   /**
    * This constructor initializes the factory and sets the number of each crew card to the passed integer. For example if you want a deck with
    * n copies of a Black Value 1 crew card, pass n to the constructor.
    *
    * @param copiesOfCrewCards The number of copies of each crew card to generate into a deck.
    */
   public CardFactory(int copiesOfCrewCards) {
      this.numRepeatsCrewCards = copiesOfCrewCards;
   }

   // Public Methods

   /**
    * Gets the full deck of crew cards in a shuffled order.
    *
    * @return A shuffled queue of CrewCards, ready to be used as a deck.
    */
   public Queue<CrewCard> getShuffledCrewCards() {
      ArrayList<CrewCard> crewCards = getInOrderCrewCards();

      Collections.shuffle(crewCards);

      Queue<CrewCard> shuffledCards = new LinkedList<>();

      crewCards.forEach(shuffledCards::add);

      return shuffledCards;
   }

   /**
    * Gets the full set of crew cards in order.
    *
    * @return An arraylist of Crew cards in order of Red-1, Red-1... Black-1, Black-1... Red-2, Red-2... Black-2, Black-2...
    */
   public ArrayList<CrewCard> getInOrderCrewCards() {
      ArrayList<CrewCard> crewCards = new ArrayList<>();

      for (int j = 1; j <= 3; j++) {
         for (Color col : cols) {
            for (int i = 0; i < numRepeatsCrewCards; i++) {
               crewCards.add(new CrewCard(col, j));
            }
         }
      }
      return crewCards;
   }

   /**
    * Gets a queue of chance cards which are shuffled.
    *
    * @return A queue of chance cards in a shuffled order.
    */
   public Queue<ChanceCard> getShuffledChanceCards() {
      Queue<ChanceCard> chanceCards = new LinkedList<>();
      // get chance cards in order.
      List<ChanceCard> inOrder = getInOrderChanceCards();

      Random r = new Random();
      // shuffle
      Collections.shuffle(inOrder, r);

      // add each to queue
      inOrder.forEach(chanceCards::add);

      return chanceCards;
   }

   /**
    * This is a very large function which has a main purpose of creating chance cards with their actions passed as functions.
    *
    * @return The deck of chance cards which are being used in the game.
    */
   public List<ChanceCard> getInOrderChanceCards() {
      ArrayList<ChanceCard> chanceCards = new ArrayList<>();
      List<String> cardTexts;
      try {
         cardTexts = Files.readAllLines(Paths.get("res/config/ChanceCards.txt"));
      } catch (IOException e) {
         System.out.println("LOG: Failed to read chance card file.");
         cardTexts = new ArrayList<>();
      }

      // Card 1 - Blown 5 squares away.
      chanceCards.add(new ChanceCard(1, cardTexts.get(1), (player, board) -> blowAway(5, board, player)));

      // Card 2 - Steal 3 cards from a targeted player
      chanceCards.add(new ChanceCard(2, cardTexts.get(2), (player, board) -> {
         if (checkOthersHaveCrew(board, player)) {
            stealFromChosen(3, board, player);
         } else {
            new GenericMsg(Controller.stage, "Skipped as no other players have crew cards!!");
         }
      }));

//       Card 3 - Move to Mud bay
      chanceCards.add(new ChanceCard(3, cardTexts.get(3), (player, board) -> {
         // Move to 0, 19 Mud bay
         player.getShip().moveTo(board.getGrid()[0][19], board);
      }));

      // Card 4 - Blown to cliff creek, take 4 cards from pirate island if you have 3 or less crew cards, if possible.
      chanceCards.add(new ChanceCard(4, cardTexts.get(4), (player, board) -> {
         // move to 19, 0 Cliff Creek/bay
         player.getShip().moveTo(board.getGrid()[19][0], board);
         // take cards
         if (player.numCrewCards() <= 3) {
            cardsFromPirateIsland(4, board, player);
         }
      }));

      // Card 5 - Blown to home port, take 4 cards from pirate island if you have 3 or less crew cards, if possible.
      chanceCards.add(new ChanceCard(5, cardTexts.get(5), (player, board) -> {
         player.getShip().moveTo(player.getHomePort(), board);
         // take cards
         if (player.numCrewCards() <= 3) {
            cardsFromPirateIsland(4, board, player);
         }
      }));

      // Card 6 - You are blown to Amsterdam. If your crew total is 3 or less, take 4 crew cards from Pirate Island.
      chanceCards.add(new ChanceCard(6, cardTexts.get(6), (player, board) -> {
         // move to amsterdam
         player.getShip().moveTo(board.getAmsterdamTile(), board);
         // checks if crew total is 3 or less
         if (player.getMovementDistance() <= 3) {
            // take 4 crew cards from pirate island
            cardsFromPirateIsland(4, board, player);
         }
      }));

      // Card 7 - One treasure from your ship or 2 crew cards from your hand are lost and washed overboard to
      // the nearest ship. If 2 ships are equidistant from yours you may ignore this instruction.
      chanceCards.add(new ChanceCard(7, cardTexts.get(7), (player, board) -> {
         Player closestPlayer = getNearestShip(player, board);
         if (closestPlayer != null) {
            List<Tradable> treasure = new ArrayList<>(player.getShip().getCargo());
            if (!lowestTreasureToPlayer(treasure, board, player, closestPlayer)) {
               lowestCrewCardsToPlayer(2, player, closestPlayer);
            }
         }
      }));

      // Card 8 - lowest value treasure or if no treasure,  2 lowest value crew cards goes to Flat Island
      chanceCards.add(new ChanceCard(8, cardTexts.get(8), (player, board) -> {
         List<Tradable> treasure = new ArrayList<>(player.getShip().getCargo());
         if (!lowestTreasureToFlatIsland(treasure, board, player)) {
            for (int i = 0; i < 2; i++) {
               List<Tradable> cards = new ArrayList<>(player.getCrewCards());
               lowestCrewCardToFlatIsland(cards, board, player);
            }
         }
      }));

      // Card 9 - highest value treasure or if no treasure, highest value crew card goes to Flat Island
      chanceCards.add(new ChanceCard(9, cardTexts.get(9), (player, board) -> {
         List<Tradable> treasure = new ArrayList<>(player.getShip().getCargo());
         if (!highestTreasureToFlatIsland(treasure, board, player)) {
            List<Tradable> cards = new ArrayList<>(player.getCrewCards());
            highestCrewCardToFlatIsland(cards, board, player);
         }
      }));

      // Card 10 - highest value crew card goes to Pirate Island
      chanceCards.add(new ChanceCard(10, cardTexts.get(10), (player, board) -> {
         List<Tradable> cards = new ArrayList<>(player.getCrewCards());
         highestCrewCardToPirateIsland(cards, board, player);
      }));

      // Card 11 - take treasure up to 5 val or 2 crew cards.
      chanceCards.add(new ChanceCard(11, cardTexts.get(11), (player, board) ->
              takeTreasureOrCards(5, 2, board, player)));

      // Card 12 - take treasure up to 4 val or 2 crew cards.
      chanceCards.add(new ChanceCard(12, cardTexts.get(12), (player, board) ->
              takeTreasureOrCards(4, 2, board, player)));

      // Card 13 - take treasure up to 5 val or 2 crew cards.
      chanceCards.add(new ChanceCard(13, cardTexts.get(13), (player, board) ->
              takeTreasureOrCards(5, 2, board, player)));

      // Card 14 - take treasure up to 7 val or 2 crew cards.
      chanceCards.add(new ChanceCard(14, cardTexts.get(14), (player, board) ->
              takeTreasureOrCards(7, 2, board, player)));

      // Card 15 - take 2 crew cards from pirate island
      chanceCards.add(new ChanceCard(15, cardTexts.get(15), (player, board) ->
              cardsFromPirateIsland(2, board, player)));

      // Card 16 - take treasure up to 7 and reduce total value of crew cards to 10 if player has > 10
      chanceCards.add(new ChanceCard(16, cardTexts.get(16), (player, board) -> {
         takeTreasure(7, board, player);
         reduceHandValueTo(10, player, board);
      }));

      // Card 17 - take treasure up to 6 and reduce total value of crew cards to 11 if player has > 11
      chanceCards.add(new ChanceCard(17, cardTexts.get(17), (player, board) -> {
         takeTreasure(6, board, player);
         reduceHandValueTo(11, player, board);
      }));

      // Card 18 - take treasure up to 4 value and 2 crew cards if crew is <=7
      chanceCards.add(new ChanceCard(18, cardTexts.get(18), (player, board) -> {
         takeTreasure(4, board, player);
         if (player.numCrewCards() <= 7) {
            cardsFromPirateIsland(2, board, player);
         }
      }));

      // Card 19 - Exchange all crew cards in your hand as far as possible
      // for the same number of  crew cards from Pirate Island.
      chanceCards.add(new ChanceCard(19, cardTexts.get(19), this::exchangeCrewCards));

      // Card 20 - Exchange 2 cards with another player at Treasure island,
      // if noone else adjacent, return the cards to Pirate Island
      chanceCards.add(new ChanceCard(20, cardTexts.get(20), (player, board) ->
              exchangeCardsAtTreasureIsland(2, player, board)));

      // Card 21 - Replace long John silver with value 7 (trade with any)
      chanceCards.add(new ChanceCard(21, cardTexts.get(21),
              (player, board) ->
                      takeTreasure(7, board, player),
              (player, board) -> { // condition
                 return player.getShip().getTile() == board.getVeniceTile();
              }
      ));

      // Card 22 - YELLOW FEVER
      chanceCards.add(new ChanceCard(22, cardTexts.get(22), (player, board) -> {
         Set<Player> affectedPlayers = new HashSet<>();
         StringBuilder sBuilder = new StringBuilder("The following players lost the following cards:\n");
         for (Player p : board.getPlayers()) {
            Set<CrewCard> toRemove = new HashSet<>();
            Iterator<CrewCard> sortedCards = p.getCrewCards().stream()
                    .sorted(Comparator.naturalOrder()).iterator();
            while (p.getCrewCards().size() > 7) {
               if (!affectedPlayers.contains(p))
                  sBuilder.append("\n").append(p.getName()).append("\n");
               CrewCard curr = sortedCards.next();
               sBuilder.append(curr).append("\n");
               toRemove.add(curr);
               p.removeTradable(curr);
               affectedPlayers.add(p);
            }
            board.getPirateIslandContainer().getCrewCards().addAll(toRemove);
         }
         if (affectedPlayers.size() > 0) {
            new GenericMsg(Controller.stage, sBuilder.append("\n").toString());
         }
      }));

      // Card 23 - Doubloons - Chance card to be traded for value 5.
      chanceCards.add(new ChanceCard(23, cardTexts.get(23), 5));

      // Card 24 - Pieces of Eight - Chance card to be traded for value 4.
      chanceCards.add(new ChanceCard(24, cardTexts.get(24), 4));

      // Card 25 - Kidds chart - You may sail to anchor bay and collect treasure
      // up to value 7
      chanceCards.add(createKiddsChart(25, cardTexts.get(25)));

      // Card 26 - Same as 25. ^^^^^^
      chanceCards.add(createKiddsChart(26, cardTexts.get(26)));

      // Card 27 - Take treasure up to 5 value, or 3 crew cards from Pirate Island.
      chanceCards.add(new ChanceCard(27, cardTexts.get(27), (player, board) ->
              takeTreasureOrCards(5, 3, board, player)));

      // Card 28 - take 2 crew cards from pirate island
      chanceCards.add(new ChanceCard(28, cardTexts.get(28), (player, board) ->
              cardsFromPirateIsland(2, board, player)));


      return chanceCards;
//              .subList(15,16);
   }

   /**
    * Performs a chance card operation which checks for any other players adjacent
    * to Treasure Island. If there is a player, that player exchanges cards with the
    * player passed in as a parameter, if there is no player, the same number of cards
    * are returned to Pirate Island.
    *
    * @param numCardsToSwap The number of cards to swap / lose to Pirate Island
    * @param player         The player who is using the Chance Card
    * @param board          The board which the game is played on.
    */
   private void exchangeCardsAtTreasureIsland(int numCardsToSwap, Player player, Board board) {
      StringBuilder sBuilder = new StringBuilder();
      Player exchangeWith = null;
      for (Player p : board.getPlayers()) {
         Tile shipTile = p.getShip().getTile();
         boolean nextToTreasureIsland = board.nextToTreasureIsland(shipTile.getX(),
                 shipTile.getY()) != null;
         if (p != player && nextToTreasureIsland) {
            exchangeWith = p;
            break;
         }
      }
      Iterator<CrewCard> curPlayerCards = player.getCrewCards().iterator();
      if (exchangeWith != null) {
         Set<CrewCard> toCurPlayer = new HashSet<>();
         Set<CrewCard> toOtherPlayer = new HashSet<>();
         sBuilder.append("You exchange cards with " + exchangeWith.getName() + ":\n\n");
         Iterator<CrewCard> otherPlayerCards = exchangeWith.getCrewCards().iterator();
         while (numCardsToSwap > 0) {
            CrewCard cardToSwap;
            // take from the current player to a temporary set.
            if (curPlayerCards.hasNext()) {
               cardToSwap = curPlayerCards.next();
               toOtherPlayer.add(cardToSwap);
            }
            // take the other player's cards into a temporary set.
            if (otherPlayerCards.hasNext()) {
               cardToSwap = otherPlayerCards.next();
               toCurPlayer.add(cardToSwap);
            }
            numCardsToSwap--;
         }
         sBuilder.append("You exchanged:\n");
         toOtherPlayer.forEach(c -> sBuilder.append(c).append("\n"));
         sBuilder.append("\nFor the following cards:\n");
         toCurPlayer.forEach(c -> sBuilder.append(c).append("\n"));

         // remove from their current owners.
         exchangeWith.getCrewCards().removeAll(toCurPlayer);
         player.getCrewCards().removeAll(toOtherPlayer);
         // Finally add the cards to the players
         player.getCrewCards().addAll(toCurPlayer);
         exchangeWith.getCrewCards().addAll(toOtherPlayer);

         new GenericMsg(Controller.stage, sBuilder.toString());
      } else { // No other players at Treasure Island
         sBuilder.append("No other player adjacent to Treasure Island!\nYou discard the following cards to Pirate Island.\n");
         for (int i = 0; i < numCardsToSwap; i++) {
            if (curPlayerCards.hasNext()) {
               CrewCard toTake = curPlayerCards.next();
               sBuilder.append(toTake).append("\n");
               board.getPirateIslandContainer().addCrewCard(toTake);
               curPlayerCards.remove();
            }
         }
      }
   }

   /**
    * Takes in the player and the board and determines the nearest ship
    *
    * @param player takes in the player who was given the chance card
    * @param board  takes in the current board
    * @return the closest player or null if two are equidistant
    */
   private Player getNearestShip(Player player, Board board) {
      int currDeltaX = 0;
      int currDeltaY = 0;
      double smallestDelta = 0;
      double currDelta = 0;
      Player closest = null;

      // Loop through all of the players
      for (Player p : board.getPlayers()) {
         // Make sure we're not looking at the passed in player
         if (p != player) {
            // Calculate the differences in x and y coordinates
            currDeltaX = Math.abs(player.getShip().getX() - p.getShip().getX());
            currDeltaY = Math.abs(player.getShip().getY() - p.getShip().getY());

            // Use of Pythagoras theorem to calculate distance between ships
            currDelta = Math.sqrt((currDeltaX * currDeltaX) + (currDeltaY * currDeltaY));

            // If it's the smallest distance then we store that distance and
            // then store the player that has a ship closest
            if (currDelta < smallestDelta || smallestDelta == 0) {
               smallestDelta = currDelta;
               closest = p;
            }
//            else if (currDelta == smallestDelta) {
//               // If the two NEAREST ships are  equidistant, returns null.
//               return null;
//            }
         }
      }
      return closest;
   }

   // Private Methods

   /**
    * Asks the user whether they want to receive treasure or cards.
    *
    * @return True if the player wants treasure, false if they want cards.
    */
   private boolean treasureOrCards(String passedMsg) {
      return new Choice(Controller.stage, passedMsg,
              new String[]{"Treasure", "Crew Cards"}).getAnswer();
   }

   /**
    * Check class to set boolean noCards correctly
    *
    * @return true if there are no crew cards on pirate island.
    */
   private boolean cardNoCheck(Board b) {
      int size = b.getPirateIslandContainer().getCrewCards().size();
      if (size > 0) {
         return false;
      } else {
         return true;
      }
   }

   /**
    * Check class to set boolean noTreasure correctly
    *
    * @return true if there is no treasure on treasure island.
    */
   private boolean treasureNoCheck(Board b) {
      int size = b.getTreasureIslandContainer().getTreasures().size();
      if (size > 0) {
         return false;
      } else {
         return true;
      }
   }

   /**
    * Checks whether there is an exact match of treasure on the island for the desired value.
    *
    * @param desiredValue The desired value to obtain from Treasure Island,
    * @param b            The Board which the player is playing on.
    * @return true if there is the correct treasure to get the desired value. False otherwise.
    */
   private boolean notExactValueOfTreasure(int desiredValue, Player player, Board b) {
      int sum = 0;
      for (Treasure treasure : b.getTreasureIslandContainer().getTreasuresOfValue(desiredValue, player.getShip().getRemainingCapacity(), false)) {
         sum += treasure.getValue();
      }
      return sum != desiredValue;
   }


   /**
    * Performs the actions required for the chance cards requiring a choice to be made and treasures or crew cards to be
    * dealt out.
    *
    * @param maxTreasureValue The maximum value of treasure you can take.
    * @param maxCrewCards     The max number of crew cards you can take.
    * @param board            The Board the game is being played on.
    * @param player           The player who drew the card.
    */
   private void takeTreasureOrCards(int maxTreasureValue, int maxCrewCards, Board board, Player player) {
      boolean noCards = cardNoCheck(board);
      boolean noTreasure = treasureNoCheck(board);
      boolean shipNotFull = player.getShip().getCargo().size() < player.getShip().getCapacity();
      boolean notEnoughCards = false;
      boolean notExactTreasure = notExactValueOfTreasure(maxTreasureValue, player, board);

      if (maxCrewCards > board.getPirateIslandContainer().getCrewCards().size())
         notEnoughCards = true;

      if (noCards && noTreasure) {
         new GenericMsg(Controller.stage, "No crew cards or treasures.");
         return;
      } else if (noCards) {
         new GenericMsg(Controller.stage, "There are no Crew Cards available! Treasure to be taken.");
         takeTreasure(maxTreasureValue, board, player);
         return;
      } else if (noTreasure) {
         new GenericMsg(Controller.stage, "There are no treasures available! Crew cards to be taken instead.");
         cardsFromPirateIsland(maxCrewCards, board, player);
      } else if (notEnoughCards) {
         new GenericMsg(Controller.stage, "WARNING: There are not enough cards, Taking Cards will give you what remains!");
      } else if (notExactTreasure) {
         new GenericMsg(Controller.stage, "WARNING: From the remaining Treasure you can not obtain the maximum value" +
                 " you are allowed! Taking Treasure will give you the next best thing.");
      }

      if (!shipNotFull) {
         new GenericMsg(Controller.stage, "You currently carry " + player.getShip().getCapacity() + "Treasures and therefore can only take Cards!");
      } else if (!noTreasure && shipNotFull && treasureOrCards("Do you want Treasures or Crew Cards?")) {
         takeTreasure(maxTreasureValue, board, player);
         return;
      }

      if (!noCards) {
         cardsFromPirateIsland(maxCrewCards, board, player);
      }
   }

   /**
    * Attempts to take a given number of cards from pirate island and give them to the player passed.
    *
    * @param numCards The number of cards to attempt to take.
    * @param board    The board.
    * @param player   The player to give the cards to.
    */
   private void cardsFromPirateIsland(int numCards, Board board, Player player) {
      if (board.getPirateIslandContainer().getCrewCards().size() == 0) {
         new GenericMsg(Controller.stage, "No crew cards. Sorry. :(");
         return;
      }
      StringBuilder sBuilder = new StringBuilder("You have picked up: \n");
      for (int i = 0; i < numCards; i++) {
         // One pirate island tile is at 16, 15
         CrewCard c = board.getPirateIslandContainer().takeCrewCard();
         if (c != null) {
            sBuilder.append(c.toString()).append("\n");
            player.addCrewCard(c);
         } else {
            break; // no more cards :(
         }
      }
      new GenericMsg(Controller.stage, sBuilder.toString());
   }

   /**
    * Takes up to 2 treasure up to a maximum value.
    *
    * @param maxValue Maximum value to take.
    * @param board    The board.
    * @param player   The player who's ship to give treasure to.
    */
   private void takeTreasure(int maxValue, Board board, Player player) {
      Set<Treasure> treasures = board.getTreasureIslandContainer().getTreasuresOfValue(maxValue, player.getShip().getRemainingCapacity(), true);
      StringBuilder sBuilder = new StringBuilder("You have taken: \n");
      int taken = 0;
      for (Treasure treasure : treasures) {
         if (player.getShip().addTreasure(treasure)) {
            sBuilder.append(treasure.getType()).append(" of value ").append(treasure.getValue()).append("\n");
            taken++;
         }
      }
      if (taken > 0) {
         new GenericMsg(Controller.stage, sBuilder.toString());
      } else {
         new GenericMsg(Controller.stage, "Your ship is full!");
      }
   }

   /**
    * Creates the chance card appropriate for #25 and #26, as they are identical
    * other than their ID
    *
    * @param id The id to use on the card.
    * @return The KiddsChart chance card allowing players to collect up to 7
    * value of treasure at Anchor Bay.
    */
   private ChanceCard createKiddsChart(int id, String text) {
      return new ChanceCard(id, text,
              (player, board) -> // action
                      takeTreasure(7, board, player),
              (player, board) -> // check if the players ship is at anchor bay.
                      player.getShip().getTile() == board.getAnchorBayTile()
      );
   }

   /**
    * A players ship is blown away from TreasaureIsland by a passed number of squares. If the tile at numSquares away is
    * not a valid tile to sail onto, the closest sailable tile on the path is selected.
    *
    * @param numSquares The number of squares to blow the ship away.
    * @param board      The board which the ship is on.
    * @param player     The player who's ship should be blown away.
    */
   private void blowAway(int numSquares, Board board, Player player) {
      Tile curTile = player.getShip().getTile();
      // nearby island tiles
      ArrayList<IslandTile> nearTiles = MoveAssistance.getSurroundingIslandTiles(curTile.getX(), curTile.getY(),
              board.getGrid());
      // remove all non treasure island tiles.
      nearTiles.removeIf(tile -> !(tile instanceof TreasureIslandTile));

      if (nearTiles.size() > 0) {
         Tile tileToMoveAwayFrom;
         if (nearTiles.size() > 1) {
            // remove corner tiles
            nearTiles.removeIf(t -> {
               int dx = curTile.getX() - t.getX();
               int dy = curTile.getY() - t.getY();
               // remove if dx and dy are both not 0 e.g. diagonal.
               return dx != 0 && dy != 0;
            });
         }
         tileToMoveAwayFrom = nearTiles.get(0);
         // get the direction vector
         int dx = curTile.getX() - tileToMoveAwayFrom.getX();
         int dy = curTile.getY() - tileToMoveAwayFrom.getY();

         // find the furthest point within numSquares which is a sailable tile.
         for (int i = numSquares; i > 0; i--) {
            int newX = curTile.getX() + (dx * i);
            int newY = curTile.getY() + (dy * i);
            Tile dest = board.getGrid()[newX][newY];
            // if this is the furthest distance, AND the tile is occupied,
            // attempt to move one tile further
            if (i == numSquares && dest.isOccupied()) {
               newX = curTile.getX() + (dx * i + 1);
               newY = curTile.getY() + (dy * i + 1);
               dest = board.getGrid()[newX][newY];
            }
            if (MoveAssistance.isSailableCoordinate(newX, newY, board.getGrid())) {
               player.getShip().moveTo(dest, board);
               // break once found a valid move.
               break;
            }
         }
      }


   }


   /**
    * Determine highest value tradables
    *
    * @param tradables Tradeable items (treasure or chance cards)
    * @return Highest value treasure or chance card.
    */
   private Tradable getHighestTradable(List<Tradable> tradables) {
      Tradable highest = null;
      for (Tradable t : tradables) {
         if (highest == null || highest.getValue() < t.getValue()) {
            highest = t;
         }
      }
      return highest;
   }

   /**
    * Determine lowest value tradables
    *
    * @param tradables Tradeable items (treasure or chance cards)
    * @return Lowest value treasure or chance card.
    */
   private Tradable getLowestTradable(List<Tradable> tradables) {
      Tradable lowest = null;
      for (Tradable t : tradables) {
         if (lowest == null || lowest.getValue() > t.getValue()) {
            lowest = t;
         }
      }
      return lowest;
   }

   /**
    * Add highest value crew card to Pirate Island.
    *
    * @param cards  Crew cards.
    * @param board  The board.
    * @param player Player giving the card away.
    */
   private void highestCrewCardToPirateIsland(List<Tradable> cards, Board board, Player player) {
      if (cards.size() > 0) {
         CrewCard highest = (CrewCard) getHighestTradable(cards);
         player.getCrewCards().remove(highest);
         board.getPirateIslandContainer().addCrewCard(highest);
      }
   }

   /**
    * Add highest value crew card to Flat Island.
    *
    * @param cards  Crew cards.
    * @param board  The board.
    * @param player Player giving the card away.
    */
   private void highestCrewCardToFlatIsland(List<Tradable> cards, Board board, Player player) {
      if (cards.size() > 0) {
         CrewCard highest = (CrewCard) getHighestTradable(cards);
         player.getCrewCards().remove(highest);
         board.getFlatIslandContainer().addCrewCard(highest);
      }
   }

   /**
    * Add lowest value crew card to Flat Island.
    *
    * @param cards  Crew cards.
    * @param board  The board.
    * @param player Player giving the card away.
    */
   private void lowestCrewCardToFlatIsland(List<Tradable> cards, Board board, Player player) {
      if (cards.size() > 0) {
         CrewCard lowest = (CrewCard) getLowestTradable(cards);
         player.getCrewCards().remove(lowest);
         board.getFlatIslandContainer().addCrewCard(lowest);
      }
   }

   /**
    * Add lowest value crew card to the a passed player.
    *
    * @param player        Player giving the card away.
    * @param closestPlayer Closest player to the player who picks up the chance card.
    */
   private void lowestCrewCardsToPlayer(int numCards, Player player, Player closestPlayer) {
      List<Tradable> cards = new ArrayList<>(player.getCrewCards());
      StringBuilder sBuilder = new StringBuilder("The following Crew cards have been washed overboard to " + closestPlayer.getName()).append("\n");
      boolean takenOne = false;
      for (int i = 0; i < numCards; i++) {
         if (player.getCrewCards().size() > 0) {
            takenOne = true;
            CrewCard lowest = (CrewCard) getLowestTradable(cards);
            player.getCrewCards().remove(lowest);
            closestPlayer.addCrewCard(lowest);
            cards.remove(lowest);
            sBuilder.append(lowest).append("\n");
         }
      }
      if (takenOne) {
         new GenericMsg(Controller.stage, sBuilder.toString());
      } else {
         new GenericMsg(Controller.stage, player.getName() + " has no Crew cards to give!");
      }

   }

   /**
    * Add lowest value treasure to the nearest player.
    *
    * @param treasure      Treasure.
    * @param board         The board.
    * @param player        Player giving lowest value treasure away.
    * @param closestPlayer Closest player to the player who picks up the chance card.
    * @return Giving away treasure if there is any, else false.
    */
   private boolean lowestTreasureToPlayer(List<Tradable> treasure, Board board, Player player, Player closestPlayer) {
      if (treasure.size() > 0) {
         Treasure lowest = (Treasure) getLowestTradable(treasure);
         player.getShip().getCargo().remove(lowest);
         closestPlayer.getShip().addTreasure(lowest);
         new GenericMsg(Controller.stage, lowest.getType() +
                 " has washed overboard to " + closestPlayer.getName() +
                 "'s ship.");
         return true;
      } else {
         return false;
      }
   }

   /**
    * Add lowest value treasure to Flat Island.
    *
    * @param treasure Treasure.
    * @param board    The board.
    * @param player   Player giving lowest value treasure away.
    * @return Giving away treasure if there is any, else false.
    */
   private boolean lowestTreasureToFlatIsland(List<Tradable> treasure, Board board, Player player) {
      if (treasure.size() > 0) {
         Treasure lowest = (Treasure) getLowestTradable(treasure);
         player.getShip().getCargo().remove(lowest);
         board.getFlatIslandContainer().addTreasure(lowest);
         return true;
      } else {
         return false;
      }
   }

   /**
    * Add highest value treasure to Flat Island.
    *
    * @param treasure Treasure.
    * @param board    The board.
    * @param player   Player giving highest value treasure away.
    * @return Giving away treasure if there is any, else false.
    */
   private boolean highestTreasureToFlatIsland(List<Tradable> treasure, Board board, Player player) {
      if (treasure.size() > 0) {
         Treasure highest = (Treasure) getHighestTradable(treasure);
         player.getShip().getCargo().remove(highest);
         board.getFlatIslandContainer().addTreasure(highest);
         return true;
      } else {
         return false;
      }
   }

   /**
    * Exchange all crew cards from you hand for the same amount of crew cards from Pirate Island
    *
    * @param board  The Board.
    * @param player Player exchanging the cards.
    */
   private void exchangeCrewCards(Player player, Board board) {
      int numToTake = 0;
      if (player.getCrewCards().size() >
              board.getPirateIslandContainer().getCrewCards().size()) {
         numToTake = board.getPirateIslandContainer().getCrewCards().size();
         // if player has more crew cards than there are on Pirate Island, exchange only to the amount on the Island
      } else {
         numToTake = player.getCrewCards().size();
         // else exchange all crew cards
      }
      for (int i = 0; i < numToTake; i++) {
         board.getPirateIslandContainer().addCrewCard(player.getCrewCards().remove(0));
         player.addCrewCard(board.getPirateIslandContainer().takeCrewCard());
      }
   }

   /**
    * Checks to see if the other players on the board have at least one crew
    * card.
    *
    * @param board  The board is used to check the other players.
    * @param player The player who needs to check the other players.
    * @return true if at least one player has a crew card, false otherwise.
    */

   private boolean checkOthersHaveCrew(Board board, Player player) {
      int playersWithoutCrew = 0;
      for (int i = 0; i < 4; i++) {
         // increments variable if other players have no crew cards
         if (board.getPlayers()[i] != player && board.getPlayers()[i].getCrewCards().size() < 1) {
            playersWithoutCrew++;
         }
      }
      if (playersWithoutCrew > 2) {
         return false;
      } else {
         return true;
      }
   }

   /**
    * Takes the specified number of chance cards from a chosen player. Note that
    * a player is considered a valid choice as long as they have more than one
    * card to steal.
    *
    * @param num    The number of chance cards to take.
    * @param board  The board being played on.
    * @param player The person who is stealing people's cards.
    * @return boolean that allows us to report if no players can be stolen from.
    */
   private boolean stealFromChosen(int num, Board board, Player player) {
      // selectedPlayer is the player chosen from the choices presented
      Player selectedPlayer = new
              PlayerSelection(Controller.stage, board,
              ("Steal " + num + " crew cards from a player of choice:")).getSelectedPlayer();
      // Loops through until cards have been successfully stolen
      boolean done = false;
      while (!done) {
         if (selectedPlayer.getCrewCards().size() == 0) {
            selectedPlayer = new
                    PlayerSelection(Controller.stage, board,
                    "There was no cards to steal! \nChoose a different player:").getSelectedPlayer();
         } else {
            StringBuilder stringBuilder = new StringBuilder("You stole the following cards from ").append(selectedPlayer.getName()).append(":\n");
            // entered when the player can have cards stolen
            for (int i = 0; i < num; i++) {
               List<Tradable> cards = new ArrayList<>(selectedPlayer.getCrewCards());
               getLowestTradable(cards);
               if (cards.size() > 0) {
                  CrewCard lowest = (CrewCard) getLowestTradable(cards);
                  selectedPlayer.getCrewCards().remove(lowest);
                  stringBuilder.append(lowest).append("\n");
                  player.addCrewCard(lowest);
               }
            }
            done = true;
            new GenericMsg(Controller.stage, stringBuilder.toString());
         }
      }
      return true;
   }

   /**
    * Reduces the player's hand to a specified value or as close as possible
    * to that value
    *
    * @param reduceTo Value to reduce the players hand to.
    * @param player   The player who's hand to reduce.
    * @param board    The board the game is played on.
    */
   private void reduceHandValueTo(int reduceTo, Player player, Board board) {
      StringBuilder stringBuilder = new StringBuilder("The following cards have been returned to Pirate Island:\n");
      boolean reduced = false;
      while (player.getCrewCards().stream().mapToInt(CrewCard::getValue).sum() > reduceTo) {
         Tradable cardToRemove = getLowestTradable(player.getCrewCards().stream()
                 .sorted(Comparator.naturalOrder()).map(c -> (Tradable) c).collect(Collectors.toList()));
         player.removeTradable(cardToRemove);
         board.getPirateIslandContainer().addCrewCard((CrewCard) cardToRemove);
         reduced = true;
         stringBuilder.append(cardToRemove).append("\n");
      }
      if (reduced) {
         new GenericMsg(Controller.stage, stringBuilder.toString());
      }
   }

}
