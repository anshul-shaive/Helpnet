package com.example.madad_pro.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.madad_pro.Frag_GetHelp;
import com.example.madad_pro.Frag_HelpOthers;
import com.example.madad_pro.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
       Fragment fragment = null;
       switch (position)
       {
           case 0:

               fragment = new Frag_GetHelp();
               break;
           case 1:
               fragment = new Frag_HelpOthers();
               break;
       }
       return fragment;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       //Fragment fragment =null;
       switch (position)
       {
           case 0:
               return "Get Help";
           case 1:
                return "Help Others";

       }
       return null;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}