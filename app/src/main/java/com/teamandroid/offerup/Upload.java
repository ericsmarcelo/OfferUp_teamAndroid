package com.teamandroid.offerup;

import com.google.firebase.database.IgnoreExtraProperties;

public class Upload {
    public String itemName;
    public String image, owner,key;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String itemName, String image, String owner) {
        this.itemName = itemName;
        this.image= image;
        this.owner = owner;
    }

    public String getName() {
        return itemName;
    }

    public String getUrl() {
        return image;
    }

    public String getOwner(){return  owner;}
}

