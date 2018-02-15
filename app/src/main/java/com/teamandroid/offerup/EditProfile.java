package com.teamandroid.offerup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    private FirebaseDatabase fbDatabase;
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private User currentUser;

    private EditText editName;
    private EditText editPhone;
    private EditText editCity;
    private EditText editState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.editProfileEditName);
        editPhone = findViewById(R.id.editProfileEditPhone);
        editCity = findViewById(R.id.editProfileEditCity);
        editState = findViewById(R.id.editProfileEditState);
        Button saveChangesButton = findViewById(R.id.saveChangesButton);
        Button restoreButton = findViewById(R.id.restoreValuesButton);

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

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.setName(editName.getText().toString());
                currentUser.setPhoneNumber(editPhone.getText().toString());
                currentUser.setCity(editCity.getText().toString());
                currentUser.setState(editState.getText().toString());

                fbDatabase.getReference("users").child(fbUser.getUid()).setValue(currentUser);

                // Go to User Profile when changes are saved.
                Intent intent = new Intent(EditProfile.this, UserProfile.class);
                startActivity(intent);
            }
        });

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setText(currentUser.getName());
                editPhone.setText(currentUser.getPhoneNumber());
                editCity.setText(currentUser.getCity());
                editState.setText(currentUser.getState());
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
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // do nothing
        }
    };

    private void notifyUser(String message) {
        Toast.makeText(EditProfile.this, message, Toast.LENGTH_SHORT).show();
    }
}
