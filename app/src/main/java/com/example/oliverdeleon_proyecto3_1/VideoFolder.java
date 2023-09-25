package com.example.oliverdeleon_proyecto3_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.oliverdeleon_proyecto3_1.Adapters.VideosAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class VideoFolder extends AppCompatActivity {

    RecyclerView recyclerView;
     String name;
    private ArrayList<VideoModel> videoModelArrayList = new ArrayList<>();
    private VideosAdapter videosAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);

        recyclerView = findViewById(R.id.recyclerviewfol);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadVideos();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
//        name = getIntent().getStringExtra("folderName");

        name = getIntent().getStringExtra("folderName");
        loadVideos();

    }//onCreate


    @Override
    protected void onResume() {
        super.onResume();
        loadVideos();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    private void loadVideos() {
        videoModelArrayList = getallVideoFromFolder(this, name);
        if (name != null && videoModelArrayList.size() > 0) {
            videosAdapter = new VideosAdapter(videoModelArrayList, this);
            //if your recyclerview lagging then just add this line
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerView.setNestedScrollingEnabled(false);

            recyclerView.setAdapter(videosAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,
                    RecyclerView.VERTICAL, false));
        } else {
            Toast.makeText(this, "can't find any videos", Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<VideoModel> getallVideoFromFolder(Context context, String name) {
        Toast.makeText(getApplicationContext(),"folderName: "+name, Toast.LENGTH_SHORT).show();

        ArrayList<VideoModel> list = new ArrayList<>();
//        videoModelArrayList = getallVideoFromFolder(this, name);
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String order = MediaStore.Video.Media.DATE_ADDED + " DESC";
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.RESOLUTION
        };
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + name + "%"};


        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, order);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                int size = cursor.getInt(3);
                String resolution = cursor.getString(4);
                int duration = cursor.getInt(5);
                String disName = cursor.getString(6);
                String bucket_display_name = cursor.getString(7);
                String width_height = cursor.getString(8);

                //this method convert 1204 in 1MB
                String human_can_read = null;
                if (size < 1024) {
                    human_can_read = String.format(context.getString(R.string.size_in_b), (double) size);
                } else if (size < Math.pow(1024, 2)) {
                    human_can_read = String.format(context.getString(R.string.size_in_kb), (double) (size / 1024));
                } else if (size < Math.pow(1024, 3)) {
                    human_can_read = String.format(context.getString(R.string.size_in_mb), size / Math.pow(1024, 2));
                } else {
                    human_can_read = String.format(context.getString(R.string.size_in_gb), size / Math.pow(1024, 3));
                }

                //this method convert any random video duration like 1331533132 into 1:21:12
                String duration_formatted;
                int sec = (duration / 1000) % 60;
                int min = (duration / (1000 * 60)) % 60;
                int hrs = duration / (1000 * 60 * 60);

                if (hrs == 0) {
                    duration_formatted = String.valueOf(min)
                            .concat(":".concat(String.format(Locale.UK, "%02d", sec)));
                } else {
                    duration_formatted = String.valueOf(hrs)
                            .concat(":".concat(String.format(Locale.UK, "%02d", min)
                                    .concat(":".concat(String.format(Locale.UK, "%02d", sec)))));
                }


                VideoModel files = new VideoModel(id, path, title,
                        human_can_read, resolution, duration_formatted,
                        disName, width_height);
                if (name.endsWith(bucket_display_name))
                    list.add(files);

            }
            cursor.close();
        }

        return list;
    }

}//class VideoFolderActivity