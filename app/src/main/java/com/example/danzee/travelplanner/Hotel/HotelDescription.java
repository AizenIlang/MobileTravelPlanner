package com.example.danzee.travelplanner.Hotel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danzee.travelplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * Created by DanZee on 08/08/2017.
 */


public class HotelDescription extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView hotelAddImageView;
    private TextView hotelAddName;
    private TextView hotelAddDescription;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.hotel_add);
        initPage();
    }

    private void initPage(){
        Hotel hotel = HotelList.selection;
        hotelAddImageView = (ImageView) findViewById(R.id.hotel_add_imageview);
        hotelAddDescription = (TextView) findViewById(R.id.hotel_add_details);
        hotelAddName = (TextView) findViewById(R.id.hotel_add_name);

        hotelAddDescription.setText(hotel.getDetails());
        hotelAddName.setText(hotel.getName());

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
                        .into(hotelAddImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
