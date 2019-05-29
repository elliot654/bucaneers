/*
* @(#) RumTreasure.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.items.treasures;

import javafx.scene.image.Image;

/**
 * @author Josh Smith
 * @version 1.0
 */
public class RumTreasure
        implements Treasure {

   // Constructors
   public RumTreasure() {
   }

   ;
   // Public Methods

   @Override
   public Image getImage() {
      return null;
   }

   @Override
   public int getValue() {
      return 2;
   }

   @Override
   public String getType() {
      return "Barrel of Rum";
   }
}
