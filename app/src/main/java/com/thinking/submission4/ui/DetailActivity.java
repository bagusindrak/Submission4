package com.thinking.submission4.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.thinking.submission4.R;
import com.thinking.submission4.db.MovieHelper;
import com.thinking.submission4.db.TvShowHelper;
import com.thinking.submission4.entity.Movie;

import static com.thinking.submission4.ui.Constant.EXTRA_MOVIE;
import static com.thinking.submission4.ui.Constant.EXTRA_SECTION;
import static com.thinking.submission4.ui.Constant.RESULT_ADD;
import static com.thinking.submission4.ui.Constant.RESULT_DELETE;
import static com.thinking.submission4.ui.Constant.SECTION_MOVIE;
import static com.thinking.submission4.ui.Constant.STATUS_FAV;


public class DetailActivity extends AppCompatActivity {

   private TextView tvName, tvDescription;
   private ImageView img;
   private ProgressBar progressBar;
   private boolean isFav = false;
   private int index;
   private MovieViewModel movieViewModel;

   private Movie movie;

   private void initComponent() {
      tvName = findViewById(R.id.txt_name);
      tvDescription = findViewById(R.id.txt_description);
      img = findViewById(R.id.img_photo);
      progressBar = findViewById(R.id.progressBar);
      movieViewModel = new ViewModelProvider(getViewModelStore(),
              new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
      index = getIntent().getIntExtra(EXTRA_SECTION, 1);
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);
      initComponent();

      movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
      showLoading(true);
      if (movie != null) {
         movieViewModel.setItemMovie(movie);
         tvName.setText(movie.getName());
         tvDescription.setText(movie.getDescription());
         Glide.with(this)
                 .asBitmap()
                 .load(movie.getPhoto())
                 .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                       showLoading(true);
                       return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                       showLoading(false);
                       return false;
                    }
                 })
                 .into(img);
      }
      if (getSupportActionBar() != null) {
         getSupportActionBar().setTitle(movie.getName());
      }
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
   }


   private void showLoading(Boolean state) {
      if (state) {
         progressBar.setVisibility(View.VISIBLE);
      } else {
         progressBar.setVisibility(View.GONE);
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.detail_menu, menu);
      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onPrepareOptionsMenu(Menu menu) {
      MenuItem item = menu.findItem(R.id.action_favorite);
      if (movie != null) {
         if (index == SECTION_MOVIE) {
            movieViewModel.setMovieHelper(MovieHelper.getInstance(getApplicationContext()));
            if (movieViewModel.isFavMovie()) {
               setIconFav(item, true);
            } else {
               setIconFav(item, false);
            }
         } else {
            movieViewModel.setTvShowHelper(TvShowHelper.getInstance(getApplicationContext()));
            if (movieViewModel.isFavTvShow()) {
               setIconFav(item, true);
            } else {
               setIconFav(item, false);
            }
         }
      }
      item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            if (!isFav) {
               if (index == SECTION_MOVIE) {
                  if (movieViewModel.insertMovieFav() > 0) {
                     Intent intent = new Intent();
                     intent.putExtra(STATUS_FAV, RESULT_ADD);
                     setIconFav(item, true);
                     showSnackbarMessage("Sukses nemambah Movie");
                  } else {
                     setIconFav(item, false);
                     showSnackbarMessage("Gagal nemambah data");
                  }
               } else {
                  if (movieViewModel.insertTvShowFav() > 0) {
                     Intent intent = new Intent();
                     intent.putExtra(STATUS_FAV, RESULT_ADD);
                     setIconFav(item, true);
                     showSnackbarMessage("Sukses nemambah TV Show");
                  } else {
                     setIconFav(item, false);
                     showSnackbarMessage("Gagal nemambah data");
                  }
               }
            } else {
               if (index == SECTION_MOVIE) {
                  if (movieViewModel.deleteMovieFav() > 0) {
                     Intent intent = new Intent();
                     intent.putExtra(STATUS_FAV, RESULT_DELETE);
                     setIconFav(item, false);
                     showSnackbarMessage("Sukses menghapus data");
                  } else {
                     setIconFav(item, true);
                     showSnackbarMessage("Gagal menghapus data");
                  }
               } else {
                  if (movieViewModel.deleteTvShowFav() > 0) {
                     Intent intent = new Intent();
                     intent.putExtra(STATUS_FAV, RESULT_DELETE);
                     setIconFav(item, false);
                     showSnackbarMessage("Sukses menghapus data");
                  } else {
                     setIconFav(item, true);
                     showSnackbarMessage("Gagal menghapus data");
                  }
               }
            }

            return true;
         }
      });

      return super.onPrepareOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            finish();
            break;
         case R.id.action_favorite:
            invalidateOptionsMenu();
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      if (index == SECTION_MOVIE) {
         movieViewModel.closeMovieHelper();
      } else {
         movieViewModel.closeTvShowHelper();
      }

   }

   private void showSnackbarMessage(String message) {
      Snackbar.make(findViewById(R.id.scroll), message, Snackbar.LENGTH_SHORT).show();
   }

   private void setIconFav(MenuItem item, boolean state) {
      if (!state) {
         isFav = false;
         item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));
      } else {
         isFav = true;
         item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));
      }

   }

}
