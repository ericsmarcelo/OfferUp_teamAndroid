package com.teamandroid.offerup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OfferPage extends AppCompatActivity {

    private EditText userOffer;
    private Button offerButton;

    public FirebaseAuth fbAuth;
    public FirebaseUser fbUser;
    public FirebaseDatabase fbDatabase;

    private Offer newOffer;
    private Post currentItem;
    private List<Offer> listOffers;

    public String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_page);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        fbDatabase = FirebaseDatabase.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newOffer = new Offer();
        currentItem = new Post();
        listOffers = new ArrayList<>();
        itemId = getIntent().getStringExtra("ITEM_ID");

        TextView sellerOffer = findViewById(R.id.sellerOffer);
        sellerOffer.setText(getIntent().getStringExtra("ITEM_PRICE"));

        userOffer = findViewById(R.id.userOffer);
        final TextView itemName = findViewById(R.id.itemName);
        final TextView sellerPrice = findViewById(R.id.sellerOffer);

        offerButton = findViewById(R.id.offerButton);
        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send offer to database, and send message to seller

                // set values in the Offer object
                newOffer.setBuyerId(fbUser.getUid());
                newOffer.setSellerId(getIntent().getStringExtra("ITEM_OWNER"));
                newOffer.setOfferPrice(userOffer.getText().toString());

                // build message
                String message = fbUser.getEmail() + " is offering $" + newOffer.getOfferPrice() + " for your item: ";
                message += getIntent().getStringExtra("ITEM_NAME");
                newOffer.setMessage(message);

                // add this new offer to the list of offers the product already has
                listOffers.add(newOffer);

                // TODO: SEND MESSAGE TO SELLER FROM BUYER

                // send offer information to firebase
                fbDatabase.getReference("posts").child(itemId).child("offers").setValue(listOffers);

                // finish activity
                finish();

            }
        });

        fbDatabase.getReference("posts").child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentItem = dataSnapshot.getValue(Post.class);
                if (currentItem != null) {
                    if (currentItem.getOffers() != null) {
                        listOffers = currentItem.getOffers();
                    }

                    itemName.setText(currentItem.getItemName());
                    sellerPrice.setText("$" + String.format("%.2f", currentItem.getPrice()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

}