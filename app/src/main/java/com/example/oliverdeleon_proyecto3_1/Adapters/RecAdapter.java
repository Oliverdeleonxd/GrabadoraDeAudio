package com.example.oliverdeleon_proyecto3_1.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oliverdeleon_proyecto3_1.Fecha;
import com.example.oliverdeleon_proyecto3_1.Fragments.Recordings_Fragment;
import com.example.oliverdeleon_proyecto3_1.R;

import com.example.oliverdeleon_proyecto3_1.OnSelectListener;
import com.google.android.material.transition.Hold;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {
    private Context context;
//    private List<File> fileList;
    private File[] allFiles;
    private OnSelectListener listener;
    onItemListClick onItemListClick;

    Activity activity;

    Fecha fecha;

//    Date date = new Date();
//
//    SimpleDateFormat holaf = new SimpleDateFormat( "h:mm:ss a", Locale.getDefault());





    public RecAdapter( File [] allFiles,onItemListClick onItemListClick) {
//        this.context = context;
//        this.fileList = fileList;
        this.allFiles = allFiles;
//        this.listener = listener;
        this.onItemListClick = onItemListClick;
    }



//    public RecAdapter(File[] allfiles, Recordings_Fragment onItemListClick) {
//    }

//    public RecAdapter(Context context, File[] allfiles, Recordings_Fragment recordings_fragment) {
////        this.allFiles = allFiles;
//////        this.listener = listener;
////        this.onItemListClick = onItemListClick;
//    }

//    public RecAdapter(File[] allFiles, Recordings_Fragment listener) {
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler,parent,false);
//        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_recycler,parent,false));
        fecha = new Fecha();
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {



//        holder.tvName.setText(fileList.get(position).getName());
        holder.tvName.setText(allFiles[position].getName());
        holder.tvName.setSelected(true);
        holder.tvFecha.setText(MediaStore.Audio.Media._ID);
//        holaf.format(date);
//        holder.tvFecha.setText((CharSequence) holaf);

//        holder.tvFecha.setText();
//        holder.cardcontainer.setBackgroundColor(Integer.parseInt("#0068D1"));






//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onSelected(fileList.get(position));
//            }
//        });


        holder.btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu2,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.btnEliminar:{

                             //   eliminarArchivo(position,v);
                                Toast.makeText(v.getContext(), "Eliminar quizas Proximamente :c",Toast.LENGTH_SHORT).show();


                            }break;
                            case R.id.btnRename:{
                                Toast.makeText(v.getContext(), "Proximamente dije >:c",Toast.LENGTH_SHORT).show();
                            }break;
                        }
                        return false;
                    }
                });


//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//
//                builder.setTitle("Confirmacion");
//                builder.setMessage("Estas seguro que desea eliminar?");
//
//                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(activity,"Si :c",Toast.LENGTH_SHORT).show();
////                        Toast.makeText(activity,":D",Toast.LENGTH_SHORT).show();
////                        Toast.makeText(activity,"id: "+id,Toast.LENGTH_SHORT).show();
//                       // allFiles[position].getAbsoluteFile().delete();
//
//
//
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        Toast.makeText(activity,"No :3",Toast.LENGTH_SHORT).show();
//                        // Do nothing
//                        dialog.dismiss();
//                    }
//                });
//
//                AlertDialog alert = builder.create();
//                alert.show();


            }

            private void eliminarArchivo(int position, View v) {
                MediaStore mediaStore = new MediaStore();
                String id = MediaStore.Audio.Media._ID;

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong( allFiles[position]+id));

                File file = new File( allFiles[position].getPath());
                boolean deleted = file.delete();
                if (deleted){
                    context.getContentResolver().delete(contentUri,null,null);

                    allFiles[position].delete();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,allFiles.length);
                    Toast.makeText(v.getContext(), "Eliminado :c",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(v.getContext(), "no se pudo eliminar :c",Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public interface onItemListClick{
        void onClickListener(File file,int position);

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvName,tvFecha;
        public ConstraintLayout container;
        public ConstraintLayout cardcontainer;
        public ImageButton btnmenu;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNombre);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            container = itemView.findViewById(R.id.container);
            cardcontainer = itemView.findViewById(R.id.cardcontainer);
            btnmenu = itemView.findViewById(R.id.btnmenu);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    onItemListClick.onClickListener(fileList.get(getAdapterPosition()),getAdapterPosition());
//                }
//            });

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

        onItemListClick.onClickListener(allFiles[getAdapterPosition()],getAdapterPosition());



        }
    }

}


