package jm.transcribebuddy.dao.db;

import jm.transcribebuddy.dao.ClassifierDao;
import jm.transcribebuddy.logics.Classifier;
import jm.transcribebuddy.logics.storage.ProjectInfo;

/**
 * This DAO class is responsible for storing data of classifier structure.
 * 
 * @author juham
 */
public class DBClassifierDao extends DBDao implements ClassifierDao {
    
    final public static String CATEGORIESTABLE = "tb_categories";
    
    public DBClassifierDao(String databaseURL, String databaseUser, String databasePass) {
        super(databaseURL, databaseUser, databasePass);
        
        // Create classifiers table if not exists
//        createCategoriesTable();
    }
    
    @Override
    public boolean save(final ProjectInfo projectInfo, final Classifier classifier) {
        return false;
    }
    
}
