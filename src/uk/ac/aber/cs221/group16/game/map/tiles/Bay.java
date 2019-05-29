/*
* @(#) Bay.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.map.tiles;

import javafx.scene.image.Image;
import uk.ac.aber.cs221.group16.game.GraphicLoader;

/**
 * An Enum to represent the names of Bays.
 *
 * @author Josh Smith
 * @version 1.0
 */
public enum Bay {
   Cliff_Creek,
   Anchor_Bay,
   Mud_Bay;
   //Public methods

   /**
    * Gets the image associated with the Bay.
    *
    * @return The image associated with the enum value.
    */
   public Image getImage() {
      GraphicLoader gLoader = GraphicLoader.getInstance();
      switch (this) {
         case Anchor_Bay:
            return gLoader.getAnchorBayTile();
         case Cliff_Creek:
            return gLoader.getCliffCreekTile();
         case Mud_Bay:
            return gLoader.getMudBayTile();
      }
      return null;
   }
}
