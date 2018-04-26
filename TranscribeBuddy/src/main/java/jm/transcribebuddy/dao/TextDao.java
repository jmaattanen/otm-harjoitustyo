package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.TextBuilder;
import jm.transcribebuddy.logics.ProjectInfo;

public interface TextDao {
    
    public boolean save(final ProjectInfo projectInfo, TextBuilder textBuilder);
    
    public TextBuilder load(final ProjectInfo projectInfo);
    
}
