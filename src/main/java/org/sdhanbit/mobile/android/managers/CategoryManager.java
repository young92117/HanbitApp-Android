package org.sdhanbit.mobile.android.managers;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.sdhanbit.mobile.android.entities.Category;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CategoryManager extends BaseRssFeedDatabaseManager {

    private static final String TAG = CategoryManager.class.getName();

    private HashMap<String, Integer> hashMap;

    public void insertCategories(List<Category> categories) {

        if (hashMap == null) {
            hashMap = new HashMap<String, Integer>();
            loadCategoriesToMap();
        }

        for (Category category: categories) {
            if (hashMap.containsKey(category.getName())) {
                category.setId(hashMap.get(category.getName()));
            } else {
                try {
                    Dao<Category, Integer> dao = getCategoryDao();
                    // save it to the database
                    dao.create(category);
                    // save it in the hash
                    hashMap.put(category.getName(), category.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Database Exception", e);
                }
            }
        }

    }

    private void loadCategoriesToMap() {
        try {
            Dao<Category, Integer> dao = getCategoryDao();

            for (Category category : dao) {
                hashMap.put(category.getName(), category.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "Can't load categories to map", e);
        }
    }
}
