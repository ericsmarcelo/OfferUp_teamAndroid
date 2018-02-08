package com.teamandroid.offerup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfile extends AppCompatActivity {

    private TextView userName;
    private TextView userEmail;
    private TextView userPhone;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //User Profile display
        userName = (TextView) findViewById(R.id.userNameText);
        userEmail = (TextView) findViewById(R.id.userEmailText);
        userPhone = (TextView) findViewById(R.id.userPhoneText);

        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();

        userName.setText("Name");
        userEmail.setText("email");
        userPhone.setText("(000) 000-0000");
        if(user != null) {
            userEmail.setText("" + user.getEmail());
            if(user.getDisplayName() != null) userName.setText("" + user.getDisplayName());
            if(user.getPhoneNumber() != null) userPhone.setText("" + user.getPhoneNumber());
        }

    }


}
