package com.teamandroid.offerup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Welcome extends AppCompatActivity {

    public FirebaseAuth fbAuth;
    public FirebaseUser fbUser;
    private Menu menu;

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public DatabaseReference mDatabase;
    public ProgressDialog progressDialog;
    public List<Upload> uploads;
    public String DatabasePath = "posts";

    static final int AUTH_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        uploads = new ArrayList<>();

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference(DatabasePath);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                //creating adapter
                adapter = new RecyclerViewAdapter(getApplicationContext(), uploads);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_REQUEST && resultCode == RESULT_OK) {
            if(data.getBooleanExtra("loginStatus", true)) {
                fbUser = fbAuth.getCurrentUser();
                menu.findItem(R.id.action_profile).setVisible(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set global menu variable pointing to current menu
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        if (fbUser != null) {
            // if user is logged in, hide the "log in" option in menu
            menu.findItem(R.id.action_authentication).setVisible(false);
            menu.findItem(R.id.action_registration).setVisible(false);
        }
        else {
            // if user is not logged in, hide the "log out" and "profile" options in menu
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_profile).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // function called every time someone opens the menu
        if (fbUser != null) {
            // if user is logged in, hide the "log in" option in menu
            menu.findItem(R.id.action_authentication).setVisible(false);
            menu.findItem(R.id.action_registration).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_profile).setVisible(true);
        }
        else {
            // if user is not logged in, hide the "log out" and "profile" options in menu
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_profile).setVisible(false);
            menu.findItem(R.id.action_authentication).setVisible(true);
            menu.findItem(R.id.action_registration).setVisible(true);
        }
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
        }
        else if (id == R.id.action_authentication) {
            Intent intent = new Intent(this, Authentication.class);
            startActivityForResult(intent, AUTH_REQUEST);
            return true;
        }
        else if (id == R.id.action_registration) {
            Intent intent = new Intent (this, Registration.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_profile) {
            Intent intent = new Intent(this, UserProfile.class);
            intent.putExtra("profileUid", fbUser.getUid());
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_logout) {
            // sign out, give notice to user
            fbAuth.signOut();
            fbUser = null;
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

            // After logged out, change menu to show log in button and hide log out button
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_profile).setVisible(false);
            menu.findItem(R.id.action_authentication).setVisible(true);
            menu.findItem(R.id.action_registration).setVisible(true);

            return true;
        }
//        else if (id == R.id.action_googleIn) {
//            Intent intent = new Intent (this, GoogleInActivity.class);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //@Override
    /*public void onItemClick(DataModel item) {
        // launch activity to view specific item
        Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        fbUser = fbAuth.getCurrentUser();
    }
}
