package jm.transcribebuddy.dao.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jm.transcribebuddy.dao.ClassifierDao;
import jm.transcribebuddy.logics.Classifier;
import jm.transcribebuddy.logics.storage.Category;
import jm.transcribebuddy.logics.storage.InternalCategory;
import jm.transcribebuddy.logics.storage.ProjectInfo;

/**
 * This DAO class is responsible for storing data of classifier structure.
 * 
 * @author juham
 */
public class DBClassifierDao extends DBDao implements ClassifierDao {
    
    final public static String CATEGORIESTABLE = "tb_categories";
    final public static String CATSANDSTATESTABLE = "tb_cats_and_states";
    
    public DBClassifierDao(String databaseURL, String databaseUser, String databasePass) {
        super(databaseURL, databaseUser, databasePass);
        
        // Create classifiers table if not exists
        createCategoriesTable();
        createCatsAndStatesTable();
    }
    
    @Override
    public boolean save(final ProjectInfo projectInfo, final Classifier classifier) {
        if (connectDatabase() == false) {
            return false;
        }
        final int projectId = projectInfo.getId();
        
        // Delete old text info of the project
        deleteAllProjectCategories(projectId);
        
//        insertTreeBase(projectId, classifier.getHeightOfTree(), classifier.getUndefinedName());
        if (insertCategory(projectId, 0, "*ROOT*", 0)) {
            final int rootId = getCategoryId(projectId, "*ROOT*", 0);
            ArrayList<Category> headcategories = classifier.getHeadcategories();
            for (Category headcategory : headcategories) {
                final String name = headcategory.toString();
                insertCategory(projectId, rootId, name, 1);
                insertChildren(projectId, headcategory, 1);
            }
        }
        
        closeConnection();
        return true;
    }
    
    @Override
    public void load(final int projectId, final Classifier classifier) {
        if (connectDatabase() == false) {
            return;
        }
        loadCategories(projectId, classifier);
        
        closeConnection();
    }
    
    private boolean insertChildren(final int projectId, Category parent, int depth) {
        final int parentId = getCategoryId(projectId, parent.toString(), depth);
        if (parentId != 0 && parent instanceof InternalCategory) {
            ArrayList<Category> children = ((InternalCategory) parent).getChildren();
            boolean result = true;
            for (Category c : children) {
                if (insertCategory(projectId, parentId, c.toString(), depth + 1) == false) {
                    result = false;
                }
            }
            return result;
        }
        return false;
    }
    
    private boolean insertCategory(int projectId, int parentId, String name, int depth) {
        if (tableExists(CATEGORIESTABLE)) {
            String sqlQuery = getInsertCategoryQuery();
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setInt(2, parentId);
                ps.setString(3, name);
                ps.setInt(4, depth);
                int result = ps.executeUpdate();
                return result == 1;
            } catch (SQLException ex) {
//                System.out.println("insert error:\n" + ex);
            }
        }
        return false;
    }
    
    private int getCategoryId(int projectId, String name, int depth) {
        if (tableExists(CATEGORIESTABLE)) {
            String sqlQuery = "SELECT  id FROM " + CATEGORIESTABLE
                    + " WHERE project_id = ? AND name = ? AND depth = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setString(2, name);
                ps.setInt(3, depth);
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    return results.getInt(1);
                }
            } catch (SQLException ex) { }
        }
        return 0;
    }
    
    public int delete(final int projectId) {
        if (connectDatabase()) {
            int result = deleteAllProjectCategories(projectId);
            closeConnection();
            return result;
        }
        return 0;
    }
    
    private int deleteAllProjectCategories(final int projectId) {
        if (tableExists(CATEGORIESTABLE) == false) {
            return 0;
        }
        String sqlQuery = "DELETE FROM " + CATEGORIESTABLE + " WHERE project_id = ?";
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
            ps.setInt(1, projectId);
            int result = ps.executeUpdate();
            return result;
        } catch (SQLException ex) { }
        return 0;
    }
    
    private void loadCategories(final int projectId, final Classifier classifier) {
        if (tableExists(CATEGORIESTABLE)) {
            String sqlQuery = "SELECT name, parent_id FROM " + CATEGORIESTABLE
                + " WHERE project_id = ? AND depth = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setInt(2, classifier.getHeightOfTree());
                ResultSet results = ps.executeQuery();
                while (results.next()) {
                    String name = results.getString(1);
                    Category subcategory = classifier.addSubcategory(name);
                    int parentId = results.getInt(2);
                    String parentName = getCategoryName(parentId);
                    classifier.addHeadcategory(parentName, subcategory);
                }
            } catch (SQLException ex) { }
        }
    }
    
    String getCategoryName(int parentId) {
        String sqlQuery = "SELECT name FROM " + CATEGORIESTABLE + " WHERE id = ?";
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
            ps.setInt(1, parentId);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                String name = results.getString(1);
                return name;
            }
        } catch (SQLException ex) { }
        return "";
    }
    
    private void createCategoriesTable() {
        if (connectDatabase() == false) {
            return;
        }
        try {
            String sqlQuery = getCreateCategoriesTableQuery();
            PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
            ps.execute();
        } catch (SQLException ex) { }
        closeConnection();
    }
    
    private void createCatsAndStatesTable() {
        if (connectDatabase() == false) {
            return;
        }
        try {
            String sqlQuery = "CREATE TABLE IF NOT EXISTS " + CATSANDSTATESTABLE + " (\n"
//                + "id serial PRIMARY KEY, \n"
                + "project_id serial REFERENCES " + DBProjectInfoDao.PROJECTSTABLE + ", \n"
                + "category_id serial REFERENCES " + CATEGORIESTABLE + ", \n"
                + "statement_id serial REFERENCES " + DBTextInfoDao.STATEMENTSTABLE + " \n"
                + ");";
        
            PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
            ps.execute();
        } catch (SQLException ex) { }
        closeConnection();
    }
    
}
