package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.word.DetailedTextBuilder;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import jm.transcribebuddy.logics.word.TextBuilder;

public interface TextInfoDao {
    
    public boolean save(final ProjectInfo projectInfo, final TextBuilder textBuilder);
    
    public DetailedTextBuilder load(final ProjectInfo projectInfo, final DetailedTextBuilder textBuilder);
    
}
