package com.devsystem.aliados.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


import com.devsystem.aliados.model.Award;
import com.devsystem.aliados.model.Category;
import com.devsystem.aliados.model.AwardDetail;
import com.devsystem.aliados.model.CategoryAwardDetail;
import com.devsystem.aliados.model.Client;
import com.devsystem.aliados.model.ClientType;
import com.devsystem.aliados.model.ClientTypeCategory;
import com.devsystem.aliados.model.Program;
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
	private static final String DATABASE_NAME = "db_aliados";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;
    private Context myContext;
	// the DAO object we use to access the SimpleData table
    //pressure

	private Dao<Client, Integer> ClientDao                                  = null;
    private Dao<Program, Integer> ProgramDao                                = null;
    private Dao<Award, Integer> AwardDao                                    = null;
    private Dao<Category, Integer> CategoryDao                              = null;
    private Dao<AwardDetail, Integer> AwardDetailDao                        = null;
    private Dao<CategoryAwardDetail, Integer> CategoryAwardDetailDao        = null;
    private Dao<ClientType, Integer> ClientTypeDao                          = null;
    private Dao<ClientTypeCategory, Integer> ClientTypeCategoryDao          = null;


	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			

			TableUtils.createTable(connectionSource, Client.class                     );
            TableUtils.createTable(connectionSource, Program.class                     );
            TableUtils.createTable(connectionSource, Award.class                     );
            TableUtils.createTable(connectionSource, AwardDetail.class                     );
            TableUtils.createTable(connectionSource, Category.class                     );
            TableUtils.createTable(connectionSource, CategoryAwardDetail.class                     );
            TableUtils.createTable(connectionSource, ClientType.class                     );
            TableUtils.createTable(connectionSource, ClientTypeCategory.class                     );



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


            TableUtils.dropTable(connectionSource, Client.class,true              );
            TableUtils.dropTable(connectionSource, Program.class,true                  );
            TableUtils.dropTable(connectionSource, Award.class ,true                    );
            TableUtils.dropTable(connectionSource, AwardDetail.class    ,true                 );
            TableUtils.dropTable(connectionSource, Category.class     ,true                );
            TableUtils.dropTable(connectionSource, CategoryAwardDetail.class,true                     );
            TableUtils.dropTable(connectionSource, ClientType.class,true                     );
            TableUtils.dropTable(connectionSource, ClientTypeCategory.class,true                     );


            onCreate(db,connectionSource);

            Log.i(LOG_TAG, "execute method onUpgrade: drop Tables");

		} catch (SQLException e) {
			Log.e(LOG_TAG, "exception during onUpgrade", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }



    public Dao<Client, Integer> getClientDao() {
        if (null == ClientDao) {
            try {
                ClientDao = getDao(Client.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return ClientDao;
    }

    public Dao<Program, Integer> getProgramDao() {
        if (null == ProgramDao) {
            try {
                ProgramDao = getDao(Program.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return ProgramDao;
    }

    public Dao<Award, Integer> getAwardDao() {
        if (null == AwardDao) {
            try {
                AwardDao = getDao(Award.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return AwardDao;
    }

    public Dao<AwardDetail, Integer> getAwardDetailDao() {
        if (null == AwardDetailDao) {
            try {
                AwardDetailDao = getDao(AwardDetail.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return AwardDetailDao;
    }
    public Dao<Category, Integer> getCategoryDao() {
        if (null == CategoryDao) {
            try {
                CategoryDao = getDao(Category.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return CategoryDao;
    }
    public Dao<CategoryAwardDetail, Integer> getCategoryAwardDetailDao() {
        if (null == CategoryAwardDetailDao) {
            try {
                CategoryAwardDetailDao = getDao(CategoryAwardDetail.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return CategoryAwardDetailDao;
    }
    public Dao<ClientType, Integer> getClientTypeDao() {
        if (null == ClientTypeDao) {
            try {
                ClientTypeDao = getDao(ClientType.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return ClientTypeDao;
    }
    public Dao<ClientTypeCategory, Integer> getClientTypeCategoryDao() {
        if (null == ClientTypeCategoryDao) {
            try {
                ClientTypeCategoryDao = getDao(ClientTypeCategory.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return ClientTypeCategoryDao;
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
