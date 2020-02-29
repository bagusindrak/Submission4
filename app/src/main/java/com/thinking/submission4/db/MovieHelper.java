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
   private static final String DATABASE_TABLE = TABLE_MOVIE;
   private static DatabaseHelper databaseHelper;
   private static MovieHelper INSTANCE;

   private static SQLiteDatabase database;

   private MovieHelper(Context context) {
      databaseHelper = new DatabaseHelper(context);
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
      database = databaseHelper.getWritableDatabase();
   }

   public void close() {
      databaseHelper.close();
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
