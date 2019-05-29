/*
* @(#) Choice.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.windows;

import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.aber.cs221.group16.game.GraphicLoader;


/**
 * @author Josh Smith
 * @author Kamyab Sherafat
 */
public class Choice {
   Stage window;
   boolean answer;

   public Choice(Stage owner, String msg, String[] answers) {
      window = new Stage(StageStyle.UNDECORATED);
      window.initModality(Modality.APPLICATION_MODAL);
      window.initOwner(owner);
      window.setResizable(false);
      window.setAlwaysOnTop(true);

      display(owner, msg, answers);
   }

   public boolean getAnswer() {
      return answer;
   }

   private void display(Stage owner, String msg, String[] answers) {

      GridPane grid = new GridPane();
      grid.getStylesheets().add("/css/generic_popup.css");
      grid.setMinSize(grid.USE_COMPUTED_SIZE, grid.USE_COMPUTED_SIZE);
      grid.setMaxWidth(owner.getWidth() * 0.5);

      Label label = new Label(msg);
      label.setWrapText(true);
      GridPane.setConstraints(label, 0, 0);

      Button leftButton = new Button(answers[0]);
      GridPane.setConstraints(leftButton, 0, 1);

      Button rightButton = new Button(answers[1]);
      GridPane.setConstraints(rightButton, 1, 1);

      grid.getChildren().addAll(label, leftButton, rightButton);

      leftButton.setOnAction(e -> yesAction(window));
      rightButton.setOnAction(e -> noAction(window));
      rightButton.requestFocus();

      rightButton.defaultButtonProperty().bind(rightButton.focusedProperty());
      leftButton.defaultButtonProperty().bind(leftButton.focusedProperty());

      // cursor event to buttons
      for (Node node : grid.getChildren()) {
         if (node instanceof Button) {
            node.setOnMouseEntered(e -> {
               changeCursor((Button) node, GraphicLoader.getInstance().getFocusCursor());
            });
            node.setOnMouseExited(e -> {
               changeCursor((Button) node, GraphicLoader.getInstance().getDefaultCursor());
            });
         }
      }

      Scene scene = new Scene(grid);
      scene.setCursor(new ImageCursor(GraphicLoader.getInstance().getDefaultCursor()));

      window.setScene(scene);

      window.showAndWait();
   }

   private void yesAction(Stage window) {
      answer = true;
      window.close();
   }

   private void noAction(Stage window) {
      answer = false;
      window.close();
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
