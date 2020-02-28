package com.thinking.submission4.ui.adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.thinking.submission4.R;
import com.thinking.submission4.entity.Movie;
import com.thinking.submission4.ui.DetailActivity;

import java.util.ArrayList;

import static com.thinking.submission4.ui.Constant.EXTRA_MOVIE;
import static com.thinking.submission4.ui.Constant.EXTRA_POSITION;

public class CardViewMovieAdapter extends RecyclerView.Adapter<CardViewMovieAdapter.CardViewViewHolder> {

   private ArrayList<Movie> listMovies = new ArrayList<>();

   public void setListMovies(ArrayList<Movie> items) {
      if (listMovies.size() > 0) {
         this.listMovies.clear();
      }
      listMovies.addAll(items);
      notifyDataSetChanged();
   }

   public ArrayList<Movie> getListMovies() {
      return listMovies;
   }
   @Override
   public int getItemViewType(int position) {
      return super.getItemViewType(position);
   }

   @NonNull
   @Override
   public CardViewMovieAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
      View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_movie, parent, false);
      return new CardViewViewHolder(itemView);

   }

   @Override
   public void onBindViewHolder(@NonNull final CardViewMovieAdapter.CardViewViewHolder holder, final int position) {
      final Movie movies = listMovies.get(position);

      holder.progressBar.setVisibility(View.VISIBLE);
      Glide.with(holder.itemView.getContext())
              .asBitmap()
              .load(movies.getPhoto())
              .listener(new RequestListener<Bitmap>() {
                 @Override
                 public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    return false;
                 }

                 @Override
                 public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                 }
              })
              .apply(new RequestOptions().override(350, 550))
              .into(holder.imgPhoto);
      holder.tvName.setText(movies.getName());
      holder.tvDescription.setText(movies.getDescription());
      holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            Intent moveWithObjectIntent = new Intent(v.getContext(), DetailActivity.class);
            moveWithObjectIntent.putExtra(EXTRA_POSITION, holder.getAdapterPosition());
            moveWithObjectIntent.putExtra(EXTRA_MOVIE, listMovies.get(holder.getAdapterPosition()));
            v.getContext().startActivity(moveWithObjectIntent);
         }
      });

   }

   @Override
   public int getItemCount() {
      return listMovies.size();
   }

   public class CardViewViewHolder extends RecyclerView.ViewHolder {

      TextView tvName, tvDescription;
      ImageView imgPhoto;
      ProgressBar progressBar;

      public CardViewViewHolder(@NonNull View itemView) {
         super(itemView);
         imgPhoto = itemView.findViewById(R.id.img_item_photo);
         tvName = itemView.findViewById(R.id.tv_item_name);
         tvDescription = itemView.findViewById(R.id.tv_item_description);
         progressBar = itemView.findViewById(R.id.progressBarImg);
      }
   }


}
