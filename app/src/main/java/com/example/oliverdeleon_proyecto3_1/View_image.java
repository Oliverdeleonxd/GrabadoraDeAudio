package com.example.oliverdeleon_proyecto3_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class View_image extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        imageView = findViewById(R.id.imgview123);

        Intent intent = getIntent();
        imageView.setImageURI(intent.getData());

        imageView.setImageURI(intent.getData());




    }





}