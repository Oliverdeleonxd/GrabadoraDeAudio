package com.example.oliverdeleon_proyecto3_1.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oliverdeleon_proyecto3_1.R;
import com.example.oliverdeleon_proyecto3_1.VideoFolder;
import com.example.oliverdeleon_proyecto3_1.VideoModel;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder>{

    private ArrayList<String> folderName;
    private ArrayList<VideoModel> videoModels;
    private Context context;

    public FolderAdapter(ArrayList<String> folderName, ArrayList<VideoModel> videoModels, Context context) {
        this.folderName = folderName;
        this.videoModels = videoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.folder_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        int index = folderName.get(position).lastIndexOf("/");
        String foldersNames = folderName.get(position).substring(index+1);

        holder.name.setText(foldersNames);
        holder.countVideos.setText(String.valueOf(countVideos(folderName.get(position))));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoFolder.class);
//                intent.putExtra("folderName", folderName);
                intent.putExtra("folderName", folderName.get(position));
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return folderName.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,countVideos;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.folderName);
        countVideos = itemView.findViewById(R.id.videoCount);

    }
}


    int countVideos(String folders) {
        int count = 0;
        for (VideoModel model : videoModels) {
            if (model.getPath().substring(0,
                    model.getPath().lastIndexOf("/"))
                    .endsWith(folders)) {
                count++;
            }
        }
        return count;
    }


}//FolderAdapter
