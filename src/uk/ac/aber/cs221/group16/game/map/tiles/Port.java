/*
* @(#) Port.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.map.tiles;

import javafx.scene.image.Image;
import uk.ac.aber.cs221.group16.game.GraphicLoader;

/**
 * An Enum to represent the names of Ports.
 *
 * @see PortTile
 */
public enum Port {
   London,
   Genoa,
   Marseilles,
   Cadiz,
   Venice,
   Amsterdam;
   //Public methods

   /**
    * Gets the image associated with the specific Port.
    *
    * @return The image relevant to the port.
    */
   public Image getImage() {
      GraphicLoader gLoader = GraphicLoader.getInstance();
      switch (this) {
         case London:
            return gLoader.getLondonTile();
         case Venice:
            return gLoader.getVeniceTile();
         case Cadiz:
            return gLoader.getCadizTile();
         case Marseilles:
            return gLoader.getMarseillesTile();
         case Amsterdam:
            return gLoader.getAmsterdamTile();
         case Genoa:
            return gLoader.getGenoaTile();
      }
      return null;
   }
}
