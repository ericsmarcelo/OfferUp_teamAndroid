package com.teamandroid.offerup;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.zip.Inflater;

/**
 * Created by suridx on 2/21/2018.
 */

public class RatingBarDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = LayoutInflater.from(builder.getContext());

        final View view = inflater.inflate(R.layout.activity_rate_user, null);
        final RatingBar ratingBar = view.findViewById(R.id.editRatingBar);

        final String profileUid = getArguments().getString("profileUid");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle("Rate User")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getActivity(), "Rating: "+String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
                        UserProfile.addRatingToUser(ratingBar.getRating(), profileUid);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RatingBarDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        final LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.activity_rate_user, null))
//                .setTitle("Rate User")
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        View v = inflater.inflate(R.layout.activity_rate_user, null);
//                        RatingBar bar = v.findViewById(R.id.editRatingBar);
//                        Toast.makeText(getActivity(), "Rating: "+String.valueOf(bar.getRating()), Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        RatingBarDialog.this.getDialog().cancel();
//                    }
//                });
//        return builder.create();
//    }
}
