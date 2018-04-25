package jm.transcribebuddy.gui;

/***   FXML controller for constant text view alias tekstinäkymä   ***/

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jm.transcribebuddy.logics.MainController;

public class ConstantTextController implements Initializable {
    
    private MainController mainController;
    
    @FXML
    private Label projectNameLabel, audioNameLabel;
    
    @FXML
    private TextArea workArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void setUpController(final Stage stage, MainController controller) {
        mainController = controller;
        Scene scene = stage.getScene();
        // retain the maximized stage
        if (stage.isMaximized()) {
            stage.hide();
            stage.setMaximized(true);
            stage.show();
        }
        
        // set up labels and text areas
        projectNameLabel.setText(mainController.getProjectName());
        audioNameLabel.setText(mainController.getAudioFilePath());
        String text = mainController.getFullText();
        workArea.setText(text);
        workArea.positionCaret(text.length());
        workArea.requestFocus();
        
        // set up hotkeys
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            
            @Override
            public void handle( KeyEvent keyEvent) {
                if( keyEvent.isControlDown() ) {
                    if( null != keyEvent.getCode() ) switch (keyEvent.getCode()) {
                        case PERIOD:
                            try {
                                switchToLBLS(stage);
                            } catch (IOException ex) {
                                Logger.getLogger(ConstantTextController.class.getName()).log(Level.SEVERE, null, ex);
                            }   break;
                        case SPACE:
                            mainController.changePlayingStatus();
                            break;
                        case B:
                            if (keyEvent.isShiftDown()) {
                                // [ctrl+shift+B] uses longer skip time 
                                mainController.skipBackwardLonger();
                            } else mainController.skipBackward();
                            break;
                        case N:
                            if (keyEvent.isShiftDown()) {
                                // [ctrl+shift+N] uses longer skip time 
                                mainController.skipForwardLonger();
                            } else mainController.skipForward();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
    
    // Switch to LineByLine scene
    private void switchToLBLS(final Stage stage) throws IOException {
        // save current work
        String text = workArea.getText();
        if (mainController.parseLastStatement(text) < 0) {
            // report an error
        } else {
            int index = workArea.getCaretPosition();
            mainController.selectStatementByCaretPosition(index);
        }
        
        // Load new scene
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent lineByLineParent = fxmlLoader.load(getClass().getResource("/fxml/LineByLine.fxml").openStream());
        Scene lineByLineScene = new Scene(lineByLineParent);
        lineByLineScene.getStylesheets().add("/styles/Styles.css");
        
        stage.setScene(lineByLineScene);
        stage.show();
        LineByLineController fxmlController = (LineByLineController)fxmlLoader.getController();
        fxmlController.setUpController(stage, mainController);
    }
    
    @FXML
    private void switchToLBLS(ActionEvent event) throws IOException {
        final Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        switchToLBLS(stage);
    }
    
    @FXML
    private void openAudioFile(ActionEvent event) {
        boolean confirmOpen = true;
        if (mainController.isWorkSaved() == false) {
            confirmOpen = AlertBox.confirmAction(
                    "Warning",
                    "Your work has not been saved.",
                    "Do you want to start a new project anyway?"
            );
        }
        if (confirmOpen) {
            String audioFileURI = GuiHelper.openAudioFileDialog(mainController);
            if (mainController.openAudioFile(audioFileURI)) {
                audioNameLabel.setText(mainController.getAudioFilePath());
                workArea.setText("");
            }
        }
        workArea.requestFocus();
    }
    
    @FXML
    private void openFile(ActionEvent event) {
        boolean confirmOpen = true;
        if (mainController.isWorkSaved() == false) {
            confirmOpen = AlertBox.confirmAction(
                    "Warning",
                    "Your work has not been saved.",
                    "Do you want to open another project anyway?"
            );
        }
        if (confirmOpen) {
            String textFilePath = GuiHelper.openTextFileDialog(mainController);
            mainController.loadProject(textFilePath);
            projectNameLabel.setText(mainController.getProjectName());
            audioNameLabel.setText(mainController.getAudioFilePath());
            String text = mainController.getFullText();
            workArea.setText(text);
            workArea.positionCaret(text.length());
        }
        workArea.requestFocus();
    }
    
    @FXML
    private void saveToFile(ActionEvent event) {
        // save current work
        String text = workArea.getText();
        if (mainController.parseLastStatement(text) < 0) {
            // report an error
        }
        String textFilePath = GuiHelper.saveTextFileDialog(mainController);
        if (mainController.saveProject(textFilePath) == false) {
            String daoError = mainController.getDaoError();
            while (daoError != null) {
                AlertBox.showWarning("Database error.", daoError);
                daoError = mainController.getDaoError();
            }
        }
        workArea.requestFocus();
    }
    
    @FXML
    private void editProjectInfo(ActionEvent event) {
        mainController.editProjectInfo();
        projectNameLabel.setText(mainController.getProjectName());
        workArea.requestFocus();
    }
    
    @FXML
    private void seekBeginning(ActionEvent event) {
        mainController.seekBeginningOfAudioTrack();
        workArea.requestFocus();
    }
    
    @FXML
    private void playAudio(ActionEvent event) {
        mainController.playAudio();
        workArea.requestFocus();
    }
    
    @FXML
    private void stopAudio(ActionEvent event) {
        mainController.stopAudio();
        workArea.requestFocus();
    }
    
    @FXML
    private void skipBackward(ActionEvent event) {
        mainController.skipBackward();
        workArea.requestFocus();
    }
    
    @FXML
    private void skipForward(ActionEvent event) {
        mainController.skipForward();
        workArea.requestFocus();
    }
    
}
