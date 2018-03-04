package com.teamandroid.offerup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FirebaseAuth fbAuth;
    public FirebaseUser fbUser;
    NavigationView navigationView;
    private Menu menu;
    TextView userName,userEmail;
    public int count;

    RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public DatabaseReference mDatabase,userDatabase;
    public ProgressDialog progressDialog;
    public List<Upload> uploads;
    public String DatabasePath = "posts";
    public TextView notifications;;

    static final int AUTH_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fbUser == null) {
                    // if not logged in, make user log in first
                    Snackbar.make(view, "Please log in to post an item.", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null).show();
//                    Intent intent = new Intent(getApplicationContext(), Authentication.class);
//                    startActivityForResult(intent, AUTH_REQUEST);
                }
                else {
                    // if user logged in, take them to post item form
                    Intent intent = new Intent(getApplicationContext(), ItemFormPage1.class);
                    startActivity(intent);

                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        uploads = new ArrayList<>();

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference(DatabasePath);
        userDatabase = FirebaseDatabase.getInstance().getReference("users");

        final HashMap<String,String> users = new HashMap<>();

        DatabaseReference notificationsDB = FirebaseDatabase.getInstance().getReference("offer").child("offer");

        notificationsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                count = 0;
                for(DataSnapshot snap: dataSnapshot.getChildren())
                {
                    OffersDat offs = snap.getValue(OffersDat.class);
                    if(fbUser.getUid().equals(offs.offerfor)) {
                        count++;
                    }
                }

                notifications.setText(count+"");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot usersData: dataSnapshot.getChildren())
                {

                    UserList user = usersData.getValue(UserList.class);
                    users.put(usersData.getKey(),user.name);

                }

                userName.setText(users.get(fbUser.getUid()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if(!fbUser.getUid().equals(upload.owner)) {
                        upload.owner = users.get(upload.getOwner());
                        upload.key = postSnapshot.getKey();
                        //Log.e("Danish: ",fbUser.getUid()+" "+upload.key);

                        uploads.add(upload);
                    }
                }

                Collections.reverse(uploads);
                //creating adapter
                adapter = new RecyclerViewAdapter(getApplicationContext(), uploads, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View v, int pos) {
                        Intent intent = new Intent(getApplicationContext(),ItemDetails.class);
                        String s[] = {uploads.get(pos).key,uploads.get(pos).owner};
                        intent.putExtra("Key",s);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(),uploads.get(pos).key+" Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
    });
        userName = (TextView)header.findViewById(R.id.username);
        userEmail = (TextView) header.findViewById(R.id.email);

        if (fbUser != null) {

            // get all parameters from the User (to avoid multiple function calls per each parameter)
            Log.e("Danish:",fbUser.getProviders().get(0));
            userName.setText(users.get(fbUser.getUid()));
            userEmail.setText(fbUser.getEmail());
        }
        else
        {
            userName.setText("Please log in");
            userEmail.setText("");
        }


//These lines should be added in the OnCreate() of your main activity
        notifications=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.notifications));

        notifications.setGravity(Gravity.CENTER_VERTICAL);
        notifications.setTypeface(null, Typeface.BOLD);
        notifications.setTextColor(getResources().getColor(R.color.colorAccent));
    }


    @Override
    protected void onResume() {
        super.onResume();

        // update navigation drawer based on user log in status
        updateNav();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (fbUser != null) {

                // get all parameters from the User (to avoid multiple function calls per each parameter)
                userName.setText(fbUser.getDisplayName());
                userEmail.setText(fbUser.getEmail());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // do nothing
        }
    };


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

        Menu navMenu =navigationView.getMenu();

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        if (fbUser != null) {
            // if user is logged in, hide the "log in" option in menu
            menu.findItem(R.id.action_authentication).setVisible(false);
            menu.findItem(R.id.action_registration).setVisible(false);
            navMenu.findItem(R.id.login).setVisible(false);
            navMenu.findItem(R.id.signup).setVisible(false);
            navMenu.findItem(R.id.logout).setVisible(true);
            navMenu.findItem(R.id.profile).setVisible(true);


        }
        else {
            // if user is not logged in, hide the "log out" and "profile" options in menu
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_profile).setVisible(false);
            navMenu.findItem(R.id.login).setVisible(true);
            navMenu.findItem(R.id.signup).setVisible(true);
            navMenu.findItem(R.id.logout).setVisible(false);
            navMenu.findItem(R.id.profile).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Menu navMenu = navigationView.getMenu();
        // function called every time someone opens the menu
        if (fbUser != null) {
            // if user is logged in, hide the "log in" option in menu
            menu.findItem(R.id.action_authentication).setVisible(false);
            menu.findItem(R.id.action_registration).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_profile).setVisible(true);
            navMenu.findItem(R.id.login).setVisible(false);
            navMenu.findItem(R.id.signup).setVisible(false);
            navMenu.findItem(R.id.logout).setVisible(true);
            navMenu.findItem(R.id.profile).setVisible(true);

        }
        else {
            // if user is not logged in, hide the "log out" and "profile" options in menu
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_profile).setVisible(false);
            menu.findItem(R.id.action_authentication).setVisible(true);
            menu.findItem(R.id.action_registration).setVisible(true);
            navMenu.findItem(R.id.login).setVisible(true);
            navMenu.findItem(R.id.signup).setVisible(true);
            navMenu.findItem(R.id.logout).setVisible(false);
            navMenu.findItem(R.id.profile).setVisible(false);

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
            updateNav();

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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent = new Intent(this, UserProfile.class);
            intent.putExtra("profileUid", fbUser.getUid());
            startActivity(intent);
        } else if (id == R.id.logout) {
            fbAuth.signOut();
            fbUser = null;
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

            // After logged out, change menu to show log in button and hide log out button
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_profile).setVisible(false);
            menu.findItem(R.id.action_authentication).setVisible(true);
            menu.findItem(R.id.action_registration).setVisible(true);
            updateNav();

        } else if (id == R.id.login) {
            Intent intent = new Intent(this, Authentication.class);
            startActivityForResult(intent, AUTH_REQUEST);

        } else if (id == R.id.signup) {

            Intent intent = new Intent (this, Registration.class);
            startActivity(intent);

        } else if (id == R.id.home) {
            Intent intent = new Intent (this, HomePage.class);
            startActivity(intent);

        } else if (id == R.id.settings) {
            Intent intent = new Intent (this, OfferPage.class);
            startActivity(intent);
        }

        else if (id == R.id.notifications) {
            Intent intent = new Intent (this, Notifications.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // updates navigation drawer based on user log in status
    // avoids having the same lines of code everywhere
    public void updateNav() {
        Menu navMenu = navigationView.getMenu();
        if (fbUser != null) {
            // if user is logged in, hide the "log in" option in menu
            navMenu.findItem(R.id.login).setVisible(false);
            navMenu.findItem(R.id.signup).setVisible(false);
            navMenu.findItem(R.id.logout).setVisible(true);
            navMenu.findItem(R.id.profile).setVisible(true);
        }
        else {
            // if user is not logged in, hide the "log out" and "profile" options in menu
            navMenu.findItem(R.id.login).setVisible(true);
            navMenu.findItem(R.id.signup).setVisible(true);
            navMenu.findItem(R.id.logout).setVisible(false);
            navMenu.findItem(R.id.profile).setVisible(false);
        }
    }



}
