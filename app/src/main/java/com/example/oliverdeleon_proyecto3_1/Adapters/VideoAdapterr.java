package com.example.oliverdeleon_proyecto3_1.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oliverdeleon_proyecto3_1.R;
import com.example.oliverdeleon_proyecto3_1.VideoModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by nigelhenshaw on 25/06/2015.
 */
public class VideoAdapterr extends RecyclerView.Adapter<VideoAdapterr.ViewHolder> {

//    private File imagesFile;
//private File[] videosFile;
    ArrayList<VideoModel> rvModelArray;
    Context context;
    VideoClickInterface videoClickInterface;

    @NonNull
    @Override
    public VideoAdapterr.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_videos_relative_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterr.ViewHolder holder, int position) {

        VideoModel videoMolder = rvModelArray.get(position);
//        holder.thumbnail.setImageBitmap(videoMolder.getVideoThumbNail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoClickInterface.onVideoClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return rvModelArray.size();
    }


//    public VideoAdapterr(File[] folderFile) {
//        videosFile = folderFile;
//    }


    public VideoAdapterr(ArrayList<VideoModel> rvModelArray, Context context, VideoClickInterface videoClickInterface) {
        this.rvModelArray = rvModelArray;
        this.context = context;
        this.videoClickInterface = videoClickInterface;
    }

//    @Override
////    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.gallery_videos_relative_layout, parent, false);
//
//        return new ViewHolder(view);
//    }



//    @Override
//    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//
//        VideoModel videoMolder = rvModelArray.get(position);
//        holder.thumbnail.setImageBitmap(videoMolder.getVideoThumbNail());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                videoClickInterface.onVideoClick(holder.getAdapterPosition());
//            }
//        });

//
//        File imageFiles = videosFile[position];
////        File imageFile = imagesFile.listFiles()[position];
////        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFiles.getAbsolutePath());
////        holder.getImageView().setImageBitmap(imageBitmap);
//        holder.getVideoView().setVideoURI(Uri.fromFile(imageFiles));
////        Picasso.with(holder.getImageView().getContext()).load(imageFiles).resize(200,200).into((Target) holder.getImageView());






//    @Override
//    public int getItemCount() {
//        return imagesFile.listFiles().length;
//    }
//
//    @Override
//    public int getItemCount() {
//        return rvModelArray.size();
//    }


     public static class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView imageView;

         ImageView thumbnail;
//         VideoView videoView;
        public ViewHolder(View view) {
            super(view);

//            videoView =  view.findViewById(R.id.videoGalleryView);
            thumbnail =  view.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(view.getContext(), "video PRESIONADA",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(view.getContext(), View_image.class);
//                    intent.putExtra("img",imagesFile[getAdapterPosition()]);
//                    intent.putIntegerArrayListExtra("img",imagesFile[getAdapterPosition()]);
//                   view.getContext().startActivity(intent);
                }
            });
        }

//        public VideoView getVideoView() {
//            return videoView;
//        }//


    }

    public interface VideoClickInterface{
        void onVideoClick(int position);

    }
}//class
