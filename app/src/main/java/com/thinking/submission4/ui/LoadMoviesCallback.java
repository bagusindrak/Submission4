package com.thinking.submission4.ui;

import com.thinking.submission4.entity.Movie;

import java.util.ArrayList;

public interface LoadMoviesCallback {
   void preExecute();
   void postExecute(ArrayList<Movie> movies);
}
