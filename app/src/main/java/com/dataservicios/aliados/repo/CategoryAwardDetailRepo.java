package com.dataservicios.aliados.repo;

import android.content.Context;

import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.CategoryAwardDetail;

import java.sql.SQLException;
import java.util.List;

public class CategoryAwardDetailRepo implements Crud {
    private DatabaseHelper helper;

    public CategoryAwardDetailRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        CategoryAwardDetail object = (CategoryAwardDetail) item;
        try {
            index = helper.getCategoryAwardDetailDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        CategoryAwardDetail object = (CategoryAwardDetail) item;

        try {
            helper.getCategoryAwardDetailDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        CategoryAwardDetail object = (CategoryAwardDetail) item;

        try {
            helper.getCategoryAwardDetailDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<CategoryAwardDetail> items = null;
        int counter = 0;
        try {
            items = helper.getCategoryAwardDetailDao().queryForAll();

            for (CategoryAwardDetail object : items) {
                // do something with object
                helper.getCategoryAwardDetailDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        CategoryAwardDetail wishList = null;
        try {
            wishList = helper.getCategoryAwardDetailDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<CategoryAwardDetail> items = null;

        try {
            items = helper.getCategoryAwardDetailDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getCategoryAwardDetailDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getCategoryAwardDetailDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
