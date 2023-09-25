package com.example.oliverdeleon_proyecto3_1.Fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oliverdeleon_proyecto3_1.Adapters.ImageAdapterr;
import com.example.oliverdeleon_proyecto3_1.Adapters.RecAdapter;
import com.example.oliverdeleon_proyecto3_1.OnSelectListener;
import com.example.oliverdeleon_proyecto3_1.R;

import com.google.android.material.bottomsheet.BottomSheetBehavior;


import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recordings_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recordings_Fragment extends Fragment implements RecAdapter.onItemListClick{


    RecyclerView recyclerView;
    List<File> fileList;
    File [] allfiles;

    RecAdapter recAdapter;
//    String path = getActivity().getExternalFilesDir("/").getAbsolutePath();

    ConstraintLayout playersheet, cardcontainer;
    BottomSheetBehavior bottomSheetBehavior;

     MediaPlayer mediaPlayer = null;
     boolean isPlaying = false;
     File filetoplay;

     ImageButton btnplay;
     TextView tvfilename,playerherdear;


     SeekBar seekBar;
     Handler seekbarhandler;
     Runnable updateseekbar;

    View root;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Recordings_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recordings_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Recordings_Fragment newInstance(String param1, String param2) {
        Recordings_Fragment fragment = new Recordings_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_recordings_, container, false);

        playersheet = root.findViewById(R.id.playersheet);

        cardcontainer = root.findViewById(R.id.cardcontainer);

        bottomSheetBehavior = BottomSheetBehavior.from(playersheet);

        btnplay = root.findViewById(R.id.play);
        tvfilename = root.findViewById(R.id.tvnamefile);
        playerherdear = root.findViewById(R.id.header);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        seekBar = root.findViewById(R.id.seekbar);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

       displayfiles();

       btnplay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isPlaying){
                   pauseAudio();
               }else {
                   if (filetoplay != null){
                       resumeAudio();
                   }else {
                       Toast.makeText(getContext(),"Selecciona un audio",Toast.LENGTH_SHORT).show();
                   }

               }
           }
       });

       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {
               pauseAudio();

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
               if (filetoplay !=null) {
                   int progres = seekBar.getProgress();
                   mediaPlayer.seekTo(progres);
                   resumeAudio();
               }

           }
       });

       return root;
    }

    private void displayfiles() {
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
       // recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        File directory = new File(path);
        allfiles = directory.listFiles();
        recAdapter = new RecAdapter(sortFileToLast(allfiles),this);
        recyclerView.setAdapter(recAdapter);

    }
    private File[] sortFileToLast(File[] fileImagesDir){
        File [] files = fileImagesDir;
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return Long.valueOf(rhs.lastModified()).compareTo(lhs.lastModified());
            }
        });
        return files;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            displayfiles();

        }

    }

    @Override
    public void onClickListener(File file, int position) {

        Log.d("Play log","File playing"+file.getName());
        filetoplay = file;
        if (isPlaying){
            Toast.makeText(getContext(),"sto",Toast.LENGTH_SHORT).show();
            stopAudio();
            playAudio(filetoplay);


        }else {
            Toast.makeText(getContext(),"pla",Toast.LENGTH_SHORT).show();
           // filetoplay = file;
            playAudio(filetoplay);

        }
    }

    private void pauseAudio(){
        mediaPlayer.pause();
        btnplay.setImageResource(R.drawable.ic_round_play_arrow_24);
        isPlaying = false;
        seekbarhandler.removeCallbacks(updateseekbar);

    }
    private void resumeAudio(){
        mediaPlayer.start();
        btnplay.setImageResource(R.drawable.ic_round_pause_24);
        isPlaying = true;
        updateRunnable();
        seekbarhandler.postDelayed(updateseekbar,0);

    }


    private void stopAudio() {
        isPlaying = false;
        btnplay.setImageResource(R.drawable.ic_round_play_arrow_24);
        playerherdear.setText("Stopped");
        mediaPlayer.stop();
        seekbarhandler.removeCallbacks(updateseekbar);


    }

    private void playAudio(File filetoplay) {
        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        try {
            mediaPlayer.setDataSource(filetoplay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnplay.setImageResource(R.drawable.ic_round_pause_24);
        tvfilename.setText(filetoplay.getName());
        playerherdear.setText("Playing");

        isPlaying = true;



        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                playerherdear.setText("Finish");
            }
        });

        seekBar.setMax(mediaPlayer.getDuration());

        seekbarhandler = new Handler();
   updateRunnable();
        seekbarhandler.postDelayed(updateseekbar,0);

    }//playAudio

    private void updateRunnable() {
        updateseekbar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarhandler.postDelayed(this,500);

            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isPlaying) {
            stopAudio();
        }
    }
}