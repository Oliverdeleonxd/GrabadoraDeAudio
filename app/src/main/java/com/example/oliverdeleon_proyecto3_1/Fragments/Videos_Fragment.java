package com.example.oliverdeleon_proyecto3_1.Fragments;

import static android.media.ThumbnailUtils.createVideoThumbnail;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oliverdeleon_proyecto3_1.Adapters.VideoAdapterr;
import com.example.oliverdeleon_proyecto3_1.GaleriaActivity;
import com.example.oliverdeleon_proyecto3_1.R;
import com.example.oliverdeleon_proyecto3_1.VideoModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Videos_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Videos_Fragment extends Fragment {

    private File mGalleryVideoFolder;
    private RecyclerView mRecyclerView;
    ImageView imageView;
    private String GALLERY_VIDEO_LOCATION = "image gallery/VIDEOS";

    private static final int STORAGE_PERMISION = 101;


    View root;

    ArrayList<VideoModel> videoModelArrayList;
    VideoAdapterr videoAdapterr;


    ArrayList<String> folderList = new ArrayList<String>();
    ArrayList<VideoModel> videoList = new ArrayList<>();
    FolderAdapter folderAdapter;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Videos_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Videos_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Videos_Fragment newInstance(String param1, String param2) {
        Videos_Fragment fragment = new Videos_Fragment();
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_videos_, container, false);
        AskPermision();
        createVideoGallery();

        imageView = root.findViewById(R.id.videoLibrary);
        mRecyclerView = root.findViewById(R.id.videoRecyclerView);


        videoList = fetchAllVideos(requireContext());
        if (folderList!=null && folderList.size() > 0 && videoList!= null) {
            folderAdapter = new FolderAdapter(folderList,videoList,getContext());
            mRecyclerView.setAdapter(folderAdapter);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
            mRecyclerView.setLayoutManager(layoutManager);

        }
        else {
            Toast.makeText(getContext(), "No se encrontro videos", Toast.LENGTH_SHORT).show();
        }

//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
//        mRecyclerView.setLayoutManager(layoutManager);


//        if (mGalleryVideoFolder==null){
//            imageView.setVisibility(View.VISIBLE);
//
//            Toast.makeText(getContext(),"Vacio",Toast.LENGTH_SHORT).show();
//        }else {
            imageView.setVisibility(View.INVISIBLE);
//            RecyclerView.Adapter imageAdapterr = new ImageAdapterr(sortFileToLast(mGalleryVideoFolder));

//        videoModelArrayList = new ArrayList<>();
////        videoAdapterr = new VideoAdapterr((videoModelArrayList),getActivity(),this);
//
//        mRecyclerView.setAdapter(videoAdapterr);

//        RecyclerView.Adapter imageAdapterr = new ImageAdapterr(sortFileToLast(mGalleryVideoFolder));
//            mRecyclerView.setAdapter(imageAdapterr);



//        getVideo();






        return root;

    }




    private void AskPermision() {
        Dexter.withContext(getContext()).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                Toast.makeText(getContext(),"Persimos consedidos",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }//AskPermision



    private ArrayList<VideoModel>fetchAllVideos (Context context){

        ArrayList<VideoModel> videoModels = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC";
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.RESOLUTION};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, orderBy);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String resolution = cursor.getString(4);
                String duration = cursor.getString(5);
                String disName = cursor.getString(6);
                String width_height = cursor.getString(7);

                VideoModel videoFiles = new VideoModel(id, path, title, size, resolution, duration, disName, width_height);
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0, slashFirstIndex);
                if (!folderList.contains(subString)) {
                    folderList.add(subString);
                }
                videoModels.add(videoFiles);
            }
            cursor.close();
        }
        return videoModels;



}//fetchAllVideos





//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mGalleryVideoFolder==null){
//            imageView.setVisibility(View.VISIBLE);
//
//            Toast.makeText(getContext(),"Vacio",Toast.LENGTH_SHORT).show();
//        }else {
//            imageView.setVisibility(View.INVISIBLE);
//            RecyclerView.Adapter imageAdapterr = new ImageAdapterr(sortFileToLast(mGalleryVideoFolder));
//            mRecyclerView.setAdapter(imageAdapterr);
//        }
//
//    }


    //
