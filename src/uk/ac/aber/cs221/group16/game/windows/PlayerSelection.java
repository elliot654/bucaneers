package uk.ac.aber.cs221.group16.game.windows;

import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.aber.cs221.group16.game.GraphicLoader;
import uk.ac.aber.cs221.group16.game.map.Board;
import uk.ac.aber.cs221.group16.game.players.Player;

import java.util.ArrayList;

/**
 * displays a windows to select a player from a list of players
 *
 * @author Kamyab Sherfat
 */
public class PlayerSelection {

   private Stage stage;
   private ArrayList<Player> players;
   private Player selectedPlayer;

   /**
    * Constructor
    *
    * @param owner   owner of the window
    * @param board   the board to take players from
    * @param message message to display to the user
    */
   public PlayerSelection(Stage owner, Board board, String message) {
      stage = new Stage(StageStyle.UNDECORATED);
      stage.initOwner(owner);
      players = new ArrayList<>();
      removeCurrentPlayer(board);
      display(message);
   }

   public Player getSelectedPlayer() {
      return selectedPlayer;
   }

   /**
    * removes current player from the list of 4 players
    *
    * @param board
    */
   private void removeCurrentPlayer(Board board) {
      for (Player player : board.getPlayers()) {
         if (player != board.getCurrentPlayer()) {
            players.add(player);
         }
      }
   }

   /**
    * displays a window consisting of a message and 3 buttons with
    * the names of the players
    *
    * @param message message to display to the user
    */
   private void display(String message) {

      Label label = new Label(message);

      HBox buttonsWrapper = new HBox();

      // make a button for each player
      // set action for each button to set the selected player and close the window
      for (Player player : players) {
         Button button = new Button(player.getName());

         buttonsWrapper.getChildren().add(button);

         button.setOnAction(e -> {
            selectedPlayer = players.get(buttonsWrapper.getChildren().indexOf(button));
            stage.close();
         });
      }

      VBox vbox = new VBox(label, buttonsWrapper);

      Scene scene = new Scene(vbox);

      // cursor event to buttons
      for (Node node : buttonsWrapper.getChildren()) {
         if (node instanceof Button) {
            node.setOnMouseEntered(e -> {
               changeCursor((Button) node, GraphicLoader.getInstance().getFocusCursor());
            });
            node.setOnMouseExited(e -> {
               changeCursor((Button) node, GraphicLoader.getInstance().getDefaultCursor());
            });
         }
      }

      scene.getStylesheets().add("css/player_selection.css");
      scene.setCursor(new ImageCursor(
              GraphicLoader.getInstance().getDefaultCursor()));
      stage.setScene(scene);
      stage.showAndWait();

   }

   /**
    * changes the cursor of a button
    *
    * @param button to change the cursor for
    * @param image  the image to change the cursor to
    */
   private void changeCursor(Button button, Image image) {
      button.setCursor(new ImageCursor(image));
   }

}
