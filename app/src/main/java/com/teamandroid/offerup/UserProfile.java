package com.teamandroid.offerup;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private TextView userName;
    private TextView userEmail;
    private TextView userPhone;
    private FirebaseAuth fbAuth;
    private FirebaseDatabase database;
    private User dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();


        //User Profile display
        userName = findViewById(R.id.userNameText);
        userEmail = findViewById(R.id.userEmailText);
        userPhone = findViewById(R.id.userPhoneText);

        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();


        if(user != null) {
            database.getReference("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);

            // add EditProfile button if user is logged in
            // *** we SHOULD check if logged in user matches profile ID ***
            ImageButton editProfileButton = new ImageButton(this);
            editProfileButton.setImageResource(R.drawable.ic_edit);

            // create parameter variable to store the positioning of the button
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            // add the Button to the layout
            RelativeLayout profileLayout = findViewById(R.id.content_user_profile);
            profileLayout.addView(editProfileButton, params);

            // add click listener to editProfile button to send user to EditProfile activity
            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserProfile.this, EditProfile.class);
                    startActivity(intent);
                }
            });
        }
        else {
            // user not logged in
            userName.setText("Name");
            userEmail.setText("email");
            userPhone.setText("(000) 000-0000");
        }

    }

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dbUser = dataSnapshot.getValue(User.class);
            if (dbUser != null) {

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


}
