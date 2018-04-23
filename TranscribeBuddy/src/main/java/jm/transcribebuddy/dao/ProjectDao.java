package jm.transcribebuddy.dao;

import java.util.ArrayDeque;
import jm.transcribebuddy.logics.ProjectInfo;
import jm.transcribebuddy.logics.TextBuilder;

public class ProjectDao {
    final private ArrayDeque<String> errorLog;
    private DBProjectInfoDao projectInfoDao;
    private final FileTextDao textDao;
    private TextInfoDao textInfoDao;
    
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
    
    public boolean save(final ProjectInfo projectInfo, final String textFilePath, TextBuilder textBuilder) {
        boolean saveOk = true;
        if (projectInfoDao.save(projectInfo) == false) {
            errorLog.add("Error while saving the project information.");
            saveOk = false;
        }
        if (textDao.save(textFilePath, textBuilder) == false) {
            errorLog.add("Error while saving to file.\n"
                    + "Your work may not have been saved.");
            saveOk = false;
        }
        int projectId = projectInfo.getId();
        if (textInfoDao.save(projectId, textBuilder) == false) {
            errorLog.add("Error while saving the project data.\n"
                    + "Time marks have not been saved.");
            saveOk = false;
        }
        return saveOk;
    }
    
    public TextBuilder readFile(ProjectInfo projectInfo, final String textFilePath) {
        // read text content from TXT file
        TextBuilder textBuilder = textDao.readFile(textFilePath);
        // load project information from db
        projectInfo.setUpFilePaths(textFilePath);
        projectInfo = projectInfoDao.load(projectInfo);
        // load text information from db
        final int projectId = projectInfo.getId();
        textBuilder = textInfoDao.load(projectId, textBuilder);
        return textBuilder;
    }
}
