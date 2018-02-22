package com.teamandroid.offerup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Login Page
public class Authentication extends AppCompatActivity {

    private TextView userText;
    private TextView statusText;
    private EditText emailText;
    private EditText passwordText;
    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        userText = findViewById(R.id.userText);
        statusText = findViewById(R.id.statusText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        userText.setText("");
        statusText.setText("Signed Out");

        fbAuth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null) {
                    userText.setText(user.getEmail());
                    statusText.setText("Signed In");

                    // if already signed in, send them directly to their profile.
                    //notifyUser("Already Signed In");
                    //Intent intent = new Intent(Authentication.this, UserProfile.class);
                    //startActivity(intent);

                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(authListener != null) {
            fbAuth.removeAuthStateListener(authListener);
        }
    }

    private void notifyUser(String message) {
        Toast.makeText(Authentication.this, message, Toast.LENGTH_SHORT).show();
    }

    //Create an account
    /*public void createAccount(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.length() == 0) {
            emailText.setError("Enter an email address");
            return;
        }

        if (password.length() < 7) {
            passwordText.setError("Password must be at least 7 characters");
            return;
        }

        fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            notifyUser("Account creation failed");
                        }else {
                            notifyUser("Account created");
                        }
                    }
                });
    }*/

    //Sign In
    public void signIn(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        boolean user = false;

        if(fbAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, NavigationDrawerActivity.class);
            startActivity(intent);
        }

        if (email.length() == 0) {
            emailText.setError("Enter an email address");
            return;
        }

        if (password.length() < 7) {
            passwordText.setError("Password must be at least 7 characters");
            return;
        }

        fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            notifyUser("Authentication failed");
                        }
                        else {
                            // successful login, go to profile activity (should change later)
                            Intent intent = new Intent(Authentication.this, UserProfile.class);
                            startActivity(intent);
                        }
                    }
                });
        if(fbAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, NavigationDrawerActivity.class);
            startActivity(intent);
        }

    }

    //sign out
    public void signOut(View view) {
        fbAuth.signOut();
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }

    //reset password
    public void resetPassword(View view){
        String email = emailText.getText().toString();

        if (email.length() == 0) {
            emailText.setError("Enter an email address");
            return;
        }

        fbAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            notifyUser("Reset email sent");
                        }
                    }
                });
    }


}
