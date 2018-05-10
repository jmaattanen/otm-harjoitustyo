package jm.transcribebuddy.gui;

/***   FXML controller for line by line view alias rivinäkymä   ***/

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jm.transcribebuddy.gui.popups.*;
import jm.transcribebuddy.logics.MainController;
import jm.transcribebuddy.logics.storage.ProjectInfo;

public class LineByLineController implements Initializable {
    
    private MainController mainController;
    
    @FXML
    private Label projectNameLabel, audioNameLabel;
    
    @FXML
    private TextField startTimeField, subcategoryField;
    
    @FXML
    private TextArea workArea, prevArea, nextArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void setUpTextAreas() {
        startTimeField.setText(mainController.startTimeToString());
        prevArea.setText(mainController.getPrevStatement());
        nextArea.setText(mainController.getNextStatement());
        String subcategory = mainController.getCurrentSubcategory();
        subcategoryField.setText(subcategory);
        String statement = mainController.getCurrentStatement();
        workArea.setText(statement);
        workArea.positionCaret(statement.length());
        workArea.requestFocus();
    }
    
    public void setUpController(final Stage stage, MainController controller) {
        mainController = controller;
        Scene scene = stage.getScene();
        
        // set up labels and text areas
        projectNameLabel.setText(mainController.getProjectName());
        audioNameLabel.setText(mainController.getAudioFileURI());
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
                            } catch (IOException ex) { }
                            break;
                        case MINUS:
                            try {
                                switchToOS(stage);
                            } catch (IOException ex) { }
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
                } else if (keyEvent.getCode() == KeyCode.ENTER) {
                    workArea.requestFocus();
                }
            }
        });
    }
    
    
    private void updateWork() {
        String subcategory = subcategoryField.getText();
        mainController.setSubcategory(subcategory);
        String statement = workArea.getText();
        mainController.set(statement);
    }
    
    // Switch to ConstantText scene
    private void switchToCTS(final Stage stage) throws IOException {
        // Save current work to TextBuilder
        updateWork();
        
        // Load new scene
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent constantTextParent = fxmlLoader.load(getClass().getResource("/fxml/ConstantText.fxml").openStream());
        Scene constantTextScene = new Scene(constantTextParent);
        constantTextScene.getStylesheets().add("/styles/Feather.css");
        
        GuiHelper.setUpStage(stage, constantTextScene);
        
        ConstantTextController fxmlController = (ConstantTextController)fxmlLoader.getController();
        fxmlController.setUpController(stage, mainController);
    }
    
    @FXML
    private void switchToCTS(ActionEvent event) throws IOException {
        final Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        switchToCTS(stage);
    }
    
    // Switch to Overview scene
    private void switchToOS(final Stage stage) throws IOException {
        // Save current work to TextBuilder
        updateWork();
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent overviewParent = fxmlLoader.load(getClass().getResource("/fxml/Overview.fxml").openStream());
        Scene overviewScene = new Scene(overviewParent);
        overviewScene.getStylesheets().add("/styles/Feather.css");
        
        GuiHelper.setUpStage(stage, overviewScene);
        
        OverviewController fxmlController = (OverviewController)fxmlLoader.getController();
        fxmlController.setUpController(stage, mainController);
    }
    
    @FXML
    private void switchToOS(ActionEvent event) throws IOException {
        final Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        switchToOS(stage);
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
                mainController.cleanProject(audioFileURI);
                projectNameLabel.setText(mainController.getProjectName());
                audioNameLabel.setText(mainController.getAudioFileURI());
                setUpTextAreas();
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
            audioNameLabel.setText(mainController.getAudioFileURI());
            setUpTextAreas();
        }
        workArea.requestFocus();
    }
    
    @FXML
    private void saveToFile(ActionEvent event) {
        String subcategory = subcategoryField.getText();
        mainController.setSubcategory(subcategory);
        String statement = workArea.getText();
        mainController.set(statement);
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
        ProjectInfo projectInfo = ProjectForm.show(mainController);
        mainController.setProjectInfo(projectInfo);
        projectNameLabel.setText(mainController.getProjectName());
        audioNameLabel.setText(mainController.getAudioFileURI());
        workArea.requestFocus();
    }
    
    @FXML
    private void setStartTime(ActionEvent event) {
        mainController.setStartTime();
        startTimeField.setText(mainController.startTimeToString());
        workArea.requestFocus();
    }
    
    @FXML
    private void deleteStatement(ActionEvent event) {
        mainController.deleteCurrentStatement();
        setUpTextAreas();
    }
    
    @FXML
    private void splitStatement(ActionEvent event) {
        String subcategory = subcategoryField.getText();
        mainController.setSubcategory(subcategory);
        String statement = workArea.getText();
        int index = workArea.getCaretPosition();
        mainController.splitStatement(statement, index);
        setUpTextAreas();
    }
    
    @FXML
    private void selectPrevStatement() {
        String subcategory = subcategoryField.getText();
        mainController.setSubcategory(subcategory);
        String statement = workArea.getText();
        mainController.set(statement);
        mainController.selectPrevStatement();
        setUpTextAreas();
    }
    
    @FXML
    private void selectNextStatement() {
        String subcategory = subcategoryField.getText();
        mainController.setSubcategory(subcategory);
        String statement = workArea.getText();
        mainController.set(statement);
        mainController.selectNextStatement();
        setUpTextAreas();
    }
    
    @FXML
    private void endStatement() {
        String subcategory = subcategoryField.getText();
        mainController.setSubcategory(subcategory);
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
