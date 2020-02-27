package com.thinking.submission4.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thinking.submission4.BuildConfig;
import com.thinking.submission4.db.MovieHelper;
import com.thinking.submission4.db.TvShowHelper;
import com.thinking.submission4.entity.Movie;
import com.thinking.submission4.helper.MappingHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel implements LoadMoviesCallback {
   private static final String API_KEY = BuildConfig.TMDB_API_KEY;
   private static final String POSTER_SIZE = "w154";

   private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

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

   void setMovieFavorite(MovieHelper movieHelper, LoadMoviesCallback loadMoviesCallback) {
      new LoadMoviesAsync(movieHelper, loadMoviesCallback).execute();
   }

   void setTvShowFavorite(TvShowHelper tvShowHelper, LoadMoviesCallback loadMoviesCallback) {
      new LoadTvShowAsync(tvShowHelper, loadMoviesCallback).execute();
   }

   LiveData<ArrayList<Movie>> getMoviesFavorite() {
      return listMovies;
   }

   LiveData<ArrayList<Movie>> getTvShowsFavorite() {
      return listMovies;
   }

   @Override
   public void preExecute() {

   }

   @Override
   public void postExecute(ArrayList<Movie> movies) {
      listMovies.postValue(movies);
   }


   private static class LoadMoviesAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
      private final WeakReference<MovieHelper> weakMovieHelper;
      private final WeakReference<LoadMoviesCallback> weakCallback;

      private LoadMoviesAsync(MovieHelper MovieHelper, LoadMoviesCallback callback) {
         weakMovieHelper = new WeakReference<>(MovieHelper);
         weakCallback = new WeakReference<>(callback);
      }

      @Override
      protected void onPreExecute() {
         super.onPreExecute();
         weakCallback.get().preExecute();
      }

      @Override
      protected ArrayList<Movie> doInBackground(Void... voids) {
         Cursor dataCursor = weakMovieHelper.get().queryAllMovie();
         return MappingHelper.mapCursorToArrayList(dataCursor);
      }

      @Override
      protected void onPostExecute(ArrayList<Movie> Movies) {
         super.onPostExecute(Movies);
         weakCallback.get().postExecute(Movies);
      }
   }
   private static class LoadTvShowAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
      private final WeakReference<TvShowHelper> weakTvShowHelper;
      private final WeakReference<LoadMoviesCallback> weakCallback;

      private LoadTvShowAsync(TvShowHelper tvShowHelper, LoadMoviesCallback callback) {
         weakTvShowHelper = new WeakReference<>(tvShowHelper);
         weakCallback = new WeakReference<>(callback);
      }

      @Override
      protected void onPreExecute() {
         super.onPreExecute();
         weakCallback.get().preExecute();
      }

      @Override
      protected ArrayList<Movie> doInBackground(Void... voids) {
         Cursor dataCursor = weakTvShowHelper.get().queryAllTvShow();
         return MappingHelper.mapCursorToArrayList(dataCursor);
      }

      @Override
      protected void onPostExecute(ArrayList<Movie> Movies) {
         super.onPostExecute(Movies);
         weakCallback.get().postExecute(Movies);
      }
   }
}
