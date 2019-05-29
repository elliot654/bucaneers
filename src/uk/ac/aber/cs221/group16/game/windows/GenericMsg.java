/*
* @(#) GenericMsg.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game.windows;

import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.aber.cs221.group16.game.GraphicLoader;


/**
 * This class allows for generic messages to be thrown up onto the UI with the {@link #display(Stage, String)} method.
 *
 * @author Kamyab Sherafat
 * @author Josh Smith
 */
public class GenericMsg {

   Stage window;

   /**
    * this class can be called to display a simple message
    *
    * @param owner the stage from which the class was called. this is
    *              needed in oder to keep the new window on top of the owner
    * @param msg   the message that is required to be displayed
    */
   public GenericMsg(Stage owner, String msg) {
      window = new Stage(StageStyle.UNDECORATED);
      window.initOwner(owner);
      window.initModality(Modality.APPLICATION_MODAL);
      display(owner, msg);
   }

   private void display(Stage owner, String msg) {
      Label message = new Label(msg);
      message.setWrapText(true);
      message.setTextAlignment(TextAlignment.CENTER);
      Button okButton = new Button("OK");
      okButton.requestFocus();
      okButton.defaultButtonProperty().bind(okButton.focusedProperty());

      VBox wrapper = new VBox(message, okButton);
      wrapper.setAlignment(Pos.CENTER);
      wrapper.setMinSize(wrapper.USE_COMPUTED_SIZE, wrapper.USE_COMPUTED_SIZE);
      wrapper.setMaxWidth(owner.getWidth() * 0.5);
      wrapper.getStylesheets().add("/css/generic_popup.css");

      // add cursor event to buttons
      okButton.setOnMouseEntered(e -> {
         changeCursor(okButton, GraphicLoader.getInstance().getFocusCursor());
      });
      okButton.setOnMouseExited(e -> {
         changeCursor(okButton, GraphicLoader.getInstance().getDefaultCursor());
      });

      Scene scene = new Scene(wrapper);
      scene.setCursor(new ImageCursor(GraphicLoader.getInstance().getDefaultCursor()));
      window.setScene(scene);

      okButton.setOnAction(e -> window.close());

      window.requestFocus();
      window.showAndWait();
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
