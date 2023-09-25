package com.example.oliverdeleon_proyecto3_1.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.BarVisualizer;
import com.chibde.visualizer.LineBarVisualizer;
import com.example.oliverdeleon_proyecto3_1.Adapters.ViewPagerAdapter;
import com.example.oliverdeleon_proyecto3_1.MainActivity;
import com.example.oliverdeleon_proyecto3_1.R;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.visualizer.amplitude.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Record_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Record_fragment extends Fragment {
    ImageButton btnRecord;
    TextView textViewstatus;
    Chronometer timeRec;
    GifImageView gifImageView;



    private static String filaName;
    private MediaRecorder recorder = null;
    boolean isRecording;

    int currentMaxAmplitude;

   CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    AudioRecordView audioRecordView;

    Timer timer ;

    ImageView img;


     BarVisualizer mVisualizer ;

   // File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/P3_Grabaciones");


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Record_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Record_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Record_fragment newInstance(String param1, String param2) {
        Record_fragment fragment = new Record_fragment();
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
        View root = inflater.inflate(R.layout.fragment_recorder, container, false);


        btnRecord = root.findViewById(R.id.btnrecord);
        textViewstatus =  root.findViewById(R.id.txtRexsta);
        timeRec  = root.findViewById(R.id.crono);
//        gifImageView  = root.findViewById(R.id.gif);

        //mVisualizer = root.findViewById(R.id.blast);

        audioRecordView = root.findViewById(R.id.audioRecordView);

        img = root.findViewById(R.id.imgAni);


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (isRecording){
                    Toast.makeText(getContext(),"salir :c",Toast.LENGTH_SHORT).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Deseas Salir");
                    alertDialog.setMessage("Al salir la grabacion se detendra");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si :c", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            StopRecord();

                            System.exit(0);

                            dialog.dismiss();
                        } });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No :0", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(),"No :c",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        } });
                    alertDialog.show();
                }
                else {
                    System.exit(0);
                }


            }//onBackPressed
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //isRecording = false;

        AskPermision();

//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss_", Locale.getDefault());

//        String fecha = format.format(new Date());
       // filaName = path + "/Grabacion_" + fecha + ".amr";
//        filaName = "Grabacion_"+fecha+".3gp";
//        if (path.exists()){
//            path.mkdirs();
//        }

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording){
                    try {
                      //  audioRecordView.recreate();
                        StarRecord();
                      //  gifImageView.setVisibility(View.VISIBLE);
                        timeRec.setBase(SystemClock.elapsedRealtime());
                        timeRec.start();
                        textViewstatus.setText("Grabando...");
                        btnRecord.setImageResource(R.drawable.ic_stop);
                        isRecording=true;


                        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate2);
                        animation.setRepeatCount(Animation.INFINITE);//Sets how many times the animation should be repeated.
                        animation.setInterpolator(new LinearInterpolator());//Sets the acceleration curve for this animation.
                        img.startAnimation(animation);

                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getContext(),"No se pudo grabar",Toast.LENGTH_SHORT).show();
                    }

                } else {
                  //  audioRecordView.recreate();
                    //timer.cancel();

                    StopRecord();
                   // gifImageView.setVisibility(View.GONE);
                    timeRec.setBase(SystemClock.elapsedRealtime());
                    timeRec.stop();
                    textViewstatus.setText("");
                    btnRecord.setImageResource(R.drawable.ic_record);
                    isRecording = false;

                   // img.getAnimation().cancel();
                     img.clearAnimation();


                }

            }
        });



        return root;
    }// onCreateView

    @Override
    public void onResume() {
        super.onResume();

        Animation animationsd = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
//        animationsd.setRepeatCount(Animation.INFINITE);//Sets how many times the animation should be repeated.
//        animationsd.setInterpolator(new LinearInterpolator());//Sets the acceleration curve for this animation.
        img.startAnimation(animationsd);

    }



    public void starDraw(){

        timer = new Timer();
        TimerTask task = new TimerTask() {

            // run() method to carry out the action of the task
            public void run() {

                try {
                    if (recorder !=null) {
                        currentMaxAmplitude = recorder.getMaxAmplitude();
                        audioRecordView.update(currentMaxAmplitude);
                    }
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }



            };

        };timer.schedule(task, 0, 100);

    }

    public void stopDraw(){

        timer.cancel();
        audioRecordView.recreate();
    }



    public void simplySnackbar(View view) {

//        final Snackbar snackbar = Snackbar.make(mainLayout, "", Snackbar.LENGTH_LONG);
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
        //snackbar.getView().setBackgroundColor(Color.GREEN);
        View custom_view = getLayoutInflater().inflate(R.layout.item_snackbar, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        snackBarView.addView(custom_view, 0);
        snackbar.show();

    }

    public void StarRecord() throws IOException {


          String path = getActivity().getExternalFilesDir("/").getAbsolutePath();

        SimpleDateFormat format = new SimpleDateFormat( "yyyy_MM_dd'_'HH_mm_ss", Locale.getDefault());

//        "yyyy.MM.dd G 'at' HH:mm:ss z"
        String fecha = format.format(new Date());


        filaName = "Grabacion_"+fecha+".mp3";

     //   try {
        Toast.makeText(getContext(),"Start",Toast.LENGTH_SHORT).show();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
       // recorder.setOutputFile(filaName);
        recorder.setOutputFile(path+"/"+filaName);


            try {
                Toast.makeText(getContext(),"aa",Toast.LENGTH_SHORT).show();
                recorder.prepare();

            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        recorder.start();


        starDraw();





//        Snackbar snackbar = Snackbar.make(coordinatorLayout,"This is Simple Snackbar",Snackbar.LENGTH_SHORT);
//        snackbar.show();


    }//StarRecord

//    private final MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
//        @Override
//        public void onError(MediaRecorder mr, int what, int extra) {
//            Log.d("Error: " ,what + ", " + extra);
//        }
//    };
//
//    private final MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
//        @Override
//        public void onInfo(MediaRecorder mr, int what, int extra) {
//            Log.i("Warning: " , what + ", " + extra);
//        }
//    };



    public void StopRecord(){

        if(null != recorder){
            Toast.makeText(getContext(),"Stop",Toast.LENGTH_SHORT).show();
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;

        }

        stopDraw();
        simplySnackbar(getView());

    }




    private void AskPermision() {
        Dexter.withContext(getContext()).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                Toast.makeText(getContext(),"Bienn",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
            permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (isRecording){
            StopRecord();
        }

    }
}//RecordFragment
