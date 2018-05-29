package jm.transcribebuddy.dao;

import jm.transcribebuddy.logics.Classifier;
import jm.transcribebuddy.logics.storage.ProjectInfo;

/**
 *
 * @author juham
 */
public interface ClassifierDao {
    
    public boolean save(final ProjectInfo projectInfo, final Classifier classifier);
}
