package com.teamandroid.offerup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    private static final int IMAGE_CAPTURE = 102;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Button cameraButton = (Button) findViewById(R.id.captureButton);

        if(!hasCamera()){
            cameraButton.setEnabled(false);
        }

    }


    //Check if a camera exists
    private boolean hasCamera(){
        return(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
    }

    //open the camera
    public void initiateCapture(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    //Check the result of the camera action and upload the file
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri dataUri = data.getData();

        if(requestCode == IMAGE_CAPTURE){
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Image saved to: \n" + dataUri, Toast.LENGTH_LONG).show();
                Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
            }else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_LONG).show();
            }
        }
    }
}
