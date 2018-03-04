package com.teamandroid.offerup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ItemDetails extends AppCompatActivity {

    public DatabaseReference mDatabase;
    public ProgressDialog progressDialog;
    public TextView name,price,condition,category,description,user;
    public ImageView photo;
    public LinearLayout makeoffer;
    public FirebaseDatabase fbDatabase;
    public FirebaseAuth fbAuth;
    public FirebaseUser fbUser;
    public String userKey,itemkey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        fbDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        final DatabaseReference databaseReference = fbDatabase.getReference("offer");

       final String s[] =getIntent().getStringArrayExtra("Key");
        Log.e("Danish: ",s[0]);
        name = (TextView) findViewById(R.id.itemName);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
        condition = (TextView) findViewById(R.id.condition);
        category = (TextView) findViewById(R.id.category);
        user = (TextView) findViewById(R.id.postedBy);
        photo = (ImageView) findViewById(R.id.prodImg);
        makeoffer = (LinearLayout) findViewById(R.id.makeoffer);

        makeoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Offers offer = new Offers();
                offer.offerby = fbUser.getUid();
                offer.offerfor = userKey;
                offer.offerprice = 200;
                offer.itemid = itemkey;
                DatabaseReference offers = databaseReference.child("offer").push();
                offers.setValue(offer);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("posts");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("Danish: ",postSnapshot.getKey());
                   if(postSnapshot.getKey().equals(s[0]))
                   {
                       PostTemplate temp = postSnapshot.getValue(PostTemplate.class);
                       itemkey =temp.image;
                       userKey = temp.owner;
                       name.setText(temp.itemName);
                       category.setText("Category"+temp.category.get(0));
                       condition.setText("Condition"+temp.condition);
                       price.setText("$"+(int)temp.price+"");
                       description.setText(temp.description);
                       user.setText(s[1]);
                       Glide.with(getApplicationContext()).load(temp.image).into(photo);
                   }
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    class Offers
    {
        public String offerby, offerfor, itemid; public long offerprice;
    }
}
