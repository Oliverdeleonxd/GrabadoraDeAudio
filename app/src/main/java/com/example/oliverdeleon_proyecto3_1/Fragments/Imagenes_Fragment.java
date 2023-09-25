package com.example.oliverdeleon_proyecto3_1.Fragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import com.example.oliverdeleon_proyecto3_1.Adapters.ImageAdapter;
import com.example.oliverdeleon_proyecto3_1.Adapters.ImageAdapterr;
import com.example.oliverdeleon_proyecto3_1.GaleriaActivity;
import com.example.oliverdeleon_proyecto3_1.R;
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
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Imagenes_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Imagenes_Fragment extends Fragment {


    ImageView imageView;
    GridView gridView ;
//    File[] alliImgs;
  ArrayList<File> alliImgs;
    ImageAdapter imageAdapter;

    private RecyclerView mRecyclerView;


    File filedefile;
    File mGalleryFolder;
    private String GALLERY_LOCATION = "image gallery";

    View root;


    private Uri mImageUri;
    ImageView mImageView;
//    private Animator mCurrentAnimator;
//    private int mLongAnimationDuration;

    private static final int REQUEST_OPEN_RESULT_CODE = 0;
    private static final int RESULT_OKAY = -1;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Imagenes_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fotos_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Imagenes_Fragment newInstance(String param1, String param2) {
        Imagenes_Fragment fragment = new Imagenes_Fragment();
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
    public void onResume() {
        super.onResume();
        if (mGalleryFolder==null){
            imageView.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(),"Vacio",Toast.LENGTH_SHORT).show();
        }else {
            imageView.setVisibility(View.INVISIBLE);
            RecyclerView.Adapter imageAdapterr = new ImageAdapterr(sortFileToLast(mGalleryFolder));
            mRecyclerView.setAdapter(imageAdapterr);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fotos_, container, false);

        GaleriaActivity galeriaActivity = new GaleriaActivity();

        imageView = root.findViewById(R.id.imageLibrary);

        createImageGallery();


        mRecyclerView = root.findViewById(R.id.galleryRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(layoutManager);


        AskPermision();




        return root;
    }//onCreateView

//
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = requireContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }



    private void createImageGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mGalleryFolder = new File(storageDirectory, GALLERY_LOCATION);
        if(!mGalleryFolder.exists()) {
            mGalleryFolder.mkdirs();
        }

    }




    private void AskPermision() {
        Dexter.withContext(getContext()).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
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


    // metodo prar ordenar imagenes por la mas reciente
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


}//Images_Fragments