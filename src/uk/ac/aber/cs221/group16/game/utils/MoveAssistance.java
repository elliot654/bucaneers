/*
* @(#) MoveAssistance.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.utils;

import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.map.Orientation;
import uk.ac.aber.cs221.group16.game.map.tiles.*;
import uk.ac.aber.cs221.group16.game.players.Player;
import uk.ac.aber.cs221.group16.game.states.State;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to store static functions which are used when moving and checking validity of tiles within
 * a movement.
 *
 * @author Josh Smith
 * @version 1.0
 */
public class MoveAssistance {

   /**
    * Gets the valid moves based on the ship's location and the players MovementDistance.
    *
    * @param p         The player.
    * @param gameState The current state of the game.
    * @param grid      The two dimensional array of Tiles representing the grid.
    * @return The set of valid moves as tiles.
    */
   public static Set<Tile> getValidMoves(Player p, State gameState, Tile[][] grid) {
      Set<Tile> validMoves = new HashSet<>();
      int moveDist = p.getMovementDistance();
      int curX = p.getShip().getTile().getX();
      int curY = p.getShip().getTile().getY();


      Orientation[] directions;
      // Check if in port or bay // if you just battled you must move!
      boolean anyMove = gameState == State.PostCombatMove || p.getShip().getTile() instanceof PortTile ||
              p.getShip().getTile() instanceof BayTile;
      int startDist = anyMove ? 1 : 0;

      // select which directions we will consider.
      if (anyMove)
         directions = Orientation.getAll();
      else
         directions = new Orientation[]{p.getShip().getOrientation()};

      // Loop through all directions which we are considering.
      for (Orientation dir : directions) {
         // In this direction loop from startDist to moveDist and use the direction's delta to check tiles.
         for (int curDist = startDist; curDist <= moveDist; curDist++) {
            int tx = curX + (dir.getDelta().getKey() * curDist);
            int ty = curY + (dir.getDelta().getValue() * curDist);
            if (isSailableCoordinate(tx, ty, grid)) {
               ArrayList<IslandTile> surTiles = getSurroundingIslandTiles(tx, ty, grid);
               boolean isNextToTreasureIsland = surTiles.size() > 0 && surTiles.get(0) instanceof TreasureIslandTile;
               boolean isPort = grid[tx][ty] instanceof PortTile;

               // if the tile has a player on it and is next to Treasure island
               // or is a port, it is not a valid move.
               if (!((isNextToTreasureIsland || isPort) && grid[tx][ty].isOccupied()))
                  validMoves.add(grid[tx][ty]);
            } else break;
         }
      }
      return validMoves;
   }

   /**
    * Gets the tiles surrounding a tile at the given coordinate.
    *
    * @param x    The x coordinate.
    * @param y    The y coordinate.
    * @param grid The two dimensional array of Tiles representing the grid.
    * @return An arraylist of surrounding tiles ordered by row,
    * NW, N, NE, W, E, SW, S, SE. Each of these can be null as to maintain order.
    */
   public static ArrayList<Tile> getSurroundingTiles(int x, int y, Tile[][] grid) {
      ArrayList<Tile> surroundingTiles = new ArrayList<>();
      for (int yMod = -1; yMod <= 1; yMod++) {
         for (int xMod = -1; xMod <= 1; xMod++) {
            if (xMod != 0 || yMod != 0)  // don't select the current tile
               if (isValidCoordinate(x + xMod, y + yMod)) // check validity of the tile.
                  surroundingTiles.add(grid[x + xMod][y + yMod]);
               else
                  surroundingTiles.add(null);
         }
      }
      return surroundingTiles;
   }

   /**
    * Checks if the coordinate has a valid and sailable tile.
    *
    * @param x    The x coordinate
    * @param y    The y coordinate
    * @param grid The two dimensional array of Tiles representing the grid.
    * @return True if there is a sailable tile in grid at grid[x][y]
    */
   public static boolean isSailableCoordinate(int x, int y, Tile[][] grid) {
      return isValidCoordinate(x, y) && grid[x][y].isSailable();
   }

   /**
    * Checks whether or not the coordinate is on the grid.
    *
    * @param x The x coordinate.
    * @param y The y coordinate.
    * @return True if the coordinates are on the grid otherwise false.
    */
   public static boolean isValidCoordinate(int x, int y) {
      if (x < 0 || x >= Board.gridWidth)
         return false;
      return !(y < 0 || y >= Board.gridHeight);
   }

   /**
    * Gets getAll of the Island tiles surrounding the tile at the coordinates passed.
    *
    * @param x    The x coordinate.
    * @param y    The y coordinate.
    * @param grid The two dimensional array of Tiles representing the grid.
    * @return The list of Island tiles within 1sq of the coordinate.
    */
   public static ArrayList<IslandTile> getSurroundingIslandTiles(int x, int y, Tile[][] grid) {
      ArrayList<Tile> tiles = getSurroundingTiles(x, y, grid);
      ArrayList<IslandTile> islandTiles = new ArrayList<>();
      for (Tile tile : tiles) {
         if (tile instanceof IslandTile) {
            islandTiles.add((IslandTile) tile);
         }
      }
      return islandTiles;
   }

}
