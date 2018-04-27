package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.storage.ProjectInfo;

public interface ProjectInfoDao {
    
    public boolean save(final ProjectInfo projectInfo);
    
    public ProjectInfo load(ProjectInfo projectInfo);
    
}
