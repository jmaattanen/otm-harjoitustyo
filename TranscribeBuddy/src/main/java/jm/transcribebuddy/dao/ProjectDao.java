package jm.transcribebuddy.dao;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import jm.transcribebuddy.logics.TextBuilder;
import jm.transcribebuddy.logics.Statement;

public class ProjectDao {
    private final String testFile = "testdata/myfile.txt";
    
    public ProjectDao() {
        File testDir = new File("testdata");
        if (!testDir.exists()) {
            try {
                testDir.mkdir();
            } catch (SecurityException se) {
                System.out.println("Couldn't create testdata directory");
            }
        }
    }
    
    public void save(TextBuilder textBuilder) {
        try {
            File file = new File(testFile);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            ArrayList<Statement> statements = textBuilder.getList();
            for (Statement s : statements) {
                String line  = s.toString();
                line = line.replaceAll("\n", "<endl>");
                writer.write(line + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }
    
    public TextBuilder readFile() {
        TextBuilder textBuilder = new TextBuilder();
        
        try {
            Scanner reader = new Scanner(new File(testFile));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                line = line.replaceAll("<endl>", "\n");
                textBuilder.endStatement(line);
            }
            reader.close();
            // delete empty statement
            textBuilder.deleteStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        
        return textBuilder;
    }
}
