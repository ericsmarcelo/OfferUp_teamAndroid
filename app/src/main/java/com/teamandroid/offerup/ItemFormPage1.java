package com.teamandroid.offerup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ItemFormPage1 extends AppCompatActivity {

    private Bundle bundle;
    private EditText itemName;
    private String image;

    private FirebaseAuth fbAuth;

    public ImageView imageView;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form_page1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }

        Button cameraButton = findViewById(R.id.cameraButton);
        Button galleryButton = findViewById(R.id.galleryButton);
        itemName = findViewById(R.id.itemName);
        imageView = findViewById(R.id.imageView);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getPictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getPictureIntent.setType("image/*");
                if (getPictureIntent.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(getPictureIntent, REQUEST_IMAGE_GALLERY);
                }
            }
        });

        bundle = new Bundle();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bundle = new Bundle();

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bitmap imageBitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);

            bundle.putBundle("IMAGE", data.getExtras());
        }
        else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(imageBitmap);

                bundle.putString("IMAGE_URI", data.getData().toString());
            } catch(FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void toPageTwo(View view) {

        if(bundle.getBundle("IMAGE") == null && bundle.getString("IMAGE_URI") == null) {
            Toast.makeText(this, "Please add image.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(itemName.getText().length() == 0) {
            Toast.makeText(this, "Please add item name.", Toast.LENGTH_SHORT).show();
            return;
        }
        bundle.putString("ITEM_NAME", itemName.getText().toString());
        Intent intent = new Intent(this, ItemFormPage2.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}