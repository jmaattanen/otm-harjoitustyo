package jm.transcribebuddy.gui.popups;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jm.transcribebuddy.logics.Classifier;
import jm.transcribebuddy.logics.storage.Category;

public class CategoryForm {
    
    
    public static void show(final Classifier classifier, final Category category) {
        final int maxInputFieldWidth = 250;
        
        
        final Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit category");
        window.setMinWidth(400);
        window.setMinHeight(300);
        
        Label categoryNameHeader = new Label();
        Label parentNameHeader = new Label();
        
        categoryNameHeader.setText("Category name:");
        parentNameHeader.setText("Belongs to:");
        
        final TextField categoryNameField = new TextField();
        final TextField parentNameField = new TextField();
        
        categoryNameField.setText(category.toString());
        
        Category parent = category.getParent();
        String parentName = parent.toString();
        if (classifier.isRoot(parent)) {
            parentName = "";
        }
        parentNameField.setText(parentName);
        
        categoryNameField.setMaxWidth(maxInputFieldWidth);
        
        Button confirmButton = new Button("Confirm changes");
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String newName = categoryNameField.getText();
                // Check if isn't unique!!
                Category subcategory = classifier.getSubcategory(newName);
                if (classifier.isRealCategory(subcategory)) {
                    // Combine categories
                } else {
                    category.rename(newName);
                }
                String newParentName = parentNameField.getText();
                classifier.addHeadcategory(newParentName, category);
                window.close();
            }
        });
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(20);
        
        grid.add(categoryNameHeader, 0, 0);
        grid.add(categoryNameField, 1, 0);
        grid.add(parentNameHeader, 0, 1);
        grid.add(parentNameField, 1, 1);
        grid.add(confirmButton, 1, 2);
        
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.showAndWait();
        
    }

}
