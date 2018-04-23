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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jm.transcribebuddy.logics.MainController;

public class LineByLineController implements Initializable {
    
    private MainController mainController;
    
    @FXML
    private Label projectNameLabel, audioNameLabel;
    
    @FXML
    private TextField startTimeField;
    
    @FXML
    private TextArea workArea, prevArea, nextArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void setUpTextAreas() {
        startTimeField.setText(mainController.startTimeToString());
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
        // retain the maximized stage
        if (stage.isMaximized()) {
            stage.hide();
            stage.setMaximized(true);
            stage.show();
        }
        
        // set up labels and text areas
        projectNameLabel.setText(mainController.getProjectName());
        audioNameLabel.setText(mainController.getAudioFilePath());
        setUpTextAreas();
        prevArea.setEditable(false);
        nextArea.setEditable(false);
        
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
    
    // switch to ConstantText scene
    private void switchToCTS(final Stage stage) throws IOException {
        // save current text to TextBuilder
        String statement = workArea.getText();
        mainController.set(statement);
        
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
        boolean confirmOpen = true;
        if (mainController.isWorkSaved() == false) {
            confirmOpen = AlertBox.confirmAction(
                    "Warning",
                    "Your work has not been saved.",
                    "Do you want to start a new project anyway?"
            );
        }
        if (confirmOpen && mainController.openAudioFile()) {
            audioNameLabel.setText(mainController.getAudioFilePath());
            setUpTextAreas();
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
            mainController.loadProject();
            projectNameLabel.setText(mainController.getProjectName());
            setUpTextAreas();
        }
        workArea.requestFocus();
    }
    
    @FXML
    private void saveToFile(ActionEvent event) {
        String statement = workArea.getText();
        mainController.set(statement);
        if (mainController.saveProject() == false) {
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
    private void setStartTime(ActionEvent event) {
        mainController.setStartTime();
        startTimeField.setText(mainController.startTimeToString());
        //workArea.positionCaret(statement.length());
        workArea.requestFocus();
    }
    
    @FXML
    private void deleteStatement(ActionEvent event) {
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
        mainController.set(statement);
        mainController.selectPrevStatement();
        setUpTextAreas();
    }
    
    @FXML
    private void selectNextStatement() {
        String statement = workArea.getText();
        mainController.set(statement);
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
        mainController.playAudio();
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
