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
import com.thinking.submission4.ui.adapter.MappingHelper;

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
import static com.thinking.submission4.ui.Constant.POSTER_SIZE;

class MovieViewModel extends ViewModel {
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

   void setTvShowHelper(TvShowHelper tvShowHelper) {
      this.tvShowHelper = tvShowHelper;
      this.tvShowHelper.open();
   }

   void setMovieHelper(MovieHelper movieHelper) {
      this.movieHelper = movieHelper;
      this.movieHelper.open();
   }

   LiveData<ArrayList<Movie>> getMovies() {
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

   void setMovieFav(LoadCallback loadCallback) {
      new LoadMoviesAsync(movieHelper, loadCallback, listMovies).execute();
   }

   void setTvShowFav(LoadCallback loadCallback) {
      new LoadTvShowAsync(tvShowHelper, loadCallback, listMovies).execute();
   }

   private static class LoadMoviesAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
      private final WeakReference<MovieHelper> weakMovieHelper;
      private final WeakReference<LoadCallback> weakCallback;
      private final WeakReference<MutableLiveData<ArrayList<Movie>>> weakLiveData;

      private LoadMoviesAsync(MovieHelper noteHelper, LoadCallback callback, MutableLiveData<ArrayList<Movie>> liveData) {
         weakMovieHelper = new WeakReference<>(noteHelper);
         weakCallback = new WeakReference<>(callback);
         weakLiveData = new WeakReference<>(liveData);
      }

      @Override
      protected void onPreExecute() {
         super.onPreExecute();
         weakCallback.get().preExecute();
      }

      @Override
      protected ArrayList<Movie> doInBackground(Void... voids) {
         Cursor dataCursor = weakMovieHelper.get().queryAll();
         return MappingHelper.mapCursorToArrayList(dataCursor);
      }

      @Override
      protected void onPostExecute(ArrayList<Movie> movies) {
         super.onPostExecute(movies);
         weakLiveData.get().postValue(movies);
         weakCallback.get().postExecute(movies);
      }
   }

   private static class LoadTvShowAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
      private final WeakReference<TvShowHelper> weakTvShowHelper;
      private final WeakReference<LoadCallback> weakCallback;
      private final WeakReference<MutableLiveData<ArrayList<Movie>>> weakLiveData;

      private LoadTvShowAsync(TvShowHelper tvShowHelper, LoadCallback callback, MutableLiveData<ArrayList<Movie>> liveData) {
         weakTvShowHelper = new WeakReference<>(tvShowHelper);
         weakCallback = new WeakReference<>(callback);
         weakLiveData = new WeakReference<>(liveData);
      }

      @Override
      protected void onPreExecute() {
         super.onPreExecute();
         weakCallback.get().preExecute();
      }

      @Override
      protected ArrayList<Movie> doInBackground(Void... voids) {
         Cursor dataCursor = weakTvShowHelper.get().queryAll();
         return MappingHelper.mapCursorToArrayList(dataCursor);
      }

      @Override
      protected void onPostExecute(ArrayList<Movie> movies) {
         super.onPostExecute(movies);
         weakLiveData.get().postValue(movies);
         weakCallback.get().postExecute(movies);
      }
   }
}

interface LoadCallback {
   void preExecute();

   void postExecute(ArrayList<Movie> movies);
}

