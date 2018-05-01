package jm.transcribebuddy.dao;

/***   This is the supreme leader of DAO package   ***/

import java.io.File;
import java.io.IOException;
import jm.transcribebuddy.dao.db.*;
import jm.transcribebuddy.dao.file.*;
import java.util.ArrayDeque;
import jm.transcribebuddy.logics.word.DetailedTextBuilder;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import jm.transcribebuddy.logics.word.TextBuilder;

public class ProjectDao {
    final private ArrayDeque<String> errorLog;
    final private ProjectInfoDao projectInfoDao;
    final private TextDao textDao;
    final private TextInfoDao textInfoDao;
    
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
    
    public String getError() {
        return errorLog.poll();
    }
    
    public boolean save(final ProjectInfo projectInfo, TextBuilder textBuilder) {
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
    
    public DetailedTextBuilder load(ProjectInfo projectInfo) {
        // read text content from TXT file
        DetailedTextBuilder textBuilder = textDao.load(projectInfo);
        
        // load project information from db
        projectInfo = projectInfoDao.load(projectInfo);
        
        // load text information from db
        textBuilder = textInfoDao.load(projectInfo, textBuilder);
        return textBuilder;
    }
    
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
    
    public boolean removeCreatedTextFile(final String textFilePath) {
        File file = new File(textFilePath);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
}
