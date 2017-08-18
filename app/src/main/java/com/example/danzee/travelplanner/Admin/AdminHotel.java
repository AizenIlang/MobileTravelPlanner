package com.example.danzee.travelplanner.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.danzee.travelplanner.Group.Group;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AdminHotel extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 500;
    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    ImageView imageAttach;
    FrameLayout imageContainer;

    EditText Name;
    EditText Company;
    EditText Details;
    EditText Location1;
    EditText Location2;
    EditText AveragePrice;
    Spinner Group;
    Button addHotel;
    ProgressBar progressBar;
    ConstraintLayout parentView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_admin_hotel);
        initPage();
    }

    private class ImageUploadListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT <19){
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"),GALLERY_INTENT_CALLED);
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/jpeg");
                startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
            }
        }
    }

    private class UploadOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            imageContainer.setDrawingCacheEnabled(true);
            imageContainer.buildDrawingCache();
            Bitmap bitmap = imageContainer.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            imageContainer.setDrawingCacheEnabled(false);
            byte[] data = baos.toByteArray();

            String path = "Hotels/";

            path += Name.getText().toString() +".png";
            StorageReference fireReferenceStorage = storage.getReference(path);

            progressBar.setVisibility(View.VISIBLE);
            addHotel.setEnabled(false);

            final Hotel tempHotel = new Hotel();
            tempHotel.setName(Name.getText().toString());
            tempHotel.setCompany(Company.getText().toString());
            tempHotel.setDetails(Details.getText().toString());
            tempHotel.setGroup(Group.getSelectedItem().toString());
            tempHotel.setLocation1(Location1.getText().toString());
            tempHotel.setLocation2(Location2.getText().toString());
            tempHotel.setAveragePrice(Double.parseDouble(AveragePrice.getText().toString()));
            tempHotel.setPhotoURL(path);


            final UploadTask uploadTask = fireReferenceStorage.putBytes(data);

            uploadTask.addOnSuccessListener(AdminHotel.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //TODO Create an Upload Success Fragment and Destory session
                    PostToDatabase(tempHotel);
                    progressBar.setVisibility(View.GONE);
                    addHotel.setEnabled(true);
                    Snackbar.make(parentView,"Added "+tempHotel.getName()+" Complete",Snackbar.LENGTH_SHORT).show();

                }
            });


        }
    }

    private void PostToDatabase(Hotel value){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String key = firebaseDatabase.getReference().push().getKey();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Hotels").child(key);

        databaseReference.setValue(value);

        PostToGroup(value);
    }

    private void PostToGroup(Hotel value){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String myGroup = Group.getSelectedItem().toString();
        String key = firebaseDatabase.getReference().push().getKey();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Hotels").child(myGroup).child(key);

        databaseReference.setValue(value);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //NO NEED TO GET THE BITMAP. WE GET THE URI AND LET PICASSO HANDLE IT.

            Uri tempUri = getImageUri(AdminHotel.this, imageBitmap);
            Log.e("Camera", data.getData().toString());
            Picasso.with(AdminHotel.this).load(tempUri).noPlaceholder().fit()
                    .into(imageAttach);


        }

        if (resultCode == Activity.RESULT_OK){
            if (null == data) return;
            Uri originalUri = null;
            if (requestCode == GALLERY_INTENT_CALLED) {
                originalUri = data.getData();
                Log.e("Gallery",data.getData().toString());
                Picasso.with(AdminHotel.this).load(originalUri).noPlaceholder().fit()
                        .into(imageAttach);

            } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                Log.e("Gallery",data.getData().toString());
                Picasso.with(AdminHotel.this).load(originalUri).noPlaceholder().fit()
                        .into(imageAttach);
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "tempOLB", null);
        return Uri.parse(path);
    }




    private void initPage() {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("ADMIN Hotel List");
        imageAttach = (ImageView) findViewById(R.id.admin_hotels_image_view);
        imageContainer = (FrameLayout) findViewById(R.id.admin_hotels_imageContainer);

        // SETTING UP THE UI

        Name = (EditText) findViewById(R.id.admin_hotel_name);
        Company = (EditText) findViewById(R.id.admin_hotel_company);
        Details = (EditText) findViewById(R.id.admin_hotel_details);
        Location1 = (EditText) findViewById(R.id.admin_hotel_location1);
        Location2 = (EditText) findViewById(R.id.admin_hotels_location2);
        Group = (Spinner) findViewById(R.id.admin_hotels_spinner);
        progressBar = (ProgressBar) findViewById(R.id.admin_hotels_progress_bar);
        parentView = (ConstraintLayout) findViewById(R.id.admin_hotels_parentview);
        AveragePrice = (EditText) findViewById(R.id.admin_hotel_average_price);

        imageAttach.setOnClickListener(new ImageUploadListener());

        addHotel = (Button) findViewById(R.id.admin_hotel_add_btn);
        addHotel.setOnClickListener(new UploadOnClickListener());
        TextView hotelText = (TextView) findViewById(R.id.booking_add_hotel_list_Hotel);

    }

}
