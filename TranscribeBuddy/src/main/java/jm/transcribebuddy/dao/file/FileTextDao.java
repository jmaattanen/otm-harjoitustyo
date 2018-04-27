package jm.transcribebuddy.dao.file;

/***   This is DAO for saving and loading pure text using TXT files   ***/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import jm.transcribebuddy.dao.TextDao;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import jm.transcribebuddy.logics.storage.Statement;
import jm.transcribebuddy.logics.TextBuilder;

public class FileTextDao implements TextDao {
    
    public FileTextDao() {
        
    }
    
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
                    writer.write(line + "\n");
                }
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    @Override
    public TextBuilder load(final ProjectInfo projectInfo) {
        final String textFilePath = projectInfo.getTextFilePath();
        TextBuilder textBuilder = new TextBuilder();
        
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
