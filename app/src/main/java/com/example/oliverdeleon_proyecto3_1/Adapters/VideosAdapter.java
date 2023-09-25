package com.example.oliverdeleon_proyecto3_1.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oliverdeleon_proyecto3_1.Fragments.FolderAdapter;
import com.example.oliverdeleon_proyecto3_1.R;
import com.example.oliverdeleon_proyecto3_1.VideoModel;
import com.example.oliverdeleon_proyecto3_1.VideoPlayer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.Hold;

import java.io.File;
import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyHolder> {

    public static ArrayList<VideoModel> videoFolder = new ArrayList<>();
    Context context;

    public VideosAdapter(ArrayList<VideoModel> videoFolder, Context context) {
        this.videoFolder = videoFolder;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_view, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(videoFolder.get(position).getPath()).into(holder.thumbnail);
//                with(context).load(videoFolder.get(position).getPath()).into(holder.thumbnail);
        holder.title.setText(videoFolder.get(position).getTitle());
        holder.duration.setText(videoFolder.get(position).getDuration());
        holder.size.setText(videoFolder.get(position).getSize());
        holder.resolution.setText(videoFolder.get(position).getResolution());
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomsheetDialog   = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                View bottomsheetView= LayoutInflater.from(context).inflate(R.layout.file_menu,null);
                bottomsheetView.findViewById(R.id.menu_down).setOnClickListener(v1 -> {

                    bottomsheetDialog.dismiss();

                });
                bottomsheetView.findViewById(R.id.menu_delete).setOnClickListener(v1 -> {

                      deleteFiles(position,v);
                    bottomsheetDialog.dismiss();
//                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
//                    bottomsheetDialog.dismiss();

                });
                bottomsheetView.findViewById(R.id.menu_edit).setOnClickListener(v1 -> {
                    renameFiles(position,v);
                    bottomsheetDialog.dismiss();
//                    Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
//                    bottomsheetDialog.dismiss();

                });
                bottomsheetDialog.setContentView(bottomsheetView);
                bottomsheetDialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayer.class);
                intent.putExtra("p",position);
                context.startActivity(intent);
            }
        });


    }

    private void deleteFiles(int p,View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Deseas eliminar este arcvhivo?")
//                .setMessage(videoFolder.get(p)
//                        .getTitle()).setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//            }
//        }).setPositiveButton("Eliminar",new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                        Long.parseLong(videoFolder.get(p).getPath()));
//                File file = new File(videoFolder.get(p).getPath());
//                boolean deleted = file.delete();
//                if (deleted){
//                    context.getApplicationContext().getContentResolver()
//                            .delete(contentUri, null, null);
//                    videoFolder.remove(p);
//                    notifyItemRemoved(p);
//                    notifyItemRangeChanged(p,videoFolder.size());
//                    Snackbar.make(view,"Archivo Eliminado ",Snackbar.LENGTH_SHORT).show();
//
//                }else{
//                    Snackbar.make(view,"Intenteo de eliminar fallido",Snackbar.LENGTH_SHORT).show();
//                }
//            }
//        }).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("delete")
                .setMessage(videoFolder.get(p).getTitle())
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo
                        // leave it as empty
                    }
                }).setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        Long.parseLong(videoFolder.get(p).getId()));
                File file = new File(videoFolder.get(p).getPath());
                boolean deleted = file.delete();
                if (deleted){
                    context.getApplicationContext().getContentResolver()
                            .delete(contentUri,
                                    null, null);
                    videoFolder.remove(p);
                    notifyItemRemoved(p);
                    notifyItemRangeChanged(p,videoFolder.size());
                    Snackbar.make(view,"Archivo Eliminado", Snackbar.LENGTH_SHORT).show();

                }else {
                    Snackbar.make(view,"Fallo al eliminar",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        }).show();

    }//deleteFiles

    private void renameFiles(int p,View view){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.rename_file);
        final EditText editText = dialog.findViewById(R.id.rename_edit_text);
        Button cancel = dialog.findViewById(R.id.cancel_rename_button);
        Button rename_btn = dialog.findViewById(R.id.rename_button);
        final File renameFile = new File(videoFolder.get(p).getPath());
        String nameText = renameFile.getName();
        nameText = nameText.substring(0, nameText.lastIndexOf("."));
        editText.setText(nameText);
        editText.clearFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        rename_btn.setOnClickListener(v1->{
            String onlyPath = renameFile.getParentFile().getAbsolutePath();
            String ext = renameFile.getAbsolutePath();
            ext = ext.substring(ext.lastIndexOf("."));
            String newPath = onlyPath + "/" + editText.getText() + ext;
            File newFile = new File(newPath);
            boolean rename = renameFile.renameTo(newFile);
            if (rename){
                context.getApplicationContext().getContentResolver().
                        delete(MediaStore.Files.getContentUri("external"),
                                MediaStore.MediaColumns.DATA + "=?",
                                new String[]{renameFile.getAbsolutePath()});
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(newFile));
                context.getApplicationContext().sendBroadcast(intent);
                Snackbar.make(view,"Rename Success", Snackbar.LENGTH_SHORT).show();
            }else {
                Snackbar.make(view,"Rename Failed", Snackbar.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        dialog.show();



    }//renameFiles


    @Override
    public int getItemCount() {
        return videoFolder.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{


        ImageView thumbnail,menu;
        TextView title,size,duration,resolution;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            menu = itemView.findViewById(R.id.menu_image);
            thumbnail = itemView.findViewById(R.id.imgvide);
            title = itemView.findViewById(R.id.video_title);
            size = itemView.findViewById(R.id.video_size);
            duration = itemView.findViewById(R.id.video_duration);
            resolution = itemView.findViewById(R.id.video_quality);


        }
    }

}//
