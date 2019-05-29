/*
* @(#) GraphicLoader.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game;

import javafx.scene.image.Image;

/**
 * This singleton provides a single point of loading for all graphics within the game. Graphics can be accessed with
 * GraphicLoader.getInstance().get...
 *
 * @author Josh Smith
 * @version 1.0
 */
public class GraphicLoader {

   private static GraphicLoader instance;

   // Static functions

   /**
    * Singleton function to get or create the instance of graphic loader.
    *
    * @return The singleton instance of GraphicLoader.
    */
   public static GraphicLoader getInstance() {
      if (instance == null)
         instance = new GraphicLoader();
      return instance;
   }

   // Instance Variables
   private Image[] ships;

   private Image seaTile;
   private Image[][] treasureIslandTiles;
   private Image[][] flatIslandTiles;
   private Image[][] pirateIslandTiles;

   private Image londonTile;
   private Image cadizTile;
   private Image marseillesTile;
   private Image genoaTile;
   private Image veniceTile;
   private Image amsterdamTile;

   private Image cliffCreekTile;
   private Image anchorBayTile;
   private Image mudBayTile;

   private Image rotateTriangle;
   private Image crewCardStack;
   private Image chanceCardStack;

   private Image focusCursor;
   private Image defaultCursor;
   private Image brokenCursor;

   // Constructors

   /**
    * This constructor is private so that Graphic Loader can only be instantiated by itself as a singleton.
    * Loads all of the images used within the game so that they can be accessed via getter methods.
    */
   private GraphicLoader() {
      System.out.println("Loading Graphics!:\n");
      String basePath = "/graphics/";
      System.out.println("Loading Ships");
      ships = new Image[]{
              new Image(basePath + "sprites/ships/Ship Red.png"),
              new Image(basePath + "sprites/ships/Ship Yellow.png"),
              new Image(basePath + "sprites/ships/Ship Green.png"),
              new Image(basePath + "sprites/ships/Ship Orange.png"),
      };
      System.out.println("Loading Sea Tile");
      seaTile = new Image(basePath + "sprites/SeaTile.gif");
      System.out.println("Loading Treasure Island Tiles");
      treasureIslandTiles = loadTreasureIslandTiles();
      System.out.println("Loading Flat Island Tiles.");
      flatIslandTiles = loadFlatIslandTiles();
      System.out.println("Loading Pirate Island Tiles");
      pirateIslandTiles = loadPirateIslandTiles();
      System.out.println("Loading London Tile");
      londonTile = new Image(basePath + "sprites/PortLondon.png");
      System.out.println("Loading Cadiz Tile");
      cadizTile = new Image(basePath + "sprites/PortCadiz.png");
      System.out.println("Loading Marseilles Tile");
      marseillesTile = new Image(basePath + "sprites/PortMarseilles.png");
      System.out.println("Loading Genoa Tile");
      genoaTile = new Image(basePath + "sprites/PortGenoa.png");
      System.out.println("Loading Venice Tile");
      veniceTile = new Image(basePath + "sprites/PortVenice.png");
      System.out.println("Loading Amsterdam Tile");
      amsterdamTile = new Image(basePath + "sprites/PortAmsterdam.png");
      System.out.println("Loading CliffCreek Tile");
      cliffCreekTile = new Image(basePath + "sprites/CliffCreekGif.gif");
      System.out.println("Loading AnchorBay Tile");
      anchorBayTile = new Image(basePath + "sprites/AnchorBayGif.gif");
      System.out.println("Loading Mud Bay Tile");
      mudBayTile = new Image(basePath + "sprites/MudBayGif.gif");

      System.out.println("Loading Rotate Triangle");
      rotateTriangle = new Image(basePath + "sprites/triangle.png", 20, 20, true, true);
      System.out.println("Loading Crew Card Stack");
      crewCardStack = new Image("/graphics/sprites/CrewCard.png");
      System.out.println("Loading Chance Card Stack");
      chanceCardStack = new Image("/graphics/sprites/ChanceCard.png");

      // mac os will use the image size as the cursor size hence we need to
      // set a standard 32 x 32 image size for MacOS and 64 x 64 for other to
      // maintain a better image quality
      int size = (!System.getProperty("os.name").contains("Mac OS") == false) ? 32 : 64;

      defaultCursor = new Image("/graphics/sprites/MouseCursor.png",
              size, size, true, true);
      focusCursor = new Image("/graphics/sprites/MouseCursorHighlight.gif",
              size, size, true, true);
      brokenCursor = new Image("/graphics/sprites/MouseCursorBroken.png",
              size, size, true, true);

   }

   // Public Methods

   /**
    * returns an image of the ship
    *
    * @return graphic for the ship.
    */
   public Image[] getShips() {
      return ships;
   }

