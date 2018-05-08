package jm.transcribebuddy.dao;

import java.io.File;
import java.io.IOException;
import jm.transcribebuddy.dao.db.*;
import jm.transcribebuddy.dao.file.*;
import java.util.ArrayDeque;
import jm.transcribebuddy.logics.word.DetailedTextBuilder;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import jm.transcribebuddy.logics.word.TextBuilder;

/**
 * The supreme leader of DAO package.
 * 
 * @author Juha
 */
public class ProjectDao {
    final private ArrayDeque<String> errorLog;
    final private ProjectInfoDao projectInfoDao;
    final private TextDao textDao;
    final private TextInfoDao textInfoDao;
    
    /**
     * Creates a new ProjectDao object with given database configurations.
     * 
     * @param databaseURL URL of database driver.
     * For example: "jdbc:postgres:://localhost:5432/mydatabase.db"
     * @param databaseUser Database username.
     * @param databasePass Database password.
     */
    public ProjectDao(String databaseURL, String databaseUser, String databasePass) {
        errorLog = new ArrayDeque<>();
        textDao = new FileTextDao();
        projectInfoDao = new DBProjectInfoDao(databaseURL, databaseUser, databasePass);
        textInfoDao = new DBTextInfoDao(databaseURL, databaseUser, databasePass);
        if (((DBTextInfoDao) textInfoDao).testConnection() == false) {
            errorLog.add(
                    "Couldn't connect to database.\n"
                    + "Some of the project data may be lost when opening or saving a file."
            );
        }
    }
    
    /**
     * This method polls error messages from the DAO error log.
     * @return The oldest DAO error that is still in the queue.
     */
    public String getError() {
        return errorLog.poll();
    }
    
    /**
     * This method saves the current state of project to a
     * text file and database. The storage location is defined in
     * ProjectInfo object.
     * @see jm.transcribebuddy.logics.storage.ProjectInfo
     * @see jm.transcribebuddy.logics.word.TextBuilder
     * @param projectInfo Project information to be saved.
     * @param textBuilder TextBuilder contents to be saved.
     * @return True if no error occurred during saving.
     */
    public boolean save(final ProjectInfo projectInfo, final TextBuilder textBuilder) {
        boolean saveOk = true;
        if (projectInfoDao.save(projectInfo) == false) {
            errorLog.add("Error while saving the project information.");
            saveOk = false;
        }
        if (textDao.save(projectInfo, textBuilder) == false) {
            errorLog.add("Error while saving to file.\n"
                    + "Your work may not have been saved.");
            saveOk = false;
        }
        if (textInfoDao.save(projectInfo, textBuilder) == false) {
            errorLog.add("Error while saving project data.\n"
                    + "Time marks have not been saved.");
            saveOk = false;
        }
        return saveOk;
    }
    
    /**
     * This method loads project from a text file and database.
     * The storage location is defined in ProjectInfo object.
     * @see jm.transcribebuddy.logics.storage.ProjectInfo
     * @see jm.transcribebuddy.logics.word.DetailedTextBuilder
     * @param projectInfo The project information of target project.
     * @return DetailedTextBuilder with loaded data or a new
     * DetailedTextBuilder instance if the opening failed.
     */
    public DetailedTextBuilder load(ProjectInfo projectInfo) {
        // read text content from TXT file
        DetailedTextBuilder textBuilder = textDao.load(projectInfo);
        
        // load project information from db
        projectInfo = projectInfoDao.load(projectInfo);
        
        // load text information from db
        textBuilder = textInfoDao.load(projectInfo, textBuilder);
        return textBuilder;
    }
    
    /**
     * This method creates a new blank file on a particular path if file
     * doesn't already exist.
     * @param textFilePath Path of the text file to be created.
     * @return True if file was created. False if it already exists or
     * an IO error occurred.
     */
    public boolean createTextFileIfNotExists(final String textFilePath) {
        File file = new File(textFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                return true;
            } catch (IOException ex) { }
        }
        return false;
    }
    
    /**
     * Deletes a file on a particular path.
     * @param textFilePath File path.
     * @return True if the file is deleted.
     */
    public boolean removeCreatedTextFile(final String textFilePath) {
        File file = new File(textFilePath);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
}
