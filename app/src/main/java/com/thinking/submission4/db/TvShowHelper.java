package com.thinking.submission4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.ID;
import static com.thinking.submission4.db.DatabaseContract.TABLE_MOVIE;
import static com.thinking.submission4.db.DatabaseContract.TABLE_TV_SHOW;

public class TvShowHelper {
   private static final String DATABASE_TABLE = TABLE_TV_SHOW;
   private static DBHelperTvShow dbHelperTvShow;
   private static TvShowHelper INSTANCE;

   private static SQLiteDatabase database;

   private TvShowHelper(Context context) {
      dbHelperTvShow = new DBHelperTvShow(context);
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
      database = dbHelperTvShow.getWritableDatabase();
   }

   public void close() {
      dbHelperTvShow.close();
      if (database.isOpen())
         database.close();
   }

   //   mengambil data.
   public Cursor queryAll() {
      return database.query(
              DATABASE_TABLE,
              null,
              null,
              null,
              null,
              null,
              _ID + " ASC");
   }

   //   mengambil data dengan id tertentu.
   public Cursor queryById(String id) {
      return database.query(
              DATABASE_TABLE,
              null,
              ID + " = ?",
              new String[]{id},
              null,
              null,
              null,
              null);
   }

   //   menyimpan data.
   public long insert(ContentValues values) {
      return database.insert(DATABASE_TABLE, null, values);
   }

   //   menghapus data.
   public int deleteById(String id) {
      return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
   }
}
