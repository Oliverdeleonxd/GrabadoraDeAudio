package com.example.oliverdeleon_proyecto3_1;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oliverdeleon_proyecto3_1.Adapters.ImageAdapter;
import com.example.oliverdeleon_proyecto3_1.Adapters.ImageAdapterr;
import com.example.oliverdeleon_proyecto3_1.Adapters.ViewPagerAdapter;
import com.example.oliverdeleon_proyecto3_1.Fragments.Imagenes_Fragment;
import com.example.oliverdeleon_proyecto3_1.Fragments.Videos_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GaleriaActivity extends AppCompatActivity {


    private Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
   public Uri imgUri,vidUri,contpat;
   ImageView imageView;
    private RecyclerView mRecyclerView;

   private static final int CAMERA_REQUEST = 500;

    private boolean rotate = false;


    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "" ,mVideoFileLocation;
    private String GALLERY_IMAGE_LOCATION = "image gallery",GALLERY_VIDEO_LOCATION="image gallery/VIDEOS";

    public File mGalleryFolder;
    private File mGalleryVideoFolder;


    String imgplace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);


        createImageGallery();
        createVideoGallery();

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablay);

        Toast.makeText(this,"Activity: "+mGalleryFolder,Toast.LENGTH_SHORT).show();


        setupViewpager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        toolbar.inflateMenu(R.menu.menu3);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btnGrabadora) {
                    // do something
                    Toast.makeText(getApplicationContext(), "Ir a la grabadora :c", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GaleriaActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // do something
                }

                return false;
            }

        });


        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        final FloatingActionButton fabImg = findViewById(R.id.fabImg);
        final FloatingActionButton fabVid = findViewById(R.id.fabVid);
        initShowOut(fabVid);
        initShowOut(fabImg);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotate = rotateFab(view, !rotate);
                if (rotate) {
                    showIn(fabVid);
                    showIn(fabImg);
                } else {
                    showOut(fabVid);
                    showOut(fabImg);
                }
            }
        });

        fabImg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                Toast.makeText(GaleriaActivity.this, "Image clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;
                try {

                    photoFile = createImage();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri uri = FileProvider.getUriForFile(GaleriaActivity.this,"com.example.oliverdeleon_proyecto3_1",photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

               startActivityForResult(intent, CAMERA_REQUEST);

            }//onClick camara

        });
        fabVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(GaleriaActivity.this, "Video clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);


                File VideoFile = null;
                try {

                    VideoFile = createVideo();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri uri = FileProvider.getUriForFile(GaleriaActivity.this,"com.example.oliverdeleon_proyecto3_1",VideoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(intent, CAMERA_REQUEST);



            }//onClick video
        });

    }//onCreate

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
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

            );

        }

    }//onWindowFocusChangued



    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            // Toast.makeText(this, "Picture taken successfully", Toast.LENGTH_SHORT).show();
            // Bundle extras = data.getExtras();
            // Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
            // mPhotoCapturedImageView.setImageBitmap(photoCapturedBitmap);
            // Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            // mPhotoCapturedImageView.setImageBitmap(photoCapturedBitmap);
//            setReducedImageSize();
//            RecyclerView.Adapter newImageAdapter = new ImageAdapterr(mGalleryFolder);
//            mRecyclerView.swapAdapter(newImageAdapter, false);

        }
    }



//    @RequiresApi(api = Build.VERSION_CODES.Q)
   File createImage() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        File image = File.createTempFile(imageFileName,".jpg",mGalleryFolder);
        mImageFileLocation = image.getAbsolutePath();

//

        return image;
    }
    File createVideo() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //String imgName = String.valueOf(System.currentTimeMillis());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        File video = File.createTempFile(imageFileName,".mp4",mGalleryVideoFolder);
        mVideoFileLocation = video.getAbsolutePath();


        return video;
    }




    private void createImageGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mGalleryFolder = new File(storageDirectory, GALLERY_IMAGE_LOCATION);
        if(!mGalleryFolder.exists()) {
            mGalleryFolder.mkdirs();
        }

    }

    private void createVideoGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mGalleryVideoFolder = new File(storageDirectory, GALLERY_VIDEO_LOCATION);
        if(!mGalleryVideoFolder.exists()) {
            mGalleryVideoFolder.mkdirs();
        }

    }





    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu,add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu3, menu);//Menu ResourceFile
        return true;
    }

    private void setupViewpager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter((getSupportFragmentManager()));
        viewPagerAdapter.addFragments(new Imagenes_Fragment(),"Imagenes");
        viewPagerAdapter.addFragments(new Videos_Fragment(),"Videos");
        viewPager.setAdapter(viewPagerAdapter);
    }



    public static void initShowOut(final View v) {
        v.setVisibility(View.GONE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
    }

    public static boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 135f : 0f);
        return rotate;
    }

    public static void showIn(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    public static void showOut(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }



}//GaleriaActivity