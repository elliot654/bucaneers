package uk.ac.aber.cs221.group16.game;

import javafx.scene.media.AudioClip;

/**
 * This singleton provides a single point of loading for all sounds within the game. Sounds can be accessed with
 * SoundLoader.getInstance().get...
 *
 * @author Josh Smith
 * @version 1.0
 */
public class SoundLoader {

   // Static Variables
   private static SoundLoader instance;

   // Static Methods

   /**
    * Singleton function to get or create the instance of sound loader.
    *
    * @return The singleton instance of SoundLoader.
    */
   public static SoundLoader getInstance() {
      if (instance == null)
         instance = new SoundLoader();
      return instance;
   }

   // Instance Variables
   private AudioClip shipMoveSound;

   // Constructors

   /**
    * Default constructor, initialises the sounds.
    */
   private SoundLoader() {
      shipMoveSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
   }

   // Public Methods

   /**
    * Gets the instantiated AudioClip of the ship movement sound.
    *
    * @return An audio clip of the sound to be played when the ship moves.
    */
   public AudioClip getShipMoveSound() {
      return shipMoveSound;
   }
}
