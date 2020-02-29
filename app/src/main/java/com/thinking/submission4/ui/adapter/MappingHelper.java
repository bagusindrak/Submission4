package com.thinking.submission4.ui.adapter;

import android.database.Cursor;

import com.thinking.submission4.entity.Movie;

import java.util.ArrayList;

import static com.thinking.submission4.db.DatabaseContract.MovieColumns.DESCRIPTION;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.ID;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.NAME;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.PHOTO;

public class MappingHelper {
   public static ArrayList<Movie> mapCursorToArrayList(Cursor cursor) {
      ArrayList<Movie> moviesList = new ArrayList<>();
      while (cursor.moveToNext()) {
         String id = cursor.getString(cursor.getColumnIndexOrThrow(ID));
         String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
         String description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION));
         String photo = cursor.getString(cursor.getColumnIndexOrThrow(PHOTO));
         moviesList.add(new Movie(id, name, description, photo));
      }
      return moviesList;
   }
}
