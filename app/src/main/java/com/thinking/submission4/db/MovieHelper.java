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

public class MovieHelper {
   private static final String DATABASE_TABLE_MOVIE = TABLE_MOVIE;
   private static DatabaseHelper dataBaseHelper;
   private static MovieHelper INSTANCE;

   private static SQLiteDatabase database;

   private MovieHelper(Context context) {
      dataBaseHelper = new DatabaseHelper(context);
   }

   //   menginisiasi database.
   public static MovieHelper getInstance(Context context) {
      if (INSTANCE == null) {
         synchronized (SQLiteOpenHelper.class) {
            if (INSTANCE == null) {
               INSTANCE = new MovieHelper(context);
            }
         }
      }
      return INSTANCE;
   }

   //   membuka dan menutup koneksi ke database-nya.
   public void open() throws SQLException {
      database = dataBaseHelper.getWritableDatabase();
   }

   public void close() {
      dataBaseHelper.close();
      if (database.isOpen())
         database.close();
   }

   //   mengambil data.
   public Cursor queryAllMovie() {
      return database.query(
              DATABASE_TABLE_MOVIE,
              null,
              null,
              null,
              null,
              null,
              _ID + " ASC");
   }

   //   mengambil data dengan id tertentu.
   public Cursor queryByIdMovie(String id) {
      return database.query(
              DATABASE_TABLE_MOVIE,
              null,
              ID + " = ?",
              new String[]{id},
              null,
              null,
              null,
              null);
   }


   //   menyimpan data.
   public long insertMovie(ContentValues values) {
      return database.insert(DATABASE_TABLE_MOVIE, null, values);
   }

   //   memperbaharui data.
   public int updateMovie(String id, ContentValues values) {
      return database.update(DATABASE_TABLE_MOVIE, values, _ID + " = ?", new String[]{id});
   }

   //   menghapus data.
   public int deleteByIdMovie(String id) {
      return database.delete(DATABASE_TABLE_MOVIE, ID + " = ?", new String[]{id});
   }
}
