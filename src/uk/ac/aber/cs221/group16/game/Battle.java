/*
* @(#) Battle.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game;

import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.players.Player;
import uk.ac.aber.cs221.group16.game.windows.GenericMsg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class handles engagements between two players, performing the checks necessary to calculate the winner and
 * also handle the distribution of loot afterwards.
 *
 * @author Josh Smith
 */
public class Battle {

   private Player attacker;
   private Player defender;

   private StringBuilder informMessageBuilder;
   private Board board;

   /**
    * Instantiates the object with a set attacker, defender and a default message.
    *
    * @param atk The attacker.
    * @param def The defender
    * @param b   The board which this battle is happening on.
    */
   public Battle(Player atk, Player def, Board b) {
      attacker = atk;
      defender = def;
      informMessageBuilder = new StringBuilder().append(attacker.getName())
              .append(" attacks ").append(defender.getName()).append(" (")
              .append(attacker.getFightingStrength()).append(" vs ")
              .append(defender.getFightingStrength()).append(")\n");
      board = b;
   }

   /**
    * Performs the logic of the battle to see who wins, also leads into distribution of the loot afterwards.
    */
   public void engage() {
      Player loser = attacker.attack(defender);
      Player winner = loser == null ? null : attacker == loser ? defender : attacker;
      informBattleOutcome(winner, loser);

      if (loser != null) {
         distributeLoot(winner, loser);
         board.setCurrentPlayer(loser);
      } else {
         board.setCurrentPlayer(defender);
      }

   }

   /**
    * Provides a popup for the players informing them of who won and who must move.
    *
    * @param winner The victor of the battle.
    * @param loser  The loser of the battle.
    */
   private void informBattleOutcome(Player winner, Player loser) {
      Player whoMoves;
      if (loser == null) {
         whoMoves = defender;
         informMessageBuilder.append("DRAW!\n");
      } else {
         whoMoves = loser;
         informMessageBuilder.append(winner.getName()).append(" Wins!\n");
      }
      new GenericMsg(Controller.stage, informMessageBuilder.append(whoMoves.getName())
              .append(" Must move!").toString());
   }

   /**
    * Distributes loot from the loser to the winner based on the spec. If the loser does not have treasure,
    * the winner takes crew cards. If the winner cannot carry enough treasure, he takes what he can and the rest is
    * returned to treasure island.
    *
    * @param winner The victor of the battle.
    * @param loser  The loser of the battle.
    */
   private void distributeLoot(Player winner, Player loser) {
      Iterator<Treasure> treasures = loser.getShip().getCargo().stream()
              .sorted(Treasure::valueComp).iterator();
      if (loser.getShip().getCargo().size() > 0) {
         List<Treasure> spoils = new ArrayList<>();
         List<Treasure> returned = new ArrayList<>();
         while (treasures.hasNext()) {
            Treasure t = treasures.next();
            // If the ship rejects the treasure, send it to treasure island.
            if (!winner.getShip().addTreasure(t)) {
               returned.add(t);
               board.getTreasureIslandContainer().addTreasure(t);
            } else spoils.add(t);
            loser.getShip().getCargo().remove(t);
            // UI Popup for details
         }
         informOfSpoils(spoils, returned, winner, loser);
      } else if (loser.getCrewCards().size() > 0) { // Cards instead of treasure, 2 lowest
         List<CrewCard> spoils = new ArrayList<>();
         List<CrewCard> crewCards = loser.getCrewCards();
         // sort so we can grab the two lowest
         crewCards.sort((c1, c2) -> c1.getValue() > c2.getValue() ? 1 : -1);
         // take two crew cards, if possible
         for (int i = 0; i < 2; i++)
            if (crewCards.size() > 0)
               spoils.add(crewCards.remove(0));
            else
               break;
         spoils.forEach(winner::addCrewCard);
         // UI popup for details
         informOfSpoils(spoils, winner, loser);
      } else { // if no treasure or crew cards
         new GenericMsg(Controller.stage, loser + " has no Treasure or Crew Cards\n" +
                 "Why are you being so mean? :(");
      }
   }

   /**
    * Pops up information regarding the spoils of the battle for the players to see.
    *
    * @param spoils The crew cards taken by the victor from the loser.
    * @param winner The winner of the battle.
    * @param loser  The loser of the battle
    */
   private void informOfSpoils(List<CrewCard> spoils, Player winner, Player loser) {
      StringBuilder stringBuilder = new StringBuilder(loser.getName()).append(" had no treasure.\n")
              .append(winner.getName()).append(" takes the two lowest valued crew cards:\n");
      spoils.forEach(c -> stringBuilder.append(c.toString()).append("\n"));
      new GenericMsg(Controller.stage, stringBuilder.toString());
   }

   /**
    * Pops up information regarding the spoils of the battle for the players to see.
    *
    * @param spoils The treasures which are given to the victor.
    * @param winner The winner of the battle.
    * @param loser  The loser of the battle
    */
   private void informOfSpoils(List<Treasure> spoils, List<Treasure> returned, Player winner, Player loser) {
      StringBuilder stringBuilder = new StringBuilder(winner.getName()).append(" has plundered ")
              .append(loser.getName()).append("'s cargo!\n");
      if (spoils.size() > 0) {
         stringBuilder.append("He has claimed: \n");
         spoils.forEach(t -> stringBuilder.append("\t").append(t.getType()).append("\n"));
      } else {
         stringBuilder.append(winner.getName()).append("'s ship is at maximum capacity!\n");
      }
      if (returned.size() > 0) {
         stringBuilder.append("\nThe treasures below were returned to Treasure Island!\n");
         returned.forEach(t -> stringBuilder.append("\t").append(t.getType()).append("\n"));
      }
      new GenericMsg(Controller.stage, stringBuilder.toString());
   }
}
