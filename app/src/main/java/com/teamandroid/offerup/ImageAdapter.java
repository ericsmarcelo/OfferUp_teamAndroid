package com.teamandroid.offerup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Post> mPosts;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public ImageAdapter(Context c, ArrayList<Post> posts) {
        mContext = c;
        mPosts = posts;
    }

    public int getCount() {
        return mPosts.size();
    }

    public Post getItem(int position) {
        return mPosts.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout postTile;
        if (convertView == null) {
            postTile = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.post_tile_layout, parent, false);
            ImageView postImage = postTile.findViewById(R.id.postImage);
            TextView postName = postTile.findViewById(R.id.postName);

            postImage.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
            postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            postImage.setPadding(8, 8, 8, 8);

            postName.setText(mPosts.get(position).getItemName());

        }
        else {
            postTile = (LinearLayout)convertView;
        }

        Picasso.with(mContext)
                .load(mPosts.get(position).getImage())
                .placeholder(R.drawable.ic_load)
                .fit()
                .centerCrop()
                .into((ImageView)postTile.findViewById(R.id.postImage));

        return postTile;
    }

//    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//        String url = getItem(position).getImage();
//        Picasso.with(mContext)
//                .load(url)
//                .placeholder(R.drawable.ic_load)
//                .fit()
//                .centerCrop().into(imageView);
//        return imageView;
//    }

}
