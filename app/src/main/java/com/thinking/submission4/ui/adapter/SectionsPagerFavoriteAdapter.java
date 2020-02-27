package com.thinking.submission4.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.thinking.submission4.R;
import com.thinking.submission4.ui.FavoriteFragment;

public class SectionsPagerFavoriteAdapter extends FragmentPagerAdapter {
   private final Context mContext;

   public SectionsPagerFavoriteAdapter(Context context, FragmentManager fm) {
      super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
      mContext = context;
   }

   @StringRes
   private final int[] TAB_TITLE = new int[]{
           R.string.tab_text_1,
           R.string.tab_text_2,
   };

   @NonNull
   @Override
   public Fragment getItem(int position) {
      return FavoriteFragment.newInstance(position + 1);
   }

   @Nullable
   @Override
   public CharSequence getPageTitle(int position) {
      return mContext.getResources().getString(TAB_TITLE[position]);
   }

   @Override
   public int getCount() {
      return 2;
   }
}
