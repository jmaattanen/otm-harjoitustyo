package jm.transcribebuddy.gui;

/***   FXML controller for overview alias hakunäkymä   ***/

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jm.transcribebuddy.gui.popups.*;
import jm.transcribebuddy.logics.Classifier;
import jm.transcribebuddy.logics.word.DetailedTextBuilder;
import jm.transcribebuddy.logics.MainController;
import jm.transcribebuddy.logics.storage.Category;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import jm.transcribebuddy.logics.storage.TableRow;

public class OverviewController implements Initializable {
    
    private MainController mainController;
    // NOTE TO MYSELF: create a new class for handling overview logics
    private Classifier classifier;
    private DetailedTextBuilder textBuilder;
    
    @FXML
    private Label projectNameLabel, subcategorySizeLabel;
    
    @FXML
    private ComboBox subCategoryComboBox, headCategoryComboBox;
    
    @FXML
    private TableView statementsTableView;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void setUpHeadcategories() {
        headCategoryComboBox.getItems().clear();
        ArrayList<Category> headcategories = classifier.getHeadcategories();
        Collections.sort(headcategories);
        for (Category c : headcategories) {
            headCategoryComboBox.getItems().add(c);
        }
    }
    
    private void setUpSubcategories() {
        subCategoryComboBox.getItems().clear();
        ArrayList<Category> subcategories = classifier.getSubcategories();
        Collections.sort(subcategories);
        for (Category c : subcategories) {
            subCategoryComboBox.getItems().add(c);
        }
    }
    
    public void setUpController(final Stage stage, MainController controller) {
        mainController = controller;
        classifier = mainController.getClassifier();
        textBuilder = mainController.getTextBuilder();
        Scene scene = stage.getScene();
        
        // Set up labels and combo box
        projectNameLabel.setText(mainController.getProjectName());
        setUpHeadcategories();
        setUpSubcategories();
//        ArrayList<Category> subcategories = classifier.getSubcategories();
//        Collections.sort(subcategories);
//        for (Category c : subcategories) {
//            subCategoryComboBox.getItems().add(c);
//        }
        
        // Update the table view every time the combo box value changes
        subCategoryComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateTable();
            }
        });
        
        // Set up hotkeys
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            
            @Override
            public void handle(KeyEvent keyEvent) {
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
        
        GuiHelper.setUpStage(stage, constantTextScene);
        
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
        
        GuiHelper.setUpStage(stage, lineByLineScene);
        
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
    
    private void updateTable() {
        final Category subcategory = (Category) subCategoryComboBox.getValue();
        if (subcategory == null) {
            return;
        }
        subcategorySizeLabel.setText(Integer.toString(subcategory.getSize()));
        ObservableList<TableRow> tableContent = statementsTableView.getItems();
        tableContent.clear();
        HashMap<Integer, String> statements = textBuilder.getStatementsIn(subcategory);
        final String categoryName = subcategory.toString();
        for (HashMap.Entry<Integer, String> entry : statements.entrySet()) {
            Integer index = entry.getKey();
            String statement = entry.getValue();
            if (!statement.isEmpty()) {
                tableContent.add(new TableRow(categoryName, statement, index));
            }
        }
    }
    
    @FXML
    private void editSubcategory(ActionEvent event) {
        final Category subcategory = (Category) subCategoryComboBox.getValue();
        if (classifier.isRealCategory(subcategory)) {
            // BUG HERE
            CategoryForm.show(classifier, subcategory);
            setUpHeadcategories();
            setUpSubcategories();
            subCategoryComboBox.setValue(subcategory);
        }
    }
}
