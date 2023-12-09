package com.devsystem.aliados.repo;

import android.content.Context;

import com.devsystem.aliados.db.DatabaseHelper;
import com.devsystem.aliados.db.DatabaseManager;
import com.devsystem.aliados.model.Program;

import java.sql.SQLException;
import java.util.List;

public class ProgramRepo implements Crud{
    private DatabaseHelper helper;

    public ProgramRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Program object = (Program) item;
        try {
            index = helper.getProgramDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Program object = (Program) item;

        try {
            helper.getProgramDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Program object = (Program) item;

        try {
            helper.getProgramDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Program> items = null;
        int counter = 0;
        try {
            items = helper.getProgramDao().queryForAll();

            for (Program object : items) {
                // do something with object
                helper.getProgramDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Program wishList = null;
        try {
            wishList = helper.getProgramDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Program> items = null;

        try {
            items = helper.getProgramDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getProgramDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getProgramDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
