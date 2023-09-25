package com.example.oliverdeleon_proyecto3_1.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oliverdeleon_proyecto3_1.R;
import com.example.oliverdeleon_proyecto3_1.View_image;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by nigelhenshaw on 25/06/2015.
 */
public class ImageAdapterr extends RecyclerView.Adapter<ImageAdapterr.ViewHolder> {

//    private File imagesFile;
private File[] imagesFile;
    public ImageAdapterr(File[] folderFile) {
        imagesFile = folderFile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_images_relative_layout, parent, false);
        return new ViewHolder(view);
    }


//    @Override
//    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
//
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File imageFiles = imagesFile[position];
//        File imageFile = imagesFile.listFiles()[position];
//        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//        holder.getImageView().setImageBitmap(imageBitmap);
        Picasso.with(holder.getImageView().getContext()).load(imageFiles).resize(200,200).into(holder.getImageView());



    }

//    @Override
//    public int getItemCount() {
//        return imagesFile.listFiles().length;
//    }

    @Override
    public int getItemCount() {
        return imagesFile.length;
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageGalleryView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(view.getContext(), "IMAGEN PRESIONADA",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(view.getContext(), View_image.class);
//                    intent.putExtra("img",imagesFile[getAdapterPosition()]);
//                    intent.putIntegerArrayListExtra("img",imagesFile[getAdapterPosition()]);
//                   view.getContext().startActivity(intent);
                }
            });
        }

        public ImageView getImageView() {
            return imageView;
        }//
    }
}
