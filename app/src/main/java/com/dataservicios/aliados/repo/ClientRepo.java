package com.dataservicios.aliados.repo;

import android.content.Context;

import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Client;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 21/03/2017.
 */
public class ClientRepo implements Crud {
    private DatabaseHelper helper;

    public ClientRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Client object = (Client) item;
        try {
            index = helper.getClientDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Client object = (Client) item;

        try {
            helper.getClientDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Client object = (Client) item;

        try {
            helper.getClientDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Client> items = null;
        int counter = 0;
        try {
            items = helper.getClientDao().queryForAll();

            for (Client object : items) {
                // do something with object
                helper.getClientDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Client wishList = null;
        try {
            wishList = helper.getClientDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Client> items = null;

        try {
            items = helper.getClientDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getClientDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getClientDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}