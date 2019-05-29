/*
* @(#) Help.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.windows;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uk.ac.aber.cs221.group16.game.GraphicLoader;


/**
 * displays a help screen
 *
 * @author Kamyab Sherafat
 */
public class Help {

   int imageNum, i; // current image being displayed
   Stage window;
   Pane center;
   Button next, back;

   public Help(Stage owner) {
      window = new Stage();
      window.setTitle("Buccaneer Help");
      window.initOwner(owner);
      window.setResizable(false);
      imageNum = 1;
      display(owner);
   }

   private void display(Stage owner) {

      next = new Button();
      next.setGraphic(new ImageView(new Image("graphics/sprites/HelpArrowRight.png")));
      next.setOnMouseEntered(e -> {
         changeCursor(next, GraphicLoader.getInstance().getFocusCursor());
      });
      next.setOnMouseExited(e -> {
         changeCursor(next, GraphicLoader.getInstance().getDefaultCursor());
      });

      back = new Button();
      back.setGraphic(new ImageView(new Image("graphics/sprites/HelpArrowLeft.png")));
      back.setOnMouseEntered(e -> {
         changeCursor(back, GraphicLoader.getInstance().getFocusCursor());
      });
      back.setOnMouseExited(e -> {
         changeCursor(back, GraphicLoader.getInstance().getDefaultCursor());
      });

      center = new Pane();
      center.setStyle("-fx-background-image: url('/graphics/help(1).PNG');"
              + "-fx-background-repeat: no-repeat;" +
              "-fx-background-size: 100% 100%;");

      BorderPane wrapper = new BorderPane();
      wrapper.setPrefWidth(Screen.getPrimary().getBounds().getWidth() * (0.7));
      wrapper.setPrefHeight(Screen.getPrimary().getBounds().getHeight() * (0.7));
      wrapper.setLeft(back);
      wrapper.setRight(next);
      wrapper.setCenter(center);
      wrapper.getStylesheets().add("/css/help_screen.css");

      for (int i = 0; i < 2; i++) {
         ((i > 0) ? back : next).prefHeightProperty().bind(wrapper.heightProperty());
      }

      back.setOnAction(e -> {
         if (imageNum > 1) {
            center.setStyle(String.format("-fx-background-image: url('/graphics/help(%d).PNG');" +
                    "-fx-background-size: 100%% 100%%;", --imageNum));
         }
      });

      next.setOnAction(e -> {
         if (imageNum < 8) {
            center.setStyle(String.format("-fx-background-image: url('/graphics/help(%d).PNG');" +
                    "-fx-background-size: 100%% 100%%;", ++imageNum));
         }
      });

      window.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
         if (KeyCode.ESCAPE == event.getCode()) {
            displayExitPopup(window);
         }
      });

      // on press of x in top corner.
      window.setOnCloseRequest((event) -> {
         displayExitPopup(window);
         // consume event so it doesn't propagate and close
         event.consume();
      });

      Scene scene = new Scene(wrapper);
      scene.setCursor(new ImageCursor(GraphicLoader.getInstance().getDefaultCursor()));
      window.setScene(scene);
      window.show();
   }

   /**
    * Displays an exit popup and if yes is pressed then it closes the stage.
    *
    * @param stage The stage instance.
    */
   private void displayExitPopup(Stage stage) {
      if (new Choice(stage, "Close help screen?", new String[]{"No", "Yes"}).getAnswer() == false) {
         stage.close();
      }
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
