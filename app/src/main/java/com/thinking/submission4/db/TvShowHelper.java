package com.thinking.submission4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.ID;
import static com.thinking.submission4.db.DatabaseContract.TABLE_TV_SHOW;

public class TvShowHelper {
   private static final String DATABASE_TABLE_TV_SHOW = TABLE_TV_SHOW;
   private static DatabaseHelper databaseHelper;
   private static TvShowHelper INSTANCE;

   private static SQLiteDatabase database;

   private TvShowHelper(Context context) {
      databaseHelper = new DatabaseHelper(context);
   }

   //   menginisiasi database.
   public static TvShowHelper getInstance(Context context) {
      if (INSTANCE == null) {
         synchronized (SQLiteOpenHelper.class) {
            if (INSTANCE == null) {
               INSTANCE = new TvShowHelper(context);
            }
         }
      }
      return INSTANCE;
   }

   //   membuka dan menutup koneksi ke database-nya.
   public void open() throws SQLException {
      database = databaseHelper.getWritableDatabase();
   }

   public void close() {
      databaseHelper.close();
      if (database.isOpen())
         database.close();
   }

   //   mengambil data.
   public Cursor queryAllTvShow() {
      return database.query(
              DATABASE_TABLE_TV_SHOW,
              null,
              null,
              null,
              null,
              null,
              _ID + " ASC");
   }


   //   mengambil data dengan id tertentu.
   public Cursor queryByIdTvShow(String id) {
      return database.query(
              DATABASE_TABLE_TV_SHOW,
              null,
              ID + " = ?",
              new String[]{id},
              null,
              null,
              null,
              null);
   }

   //   menyimpan data.
   public long insertTvShow(ContentValues values) {
      return database.insert(DATABASE_TABLE_TV_SHOW, null, values);
   }

   //   memperbaharui data.
   public int updateTvShow(String id, ContentValues values) {
      return database.update(DATABASE_TABLE_TV_SHOW, values, _ID + " = ?", new String[]{id});
   }

   //   menghapus data.
   public int deleteByIdTvShow(String id) {
      return database.delete(DATABASE_TABLE_TV_SHOW, _ID + " = ?", new String[]{id});
   }
}
