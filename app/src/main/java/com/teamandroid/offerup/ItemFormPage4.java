package com.teamandroid.offerup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class ItemFormPage4 extends AppCompatActivity {

    Bundle b;


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

        TextView tempItemName = findViewById(R.id.tempItemName);
        TextView tempPrice = findViewById(R.id.tempPrice);
        TextView tempCategory = findViewById(R.id.tempCategory);

        tempItemName.setText(b.getString("ITEM_NAME"));
        tempPrice.setText(String.format(Locale.getDefault(),"%f", b.getFloat("ITEM_PRICE")));
        tempCategory.setText(b.getString("CATEGORY"));
    }

    public void toPageOne(View view) {
        Intent intent = new Intent(this, ItemFormPage1.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void toPageTwo(View view) {
        Intent intent = new Intent(this, ItemFormPage2.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void toPageThree(View view) {
        Intent intent = new Intent(this, ItemFormPage3.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void toHome (View view) {
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }
}
