package com.teamandroid.offerup;

import com.google.firebase.database.IgnoreExtraProperties;

public class Upload {
    public String itemName;
    public String image;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String itemName, String image) {
        this.itemName = itemName;
        this.image= image;
    }

    public String getName() {
        return itemName;
    }

    public String getUrl() {
        return image;
    }
}
