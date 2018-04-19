package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.TextBuilder;

public interface TextInfoDao {
    
    public boolean save(final int projectId, TextBuilder textBuilder);
    
    public TextBuilder loadTextInfo(final int projectId, TextBuilder textBuilder);
    
}
