/*
* @(#) TileFactory.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.map;

import uk.ac.aber.cs221.group16.game.container.FlatIslandContainer;
import uk.ac.aber.cs221.group16.game.container.PirateIslandContainer;
import uk.ac.aber.cs221.group16.game.container.TreasureIslandContainer;
import uk.ac.aber.cs221.group16.game.items.cards.CrewCard;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.map.tiles.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class is used to assist in translating the string types of tiles into a subclass of the Tile class. The relevant
 * type strings are taken from the properties file which is loaded.
 * <p>
 * Provides a way of creating new Tiles without exposing the logic behind creating them.
 * <p>
 * Loads a properties file containing the tile types and which strings areused to represent specific tiles.
 *
 * @author Josh Smith
 * @version 1.0
 */
public class TileFactory {

   // Static Variables
   private static final String defaultConfigPath = "res/config/TILETYPES.properties";

   // Instance Variables
   private String configPath;

   private Properties properties;

   private FlatIslandContainer flatIslandContainer;
   private PirateIslandContainer pirateIslandContainer;
   private TreasureIslandContainer treasureIslandContainer;

   // The position markers for the where islands start
   private int flatLeft = -1;
   private int flatTop = -1;
   private int pirateLeft = -1;
   private int pirateTop = -1;
   private int treasureLeft = -1;
   private int treasureTop = -1;


   // Constructors

   /**
    * This constructor uses the default config file where the path is equal to {@link #defaultConfigPath} you to specify
    * the config path for the tiles.
    *
    * @param flatIslandContainer     The FlatIslandContainer to be passed throughout
    * @param pirateIslandContainer   The PirateIslandContainer to be passed throughout
    * @param treasureIslandContainer The TreasureIslandContainer to be passed throughout
    */
   public TileFactory(FlatIslandContainer flatIslandContainer, PirateIslandContainer pirateIslandContainer,
                      TreasureIslandContainer treasureIslandContainer) {
      this(defaultConfigPath, flatIslandContainer, pirateIslandContainer, treasureIslandContainer);
   }

   /**
    * This constructor allows you to specify the config path for the tiles, this should be a properties file with the
    * relevant properties, see the default one for example {@link #defaultConfigPath}.
    *
    * @param cfgPath                 The path to the properties file.
    * @param flatIslandContainer     The FlatIslandContainer to be passed throughout
    * @param pirateIslandContainer   The PirateIslandContainer to be passed throughout
    * @param treasureIslandContainer The TreasureIslandContainer to be passed throughout
    */
   public TileFactory(String cfgPath, FlatIslandContainer flatIslandContainer, PirateIslandContainer pirateIslandContainer,
                      TreasureIslandContainer treasureIslandContainer) {
      configPath = cfgPath;
      loadConfig();
      this.flatIslandContainer = flatIslandContainer;
      this.pirateIslandContainer = pirateIslandContainer;
      this.treasureIslandContainer = treasureIslandContainer;
   }

   // Public Methods

