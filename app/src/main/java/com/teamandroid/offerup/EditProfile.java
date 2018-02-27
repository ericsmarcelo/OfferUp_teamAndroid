package com.teamandroid.offerup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {

    private FirebaseDatabase fbDatabase;
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private User currentUser;

    private EditText editName;
    private EditText editPhone;
    private EditText editCity;
    private EditText editState;
    private ImageView profilePicture;
    private Button newPhotoButton;
    private Button clearPhotoButton;

    public final int SAVE_CHANGES_CODE = 1;
    public final int REVERT_CHANGES_CODE = 2;
    public final int EXIT_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editName = findViewById(R.id.editProfileEditName);
        editPhone = findViewById(R.id.editProfileEditPhone);
        editCity = findViewById(R.id.editProfileEditCity);
        editState = findViewById(R.id.editProfileEditState);
        profilePicture = findViewById(R.id.viewProfilePicture);
        newPhotoButton = findViewById(R.id.newPhotoButton);
        clearPhotoButton = findViewById(R.id.clearPhotoButton);

        fbDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        if (fbUser == null) {
            // no user logged in, send them to login screen
            notifyUser("No user logged in");
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
        }
        else {
            fbDatabase.getReference("users").child(fbUser.getUid()).addListenerForSingleValueEvent(userListener);
        }

        newPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        clearPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // in future, we should store photo uri to another global variable in order to
                // delete it from firebase storage
                profilePicture.setImageResource(R.color.lightGrey);
                Toast.makeText(EditProfile.this, "Profile picture cleared", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            currentUser = dataSnapshot.getValue(User.class);
            if (currentUser != null) {
                // set input values to current User data, so they only change what they want
                editName.setText(currentUser.getName());
                editPhone.setText(currentUser.getPhoneNumber());
                editCity.setText(currentUser.getCity());
                editState.setText(currentUser.getState());

                // get user profile picture uri (as a string) from firebase
                String photoString = currentUser.getPhoto();
                if (!photoString.equals("")) {
                    // if string exists (not empty) then load image into imageview
                    Uri photoUrl = Uri.parse(photoString);
                    Picasso.with(EditProfile.this).load(photoUrl).into(profilePicture);
                }
                // if string empty, don't load anything

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // do nothing
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add options to save and revert changes to action bar
        MenuItem saveMenuItem = menu.add(1, SAVE_CHANGES_CODE, 101, "Save Changes");
        saveMenuItem.setIcon(R.drawable.ic_save);
        saveMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem revertMenuItem = menu.add(1, REVERT_CHANGES_CODE, 102, "Revert Changes");
        revertMenuItem.setIcon(R.drawable.ic_refresh);
        revertMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem exitMenuItem = menu.add(1, EXIT_CODE, 103, "Exit");
        exitMenuItem.setIcon(R.drawable.ic_exit);
        exitMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == SAVE_CHANGES_CODE) {
            currentUser.setName(editName.getText().toString());
            currentUser.setPhoneNumber(editPhone.getText().toString());
            currentUser.setCity(editCity.getText().toString());
            currentUser.setState(editState.getText().toString());

            // TODO
            // setphoto = imageview (if from gallery, its a uri, from camera its a bitmap)
            // if imageview is empty: currentUser.setPhoto("");
            // else: upload photo to firebase, then currentUser.setPhoto(<firebase storage uri>)

            fbDatabase.getReference("users").child(fbUser.getUid()).setValue(currentUser);

            // Go to User Profile when changes are saved.
            notifyUser("Changes saved.");
            finish();
        }
        else if (id == REVERT_CHANGES_CODE) {
            editName.setText(currentUser.getName());
            editPhone.setText(currentUser.getPhoneNumber());
            editCity.setText(currentUser.getCity());
            editState.setText(currentUser.getState());
        }
        else if (id == EXIT_CODE) {
            notifyUser("Changes not saved.");
            finish();
        }
        return true;
    }

    private void notifyUser(String message) {
        Toast.makeText(EditProfile.this, message, Toast.LENGTH_SHORT).show();
    }
}
