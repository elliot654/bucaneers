/*
* @(#) MapBuilder.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.map;

import uk.ac.aber.cs221.group16.game.container.FlatIslandContainer;
import uk.ac.aber.cs221.group16.game.container.PirateIslandContainer;
import uk.ac.aber.cs221.group16.game.container.TreasureIslandContainer;
import uk.ac.aber.cs221.group16.game.items.cards.ChanceCard;
import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.map.tiles.BayTile;
import uk.ac.aber.cs221.group16.game.map.tiles.PortTile;
import uk.ac.aber.cs221.group16.game.map.tiles.Tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

/**
 * This class is intended to assist in building a two dimensional array of Tiles which we call a map.
 *
 * @author Josh Smith
 * @author Dylan Lewis
 * @version 1.0
 */
public class MapBuilder {

   // Static Variables
   public static final String defaultMapPath = "res/config/defaultMap.csv";
   private static final String defaultTileType = "Sea";

   // Instance Variables
   private Tile[][] map;
   private TileFactory tileFactory;
   private int width;
   private int height;

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
    * This constructor takes the size of the map and the parts which will be used to populate the containers of islands.
    *
    * @param width       The width of the map in tiles.
    * @param height      The height of the map in tiles.
    * @param treasures   The list of treasures to be assigned to treasure island.
    * @param crewCards   The queue of crew cards to be assigned to pirate island.
    * @param chanceCards The queue of chance cards to be assigned to treasure island.
    */
   public MapBuilder(int width, int height, List<Treasure> treasures, Queue<CrewCard> crewCards,
                     Queue<ChanceCard> chanceCards) {
      map = new Tile[width][height];
      this.width = width;
      this.height = height;

      // initialize containers
      FlatIslandContainer flatIslandContainer = new FlatIslandContainer();
      PirateIslandContainer pirateIslandContainer = new PirateIslandContainer(crewCards);
      TreasureIslandContainer treasureIslandContainer = new TreasureIslandContainer(treasures, chanceCards);

      // initialize tile factory.
      tileFactory = new TileFactory(flatIslandContainer, pirateIslandContainer, treasureIslandContainer);
   }

   // Public Methods

   /**
    * This function takes a CSV of tile types, these should match the configurations in the TILETYPES properties file
    * and build the map based on the rows/columns in the CSV.
    *
    * @param csvPath The path to the CSV file.
    */
   public void loadCSV(String csvPath) {
      String line;

      int y = 0;
      try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
         // if you reach the end of the file stop.
         while ((line = br.readLine()) != null) {
            int x = 0;
            // use comma as separator
            String[] row = line.split(",");

            for (String type : row) {
               // add a new tile with type from the csv and the x and y based on position in the csv.
               addTile(type, x, y);

               // if the next column extends further than the width, break;
               if (++x > width)
                  break;
            }
            // If the next row extends further than the height, break;
            if (++y > height)
               break;
         }

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * Adds a tile to the map based on a type string which should match a type in the TILETYPES properties file.
    *
    * @param type Type of tile which should match the loaded TILETYPES properties file.
    * @param x    The x coordinate to add the Tile.
    * @param y    The y coordinate to add the Tile.
    */
   public void addTile(String type, int x, int y) {
      Tile t = tileFactory.makeTile(type, x, y);
      switch (type) {
         case "LONDON":
            londonTile = (PortTile) t;
            break;
         case "CADIZ":
            cadizTile = (PortTile) t;
            break;
         case "AMSTERDAM":
            amsterdamTile = (PortTile) t;
            break;
         case "MARSEILLES":
            marseillesTile = (PortTile) t;
            break;
         case "VENICE":
            veniceTile = (PortTile) t;
            break;
         case "GENOA":
            genoaTile = (PortTile) t;
            break;
         case "ANCHOR":
            anchorBayTile = (BayTile) t;
            break;
         case "MUD":
            mudBayTile = (BayTile) t;
            break;
         case "CLIFF":
            cliffCreekTile = (BayTile) t;
      }

      // if its null we make it the default in the toMap function
      map[x][y] = t;
   }

   /**
    * Gets the two dimensional array of tiles that you have built through function calls. Checks the array for null
    * values and sets them to the default tile type which is defined in the {@link #defaultTileType} variable.
    *
    * @return The generated map from all the changes made so far.
    */
   public Tile[][] toMap() {
      // check for any null values and make them the default tile.
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            if (map[x][y] == null)
               map[x][y] = tileFactory.makeTile(defaultTileType, x, y);
         }
      }
      return map;
   }

   /**
    * Gets the treasure island container passed onto the map.
    *
    * @return The TreasureIslandContainer used as storage for TreasureIsland Tiles on the map.
    */
   public TreasureIslandContainer getTreasureIslandContainer() {
      return tileFactory.getTreasureIslandContainer();
   }

   /**
    * Gets the pirate island container passed onto the map.
    *
    * @return The PirateIslandContainer used as storage for PirateIsland Tiles on the Map.
    */
   public PirateIslandContainer getPirateIslandContainer() {
      return tileFactory.getPirateIslandContainer();
   }

   /**
    * Gets the Flat island container passed onto the map.
    *
    * @return The FlatIslandContainer used as storage for FlatIsland Tiles on the map.
    */
   public FlatIslandContainer getFlatIslandContainer() {
      return tileFactory.getFlatIslandContainer();
   }

   /**
    * Gets the Cadiz port tile passed onto the map.
    *
    * @return The cadizTile used to represent Cadiz on the map with its location image and contents.
    */
   public PortTile getCadizTile() {
      return cadizTile;
   }

   /**
    * Gets the London port tile passed onto the map.
    *
    * @return The londonTile used to represent London on the map with its location image and contents.
    */
   public PortTile getLondonTile() {
      return londonTile;
   }

   /**
    * Gets the Venice port tile passed onto the map.
    *
    * @return The veniceTile used to represent Venice on the map with its location image and contents.
    */
   public PortTile getVeniceTile() {
      return veniceTile;
   }

   /**
    * Gets the Amsterdam port tile passed onto the map.
    *
    * @return The AmsterdamTile used to represent Amsterdam on the map with its location image and contents.
    */
   public PortTile getAmsterdamTile() {
      return amsterdamTile;
   }

   /**
    * Gets the Genoa port tile passed onto the map.
    *
    * @return The genoaTile used to represent Genoa on the map with its location image and contents.
    */
   public PortTile getGenoaTile() {
      return genoaTile;
   }

   /**
    * Gets the Marseilles port tile passed onto the map.
    *
    * @return The marseillesTile used to represent Marseilles on the map with its location image and contents.
    */
   public PortTile getMarseillesTile() {
      return marseillesTile;
   }

   /**
    * Gets the Clff creek bay tile passed onto the map.
    *
    * @return The cliffCreekTile used to represent Cliff creek on the map with its location image and contents.
    */
   public BayTile getCliffCreekTile() {
      return cliffCreekTile;
   }

   /**
    * Gets the  Anchor bay bay tile passed onto the map.
    *
    * @return The anchorBayTile used to represent Anchor bay on the map with its location image and contents.
    */
   public BayTile getAnchorBayTile() {
      return anchorBayTile;
   }

   /**
    * Gets the Mud bay bay tile passed onto the map.
    *
    * @return The mudBayTile used to represent Mud bay on the map with its location image and contents.
    */
   public BayTile getMudBayTile() {
      return mudBayTile;
   }
}
