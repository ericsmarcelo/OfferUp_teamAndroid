package com.teamandroid.offerup;

import android.content.Intent;
import android.graphics.Bitmap;
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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bundle = data.getExtras();
            Bitmap imageBitmap = (Bitmap)bundle.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

//        if(getIntent().getExtras() == null){
//            bundle = new Bundle();
//        }
//        else{
//            bundle = getIntent().getExtras();
//        }

    }

    @Override
    public void onResume(){
        super.onResume();
        if(bundle.getString("ITEM_NAME") != null) {
            itemName.setText(bundle.getString("ITEM_NAME"));
        }
    }

    public void toPageTwo(View view) {
        bundle.putString("ITEM_NAME", itemName.getText().toString());
        Intent intent = new Intent(this, ItemFormPage2.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void toPageThree(View view) {
        bundle.putString("ITEM_NAME", itemName.getText().toString());
        Intent intent = new Intent(this, ItemFormPage3.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void toPageFour(View view) {
        bundle.putString("ITEM_NAME", itemName.getText().toString());
        Intent intent = new Intent(this, ItemFormPage4.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}