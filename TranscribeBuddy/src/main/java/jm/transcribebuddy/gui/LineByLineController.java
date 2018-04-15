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

public class LineByLineController implements Initializable {
    
    static private MainController mainController;
    
    @FXML
    private Label audioNameLabel;
    
    @FXML
    private TextArea workArea, prevArea, nextArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void setUpTextAreas() {
        prevArea.setText(mainController.getPrevStatement());
        nextArea.setText(mainController.getNextStatement());
        final String statement = mainController.getCurrentStatement();
        workArea.setText(statement);
        workArea.positionCaret(statement.length());
        workArea.requestFocus();
    }
    
    public void setUpController(final Stage stage, MainController controller) {
        mainController = controller;
        Scene scene = stage.getScene();
        
        // set up hotkeys
        scene.setOnKeyPressed( new EventHandler<KeyEvent>() {
            
            @Override
            public void handle( KeyEvent keyEvent) {
                if (keyEvent.isControlDown()) {
                    if (null != keyEvent.getCode()) switch (keyEvent.getCode()) {
                        case COMMA:
                            try {
                                switchToCTS(stage);
                            } catch (IOException ex) {
                                Logger.getLogger(LineByLineController.class.getName()).log(Level.SEVERE, null, ex);
                            }   break;
                        case O:
                            openAudioFile(null);
                            break;
                        case PAGE_UP:
                            selectPrevStatement();
                            break;
                        case PAGE_DOWN:
                            selectNextStatement();
                            break;
                        case ENTER:
                            endStatement();
                            break;
                        case SPACE:
                            if (keyEvent.isShiftDown()) {
                                // [ctrl+shift+space] starts playing from last mark
                                mainController.seekBeginningOfCurrentStatement();
                                mainController.playAudio();
                            } else mainController.changePlayingStatus();
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
        setUpTextAreas();
        prevArea.setEditable(false);
        nextArea.setEditable(false);
    }
    
    // switch to ConstantText scene
    private void switchToCTS(final Stage stage) throws IOException {
        // save current text to TextBuilder
        String statement = workArea.getText();
        mainController.setCurrentStatement(statement);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent constantTextParent = fxmlLoader.load(getClass().getResource("/fxml/ConstantText.fxml").openStream());
        Scene constantTextScene = new Scene(constantTextParent);
        constantTextScene.getStylesheets().add("/styles/Styles.css");
        
        stage.setScene(constantTextScene);
        stage.show();
        ConstantTextController fxmlController = (ConstantTextController)fxmlLoader.getController();
        fxmlController.setUpController(stage, mainController);
    }
    
    @FXML
    private void switchToCTS(ActionEvent event) throws IOException {
        final Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        switchToCTS(stage);
    }

    @FXML
    private void openAudioFile(ActionEvent event) {
        if (mainController.openAudioFile()) {
            audioNameLabel.setText(mainController.getAudioFilePath());
            setUpTextAreas();
        }
    }
    
    @FXML
    private void deleteStatement() {
        mainController.deleteCurrentStatement();
        setUpTextAreas();
    }
    
    @FXML
    private void splitStatement(ActionEvent event) {
        String statement = workArea.getText();
        int index = workArea.getCaretPosition();
        mainController.splitStatement(statement, index);
        setUpTextAreas();
    }
    
    @FXML
    private void selectPrevStatement() {
        String statement = workArea.getText();
        mainController.setCurrentStatement(statement);
        mainController.selectPrevStatement();
        setUpTextAreas();
    }
    
    @FXML
    private void selectNextStatement() {
        String statement = workArea.getText();
        mainController.setCurrentStatement(statement);
        mainController.selectNextStatement();
        setUpTextAreas();
    }
    
    @FXML
    private void endStatement() {
        String statement = workArea.getText();
        mainController.endStatement(statement);
        setUpTextAreas();
    }
    
    @FXML
    private void seekBeginning(ActionEvent event) {
        mainController.seekBeginningOfCurrentStatement();
    }
    
    @FXML
    private void playAudio(ActionEvent event) {
        mainController.playAudio();
    }
    
    @FXML
    private void stopAudio(ActionEvent event) {
        mainController.stopAudio();
    }
    
    @FXML
    private void skipBackward(ActionEvent event) {
        mainController.skipBackward();
    }
    
    @FXML
    private void skipForward(ActionEvent event) {
        mainController.skipForward();
    }
}
