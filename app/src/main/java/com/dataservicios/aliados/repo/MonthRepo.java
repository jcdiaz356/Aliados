package com.dataservicios.aliados.repo;

import android.content.Context;

import com.dataservicios.aliados.db.DatabaseHelper;
import com.dataservicios.aliados.db.DatabaseManager;
import com.dataservicios.aliados.model.Month;

import java.sql.SQLException;
import java.util.List;

public class MonthRepo implements Crud {
   private DatabaseHelper helper;

   public MonthRepo(Context context) {

      DatabaseManager.init(context);
      helper = DatabaseManager.getInstance().getHelper();
   }

   @Override
   public int create(Object item) {
      int index = -1;
      Month object = (Month) item;
      try {
         index = helper.getMonthDao().create(object);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return index;
   }


   @Override
   public int update(Object item) {

      int index = -1;

      Month object = (Month) item;

      try {
         helper.getMonthDao().update(object);
      } catch (SQLException e) {
         e.printStackTrace();
      }

      return index;
   }


   @Override
   public int delete(Object item) {

      int index = -1;

      Month object = (Month) item;

      try {
         helper.getMonthDao().delete(object);

      } catch (SQLException e) {
         e.printStackTrace();
      }

      return index;
   }

   @Override
   public int deleteAll() {

      List<Month> items = null;
      int counter = 0;
      try {
         items = helper.getMonthDao().queryForAll();

         for (Month object : items) {
            // do something with object
            helper.getMonthDao().deleteById(object.getId());
            counter++;
         }

      } catch (SQLException e) {
         e.printStackTrace();
      }
      return counter;
   }


   @Override
   public Object findById(int id) {

      Month wishList = null;
      try {
         wishList = helper.getMonthDao().queryForId(id);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return wishList;
   }


   @Override
   public List<?> findAll() {

      List<Month> items = null;

      try {
         items = helper.getMonthDao().queryForAll();
      } catch (SQLException e) {
         e.printStackTrace();
      }

      return items;

   }

   @Override
   public Object findFirstReg() {

      Object wishList = null;
      try {
         wishList = helper.getMonthDao().queryBuilder().queryForFirst();
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return wishList;
   }

   @Override
   public long countReg() {
      long count = 0;
      try {
         count = helper.getMonthDao().countOf();
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return count;
   }

}