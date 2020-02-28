package com.thinking.submission4.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thinking.submission4.BuildConfig;
import com.thinking.submission4.R;
import com.thinking.submission4.db.MovieHelper;
import com.thinking.submission4.db.TvShowHelper;
import com.thinking.submission4.entity.Movie;
import com.thinking.submission4.ui.adapter.CardViewMovieAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.thinking.submission4.db.DatabaseContract.MovieColumns.DESCRIPTION;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.ID;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.NAME;
import static com.thinking.submission4.db.DatabaseContract.MovieColumns.PHOTO;
import static com.thinking.submission4.ui.Constant.API_KEY;
import static com.thinking.submission4.ui.Constant.ARG_SECTION_NUMBER;
import static com.thinking.submission4.ui.Constant.POSTER_SIZE;
import static com.thinking.submission4.ui.Constant.SECTION_MOVIE;

public class MovieViewModel extends ViewModel{
   private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

   private TvShowHelper tvShowHelper;
   private MovieHelper movieHelper;
   private Activity activity;

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

   LiveData<ArrayList<Movie>> getMovies() {
      return listMovies;
   }

   LiveData<ArrayList<Movie>> getTvShows() {
      return listMovies;
   }

   void setActivity(Activity activity) {
      this.activity = activity;
   }

   void setDBHelper(int index) {
      if (index == SECTION_MOVIE)
         movieHelper = MovieHelper.getInstance(activity.getApplicationContext());
      else
         tvShowHelper = TvShowHelper.getInstance(activity.getApplicationContext());
   }

   public TvShowHelper getTvShowHelper() {
      return tvShowHelper;
   }

   public MovieHelper getMovieHelper() {
      return movieHelper;
   }



   void openDBHelper(int index) {
      if (index == SECTION_MOVIE)
         movieHelper.open();
      else
         tvShowHelper.open();
   }

   void closeDBHelper(int index) {
      if (index == SECTION_MOVIE)
         movieHelper.close();
      else
         tvShowHelper.close();
   }

   boolean isFavDB(Movie movie, int index) {
      Cursor cursor;
      if (index == SECTION_MOVIE)
         cursor = movieHelper.queryById(movie.getId());
      else
         cursor = tvShowHelper.queryById(movie.getId());

      return cursor.getCount() > 0;
   }

   long insertFav(Movie movie, int index) {
      ContentValues values = new ContentValues();
      values.put(ID, movie.getId());
      values.put(NAME, movie.getName());
      values.put(DESCRIPTION, movie.getDescription());
      values.put(PHOTO, movie.getPhoto());
      if (index == SECTION_MOVIE)
         return movieHelper.insert(values);
      else
         return tvShowHelper.insert(values);
   }

   int deleteFav(Movie movie, int index) {
      if (index == SECTION_MOVIE)
         return movieHelper.deleteById(String.valueOf(movie.getId()));
      else
         return tvShowHelper.deleteById(String.valueOf(movie.getId()));
   }


   @SuppressLint("StaticFieldLeak")
   void setFav(final int index) {
      try {
         new AsyncTask<Void, Void, ArrayList<Movie>>(){
            @Override
            protected ArrayList<Movie> doInBackground(Void... voids) {
               Cursor cursor;
               if(index == SECTION_MOVIE)
               cursor = movieHelper.queryAll();
               else
               cursor = tvShowHelper.queryAll();
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
      }catch (Exception e){
         Log.e("TAG", "setMovieFav: ", e );
      }
   }
   LiveData<ArrayList<Movie>> getMovieFav() {
      return listMovies;
   }
   @SuppressLint("StaticFieldLeak")
   void setTvShowFav() {
      try {
         new AsyncTask<MovieHelper, CardViewMovieAdapter, ArrayList<Movie>>(){
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
      }catch (Exception e){
         Log.e("TAG", "setTvShowFav: ", e );
      }
   }
   LiveData<ArrayList<Movie>> getTvShowFav() {
      return listMovies;
   }

}

