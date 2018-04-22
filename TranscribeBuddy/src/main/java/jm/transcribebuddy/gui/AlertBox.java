package jm.transcribebuddy.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    
    public static void showSimpleAlert(String title, String message) {
        final Stage alertBox = new Stage();
        alertBox.initModality(Modality.APPLICATION_MODAL);
        alertBox.setTitle(title);
        alertBox.setMinWidth(400);
        alertBox.setMinHeight(150);
        Label messageLabel = new Label();
        messageLabel.setText(message);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                alertBox.close();
            }
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(messageLabel, closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        alertBox.setScene(scene);
        alertBox.showAndWait();
    }
    
}
