package jm.transcribebuddy.dao.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import jm.transcribebuddy.dao.TextDao;
import jm.transcribebuddy.logics.word.DetailedTextBuilder;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import jm.transcribebuddy.logics.storage.Statement;
import jm.transcribebuddy.logics.word.TextBuilder;

/**
 * This is DAO for saving and loading pure text using TXT files
 * 
 * @author juham
 */
public class FileTextDao implements TextDao {
    
    public FileTextDao() {
        
    }
    
    /**
     * Saves the state of TextBuilder object to a text file.
     * 
     * @param projectInfo Project information that determines the location of the text file.
     * @param textBuilder The object to be saved.
     * @return True if save was successful.
     */
    @Override
    public boolean save(final ProjectInfo projectInfo, TextBuilder textBuilder) {
        try {
            final String textFilePath = projectInfo.getTextFilePath();
            File file = new File(textFilePath);
            // Start writing in an empty file
            clean(file);
            try (FileWriter writer = new FileWriter(file)) {
                ArrayList<Statement> statements = textBuilder.getAllStatements();
                for (Statement s : statements) {
                    String line = s.toString();
                    line = line.replaceAll("\n", "<endl>");
                    writer.write(line + System.getProperty("line.separator"));
                }
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    /**
     * Loads saved state of DetailedTextBuilder object from a text file.
     * 
     * @param projectInfo Project information that determines the location of the text file.
     * @return DetailedTextBuilder with loaded data.
     */
    @Override
    public DetailedTextBuilder load(final ProjectInfo projectInfo) {
        final String textFilePath = projectInfo.getTextFilePath();
        DetailedTextBuilder textBuilder = new DetailedTextBuilder();
        
        try {
            try (Scanner reader = new Scanner(new File(textFilePath))) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    line = line.replaceAll("<endl>", "\n");
                    textBuilder.endStatement(line);
                }
            }
            // delete empty statement in the end of list
            textBuilder.deleteStatement();
        } catch (FileNotFoundException e) {
            //System.out.println("Couldn't read file " + textFilePath + "\n" + e);
        }
        
        return textBuilder;
    }
    
    private void clean(File file) {
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException ex) { }
        }
    }
}
