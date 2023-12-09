package com.devsystem.aliados.repo;

import android.content.Context;

import com.devsystem.aliados.db.DatabaseHelper;
import com.devsystem.aliados.db.DatabaseManager;
import com.devsystem.aliados.model.ClientType;

import java.sql.SQLException;
import java.util.List;

public class ClientTypeRepo  implements Crud {
    private DatabaseHelper helper;

    public ClientTypeRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        ClientType object = (ClientType) item;
        try {
            index = helper.getClientTypeDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        ClientType object = (ClientType) item;

        try {
            helper.getClientTypeDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        ClientType object = (ClientType) item;

        try {
            helper.getClientTypeDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<ClientType> items = null;
        int counter = 0;
        try {
            items = helper.getClientTypeDao().queryForAll();

            for (ClientType object : items) {
                // do something with object
                helper.getClientTypeDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        ClientType wishList = null;
        try {
            wishList = helper.getClientTypeDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<ClientType> items = null;

        try {
            items = helper.getClientTypeDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getClientTypeDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getClientTypeDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
