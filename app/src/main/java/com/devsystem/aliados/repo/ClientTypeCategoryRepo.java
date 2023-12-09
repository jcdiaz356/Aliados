package com.devsystem.aliados.repo;

import android.content.Context;

import com.devsystem.aliados.db.DatabaseHelper;
import com.devsystem.aliados.db.DatabaseManager;
import com.devsystem.aliados.model.ClientTypeCategory;

import java.sql.SQLException;
import java.util.List;

public class ClientTypeCategoryRepo  implements Crud {
    private DatabaseHelper helper;

    public ClientTypeCategoryRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        ClientTypeCategory object = (ClientTypeCategory) item;
        try {
            index = helper.getClientTypeCategoryDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        ClientTypeCategory object = (ClientTypeCategory) item;

        try {
            helper.getClientTypeCategoryDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        ClientTypeCategory object = (ClientTypeCategory) item;

        try {
            helper.getClientTypeCategoryDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<ClientTypeCategory> items = null;
        int counter = 0;
        try {
            items = helper.getClientTypeCategoryDao().queryForAll();

            for (ClientTypeCategory object : items) {
                // do something with object
                helper.getClientTypeCategoryDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        ClientTypeCategory wishList = null;
        try {
            wishList = helper.getClientTypeCategoryDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<ClientTypeCategory> items = null;

        try {
            items = helper.getClientTypeCategoryDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getClientTypeCategoryDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getClientTypeCategoryDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
