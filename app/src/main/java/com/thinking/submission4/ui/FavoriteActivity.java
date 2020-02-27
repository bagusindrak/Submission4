package com.thinking.submission4.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.thinking.submission4.R;
import com.thinking.submission4.ui.adapter.SectionsPagerAdapter;
import com.thinking.submission4.ui.adapter.SectionsPagerFavoriteAdapter;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_favorite);
      getSupportActionBar().setTitle(R.string.favorite);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);

      SectionsPagerFavoriteAdapter sectionsPagerAdapter = new SectionsPagerFavoriteAdapter(this, getSupportFragmentManager());
      ViewPager viewPager = findViewById(R.id.view_pager);
      viewPager.setAdapter(sectionsPagerAdapter);
      TabLayout tabs = findViewById(R.id.tabs);
      tabs.setupWithViewPager(viewPager);

      getSupportActionBar().setElevation(0);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      if(item.getItemId() == android.R.id.home){
         finish();
      }
      return super.onOptionsItemSelected(item);
   }
}