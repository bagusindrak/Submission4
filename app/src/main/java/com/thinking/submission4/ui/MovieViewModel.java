package com.thinking.submission4.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thinking.submission4.db.MovieHelper;
import com.thinking.submission4.db.TvShowHelper;
import com.thinking.submission4.entity.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.thinking.submission4.db.DatabaseContract.MovieColumns.DESCRIPTION;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.ID;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.NAME;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.PHOTO;
import static com.thinking.submission4.ui.Constant.API_KEY;
import static com.thinking.submission4.ui.Constant.POSTER_SIZE;

public class MovieViewModel extends ViewModel {
   private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

   private TvShowHelper tvShowHelper;
   private MovieHelper movieHelper;
   private Movie movie;

   void setMovie() {
      AsyncHttpClient client = new AsyncHttpClient();
      final ArrayList<Movie> listItems = new ArrayList<>();
      String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1";
      client.get(url, new AsyncHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
               String result = new String(responseBody);
               JSONObject responseObject = new JSONObject(result);
               JSONArray list = responseObject.getJSONArray("results");
               for (int i = 0; i < list.length(); i++) {
                  JSONObject indexMovie = list.getJSONObject(i);
                  Movie movie = new Movie();
                  movie.setId(indexMovie.getString("id"));
                  movie.setDescription(indexMovie.getString("overview"));
                  movie.setName(indexMovie.getString("title"));
                  movie.setPhoto("https://image.tmdb.org/t/p/" + POSTER_SIZE + indexMovie.getString("poster_path"));
                  listItems.add(movie);
               }
               listMovies.postValue(listItems);
            } catch (Exception e) {
               Log.d("Exception", e.getMessage());
            }
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.d("onFailure", error.getMessage());
         }
      });
   }

   void setTvShow() {
      AsyncHttpClient client = new AsyncHttpClient();
      final ArrayList<Movie> listItems = new ArrayList<>();
      String url = "https://api.themoviedb.org/3/tv/popular?api_key=" + API_KEY + "&language=en-US&page=1";
      client.get(url, new AsyncHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
               String result = new String(responseBody);
               JSONObject responseObject = new JSONObject(result);
               JSONArray list = responseObject.getJSONArray("results");
               for (int i = 0; i < list.length(); i++) {
                  JSONObject indexMovie = list.getJSONObject(i);
                  Movie movie = new Movie();
                  movie.setId(indexMovie.getString("id"));
                  movie.setName(indexMovie.getString("name"));
                  movie.setDescription(indexMovie.getString("overview"));
                  movie.setPhoto("https://image.tmdb.org/t/p/" + POSTER_SIZE + indexMovie.getString("poster_path"));
                  listItems.add(movie);
               }
               listMovies.postValue(listItems);
            } catch (Exception e) {
               Log.d("Exception", e.getMessage());
            }
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.d("onFailure", error.getMessage());
         }
      });
   }

   public void setTvShowHelper(TvShowHelper tvShowHelper) {
      this.tvShowHelper = tvShowHelper;
      this.tvShowHelper.open();
   }

   public void setMovieHelper(MovieHelper movieHelper) {
      this.movieHelper = movieHelper;
      this.movieHelper.open();
   }

   LiveData<ArrayList<Movie>> getMovies() {
      return listMovies;
   }

   LiveData<ArrayList<Movie>> getTvShows() {
      return listMovies;
   }

   void setItemMovie(Movie movie) {
      this.movie = movie;
   }

   void closeMovieHelper() {
      this.movieHelper.close();
   }

   void closeTvShowHelper() {
      this.tvShowHelper.close();
   }

   boolean isFavMovie() {
      Cursor cursor = movieHelper.queryById(movie.getId());
      return cursor.getCount() > 0;
   }

   boolean isFavTvShow() {
      Cursor cursor = tvShowHelper.queryById(movie.getId());
      return cursor.getCount() > 0;
   }

   long insertMovieFav() {
      ContentValues values = new ContentValues();
      values.put(ID, movie.getId());
      values.put(NAME, movie.getName());
      values.put(DESCRIPTION, movie.getDescription());
      values.put(PHOTO, movie.getPhoto());
      return movieHelper.insert(values);
   }

   long insertTvShowFav() {
      ContentValues values = new ContentValues();
      values.put(ID, movie.getId());
      values.put(NAME, movie.getName());
      values.put(DESCRIPTION, movie.getDescription());
      values.put(PHOTO, movie.getPhoto());
      return tvShowHelper.insert(values);
   }

   int deleteMovieFav() {
      return movieHelper.deleteById(String.valueOf(movie.getId()));
   }

   int deleteTvShowFav() {
      return tvShowHelper.deleteById(String.valueOf(movie.getId()));
   }

   @SuppressLint("StaticFieldLeak")
   void setMovieFav() {
      try {
         new AsyncTask<MovieHelper, Void, ArrayList<Movie>>() {
            @Override
            protected ArrayList<Movie> doInBackground(MovieHelper... movieHelpers) {
               Cursor cursor = movieHelper.queryAll();
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

            @Override
            protected void onPostExecute(ArrayList<Movie> movies) {
               super.onPostExecute(movies);
               listMovies.postValue(movies);
            }
         }.execute();
      } catch (Exception e) {
         Log.e("TAG", "setTvShowFav: ", e);
      }
   }

   @SuppressLint("StaticFieldLeak")
   void setTvShowFav() {
      try {
         new AsyncTask<TvShowHelper, Void, ArrayList<Movie>>() {
            @Override
            protected ArrayList<Movie> doInBackground(TvShowHelper... tvShowHelpers) {
               Cursor cursor = tvShowHelper.queryAll();
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

            @Override
            protected void onPostExecute(ArrayList<Movie> movies) {
               super.onPostExecute(movies);
               listMovies.postValue(movies);
            }
         }.execute();
      } catch (Exception e) {
         Log.e("TAG", "setTvShowFav: ", e);
      }
   }

   LiveData<ArrayList<Movie>> getMovieFav() {
      return listMovies;
   }


   LiveData<ArrayList<Movie>> getTvShowFav() {
      return listMovies;
   }

}

