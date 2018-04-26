package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.ProjectInfo;
import jm.transcribebuddy.logics.TextBuilder;

public interface TextInfoDao {
    
    public boolean save(final ProjectInfo projectInfo, TextBuilder textBuilder);
    
    public TextBuilder load(final ProjectInfo projectInfo, TextBuilder textBuilder);
    
}
