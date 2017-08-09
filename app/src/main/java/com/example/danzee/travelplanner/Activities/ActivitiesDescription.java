package com.example.danzee.travelplanner.Activities;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ActivitiesDescription extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView activityAddImageView;
    private TextView activityAddName;
    private TextView activityAddDescription;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_activities_description);
        initPage();
    }

    private void initPage(){
        Activities hotel = ActivitiesList.selection;
        activityAddImageView = (ImageView) findViewById(R.id.activity_add_imageview);
        activityAddDescription = (TextView) findViewById(R.id.activity_add_details);
        activityAddName = (TextView) findViewById(R.id.activity_add_name);

        activityAddDescription.setText(hotel.getDetails());
        activityAddName.setText(hotel.getName());

        StorageReference storageRef = storage.getReference();

// Create a reference with an initial file path and name
        String path = hotel.getPhotoURL();
//        StorageReference pathReference = storageRef.child(path);

        storageRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.with(context)
                        .load(uri)
                        .resize(1050,540)
                        .centerCrop()
                        .into(activityAddImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
