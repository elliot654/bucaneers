/*
* @(#) TradeWindow.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.windows;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.aber.cs221.group16.game.Controller;
import uk.ac.aber.cs221.group16.game.GraphicLoader;
import uk.ac.aber.cs221.group16.game.items.Tradable;
import uk.ac.aber.cs221.group16.game.items.treasures.Treasure;
import uk.ac.aber.cs221.group16.game.map.tiles.PortTile;
import uk.ac.aber.cs221.group16.game.players.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides the trading interface
 *
 * @author Kamyab Sherafat
 * @author Josh Smith
 */
public class TradeWindow {

   // Instance variables
   private Player player;
   private PortTile port;
   private Stage window;
   private Label errorLabel;

   private ArrayList<Tradable> playerTradables;
   private ArrayList<Tradable> portTradables;

   private IntegerProperty playerSum = new SimpleIntegerProperty(0);
   private IntegerProperty portSum = new SimpleIntegerProperty(0);

   // Constructors

   /**
    * The primary constructor for the TradeWindow class, when provided with a Player and ports, it sets up a popup
    * window to handle the trading and shows it to the user.
    *
    * @param player The player who has arrived in the port.
    * @param port   The port which the player wishes to trade with.
    */
   public TradeWindow(Player player, PortTile port) {
      this.player = player;
      this.port = port;

      initWindow();
   }

   // Public Methods

   public void showAndWait() {
      window.showAndWait();
   }

   // Private Methods

   private void initWindow() {
      Label topTxt = new Label(player.getName() + "  <-->  " + port.getName());
      HBox top = new HBox(topTxt);
      top.setAlignment(Pos.CENTER);

      // Layout setup
      VBox right = new VBox();
      right.getStyleClass().add("right");
      VBox left = new VBox();
      left.getStyleClass().add("left");
      ScrollPane rightScroll = new ScrollPane(right);
      rightScroll.setFitToHeight(true);
      rightScroll.setMinWidth(125);
      rightScroll.setMinHeight(275);
      ScrollPane leftScroll = new ScrollPane(left);
      leftScroll.setFitToHeight(true);
      leftScroll.setMinWidth(125);
      leftScroll.setMinHeight(275);
      HBox middle = new HBox(leftScroll, rightScroll);
      middle.setAlignment(Pos.CENTER);

      // add all of the cards and treasures into lists of tradables.
      playerTradables = new ArrayList<>(player.getShip().getCargo());
      playerTradables.addAll(player.getCrewCards());
      // adds all chance cards which have a value.
      playerTradables.addAll(player.getChanceCards().stream().filter(c -> c.getValue() > 0).collect(Collectors.toList()));

      portTradables = new ArrayList<>(port.getTreasureSet());
      portTradables.addAll(port.getCrewCardSet());

      // Sort the Lists
      playerTradables.sort(Comparator.reverseOrder());
      portTradables.sort(Comparator.reverseOrder());

      // labels for total, binding them to the sum properties.
      Label playerSumLbl = new Label();
      playerSumLbl.textProperty().bind(playerSum.asString());
      playerSumLbl.setAlignment(Pos.CENTER_LEFT);
      Label portSumLbl = new Label();
      portSumLbl.textProperty().bind(portSum.asString());
      portSumLbl.setAlignment(Pos.CENTER_LEFT);

      // Create the buttons.
      Button accept = new Button("Accept");
      accept.setOnAction(e -> onAccept(left.getChildren(), right.getChildren()));
      Button cancel = new Button("Cancel");
      cancel.setOnAction(event -> window.close());

      // add the items as checkboxes into their appropriate panes. Adding events for when toggled to update the labels.
      addItems(left, playerTradables, playerSum, accept);
      addItems(right, portTradables, portSum, accept);

      // putting it all together
      BorderPane bottom = new BorderPane();
      HBox buttons = new HBox(accept, cancel);
      buttons.setAlignment(Pos.CENTER);
      bottom.setCenter(buttons);
      bottom.setLeft(playerSumLbl);
      bottom.setRight(portSumLbl);

      this.errorLabel = new Label("");
      this.errorLabel.setVisible(false);
      bottom.setBottom(this.errorLabel);

      VBox wrapper = new VBox(top, middle, bottom);
      wrapper.getStyleClass().add("wrapper");
      wrapper.setPadding(new Insets(20));

      window = new Stage(StageStyle.UNDECORATED);
      Scene scene = new Scene(wrapper);
      scene.getStylesheets().add("css/trade_window.css");
      scene.setCursor(new ImageCursor(GraphicLoader.getInstance().getDefaultCursor()));
      window.setScene(scene);
   }


