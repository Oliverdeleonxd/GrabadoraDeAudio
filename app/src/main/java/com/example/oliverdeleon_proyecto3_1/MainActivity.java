package com.example.oliverdeleon_proyecto3_1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.oliverdeleon_proyecto3_1.Adapters.ViewPagerAdapter;
import com.example.oliverdeleon_proyecto3_1.Fragments.Record_fragment;
import com.example.oliverdeleon_proyecto3_1.Fragments.Recordings_Fragment;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView tv1;
    MediaRecorder recorder;
    MediaPlayer player;
    File archivo;
    Button b1, b2, b3;

    private Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    Record_fragment record_fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablay);


        setupViewpager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
//

//
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                grabar();
//            }
//        });
//
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                detener();
//            }
//        });
//
//        b3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reproducir();
//            }
//        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btnGalery) {
                    // do something
                    Toast.makeText(getApplicationContext(), "Ir a galeria :c", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,GaleriaActivity.class);
                    startActivity(intent);
                    finish();

                } else if (item.getItemId() == R.id.btnMas) {
                    Toast.makeText(getApplicationContext(), "Proximamente dije , todavia no >:c", Toast.LENGTH_LONG).show();
                    // do something
                } else {
                    // do something
                }

                return false;
            }
//                switch (item.getItemId()) {
//            case R.id.btnGalery:
//                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.btnMas:
//                Toast.makeText(getApplicationContext(), "Proximamente dije , todavia no >:c", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
        });

    }// OnCreate

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decoView = getWindow().getDecorView();
        if (hasFocus){
            decoView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

            );

        }


    }//onWindowFocusChangued

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//        alertDialog.setTitle("Deseas Salir");
//        alertDialog.setMessage("Al salir la grabacion se detendra");
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si :c", new DialogInterface.OnClickListener() {
//            @Override public void onClick(DialogInterface dialog, int which) {
//                record_fragment.StopRecord();
//                finish();
//
//                dialog.dismiss();
//            } });
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No :0", new DialogInterface.OnClickListener() {
//            @Override public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"No :c",Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//
//            } });
//        alertDialog.show();
//
//    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu,add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu ResourceFile
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.btnGalery:
//                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.btnMas:
//                Toast.makeText(getApplicationContext(), "Proximamente dije , todavia no >:c", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    private void setupViewpager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter((getSupportFragmentManager()));
        viewPagerAdapter.addFragments(new Record_fragment(),"Grabar");
        viewPagerAdapter.addFragments(new Recordings_Fragment(),"Grabaciones");
        viewPager.setAdapter(viewPagerAdapter);
    }

}