package com.teamandroid.offerup.ui.activities;

//import android.support.v4.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.teamandroid.offerup.R;
import com.teamandroid.offerup.ui.fragments.LoginFragment;

/**
 * Created by User on 2/28/2018.
 */

public class LoginActivity extends AppCompatActivity{
    private Toolbar mToolbar;

    public static void startIntent(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startIntent(Context context, int flags){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void init() {
        setSupportActionBar(mToolbar);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentTransaction replace = fragmentTransaction.replace(R.id.frame_layout_content_login,
                LoginFragment.newInstance(),
                LoginFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

}
