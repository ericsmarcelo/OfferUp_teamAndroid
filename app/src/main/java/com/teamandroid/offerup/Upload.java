package com.teamandroid.offerup;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.List;

public class Upload {
    public String itemName;
    public String image;
    public List<String> category;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String itemName, String image, List<String> category) {
        this.itemName = itemName;
        this.image= image;
        this.category = category;
    }

    public String getName() {
        return itemName;
    }
    public String getUrl() {
        return image;
    }
    public List<String> getCategory() { return category; }
}

