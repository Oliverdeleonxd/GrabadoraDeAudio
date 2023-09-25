package com.example.oliverdeleon_proyecto3_1.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.oliverdeleon_proyecto3_1.Fragments.Imagenes_Fragment;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context mContex;

//    private File[] alliImgs;
    ArrayList<File>alliImgs;


//    public int[] imgArray={
//
//
//    };


    public ImageAdapter(File [] alliImgs1, ArrayList<File>alliImgs,Context mContex) {
        this.mContex = mContex;
        this.alliImgs = alliImgs;
    }

    public ImageAdapter() {

    }

//    public ImageAdapter(File[] alliImgs, Context context) {
//    }

//    public ImageAdapter(File[] alliImgs) {
//        this.alliImgs = alliImgs;
//    }


    @Override
    public int getCount() {
//        return alliImgs.length;
        return  alliImgs.size();
    }

    @Override
    public Object getItem(int position) {
//        return alliImgs[position];
        return alliImgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        convertView = getLa
//
        ImageView imageView = new ImageView(mContex);

//        imageView.setImageResource(getItem(position));
//        imageView.setImageResource(alliImgs[position].get);
        imageView.setImageURI(Uri.parse(getItem(position).toString()));


        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(200,200));





        return imageView;
    }
}
