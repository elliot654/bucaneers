/*
* @(#) TreasureFactory.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.items.treasures;

import java.util.LinkedList;

/**
 * This class intends to assist in building the set of treasures used in the game.
 *
 * @author Josh Smith
 * @version 1.0
 */
public class TreasureFactory {

   // Instance Variables
   private int numCopies;

   // Constructors

   /**
    * This basic constructor tells the factory how many copies of each treasure it should make when
    * {@link #genTreasureList()}) is called.
    *
    * @param numCopies Number of copies of each treasure that the factory will produce.
    */
   public TreasureFactory(int numCopies) {
      this.numCopies = numCopies;
   }

   // Public Methods

   /**
    * Gets a set of treasure to be used in the game. Uses the {@link #numCopies} variable to help generate the treasures.
    *
    * @return A list of Treasures fitting the requirements.
    */
   public LinkedList<Treasure> genTreasureList() {
      LinkedList<Treasure> treasures = new LinkedList<>();
      for (int i = 0; i < numCopies; i++) {
         treasures.add(makeDiamond());
         treasures.add(makeGold());
         treasures.add(makePearl());
         treasures.add(makeRuby());
         treasures.add(makeRum());
      }
      return treasures;
   }


   // Private Methods

   /**
    * Makes a Diamond Treasure.
    *
    * @return A Treasure with values representing a Diamond by the spec.
    */
   private Treasure makeDiamond() {
      return new DiamondTreasure();
   }

   /**
    * Makes a Ruby Treasure.
    *
    * @return A Treasure with values representing a Ruby by the spec.
    */
   private Treasure makeRuby() {
      return new RubyTreasure();
   }

   /**
    * Makes a Gold Treasure.
    *
    * @return A Treasure with values representing a Gold by the spec.
    */
   private Treasure makeGold() {
      return new GoldTreasure();
   }

   /**
    * Makes a Pearl Treasure.
    *
    * @return A Treasure with values representing a Pearl by the spec.
    */
   private Treasure makePearl() {
      return new PearlTreasure();
   }

   /**
    * Makes Rum Treasure.
    *
    * @return A Treasure with values representing Rum by the spec.
    */
   private Treasure makeRum() {
      return new RumTreasure();
   }
}
