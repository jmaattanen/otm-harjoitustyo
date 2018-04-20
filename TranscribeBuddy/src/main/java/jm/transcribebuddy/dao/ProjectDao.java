package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.TextBuilder;

public class ProjectDao {
    private final FileTextDao textDao;
    private TextInfoDao textInfoDao;
    
    public ProjectDao() {
        textDao = new FileTextDao();
        textInfoDao = new DBTextInfoDao();
    }
    
    public void save(final int projectId, final String textFilePath, TextBuilder textBuilder) {
        textDao.save(textFilePath, textBuilder);
        textInfoDao.save(projectId, textBuilder);
    }
    
    public TextBuilder readFile(final int projectId, final String textFilePath) {
        TextBuilder textBuilder = textDao.readFile(textFilePath);
        textBuilder = textInfoDao.load(projectId, textBuilder);
        return textBuilder;
    }
}
