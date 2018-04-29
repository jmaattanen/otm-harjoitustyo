package jm.transcribebuddy.gui.popups;

/***   Project information setup window    ***/

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jm.transcribebuddy.logics.MainController;
import jm.transcribebuddy.logics.storage.ProjectInfo;

public class ProjectForm {
    
    public static ProjectInfo show(final MainController mainController) {
        mainController.stopAudio();
        final int maxInputFieldWidth = 250;
        
        final ProjectInfo projectInfo = mainController.getProjectInfo();
        
        final Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Project information");
        window.setMinWidth(400);
        window.setMinHeight(300);
        
        Label projectNameHeader = new Label();
        Label projectDescriptionHeader = new Label();
        Label textFileHeader = new Label();
        Label audioFileHeader = new Label();
        
        projectNameHeader.setText("Project name:");
        projectDescriptionHeader.setText("Description:");
        textFileHeader.setText("TXT File path:");
        audioFileHeader.setText("Audio file path:");
        
        final TextField projectNameField = new TextField();
        final TextArea projectDescriptionArea = new TextArea();
        final Label textFileLabel = new Label();
        final Label audioFileLabel = new Label();
        
        projectNameField.setText(projectInfo.getName());
        projectDescriptionArea.setText(projectInfo.getDescription());
        textFileLabel.setText(projectInfo.getTextFilePath());
        audioFileLabel.setText(projectInfo.getAudioFilePath());
        
        projectNameField.setMaxWidth(maxInputFieldWidth);
        projectDescriptionArea.setWrapText(true);
        projectDescriptionArea.setMaxSize(maxInputFieldWidth, 80);
        
        Button selectAudioButton = new Button("Select audio");
        selectAudioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String audioFileURI = GuiHelper.openAudioFileDialog(mainController);
                audioFileLabel.setText(audioFileURI);
            }
        });
        
        Button confirmButton = new Button("Confirm changes");
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String projectName = projectNameField.getText();
                projectInfo.setProjectName(projectName);
                String description = projectDescriptionArea.getText();
                projectInfo.setDescription(description);
                final String audioFileURI = audioFileLabel.getText();
                projectInfo.setAudioFilePath(audioFileURI);
                mainController.openAudioFile(audioFileURI);
                window.close();
            }
        });
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(20);
        
        grid.add(projectNameHeader, 0, 0);
        grid.add(projectNameField, 1, 0);
        grid.add(projectDescriptionHeader, 0, 1);
        grid.add(projectDescriptionArea, 1, 1);
        grid.add(textFileHeader, 0, 2);
        grid.add(textFileLabel, 1, 2);
        grid.add(audioFileHeader, 0, 3);
        grid.add(audioFileLabel, 1, 3);
        grid.add(selectAudioButton, 0, 4);
        grid.add(confirmButton, 1, 4);
        
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.showAndWait();
        
        return projectInfo;
    }
    
}
