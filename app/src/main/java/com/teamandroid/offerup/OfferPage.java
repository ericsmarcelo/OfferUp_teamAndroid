package com.teamandroid.offerup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

public class OfferPage extends AppCompatActivity {

    private EditText userOffer;

    public FirebaseAuth fbAuth;
    public FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_page);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userOffer = findViewById(R.id.userOffer);

        TextView sellerOffer = findViewById(R.id.sellerOffer);
        sellerOffer.setText(getIntent().getStringExtra("ITEM_PRICE"));
    }

    private void offerButton(View view) {
        Intent intent = new Intent(OfferPage.this, HomePage.class);
        startActivity(intent);
    }
}