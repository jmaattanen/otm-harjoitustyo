package jm.transcribebuddy;

import java.io.File;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ConstantTextController implements Initializable {
    
    static private AudioPlayer audioPlayer;
    
    @FXML
    private Label audioName;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void setUpController(final Stage stage, AudioPlayer player) {
        audioPlayer = player;
        Scene scene = stage.getScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            
            @Override
            public void handle( KeyEvent keyEvent) {
                if( keyEvent.isControlDown() ) {
                    if( null != keyEvent.getCode() ) switch (keyEvent.getCode()) {
                        case PAGE_DOWN:
                            try {
                                switchToLBLS(stage);
                            } catch (IOException ex) {
                                Logger.getLogger(ConstantTextController.class.getName()).log(Level.SEVERE, null, ex);
                            }   break;
                        case O:
                            openAudioFile();
                            break;
                        case SPACE:
                            if( audioPlayer != null ) {
                                if( audioPlayer.isPlaying() )
                                    audioPlayer.stop();
                                else audioPlayer.play();
                            }   break;
                        default:
                            break;
                    }
                }
            }
        });
        if(audioPlayer != null)
            audioName.setText(audioPlayer.getFilePath());
    }
    
    // switch to LineByLine scene
    private void switchToLBLS(final Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent lineByLineParent = fxmlLoader.load(getClass().getResource("/fxml/LineByLine.fxml").openStream());
        Scene lineByLineScene = new Scene(lineByLineParent);
        lineByLineScene.getStylesheets().add("/styles/Styles.css");
        
        stage.setScene(lineByLineScene);
        stage.show();
        LineByLineController fxmlController = (LineByLineController)fxmlLoader.getController();
        fxmlController.setUpController(stage, audioPlayer);
    }
    
    @FXML
    private void switchToLBLS(ActionEvent event) throws IOException {
        final Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        switchToLBLS(stage);
    }
    
    @FXML
    private void openAudioFile(/*ActionEvent event*/) {
        if(audioPlayer != null)
            audioPlayer.stop();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter(
                        "Select a file (*.m4a),(*.mp3),(*.wav)",
                        "*.m4a", "*.mp3", "*.wav" );
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        String audioFilePath = file.toURI().toString();
        audioPlayer = new AudioPlayer(audioFilePath);
        audioName.setText(audioPlayer.getFilePath());
    }
    
    @FXML
    private void playAudio(/*ActionEvent event*/) {
        if(audioPlayer != null)
            audioPlayer.play();
    }
    
    @FXML
    private void stopAudio(ActionEvent event) {
        if(audioPlayer != null)
            audioPlayer.stop();
    }
}
