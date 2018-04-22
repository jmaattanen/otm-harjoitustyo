package jm.transcribebuddy.dao;

import java.util.ArrayDeque;
import jm.transcribebuddy.logics.TextBuilder;

public class ProjectDao {
    private ArrayDeque<String> errorLog;
    private final FileTextDao textDao;
    private TextInfoDao textInfoDao;
    
    public ProjectDao(String databaseURL, String databaseUser, String databasePass) {
        errorLog = new ArrayDeque<>();
        textDao = new FileTextDao();
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
    
    public boolean save(final int projectId, final String textFilePath, TextBuilder textBuilder) {
        textDao.save(textFilePath, textBuilder);
        if (textInfoDao.save(projectId, textBuilder) == false) {
            errorLog.add(
                    "Error while saving the project data.\n"
                    + "Time marks have not been saved."
            );
            return false;
        }
        return true;
    }
    
    public TextBuilder readFile(final int projectId, final String textFilePath) {
        TextBuilder textBuilder = textDao.readFile(textFilePath);
        textBuilder = textInfoDao.load(projectId, textBuilder);
        return textBuilder;
    }
}
