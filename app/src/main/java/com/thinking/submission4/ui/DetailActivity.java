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
import androidx.lifecycle.ViewModelProvider;

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

import static com.thinking.submission4.ui.Constant.ARG_SECTION_NUMBER;
import static com.thinking.submission4.ui.Constant.EXTRA_MOVIE;
import static com.thinking.submission4.ui.Constant.EXTRA_POSITION;


public class DetailActivity extends AppCompatActivity {

   private TextView tvName, tvDescription;
   private ImageView img;
   private ProgressBar progressBar;
   private boolean isFav = false;
   private int index;
   private MovieViewModel movieViewModel;

   public static final int REQUEST_ADD = 100;
   public static final int RESULT_ADD = 101;
   public static final int REQUEST_UPDATE = 200;
   public static final int RESULT_UPDATE = 201;
   public static final int RESULT_DELETE = 301;
   private final int ALERT_DIALOG_CLOSE = 10;
   private final int ALERT_DIALOG_DELETE = 20;

   private Movie movie;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);

      tvName = findViewById(R.id.txt_name);
      tvDescription = findViewById(R.id.txt_description);
      img = findViewById(R.id.img_photo);
      progressBar = findViewById(R.id.progressBar);
      movieViewModel = new ViewModelProvider(getViewModelStore(),
              new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
      movieViewModel.setActivity(this);
      movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
      index = getIntent().getIntExtra(ARG_SECTION_NUMBER, 0);
      movieViewModel.setDBHelper(index);
      movieViewModel.openDBHelper(index);

      showLoading(true);
      if (movie != null) {
//         position = getIntent().getIntExtra(EXTRA_POSITION, 0);
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
         if (movieViewModel.isFavDB(movie, index)) {
            setIconFav(item, true);
         } else {
            setIconFav(item, false);
         }
      }
      item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            if (!isFav) {
               if (movieViewModel.insertFav(movie, index) > 0) {
                  setIconFav(item, true);
                  showSnackbarMessage("Sukses nemambah data");
               } else {
                  setIconFav(item, false);
                  showSnackbarMessage("Gagal nemambah data");
               }
            }else {
               if(movieViewModel.deleteFav(movie, index) > 0){
                  setIconFav(item,false);
                  showSnackbarMessage("Sukses menghapus data");
               }else {
                  setIconFav(item,true);
                  showSnackbarMessage("Gagal menghapus data");
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
      movieViewModel.closeDBHelper(index);
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
