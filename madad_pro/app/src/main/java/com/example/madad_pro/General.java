package com.example.madad_pro;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.madad_pro.ui.main.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class General extends AppCompatActivity {

    Frag_HelpOthers myFragment;
    String request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

//        try {
//            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("json"));
//
//            request = jsonObj.getString("rtypes");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Bundle myBundle = new Bundle();
//        myBundle .putString("request", request);
//       // Toast.makeText(this,""+myBundle.toString(),Toast.LENGTH_SHORT).show();
//        Log.i("bundle",""+myBundle.toString());
//        myFragment.setArguments(myBundle);

    }

//    public void popDialog(View view)
//    {
//        dialog_handler_general dialog = new dialog_handler_general();
//        dialog.show(getSupportFragmentManager(),"dialog");
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(General.this,Nav_Main.class);
        startActivity(i);
        General.this.finish();
    }
}