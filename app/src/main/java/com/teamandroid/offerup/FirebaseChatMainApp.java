package com.teamandroid.offerup;

import android.app.Application;

/**
 * Created by User on 3/3/2018.
 */

public class FirebaseChatMainApp extends Application {
    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        FirebaseChatMainApp.sIsChatActivityOpen = isChatActivityOpen;

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}