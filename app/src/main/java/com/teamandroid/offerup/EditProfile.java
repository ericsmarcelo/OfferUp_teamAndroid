package com.teamandroid.offerup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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
    }

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            currentUser = dataSnapshot.getValue(User.class);
            if (currentUser != null) {

                // get all parameters from the User (to avoid multiple function calls per each parameter)
                userName.setText(dbUser.getName());
                userEmail.setText(dbUser.getEmail());
                String userPhoneNumber = dbUser.getPhoneNumber();
//                String userCity = dbUser.getCity();
//                String userState = dbUser.getState();
//                double userRating = dbUser.getRating();

                // set all text views to the value from user, granted that the value is not empty
                if (userPhoneNumber != "") {
                    userPhone.setText(userPhoneNumber);
                }
                else {
                    userPhone.setText("(000) 000-0000");
                }
//                if (userCity != "" || userCity != null) {
//                    profileCity.setText(userCity);
//                }
//                if(userState != "" || userState != null) {
//                    profileState.setText(userState);
//                }
//                profileRating.setText(String.valueOf(userRating));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // do nothing
        }
    };

    private void notifyUser(String message) {
        Toast.makeText(Registration.this, message, Toast.LENGTH_SHORT).show();
    }
}
