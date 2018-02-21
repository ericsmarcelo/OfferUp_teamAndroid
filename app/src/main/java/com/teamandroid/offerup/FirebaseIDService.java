package com.teamandroid.offerup;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by User on 2/17/2018.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Registration Token " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

    }
}
