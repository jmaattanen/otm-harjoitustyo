package jm.transcribebuddy.gui;

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
    
    static private MainController mainController;
    
    @FXML
    private Label audioNameLabel;
    
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
                        case O:
                            openAudioFile(null);
                            break;
                        case SPACE:
                            mainController.changePlayingStatus();
                            break;
                        case B:
                            mainController.skipBackward();
                            break;
                        case N:
                            mainController.skipForward();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        
        // set up audio file label and text areas
        audioNameLabel.setText(mainController.getAudioFilePath());
        String text = mainController.getFullText();
        workArea.setText(text);
        workArea.positionCaret(text.length());
        workArea.requestFocus();
    }
    
    // Switch to LineByLine scene
    private void switchToLBLS(final Stage stage) throws IOException {
        // save current work to TextBuilder
        String text = workArea.getText();
        mainController.parseLastStatement(text);
        
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
        if (mainController.openAudioFile()) {
            audioNameLabel.setText(mainController.getAudioFilePath());
            workArea.setText("");
        }
    }
    
    @FXML
    private void openFile(ActionEvent event) {
        mainController.loadProject();
        String text = mainController.getFullText();
        workArea.setText(text);
        workArea.positionCaret(text.length());
        workArea.requestFocus();
    }
    
    @FXML
    private void saveToFile(ActionEvent event) {
        mainController.saveProject();
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
