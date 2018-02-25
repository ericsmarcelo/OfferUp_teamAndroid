package com.teamandroid.offerup;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

    private TextView userName;
    private TextView userEmail;
    private TextView userPhone;
    private FirebaseAuth fbAuth;
    private FirebaseDatabase database;
    static private User dbUser;
    static RatingBar ratingBar;
    private Menu menu;

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

        LinearLayout rating = (LinearLayout) findViewById(R.id.rating);
        //RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingsbar);
        ratingBar = (RatingBar) findViewById(R.id.ratingsbar);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RatingBarDialog().show(getFragmentManager(),"rating");
            }
        });

        database = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageView userImg = (ImageView) findViewById(R.id.userpic);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.with(getApplicationContext())
                .load("http://via.placeholder.com/350x350")
                .transform(new RoundedTransformation(100,10))
                .fit()
                .centerCrop().into(userImg);

        if(user != null)
        {
            database.getReference("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);
        }
        else {
            // user not logged in
            userName.setText(" ");
            userEmail.setText(" ");
            userPhone.setText(" ");
            ratingBar.setRating(3.5f);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            // 1 is the id for EditProfile
            Intent intent = new Intent(UserProfile.this, EditProfile.class);
            startActivity(intent);
        }
        return true;
    }

    static public void addRatingToUser(float newRating) {
        // calculate new rating within User class
        dbUser.addRating((double)newRating);

        // log info
        Log.e("UserProfile: ", "Added a new rating of: " + String.valueOf(newRating));
        Log.e("UserProfile: ", "New total rating: " + String.valueOf(dbUser.getRating()));

        // add new rating and ratingCount to firebase
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(fbUser.getUid());
        userRef.child("rating").setValue(dbUser.getRating());
        userRef.child("ratingCount").setValue(dbUser.getRatingCount());

        // set the stars of the ratingBar view
        ratingBar.setRating((float)dbUser.getRating());

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FirebaseUser user = fbAuth.getCurrentUser();
        if (user != null) {
            database.getReference("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);
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
                ratingBar.setRating((float)dbUser.getRating());

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

                // add edit profile to action bar if profile email matches logged in user
                // only check if the menu item has not yet been added to action bar
                if (menu.findItem(1) == null) {
                    FirebaseUser firebaseUser = fbAuth.getCurrentUser();
                    if (firebaseUser != null && firebaseUser.getEmail().equals(dbUser.getEmail())) {
                        // if profile email matches current logged in user email, then show edit profile button
                        MenuItem editProfile = menu.add(1, 1, 101, "Edit Profile");
                        editProfile.setIcon(R.drawable.ic_edit_profile);
                        editProfile.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                    }
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // do nothing
        }
    };

    class RoundedTransformation implements com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin;  // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);

            if (source != output) {
                source.recycle();
            }

            return output;
        }

        @Override
        public String key() {
            return "rounded";
        }
    }
}
