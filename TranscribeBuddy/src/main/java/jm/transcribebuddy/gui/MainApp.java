package jm.transcribebuddy.gui;

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
import jm.transcribebuddy.logics.AppSettings;
import jm.transcribebuddy.logics.MainController;

public class MainApp extends Application {

    private MainController mainController;
    
    @Override
    public void init() throws IOException {
        String databaseURL = "";
        String databaseUser = "";
        String databasePass = "";
        
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
            databaseURL = properties.getProperty("databaseURL");
            databaseUser = properties.getProperty("postgresUser");
            databasePass = properties.getProperty("postgresPass");
        } catch (FileNotFoundException ex) {
//            System.out.println(ex);
            createConfigFile();
        }
//        System.out.println("URL: " + databaseURL + "\nUser: " + databaseUser + "\nPass: " + databasePass);
        AppSettings settings = new AppSettings(databaseURL, databaseUser, databasePass);
        
        mainController = new MainController(settings);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/fxml/ConstantText.fxml").openStream());
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
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
        Properties properties = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
            properties.setProperty("databaseURL", "");
            properties.setProperty("postgresUser", "");
            properties.setProperty("postgresPass", "");
            properties.store(output, null);
            
        } catch (IOException ex) {
//            System.out.println("Failed to set configs");
            return false;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
//                    System.out.println(e);
                }
            }

	}
        return true;
    }
}