   /**
    * Adds the items from the passed tradables list into the VBox as checkboxes, with actions so that when toggled the
    * passed integer property updates appropriately.
    *
    * @param pane      The pane to add the checkboxes to.
    * @param tradables The list of Tradable items.
    * @param sum       The Integer Property to update when the checkboxes are toggled.
    * @param acceptBtn The button which should be disabled if the values do not match.
    */
   private void addItems(VBox pane, ArrayList<Tradable> tradables, IntegerProperty sum, Button acceptBtn) {
      for (Tradable tradable : tradables) {
         CheckBox checkBox = new CheckBox(tradable.getType() + "(" + tradable.getValue() + ")");
         checkBox.setOnAction((ActionEvent action) -> {
            if (checkBox.isSelected()) {
               sum.set(sum.getValue() + tradable.getValue());
            } else {
               sum.set(sum.getValue() - tradable.getValue());
            }
            if (playerSum.get() != portSum.get())
               acceptBtn.setDisable(true);
            else
               acceptBtn.setDisable(false);
         });
         pane.getChildren().add(checkBox);
      }
   }

   /**
    * Handles the process of a trade.
    *
    * @param playerCheckBoxes The List of checkboxes of the player items.
    * @param portCheckBoxes   The List of checkboxes of the port items.
    */
   private void onAccept(List<Node> playerCheckBoxes, List<Node> portCheckBoxes) {
      // Trade sums are equal.
      if (playerSum.get() == portSum.get()) {
         // associate the selected checkboxes with the tradables.
         HashSet<Tradable> tradedFromPlayer = getSelected(playerCheckBoxes, playerTradables);
         HashSet<Tradable> tradedFromPort = getSelected(portCheckBoxes, portTradables);

         // if the player has the capacity to support the outcome of the trade.
         if (checkSpaceForTreasure(tradedFromPlayer, tradedFromPort)) {
            this.errorLabel.setVisible(false);

            // trade the items betweeen the players
            tradedFromPlayer.forEach(item -> {
               port.addTradable(item);
               player.removeTradable(item);
            });
            tradedFromPort.forEach(item -> {
               player.addTradable(item);
               port.removeTradable(item);
            });
            window.setOpacity(0);
            if (tradedFromPlayer.size() > 0)
               new GenericMsg(Controller.stage, "Trade completed successfully!");
            window.close();
         } else {
            this.errorLabel.setText("Invalid Trade! Ship can only hold " +
                    this.player.getShip().getCapacity() + " items of Treasure.");
            this.errorLabel.setTextFill(Color.RED);
            this.errorLabel.setWrapText(true);
            this.errorLabel.setVisible(true);
         }
      }
   }

   /**
    * Checks the two sets of tradables to ensure that the outcome of the trade
    * will not cause the number of Treasures on the ship to exceed it's
    * capacity.
    *
    * @param tradedFromPlayer Set of Tradables to go from the player to the port.
    * @param tradedFromPort   Set of Tradables to go from the port to the player.
    * @return True if the trade outcome does not cause the ship to have more
    * treasures than it's capacity allows.
    */
   private boolean checkSpaceForTreasure(HashSet<Tradable> tradedFromPlayer,
                                         HashSet<Tradable> tradedFromPort) {
      int playerOutcomeTreasures = 0;
      playerOutcomeTreasures = this.player.getShip().getCargo().size();
      // subtract the number of treasures that are being sent to the port.
      playerOutcomeTreasures -= tradedFromPlayer.stream()
              .filter(t -> t instanceof Treasure).count();
      // add the number of treasures that are being taken from the port.
      playerOutcomeTreasures += tradedFromPort.stream()
              .filter(t -> t instanceof Treasure).count();

      return playerOutcomeTreasures <= player.getShip().getCapacity();
   }

   /**
    * Pairs the checkboxes with their appropriate tradable items and returns a set of tradables for the seleted checkboxes
    *
    * @param checkBoxes The checkboxes which were initially set up from the tradable list also passed.
    * @param tradables  The List of tradable items which were initially used to set up the checkboxes passed into this
    *                   method.
    * @return A set of Tradable items based on the selected CheckBoxes.
    */
   private HashSet<Tradable> getSelected(List<Node> checkBoxes, List<Tradable> tradables) {
      HashSet<Tradable> tradedItems = new HashSet<>();
      for (int i = 0; i < checkBoxes.size(); i++) {
         CheckBox cBox = (CheckBox) checkBoxes.get(i);
         if (cBox.isSelected()) {
            tradedItems.add(tradables.get(i));
         }
      }
      return tradedItems;
   }
}


