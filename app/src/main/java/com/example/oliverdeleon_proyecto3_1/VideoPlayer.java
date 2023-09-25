package com.example.oliverdeleon_proyecto3_1;

import static com.example.oliverdeleon_proyecto3_1.Adapters.VideosAdapter.videoFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {
    int position = -1;
    private VideoView videoView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = (VideoView) findViewById(R.id.video_view);

        position = getIntent().getIntExtra("p",-1);
        String path = videoFolder.get(position).getPath();

        if (path != null) {

            videoView.setVideoPath(path);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });

        }else {
            Toast.makeText(this, "ruta no exite", Toast.LENGTH_SHORT).show();
        }

    }//onCreate


}// VideoPlayer