//  @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
//        if(requestCode == REQUEST_OPEN_RESULT_CODE && resultCode == RESULT_OKAY) {
//            if(resultData != null) {
////                mImageUri = resultData.getData();
//                Uri uri = null;
//                if (resultData != null) {
//                    uri = resultData.getData();
//                    try {
//                        Bitmap bitmap = getBitmapFromUri(uri);
//                        mImageView.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
////                Glide.with(this)
////                        .load(mImageUri)
////                        .into(mImageView);
//            }
//        }
//    }
//
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = requireContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }



//    public void setImage(Uri uri){
//
//        imageView.setImageURI(uri);
//
//    }

    private void createVideoGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mGalleryVideoFolder = new File(storageDirectory, GALLERY_VIDEO_LOCATION);
        if(!mGalleryVideoFolder.exists()) {
            mGalleryVideoFolder.mkdirs();
        }

    }




    private void displayfiles() {

        GaleriaActivity galeri = new GaleriaActivity();

        //   String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
//        Uri path2 = galeri.getImg();
//        recyclerView = root.findViewById(R.id.recycler);
//        gridView = root.findViewById(R.id.gridView);
        //   gridView.setHasFixedSize(true);

        // recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
//        gridView.setLayoutManager(new LinearLayoutManager(getContext()));

//        fileList = new ArrayList<>();
//        fileList.addAll(findFile(new File(path)));

//        File directory = new File(String.valueOf(path2));

        //  alliImgs = directory.listFiles();


        //imageAdapter = new ImageAdapter(alliImgs,getContext());

//        gridView.setAdapter(imageAdapter);

//        alliImgs = imageReader(Environment.getExternalStorageDirectory());
//        getActivity().getExternalFilesDir("/").getAbsolutePath();


//        gridView.setAdapter(new ImageAdapter());

    }//display





//
//
//    ArrayList<File> imageReader(File root){
//
//        ArrayList<File> a = new ArrayList<>();
//        File[] files = root.listFiles();
//        for (int i = 0; i<files.length; i++ ){
//            if (files[i].isDirectory()){
//                a.addAll(imageReader(files[i]));
//            }else {
//                if (files[i].getName().endsWith(".jpg")){
////                    a.addAll((Collection<? extends File>) files[i]);
//                    a.add(files[i]);
//                }
//            }
//        }//for
//        return a;
//
//    }


    private File[] sortFileToLast(File fileImagesDir){
        File [] files = fileImagesDir.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return Long.valueOf(rhs.lastModified()).compareTo(lhs.lastModified());
            }
        });
        return files;
    }



//
//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    @SuppressLint("NotifyDataSetChanged")
//    public void getVideo(){
//
//        ContentResolver contentResolver = requireActivity().getContentResolver();
//        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        Cursor cursor = contentResolver.query(uri,null,null,null,null);
//        cursor.moveToFirst();
//        if (cursor != null){
//            do {
//              @SuppressLint("Range") String videoTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
////                String videoTitle = "HOLA";
////                      cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
//               @SuppressLint("Range") String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//
//
//
//                Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(videoPath,MediaStore.Images.Thumbnails.MINI_KIND);
//               Bitmap videoThumbnail =
//
////                videoThumbnail = ThumbnailUtils.extractThumbnail(videoThumbnail, 200, 200,
////                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
////                Bitmap videoThumbnail = null;
////                MediaMetadataRetriever mediaMetadataRetriever = null;
////                try {
////
////                    mediaMetadataRetriever = new MediaMetadataRetriever();
////                    mediaMetadataRetriever.setDataSource(getContext(), Uri.parse(videoPath));
////                    videoThumbnail = mediaMetadataRetriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
////                }catch(Exception e) {
////                    e.printStackTrace();
////                } finally {
////                    if (mediaMetadataRetriever != null) {
////                        mediaMetadataRetriever.release();
////                    }
////                }
//
//
////                public static Bitmap createVideoThumbnail(String videoPath, int width, int height, int kind) {
////                    Bitmap bitmap = ThumbnailUtils.extractThumbnail(createVideoThumbnail(videoPath, kind), width, height,
////                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
////                    return bitmap;
////                }
//
//
//                videoModelArrayList.add(new VideoModel(videoTitle,videoPath,videoThumbnail));
//
//
//            }while (cursor.moveToNext());
//        }videoAdapterr.notifyDataSetChanged();
//
//    }


//    @Override
//    public void onVideoClick(int position) {
//
//        Intent intent = new Intent(getActivity(), Rvideo.class);
//        intent.putExtra("videoname",videoModelArrayList.get(position).getVideoName());
//        intent.putExtra("videopath",videoModelArrayList.get(position).getVideoPath());
//        startActivity(intent);
//
//    }
}