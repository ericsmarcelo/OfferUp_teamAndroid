package com.teamandroid.offerup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemFormPage4 extends AppCompatActivity {

    Bundle b;

    FirebaseDatabase fbDatabase;
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    FirebaseStorage fbStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form_page4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().getExtras() == null){
            b = new Bundle();
        }
        else{
            b = getIntent().getExtras();
        }

        fbDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        TextView tempItemName = findViewById(R.id.tempItemName);
        TextView tempPrice = findViewById(R.id.tempPrice);
        TextView tempCategory = findViewById(R.id.tempCategory);

        tempItemName.setText(b.getString("ITEM_NAME"));
        tempPrice.setText(String.format(Locale.getDefault(),"%f", b.getFloat("ITEM_PRICE")));
        tempCategory.setText(b.getString("CATEGORY"));
    }

//    public void toPageOne(View view) {
//        Intent intent = new Intent(this, ItemFormPage1.class);
//        intent.putExtras(b);
//        startActivity(intent);
//    }
//
//    public void toPageTwo(View view) {
//        Intent intent = new Intent(this, ItemFormPage2.class);
//        intent.putExtras(b);
//        startActivity(intent);
//    }
//
//    public void toPageThree(View view) {
//        Intent intent = new Intent(this, ItemFormPage3.class);
//        intent.putExtras(b);
//        startActivity(intent);
//    }

    // finish posting the item
    public void toHome (View view) {
        // upload image to firebase storage, get uri back
        fbStorage = FirebaseStorage.getInstance();
        StorageReference imagesRef = fbStorage.getReference().child("images");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap postImage = (Bitmap)b.getBundle("IMAGE").get("data");
        postImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageData = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Upload failed
                Toast.makeText(ItemFormPage4.this, "Image upload failed. Try Again.", Toast.LENGTH_SHORT).show();
                Log.e("ItemFormPage4", "Image upload failed. Exception: " + e.getMessage());
                return;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String downloadString = downloadUrl.toString();

                Post newPost = new Post();
                newPost.setItemName(b.getString("ITEM_NAME"));
                newPost.setPrice(b.getFloat("ITEM_PRICE"));
                newPost.setDescription(b.getString("ITEM_DESC"));
                newPost.setCondition(b.getString("ITEM_COND"));
                newPost.setImage(downloadString);

                List<String> itemCategories = new ArrayList<>();
                itemCategories.add(b.getString("ITEM_CATEGORY"));
                newPost.setCategory(itemCategories);



            }
        });

//        imagesRef.putBytes(b.getBundle("IMAGE").get("data"));

        // add post to the Posts database entry


        // add post to user post lists



        // go back to welcome page
        // in future, make it go back to view the posting
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }
}
