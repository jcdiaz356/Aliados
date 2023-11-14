package com.dataservicios.aliados.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


import com.dataservicios.aliados.model.Month;
import com.dataservicios.aliados.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "db_ganamas";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 2;
    private Context myContext;
	// the DAO object we use to access the SimpleData table
    //pressure

	private Dao<User, Integer> UserDao                      = null;
	private Dao<Month, Integer> MonthDao                      = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			

			TableUtils.createTable(connectionSource, User.class                     );
			TableUtils.createTable(connectionSource, Month.class                     );

            Log.i(LOG_TAG, "execute method onCreate: Can't create Tables");
           // preloadData(db,myContext);

		} catch (SQLException e) {
			Log.e(LOG_TAG, "Can't create database", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			List<String> allSql = new ArrayList<String>();

			switch(oldVersion)
			{
				case 1:
				  //allSql.add("alter table AdData add column `new_col` VARCHAR");
				  //allSql.add("alter table AdData add column `new_col2` VARCHAR");

			}
			for (String sql : allSql) {
				db.execSQL(sql);
			}


            TableUtils.dropTable(connectionSource, User.class,true              );
            TableUtils.dropTable(connectionSource, Month.class,true              );

            onCreate(db,connectionSource);

            Log.i(LOG_TAG, "execute method onUpgrade: drop Tables");

		} catch (SQLException e) {
			Log.e(LOG_TAG, "exception during onUpgrade", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }



    public Dao<User, Integer> getUserDao() {
        if (null == UserDao) {
            try {
                UserDao = getDao(User.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return UserDao;
    }

    public Dao<Month, Integer> getMonthDao() {
        if (null == MonthDao) {
            try {
                MonthDao = getDao(Month.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return MonthDao;
    }



    private void preloadData(SQLiteDatabase db, Context context) {

        InputStream is = null;
        try {

            is = context.getAssets().open("insert.sql");
            if (is != null) {
                db.beginTransaction();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                while (!TextUtils.isEmpty(line)) {
                    db.execSQL(line);
                    line = reader.readLine();

                }
                db.setTransactionSuccessful();
            }

            is.close();

            Log.i(LOG_TAG,"Insert rows");
        } catch (IOException e) {
            // Muestra log
            Log.e(LOG_TAG, "Error in File insert.sql", e);

        } catch (Exception e) {
            // Muestra log
            Log.e(LOG_TAG, "Error preloadData", e);
        } finally {
            db.endTransaction();
        }
    }


}
