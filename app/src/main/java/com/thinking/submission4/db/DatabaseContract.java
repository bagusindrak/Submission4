package com.thinking.submission4.db;

import android.provider.BaseColumns;

public class DatabaseContract {
   static String TABLE_MOVIE = "favorite_movie";
   static String TABLE_TV_SHOW = "favorite_tv_show";
   public static final class MovieColumns implements BaseColumns {
      public static String ID = "id";
      public static String NAME = "name";
      public static String DESCRIPTION = "description";
      public static String PHOTO = "photo";
   }
}
