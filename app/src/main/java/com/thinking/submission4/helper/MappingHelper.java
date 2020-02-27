package com.thinking.submission4.helper;

import android.database.Cursor;

import com.thinking.submission4.entity.Movie;

import java.util.ArrayList;

import static com.thinking.submission4.db.DatabaseContract.MovieColumns.DESCRIPTION;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.ID;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.NAME;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.PHOTO;

public class MappingHelper {

   public static ArrayList<Movie> mapCursorToArrayList(Cursor moviesCursor) {
      ArrayList<Movie> moviesList = new ArrayList<>();

      while (moviesCursor.moveToNext()) {
         String id = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(ID));
         String name = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(NAME));
         String description = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DESCRIPTION));
         String photo = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(PHOTO));
         moviesList.add(new Movie(id, name, description, photo));
      }

      return moviesList;
   }
}

