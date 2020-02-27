package com.thinking.submission4.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thinking.submission4.BuildConfig;
import com.thinking.submission4.db.MovieHelper;
import com.thinking.submission4.entity.Movie;
import com.thinking.submission4.ui.adapter.CardViewMovieAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FavoriteViewModel extends ViewModel {
   private static final String API_KEY = BuildConfig.TMDB_API_KEY;
   private static final String POSTER_SIZE = "w154";

   private boolean isFav = false;
   private MovieHelper movieHelper;
   private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

   void setMovie(Intent data) {
      ArrayList<Movie> listItems = new ArrayList<>();
      if (data != null) {
         Movie movie = data.getParcelableExtra(DetailActivity.EXTRA_MOVIE);
         listItems.add(movie);
         listMovies.postValue(listItems);
      }

   }

   LiveData<ArrayList<Movie>> getMovies() {
      return listMovies;
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

   LiveData<ArrayList<Movie>> getTvShows() {
      return listMovies;
   }
}
