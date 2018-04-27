package jm.transcribebuddy.gui;

/***   FXML controller for overview alias hakunäkymä   ***/

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jm.transcribebuddy.gui.popups.*;
import jm.transcribebuddy.logics.Classifier;
import jm.transcribebuddy.logics.MainController;
import jm.transcribebuddy.logics.storage.Category;
import jm.transcribebuddy.logics.storage.ProjectInfo;

public class OverviewController implements Initializable {
    
    private MainController mainController;
    private Classifier classifier;
    
    @FXML
    private Label projectNameLabel, subcategorySizeLabel;
    
    @FXML
    private ChoiceBox subcategoryChoiceBox;
    
    @FXML
    private TableView statementsTableView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void setUpController(final Stage stage, MainController controller) {
        mainController = controller;
        classifier = mainController.getClassifier();
        Scene scene = stage.getScene();
        
        // Set up labels and choice box
        projectNameLabel.setText(mainController.getProjectName());
        ArrayList<Category> subcategories = classifier.getSubcategories();
        for (Category c : subcategories) {
            subcategoryChoiceBox.getItems().add(c);
        }
        
        // Set up hotkeys
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            
            @Override
            public void handle( KeyEvent keyEvent) {
                if( keyEvent.isControlDown() ) {
                    if( null != keyEvent.getCode() ) switch (keyEvent.getCode()) {
                        case COMMA:
                            try {
                                switchToCTS(stage);
                            } catch (IOException ex) { }
                            break;
                        case PERIOD:
                            try {
                                switchToLBLS(stage);
                            } catch (IOException ex) { }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
    
    // Switch to ConstantText scene
    private void switchToCTS(final Stage stage) throws IOException {
        
        // Load scene
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent constantTextParent = fxmlLoader.load(getClass().getResource("/fxml/ConstantText.fxml").openStream());
        Scene constantTextScene = new Scene(constantTextParent);
        constantTextScene.getStylesheets().add("/styles/Feather.css");
        
        final double width = stage.getWidth();
        final double height = stage.getHeight();
        
        stage.setScene(constantTextScene);
        stage.setWidth(width);
        stage.setHeight(height);
        // Next code just gets stage to refresh
        stage.setResizable(false);
        stage.setResizable(true);
        stage.show();
        
        ConstantTextController fxmlController = (ConstantTextController)fxmlLoader.getController();
        fxmlController.setUpController(stage, mainController);
    }
    
    @FXML
    private void switchToCTS(ActionEvent event) throws IOException {
        final Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        switchToCTS(stage);
    }

    // Switch to LineByLine scene
    private void switchToLBLS(final Stage stage) throws IOException {
        
        // Load new scene
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent lineByLineParent = fxmlLoader.load(getClass().getResource("/fxml/LineByLine.fxml").openStream());
        Scene lineByLineScene = new Scene(lineByLineParent);
        lineByLineScene.getStylesheets().add("/styles/Feather.css");
        
        final double width = stage.getWidth();
        final double height = stage.getHeight();
        
        stage.setScene(lineByLineScene);
        stage.setWidth(width);
        stage.setHeight(height);
        // Next code just gets stage to refresh
        stage.setResizable(false);
        stage.setResizable(true);
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
    private void editProjectInfo(ActionEvent event) {
        ProjectInfo projectInfo = ProjectForm.show(mainController);
        mainController.setProjectInfo(projectInfo);
        projectNameLabel.setText(mainController.getProjectName());
    }
    
    @FXML
    private void updateTable(ActionEvent event) {
        Category subcategory = (Category) subcategoryChoiceBox.getValue();
        if (subcategory != null) {
            subcategorySizeLabel.setText(Integer.toString(subcategory.getSize()));
        }
    }
}
