package com.teamandroid.offerup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ItemFormPage1 extends AppCompatActivity {

    private EditText itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form_page1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemName.setText("Enter Item Name");
    }

    public void toPageTwo(View view) {
        Intent intent = new Intent(this, ItemFormPage2.class);
        startActivity(intent);
    }
    public void toPageThree(View view) {
        Intent intent = new Intent(this, ItemFormPage3.class);
        startActivity(intent);
    }

    public void toPageFour(View view) {
        Intent intent = new Intent(this, ItemFormPage4.class);
        startActivity(intent);
    }
}
