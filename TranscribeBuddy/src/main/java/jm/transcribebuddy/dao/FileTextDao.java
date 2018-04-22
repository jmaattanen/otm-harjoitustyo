package jm.transcribebuddy.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import jm.transcribebuddy.logics.Statement;
import jm.transcribebuddy.logics.TextBuilder;

public class FileTextDao {
    
    public FileTextDao() {
        File testDir = new File("testdata");
        if (!testDir.exists()) {
            try {
                testDir.mkdir();
            } catch (SecurityException se) {
                System.out.println("Couldn't create testdata directory");
            }
        }
    }
    
    public boolean save(final String textFilePath, TextBuilder textBuilder) {
        try {
            File file = new File(textFilePath);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            ArrayList<Statement> statements = textBuilder.getAllStatements();
            for (Statement s : statements) {
                String line  = s.toString();
                line = line.replaceAll("\n", "<endl>");
                writer.write(line + "\n");
            }
            writer.close();
        } catch (IOException e) {
            //System.out.print(e);
            return false;
        }
        return true;
    }
    
    public TextBuilder readFile(final String textFilePath) {
        TextBuilder textBuilder = new TextBuilder();
        
        try {
            Scanner reader = new Scanner(new File(textFilePath));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                line = line.replaceAll("<endl>", "\n");
                textBuilder.endStatement(line);
            }
            reader.close();
            // delete empty statement
            textBuilder.deleteStatement();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read file " + textFilePath + "\n" + e);
        }
        
        return textBuilder;
    }
}
