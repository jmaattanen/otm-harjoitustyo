package jm.transcribebuddy.gui;

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
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jm.transcribebuddy.logics.AudioPlayer;
import jm.transcribebuddy.logics.TextBuilder;

public class LineByLineController implements Initializable {
    
    static private AudioPlayer audioPlayer;
    static private TextBuilder textBuilder;
    
    @FXML
    private Label audioName;
    
    @FXML
    private TextArea workArea, prevArea, nextArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void setUpTextAreas() {
        prevArea.setText(textBuilder.getPrev());
        nextArea.setText(textBuilder.getNext());
        final String statement = textBuilder.getCurrent();
        workArea.setText(statement);
        workArea.positionCaret(statement.length());
        workArea.requestFocus();
    }
    
    public void setUpController(final Stage stage, TextBuilder builder, AudioPlayer player) {
        textBuilder = builder;
        audioPlayer = player;
        Scene scene = stage.getScene();
        
        // set up hotkeys
        scene.setOnKeyPressed( new EventHandler<KeyEvent>() {
            
            @Override
            public void handle( KeyEvent keyEvent) {
                if( keyEvent.isControlDown() ) {
                    if( null != keyEvent.getCode() ) switch (keyEvent.getCode()) {
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
                            if( audioPlayer != null ) {
                                if( audioPlayer.isPlaying() )
                                    audioPlayer.stop();
                                else audioPlayer.play();
                            }   break;
                        case B:
                            if( audioPlayer != null ) {
                                audioPlayer.skipBackward();
                            }   break;
                        case N:
                            if( audioPlayer != null ) {
                                audioPlayer.skipForward();
                            }   break;
                        default:
                            break;
                    }
                }
            }
        });
        
        // set up audio file label
        if(audioPlayer != null)
            audioName.setText(audioPlayer.getFilePath());
        
        setUpTextAreas();
        prevArea.setEditable(false);
        nextArea.setEditable(false);
    }
    
    // switch to ConstantText scene
    private void switchToCTS(final Stage stage) throws IOException {
        // save current text to TextBuilder
        String statement = workArea.getText();
        textBuilder.set(statement);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent constantTextParent = fxmlLoader.load(getClass().getResource("/fxml/ConstantText.fxml").openStream());
        Scene constantTextScene = new Scene(constantTextParent);
        constantTextScene.getStylesheets().add("/styles/Styles.css");
        
        stage.setScene(constantTextScene);
        stage.show();
        ConstantTextController fxmlController = (ConstantTextController)fxmlLoader.getController();
        fxmlController.setUpController(stage, textBuilder, audioPlayer);
    }
    
    @FXML
    private void switchToCTS(ActionEvent event) throws IOException {
        final Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        switchToCTS(stage);
    }

    @FXML
    private void openAudioFile(ActionEvent event) {
        if(audioPlayer != null)
            audioPlayer.stop();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter(
                        "Select a file (*.m4a),(*.mp3),(*.wav)",
                        "*.m4a", "*.mp3", "*.wav" );
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            String audioFilePath = file.toURI().toString();
            audioPlayer = new AudioPlayer(audioFilePath);
            audioName.setText(audioPlayer.getFilePath());
            textBuilder = new TextBuilder();
            setUpTextAreas();
        }
    }
    
    @FXML
    private void selectPrevStatement() {
        String statement = workArea.getText();
        textBuilder.set(statement);
        textBuilder.selectPrev();
        setUpTextAreas();
    }
    
    @FXML
    private void selectNextStatement() {
        String statement = workArea.getText();
        textBuilder.set(statement);
        textBuilder.selectNext();
        setUpTextAreas();
    }
    
    @FXML
    private void endStatement() {
        String statement = workArea.getText();
        textBuilder.endStatement(statement);
        setUpTextAreas();
    }
    
    @FXML
    private void playAudio(ActionEvent event) {
        if(audioPlayer != null)
            audioPlayer.play();
    }
    
    @FXML
    private void stopAudio(ActionEvent event) {
        if(audioPlayer != null)
            audioPlayer.stop();
    }
    
    @FXML
    private void skipBackward(ActionEvent event) {
        if(audioPlayer != null)
            audioPlayer.skipBackward();
    }
    
    @FXML
    private void skipForward(ActionEvent event) {
        if(audioPlayer != null)
            audioPlayer.skipForward();
    }
}
