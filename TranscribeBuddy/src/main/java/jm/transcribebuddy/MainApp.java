package jm.transcribebuddy;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/fxml/ConstantText.fxml").openStream());
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("TranscribeBuddy");
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(400);
        stage.show();
        
        TextBuilder textBuilder = new TextBuilder();
        
        ConstantTextController fxmlController = (ConstantTextController)fxmlLoader.getController();
        fxmlController.setUpController(stage, textBuilder, null);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
