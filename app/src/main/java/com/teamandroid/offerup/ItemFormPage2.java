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

public class ItemFormPage2 extends AppCompatActivity {

    private Bundle b;
    private EditText itemDesc;

    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form_page2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().getExtras() == null){
            b = new Bundle();
        }
        else{
            b = getIntent().getExtras();
        }
        itemDesc = (EditText) findViewById(R.id.itemDesc);

        //fbAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = fbAuth.getCurrentUser();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(b.getString("ITEM_NAME") != null) {
            itemDesc.setText(b.getString("ITEM_DESC"));
        }
    }

    public void toPageOne(View view) {
        b.putString("ITEM_DESC", itemDesc.getText().toString());
        Intent intent = new Intent(this, ItemFormPage1.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void toPageThree(View view) {
        b.putString("ITEM_DESC", itemDesc.getText().toString());
        Intent intent = new Intent(this, ItemFormPage3.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void toPageFour(View view) {
        b.putString("ITEM_DESC", itemDesc.getText().toString());
        Intent intent = new Intent(this, ItemFormPage4.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}
