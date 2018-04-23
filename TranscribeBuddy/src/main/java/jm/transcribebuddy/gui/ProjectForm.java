package jm.transcribebuddy.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jm.transcribebuddy.logics.ProjectInfo;

public class ProjectForm {
    
    public static ProjectInfo show(final ProjectInfo projectInfo) {
        final Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Project information");
        window.setMinWidth(400);
        window.setMinHeight(300);
        
        Label projectNameHeader = new Label();
        Label projectDescriptionHeader = new Label();
        Label textFileHeader = new Label();
        Label audioFileHeader = new Label();
        
        final TextField projectNameField = new TextField();
        Label projectDescriptionLabel = new Label();
        Label textFileLabel = new Label();
        Label audioFileLabel = new Label();
        
        projectNameHeader.setText("Project name:");
        projectDescriptionHeader.setText("Description:");
        textFileHeader.setText("TXT File path:");
        audioFileHeader.setText("Audio file path:");
        
        projectNameField.setText(projectInfo.getName());
        projectDescriptionLabel.setText(projectInfo.getDescription());
        textFileLabel.setText(projectInfo.getTextFilePath());
        audioFileLabel.setText(projectInfo.getAudioFilePath());
        
        Button confirmButton = new Button("Confirm changes");
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String projectName = projectNameField.getText();
                projectInfo.setProjectName(projectName);
                window.close();
            }
        });
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(20);
        
        grid.add(projectNameHeader, 0, 0);
        grid.add(projectNameField, 1, 0);
        grid.add(projectDescriptionHeader, 0, 1);
        grid.add(projectDescriptionLabel, 1, 1);
        grid.add(textFileHeader, 0, 2);
        grid.add(textFileLabel, 1, 2);
        grid.add(audioFileHeader, 0, 3);
        grid.add(audioFileLabel, 1, 3);
        grid.add(confirmButton, 1, 4);
        
        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.showAndWait();
        
        return projectInfo;
    }
    
}
