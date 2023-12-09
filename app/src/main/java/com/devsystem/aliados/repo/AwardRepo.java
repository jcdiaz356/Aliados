package com.devsystem.aliados.repo;

import android.content.Context;

import com.devsystem.aliados.db.DatabaseHelper;
import com.devsystem.aliados.db.DatabaseManager;
import com.devsystem.aliados.model.Award;

import java.sql.SQLException;
import java.util.List;

public class AwardRepo implements Crud {
    private DatabaseHelper helper;

    public AwardRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Award object = (Award) item;
        try {
            index = helper.getAwardDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Award object = (Award) item;

        try {
            helper.getAwardDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Award object = (Award) item;

        try {
            helper.getAwardDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Award> items = null;
        int counter = 0;
        try {
            items = helper.getAwardDao().queryForAll();

            for (Award object : items) {
                // do something with object
                helper.getAwardDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Award wishList = null;
        try {
            wishList = helper.getAwardDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Award> items = null;

        try {
            items = helper.getAwardDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getAwardDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getAwardDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
