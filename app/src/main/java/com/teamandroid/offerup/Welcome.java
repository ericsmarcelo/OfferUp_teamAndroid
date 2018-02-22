package com.teamandroid.offerup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity implements RecyclerViewAdapter.ItemListener {

    RecyclerView recyclerView;
    ArrayList<DataModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        arrayList.add(new DataModel("Item 1", R.drawable.battle, "#09A9FF"));
        arrayList.add(new DataModel("Item 2", R.drawable.beer, "#3E51B1"));
        arrayList.add(new DataModel("Item 3", R.drawable.ferrari, "#673BB7"));
        arrayList.add(new DataModel("Item 4", R.drawable.jetpack_joyride, "#4BAA50"));
        arrayList.add(new DataModel("Item 5", R.drawable.three_d, "#F94336"));
        arrayList.add(new DataModel("Item 6", R.drawable.terraria, "#0A9B88"));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, arrayList, this);
        recyclerView.setAdapter(adapter);

        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        recyclerView.setLayoutManager(layoutManager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_authentication) {
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_registration) {
            Intent intent = new Intent (this, Registration.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_googleIn) {
            Intent intent = new Intent (this, GoogleInActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_profile) {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        }
        else if (id == R.id.action_post) {
            Intent intent = new Intent(this, ItemFormPage1.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //@Override
    public void onItemClick(DataModel item) {

        Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();

    }
}
