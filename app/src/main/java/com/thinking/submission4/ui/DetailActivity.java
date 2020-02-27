package com.thinking.submission4.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.thinking.submission4.R;
import com.thinking.submission4.db.DatabaseContract;
import com.thinking.submission4.db.MovieHelper;
import com.thinking.submission4.db.TvShowHelper;
import com.thinking.submission4.entity.Movie;


public class DetailActivity extends AppCompatActivity {

   public static final String EXTRA_MOVIE = "extra_movie";
   public static final String EXTRA_POSITION = "extra_position";
   private static final String ARG_SECTION_NUMBER = "section_number";
   private TextView tvName, tvDescription;
   private ImageView img;
   private ProgressBar progressBar;
   private boolean isFav = false;

   private int position, index;

   private Movie movie;
   private MovieHelper movieHelper;
   private TvShowHelper tvShowHelper;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);

      tvName = findViewById(R.id.txt_name);
      tvDescription = findViewById(R.id.txt_description);
      img = findViewById(R.id.img_photo);
      progressBar = findViewById(R.id.progressBar);

      tvShowHelper = TvShowHelper.getInstance(getApplicationContext());
      movieHelper = MovieHelper.getInstance(getApplicationContext());

      movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
      showLoading(true);
      if (movie != null) {
         index = getIntent().getIntExtra(ARG_SECTION_NUMBER, 0);
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

      if(isFavMovie() || isFavTvShow()){
         isFav = true;
         item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));
      }else {
         isFav = false;
         item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));
      }
      item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            if (!isFav) {

               ContentValues values = new ContentValues();
               long result;
                  values.put(DatabaseContract.MovieColumns.ID, movie.getId());
                  values.put(DatabaseContract.MovieColumns.NAME, movie.getName());
                  values.put(DatabaseContract.MovieColumns.DESCRIPTION, movie.getDescription());
                  values.put(DatabaseContract.MovieColumns.PHOTO, movie.getPhoto());
               if (index == 1) {

                  movieHelper.open();
                  result = movieHelper.insertMovie(values);
               } else {
                  tvShowHelper.open();
                  result = tvShowHelper.insertTvShow(values);
               }
               if (result > 0) {
                  isFav = true;
                  item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));
                  showSnackbarMessage("Sukses nemambah data");
               } else {
                  isFav = false;
                  item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));
                  showSnackbarMessage("Gagal nemambah data");
               }
            } else {
               long result;
               if (index == 1) {
                  result = movieHelper.deleteByIdMovie(String.valueOf(movie.getId()));
               } else {
                  result = tvShowHelper.deleteByIdTvShow(String.valueOf(movie.getId()));
               }
               if (result > 0) {
                  item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));
                  isFav = false;
                  showSnackbarMessage("Sukses menghapus data");
               } else {
                  Toast.makeText(DetailActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
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

   private boolean isFavMovie() {
      Cursor cursor = movieHelper.queryByIdMovie(String.valueOf(movie.getId()));
      return cursor.getCount() > 0;
   }

   private boolean isFavTvShow() {
      Cursor cursor = tvShowHelper.queryByIdTvShow(String.valueOf(movie.getId()));
      return cursor.getCount() > 0;
   }

   private void showSnackbarMessage(String message) {
      Snackbar.make(findViewById(R.id.scroll), message, Snackbar.LENGTH_SHORT).show();
   }

}
