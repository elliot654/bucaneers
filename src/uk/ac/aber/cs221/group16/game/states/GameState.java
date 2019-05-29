/*
* @(#) GameState.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.states;

/**
 * This class stores the details regarding the current state of the game.
 *
 * @author Josh Smith
 * @version 1.2 Move and rotate features Beta.
 */
public class GameState {

   // Instance Variables
   private State currentState;
   private int turnNumber = 0;

   // Constructors

   /**
    * This default constructor sets the currentState to State.Move.
    */
   public GameState() {
      this.currentState = State.Move;
   }

   // Public Methods

   /**
    * This returns the currentState of the game.
    *
    * @return The currentState of the game.
    */
   public State getCurrentState() {
      return currentState;
   }

   /**
    * Sets the currentState of the game.
    *
    * @param currentState The State to set the currentState to.
    */
   public void setCurrentState(State currentState) {
      this.currentState = currentState;
   }

   /**
    * Gets the current turn number.
    *
    * @return An integer value representing the number of turns that have been taken so far.
    */
   public int getTurnNumber() {
      return turnNumber;
   }

   /**
    * Increments the turnNumber by 1.
    */
   public void incrementTurnNumber() {
      turnNumber++;
   }


   // Private Methods
}
