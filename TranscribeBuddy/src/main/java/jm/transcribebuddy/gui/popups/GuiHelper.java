package jm.transcribebuddy.gui.popups;

import java.io.File;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jm.transcribebuddy.logics.MainController;

public class GuiHelper {
    
    public static String openAudioFileDialog(final MainController mainController) {
        mainController.stopAudio();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open audio file");
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter(
                        "Select a file (*.m4a),(*.mp3),(*.wav)",
                        "*.m4a", "*.mp3", "*.wav");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return null;
        }
        String audioFileURI = file.toURI().toString();
        return audioFileURI;
    }
    
    public static String openTextFileDialog(final MainController mainController) {
        mainController.stopAudio();
        final String home = mainController.getSaveDirectory();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("Select a TXT file (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File(home));
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            // Load canceled
            return null;
        }
        String textFilePath = file.toString();
        return textFilePath;
    }
    
    public static String saveTextFileDialog(final MainController mainController) {
        mainController.stopAudio();
        final String home = mainController.getSaveDirectory();
        final String initialFileName = mainController.getTextFileName();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File(home));
        fileChooser.setInitialFileName(initialFileName);
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            // Save canceled
            return null;
        }
        String textFilePath = file.toString();
        return textFilePath;
    }
    
    
    // Retain the size of the window when changing scene
    public static void setUpStage(final Stage stage, final Scene scene) {
        final double width = stage.getWidth();
        final double height = stage.getHeight();
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        // Next code just gets stage to refresh
        stage.setResizable(false);
        stage.setResizable(true);
        stage.show();
    }
    
}
