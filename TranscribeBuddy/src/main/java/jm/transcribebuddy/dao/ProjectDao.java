package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.TextBuilder;

public class ProjectDao {
    private final FileTextDao textDao;
    private TextInfoDao textInfoDao;
    
    public ProjectDao() {
        textDao = new FileTextDao();
        textInfoDao = new DBTextInfoDao();
    }
    
    public void save(final int projectId, TextBuilder textBuilder) {
        textDao.save(textBuilder);
        textInfoDao.save(projectId, textBuilder);
    }
    
    public TextBuilder readFile(final int projectId) {
        TextBuilder textBuilder = textDao.readFile();
        textBuilder = textInfoDao.load(projectId, textBuilder);
        return textBuilder;
    }
}
