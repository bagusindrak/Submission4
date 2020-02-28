package com.thinking.submission4.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.thinking.submission4.R;
import com.thinking.submission4.entity.Movie;
import com.thinking.submission4.ui.adapter.CardViewMovieAdapter;

import java.util.ArrayList;
import java.util.Locale;

import static com.thinking.submission4.ui.Constant.ARG_SECTION_NUMBER;
import static com.thinking.submission4.ui.Constant.SECTION_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment{


   private RecyclerView rvMovies;
   private ArrayList<Movie> listMovie = new ArrayList<>();
   private ProgressBar progressBar;
   private MovieViewModel movieViewModel;
   private CardViewMovieAdapter cardViewHeroAdapter;
   private int section;
   private static String sDefSystemLanguage;


   public static FavoriteFragment newInstance(int index) {
      // Required empty public constructor
      FavoriteFragment fragment = new FavoriteFragment();
      Bundle bundle = new Bundle();
      bundle.putInt(ARG_SECTION_NUMBER, index);
      fragment.setArguments(bundle);
      sDefSystemLanguage = Locale.getDefault().getLanguage();
      return fragment;
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_favorite, container, false);
   }

   private void initComponent(View view) {
      rvMovies = view.findViewById(R.id.rv_movies);
      rvMovies.setHasFixedSize(true);
      progressBar = view.findViewById(R.id.progressBar);
      movieViewModel = new ViewModelProvider(getViewModelStore(),
              new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
      showRecyclerCardView();
      movieViewModel.setActivity(getActivity());
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      initComponent(view);
      int index = 1;

      if (getArguments() != null) {
         index = getArguments().getInt(ARG_SECTION_NUMBER);
         movieViewModel.setDBHelper(index);
         movieViewModel.openDBHelper(index);
         section = index;
         Intent intent = new Intent();
         intent.putExtra(ARG_SECTION_NUMBER, index);
         showSnackbarMessage(String.valueOf(index));
      }

      if (index == SECTION_MOVIE) {
         movieViewModel.setMovieFav();
         showLoading(true);
         movieViewModel.getMovieFav().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
               if (movies != null) {
                  cardViewHeroAdapter.setListMovies(movies);
                  showLoading(false);
               }
            }
         });
      } else {
         showLoading(true);
         movieViewModel.getTvShowFav().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
               if (movies != null) {
                  cardViewHeroAdapter.setListMovies(movies);
                  showLoading(false);
               }
            }
         });
      }
   }

   private void showRecyclerCardView() {
      rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
      cardViewHeroAdapter = new CardViewMovieAdapter();
      cardViewHeroAdapter.notifyDataSetChanged();
      rvMovies.setAdapter(cardViewHeroAdapter);
   }

   private void showSnackbarMessage(String message) {
      Snackbar.make(rvMovies, message, Snackbar.LENGTH_SHORT).show();
   }
   private void showLoading(Boolean state) {
      if (state) {
         progressBar.setVisibility(View.VISIBLE);
      } else {
         progressBar.setVisibility(View.GONE);
      }
   }

   @Override
   public void onConfigurationChanged(@NonNull Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      sDefSystemLanguage = newConfig.locale.getLanguage();
   }

   @Override
   public void onDetach() {
      super.onDetach();
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      movieViewModel.closeDBHelper(section);
   }

}