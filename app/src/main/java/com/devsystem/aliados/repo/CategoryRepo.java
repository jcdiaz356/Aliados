package com.devsystem.aliados.repo;

import android.content.Context;

import com.devsystem.aliados.db.DatabaseHelper;
import com.devsystem.aliados.db.DatabaseManager;
import com.devsystem.aliados.model.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryRepo implements Crud {

    private DatabaseHelper helper;

    public CategoryRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Category object = (Category) item;
        try {
            index = helper.getCategoryDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Category object = (Category) item;

        try {
            helper.getCategoryDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Category object = (Category) item;

        try {
            helper.getCategoryDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Category> items = null;
        int counter = 0;
        try {
            items = helper.getCategoryDao().queryForAll();

            for (Category object : items) {
                // do something with object
                helper.getCategoryDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Category wishList = null;
        try {
            wishList = helper.getCategoryDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Category> items = null;

        try {
            items = helper.getCategoryDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getCategoryDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getCategoryDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}
