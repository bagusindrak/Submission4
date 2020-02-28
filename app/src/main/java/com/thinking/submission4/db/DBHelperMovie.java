package com.thinking.submission4.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thinking.submission4.db.DatabaseContract.MovieColumns;

public class DBHelperMovie  extends SQLiteOpenHelper {
   private static String DATABASE_NAME = "dbmovie";

   private static final int DATABASE_VERSION = 1;

   private static final String SQL_CREATE_TABLE = String.format("CREATE TABLE %s"
                   + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                   " %s TEXT NOT NULL," +
                   " %s TEXT NOT NULL," +
                   " %s TEXT NOT NULL," +
                   " %s TEXT NOT NULL)",
           DatabaseContract.TABLE_MOVIE,
           MovieColumns._ID,
           MovieColumns.ID,
           MovieColumns.NAME,
           MovieColumns.DESCRIPTION,
           MovieColumns.PHOTO
   );

   public DBHelperMovie(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_MOVIE);
      onCreate(db);
   }
}
