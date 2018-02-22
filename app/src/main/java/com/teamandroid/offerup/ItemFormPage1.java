package com.teamandroid.offerup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ItemFormPage1 extends AppCompatActivity {
    private Bundle b;
    private EditText itemName;
    private String image;

    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form_page1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().getExtras() == null){
            b = new Bundle();
        }
        else{
            b = getIntent().getExtras();
        }
        itemName = (EditText) findViewById(R.id.itemName);

        //fbAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = fbAuth.getCurrentUser();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(b.getString("ITEM_NAME") != null) {
            itemName.setText(b.getString("ITEM_NAME"));
        }
    }

    public void toPageTwo(View view) {
        b.putString("ITEM_NAME", itemName.getText().toString());
        Intent intent = new Intent(this, ItemFormPage2.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    public void toPageThree(View view) {
        b.putString("ITEM_NAME", itemName.getText().toString());
        Intent intent = new Intent(this, ItemFormPage3.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void toPageFour(View view) {
        b.putString("ITEM_NAME", itemName.getText().toString());
        Intent intent = new Intent(this, ItemFormPage4.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}