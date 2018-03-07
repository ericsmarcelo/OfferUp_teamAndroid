package com.teamandroid.offerup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemFormPage4 extends AppCompatActivity {

    Bundle b;

    FirebaseDatabase fbDatabase;
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    FirebaseStorage fbStorage;

    List<Object> userPosts;

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

        if (fbUser == null) {
            // no logged in user, go back to home activity
            Toast.makeText(this, "Error, user not logged in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }

        // initialize user post list for later use in ToHome function
        userPosts = new ArrayList<>();
        fbDatabase.getReference().child("users").child(fbUser.getUid()).child("posts").addListenerForSingleValueEvent(userPostListener);

        TextView tempItemName = findViewById(R.id.tempItemName);
        TextView tempPrice = findViewById(R.id.tempPrice);
        TextView tempCategory = findViewById(R.id.tempCategory);
        TextView tempDescription = findViewById(R.id.tempDescription);

        tempItemName.setText(b.getString("ITEM_NAME"));
        tempPrice.setText(String.format(Locale.getDefault(),"%f", b.getFloat("ITEM_PRICE")));
        tempCategory.setText(b.getString("CATEGORY"));
        tempDescription.setText(b.getString("ITEM_DESC"));

        tempDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    // finish posting the item
    public void toHome (View view) {
        // get unique id with push() function, we don't actually add any data here!
        String uniqueID = fbDatabase.getReference().child("images").push().getKey();

        // upload image to firebase storage, get uri back
        fbStorage = FirebaseStorage.getInstance();
        StorageReference imagesRef = fbStorage.getReference().child("images/"+uniqueID+".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap postImage = null;

        if (b.getBundle("IMAGE") != null) {
            //postImage = (Bitmap)b.getBundle("IMAGE").get("data");
            Uri imageUri = Uri.parse(b.getString("IMAGE"));
            try {
                postImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch(IOException e) {
                e.printStackTrace();
                Toast.makeText(ItemFormPage4.this, "Loading image failed.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (b.getString("IMAGE_URI") != null) {
            Uri imageUri = Uri.parse(b.getString("IMAGE_URI"));
            try {
                postImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // crudely resize postImage bitmap if its too big (it probably will be)
                int imageWidth = postImage.getWidth();
                int imageHeight = postImage.getHeight();
                if (imageWidth > 2000 || imageHeight > 2000) {
                    imageWidth = (int)(imageWidth*0.25);
                    imageHeight = (int)(imageHeight*0.25);
                    postImage = Bitmap.createScaledBitmap(postImage, imageWidth, imageHeight, true);
                }
            } catch(IOException e) {
                e.printStackTrace();
                Toast.makeText(ItemFormPage4.this, "Converting image uri to bitmap went wrong.", Toast.LENGTH_LONG).show();
            }



        }
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
                // Get url of image from storage snapshot
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String downloadString = downloadUrl.toString();

                // create Post object for storing in database
                Post newPost = new Post();
                newPost.setItemName(b.getString("ITEM_NAME"));
                newPost.setPrice(b.getFloat("ITEM_PRICE"));
                newPost.setDescription(b.getString("ITEM_DESC"));
                newPost.setCondition(b.getString("ITEM_COND"));
                newPost.setImage(downloadString);
                newPost.setOwner(fbUser.getUid());

                List<String> itemCategories = new ArrayList<>();
                itemCategories.add(b.getString("CATEGORY"));
                newPost.setCategory(itemCategories);

                // upload the Post object to database at auto-generated child key
                DatabaseReference postRef = fbDatabase.getReference().child("posts").push();
                postRef.setValue(newPost);

                // get key of post and save it in the user Posts list
                String postKey = postRef.getKey();
                DatabaseReference userRef = fbDatabase.getReference().child("users").child(fbUser.getUid());
                //userPosts = new ArrayList<>();
                //userRef.child("posts").addListenerForSingleValueEvent(userPostListener);
                userPosts.add(postKey);
                userRef.child("posts").setValue(userPosts);

                Toast.makeText(ItemFormPage4.this, "Finished Post Upload", Toast.LENGTH_SHORT).show();

                // go back to home page
                // in future, make it go back to view the posting
                Intent intent = new Intent(ItemFormPage4.this, HomePage.class);
                startActivity(intent);
            }
        });
    }

    ValueEventListener userPostListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<ArrayList<Object>> t = new GenericTypeIndicator<ArrayList<Object>>() {};
            if (dataSnapshot.getValue(t) != null) {
                // only set userPosts to current firebase user post list if such list exists
                // if the user has no current posts, there will be no list in firebase, so we don't set it
                //userPosts = new ArrayList<Object>(dataSnapshot.getValue(ArrayList.class));
                userPosts = dataSnapshot.getValue(t);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // do nothing
        }
    };
}


