package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.word.DetailedTextBuilder;
import jm.transcribebuddy.logics.word.TextBuilder;
import jm.transcribebuddy.logics.storage.ProjectInfo;

public interface TextDao {
    
    public boolean save(final ProjectInfo projectInfo, TextBuilder textBuilder);
    
    public DetailedTextBuilder load(final ProjectInfo projectInfo);
    
}