   /**
    * Takes a type which should be a string matching a type of Tile from the config.
    *
    * @param type The type of tile as a string relating to the config file passed to the constructor. Or the default
    *             config stored in {@link #defaultConfigPath}.
    * @param x    The x coordinate of the tile.
    * @param y    The y coordinate of the tile.
    * @return The relevant instantiated tile or null if the type did not match any of the config types.
    */
   public Tile makeTile(String type, int x, int y) {
      if (properties.get("Sea").equals(type)) {
         return new SeaTile(x, y);
      } else if (properties.get("FlatIsland").equals(type)) {
         if (flatTop == -1 || flatLeft == -1) {
            flatTop = y;
            flatLeft = x;
         }
         return new FlatIslandTile(x, y, x - flatLeft, y - flatTop, flatIslandContainer);
      } else if (properties.get("PirateIsland").equals(type)) {
         if (pirateLeft == -1 || treasureTop == -1) {
            pirateTop = y;
            pirateLeft = x;
         }
         return new PirateIslandTile(x, y, x - pirateLeft, y - pirateTop, pirateIslandContainer);
      } else if (properties.get("TreasureIsland").equals(type)) {
         if (treasureLeft == -1 || treasureTop == -1) {
            treasureTop = y;
            treasureLeft = x;
         }
         return new TreasureIslandTile(x, y, x - treasureLeft, y - treasureTop, treasureIslandContainer);
      } else if (properties.get("London").equals(type)) { // player port
         return new PortTile(Port.London, x, y);
      } else if (properties.get("Cadiz").equals(type)) { // player port
         return new PortTile(Port.Cadiz, x, y);
      } else if (properties.get("Marseilles").equals(type)) { // player port
         return new PortTile(Port.Marseilles, x, y);
      } else if (properties.get("Genoa").equals(type)) { // player port
         return new PortTile(Port.Genoa, x, y);
      } else if (properties.get("Venice").equals(type)) {
         PortTile ven = new PortTile(Port.Venice, x, y);
         setupNonPlayerPort(ven);
         return ven;
      } else if (properties.get("Amsterdam").equals(type)) {
         PortTile amst = new PortTile(Port.Amsterdam, x, y);
         setupNonPlayerPort(amst);
         return amst;
      } else if (properties.get("Anchor_Bay").equals(type)) {
         return new BayTile(Bay.Anchor_Bay, x, y);
      } else if (properties.get("Mud_Bay").equals(type)) {
         return new BayTile(Bay.Mud_Bay, x, y);
      } else if (properties.get("Cliff_Creek").equals(type)) {
         return new BayTile(Bay.Cliff_Creek, x, y);
      }
      return null;
   }

   /**
    * Gets the container for FlatIsland used and initialised by the Tile factory.
    *
    * @return The container object which stores the items on FlatIsland.
    */
   public FlatIslandContainer getFlatIslandContainer() {
      return flatIslandContainer;
   }

   /**
    * Gets the container for PirateIsland used and initialised by the Tile factory.
    *
    * @return The container object which stores the items on FlatIsland.
    */
   public PirateIslandContainer getPirateIslandContainer() {
      return pirateIslandContainer;
   }

   /**
    * Gets the container for TreasureIsland used and initialised by the Tile factory.
    *
    * @return The container object which stores the items on FlatIsland.
    */
   public TreasureIslandContainer getTreasureIslandContainer() {
      return treasureIslandContainer;
   }

   // Private Methods

   /**
    * This simple function loads the properties file with the path as the {@link #configPath} variable.
    */
   private void loadConfig() {
      properties = new Properties();
      try {
         properties.load(new FileReader(configPath));
      } catch (IOException e) {
         System.out.println(String.format("Failed to load properties file @ %s", configPath));
      }
   }

   /**
    * Initializes a Non player port to have a total value of 8 which consists of 2 crew cards and some treasures to make
    * up the extra value. This corresponds with FR10.
    *
    * @param pt The Port tile to set up.
    */
   private void setupNonPlayerPort(PortTile pt) {
      final int requiredValue = 8;
      // add two crew cards from the initial collection
      pt.addCrewCard(pirateIslandContainer.takeCrewCard());
      pt.addCrewCard(pirateIslandContainer.takeCrewCard());
      // calculate value of those crew cards
      int totalValue = 0;
      for (CrewCard c : pt.getCrewCardSet())
         totalValue += c.getValue();

      Set<Treasure> newTreasures = new HashSet<>();

      // sorts from highest to lowest
      Iterator<Treasure> treasures = treasureIslandContainer.getTreasures()
              .stream().sorted(Comparator.reverseOrder()).iterator();
      // search the container for treasures to bring the total up towards the required value.
      while (treasures.hasNext() && totalValue < requiredValue) {
         Treasure t = treasures.next();
         if (t.getValue() <= requiredValue - totalValue
                 && totalValue + t.getValue() != requiredValue - 1) {
            newTreasures.add(t);
            totalValue += t.getValue();
         }
      }
      if (totalValue > requiredValue)
         System.err.println("SOMETHING WENT WRONG WITH ASSIGNING TREASURES TO " +
                 "NON PLAYER PORTS");
      // add the treasures to the port and remove them from the container.
      newTreasures.forEach(t -> {
         treasureIslandContainer.getTreasures().remove(t);
         pt.addTreasure(t);
      });
   }
}
