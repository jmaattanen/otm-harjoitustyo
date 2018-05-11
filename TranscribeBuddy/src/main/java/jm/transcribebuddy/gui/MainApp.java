package jm.transcribebuddy.gui;

import jm.transcribebuddy.gui.popups.AlertBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jm.transcribebuddy.logics.storage.AppSettings;
import jm.transcribebuddy.logics.MainController;

/**
 * This is the main class of TranscribeBuddy
 * 
 * @author Juha
 */
public class MainApp extends Application {

    private MainController mainController;
    final private String configFileName = "config.properties";
    
    @Override
    public void init() throws IOException {
        AppSettings settings;
        
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(configFileName));
            settings = new AppSettings(properties);
        } catch (FileNotFoundException ex) {
            createConfigFile();
            settings = new AppSettings("", "", "");
        }
        
        mainController = new MainController(settings);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        // Load fxml resource
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/fxml/ConstantText.fxml").openStream());
        
        // Set up scene and stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Feather.css");
        
        stage.setTitle("TranscribeBuddy");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                boolean confirmExit = true;
                if (mainController.isWorkSaved() == false) {
                    confirmExit = AlertBox.confirmAction(
                            "Warning",
                            "Your work has not been saved.",
                            "Do you want to quit anyway?"
                    );
                }
                if (confirmExit) {
                    Platform.exit();
                } else {
                    event.consume();
                }
            }
        });
        
        stage.show();
        
        ConstantTextController fxmlController = (ConstantTextController)fxmlLoader.getController();
        fxmlController.setUpController(stage, mainController);
        
        // Check if an error occurred while initializing dao
        String daoError = mainController.getDaoError();
        while (daoError != null) {
            AlertBox.showWarning("Database error.", daoError);
            daoError = mainController.getDaoError();
        }
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private boolean createConfigFile() {
        // Create a new config.properties file with empty parameters
        Properties properties = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream(configFileName);
            properties.setProperty("databaseURL", "");
            properties.setProperty("postgresUser", "");
            properties.setProperty("postgresPass", "");
            properties.setProperty("documentHome", "");
            properties.store(output, null);
            
        } catch (IOException ex) {
            return false;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) { }
            }
	}
        return true;
    }
}