   /**
    * returns an image for the sea tile
    *
    * @return graphic for sea tile.
    */
   public Image getSeaTile() {
      return seaTile;
   }

   /**
    * returns the image for treasure island
    *
    * @return graphic for treasure island.
    */
   public Image[][] getTreasureIslandTiles() {
      return treasureIslandTiles;
   }

   /**
    * returns the image for flat island
    *
    * @return graphic for flat island.
    */
   public Image[][] getFlatIslandTiles() {
      return flatIslandTiles;
   }

   /**
    * returns the image for pirate island
    *
    * @return graphic for pirate island.
    */
   public Image[][] getPirateIslandTiles() {
      return pirateIslandTiles;
   }

   /**
    * returns the image for the london port tile
    *
    * @return graphic for london.
    */
   public Image getLondonTile() {
      return londonTile;
   }

   /**
    * returns an image for the cadiz port tile
    *
    * @return graphic for cadiz.
    */
   public Image getCadizTile() {
      return cadizTile;
   }

   /**
    * returns the image for the marseilles port tile
    *
    * @return graphic for marseilles.
    */
   public Image getMarseillesTile() {
      return marseillesTile;
   }

   /**
    * returns an image for the genoa port tile
    *
    * @return graphic for genoa.
    */
   public Image getGenoaTile() {
      return genoaTile;
   }

   /**
    * returns an image for the venice port tile
    *
    * @return graphic for venice.
    */
   public Image getVeniceTile() {
      return veniceTile;
   }

   /**
    * returns an image for the amsterdam port tile
    *
    * @return graphic for amersterdam.
    */
   public Image getAmsterdamTile() {
      return amsterdamTile;
   }

   /**
    * returns an image for the cliff creek tile
    *
    * @return graphic for cliff creek.
    */
   public Image getCliffCreekTile() {
      return cliffCreekTile;
   }

   /**
    * returns the image for the anchor bay tile
    *
    * @return graphic for anchor bay.
    */
   public Image getAnchorBayTile() {
      return anchorBayTile;
   }

   /**
    * returns the image for the mud bay tile
    *
    * @return graphic for mud bay.
    */
   public Image getMudBayTile() {
      return mudBayTile;
   }

   /**
    * returns an image of the triangle to rotate the ships
    *
    * @return graphic for the triangle icon to rotate ships.
    */
   public Image getRotateTriangle() {
      return rotateTriangle;
   }

   /**
    * returns an image of a stack of crew cards
    *
    * @return graphic for a stack of crew cards.
    */
   public Image getCrewCardStack() {
      return crewCardStack;
   }

   /**
    * returns an image of a stack of chance cards
    *
    * @return graphic for a stack of chance cards.
    */
   public Image getChanceCardStack() {
      return chanceCardStack;
   }

   // Private Methods

   /**
    * Retrieves the Treasure Island tiles into a 4x4 array of Images.
    *
    * @return A two dimensional array of images x by y.
    */
   private Image[][] loadTreasureIslandTiles() {
      return loadIslandTiles("TreasureIsland", 4, 4);
   }

   /**
    * Retrieves the Flat Island tiles into a 3x4 array of Images.
    *
    * @return A two dimensional array of images x by y.
    */
   private Image[][] loadFlatIslandTiles() {
      return loadIslandTiles("FlatIsland", 3, 4);
   }

   /**
    * Retrieves the Pirate Island tiles into a 3x4 array of Images.
    *
    * @return A two dimensional array of images x by y.
    */
   private Image[][] loadPirateIslandTiles() {
      return loadIslandTiles("PirateIsland", 3, 4);
   }

   /**
    * Retrieves the Cursor image for an in focus cursor.
    *
    * @return The image associated with a cursor which is in focus.
    */
   public Image getFocusCursor() {
      return focusCursor;
   }

   /**
    * Retrieves the Cursor image for an in default cursor.
    *
    * @return The image associated with a default cursor.
    */
   public Image getDefaultCursor() {
      return defaultCursor;
   }

   /**
    * Retrieves the Cursor image for an in broken cursor.
    *
    * @return The image associated with a cursor which is broken.
    */
   public Image getBrokenCursor() {
      return brokenCursor;
   }

   /**
    * Loads island images into a two dimensional array using a given island prefix e.g FlatIsland and the
    * width and height.
    *
    * @param islandPrefix The prefix of the Island image files.
    * @param width        The number of different images going across. e.g FlatIsland_y-x.png
    * @param height       The number of different images going down. e.g FlatIsland_y-x.png
    * @return The two dimensional array of images of size width by height.
    */
   private Image[][] loadIslandTiles(String islandPrefix, int width, int height) {
      Image[][] tiles = new Image[width][height];
      String islandPath = "/graphics/sprites/islands/";
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            tiles[x][y] = new Image(islandPath + islandPrefix + "_" + y + "-" + x + ".png");
         }
      }
      return tiles;
   }
}
