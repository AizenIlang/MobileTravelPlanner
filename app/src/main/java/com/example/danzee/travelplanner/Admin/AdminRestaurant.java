package com.example.danzee.travelplanner.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.danzee.travelplanner.Activities.Activities;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.R;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

/**
 * Created by DanZee on 08/08/2017.
 */

public class AdminRestaurant extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 500;
    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private EditText Name;
    private EditText Price;
    private EditText Company;
    private String PhotoUrl;
    private EditText Details;
    private EditText MapCoordinates;
    private Spinner Group;
    private ImageView imageView;
    private FrameLayout imageContainer;
    private ConstraintLayout parentview;
    private Button addRestaurantBtn;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_restaurant);

        initPage();
    }

    private void initPage(){
        Name = (EditText) findViewById(R.id.admin_restaurant_name);
        Price = (EditText) findViewById(R.id.admin_restaurant_price);
        Company = (EditText) findViewById(R.id.admin_restuarant_company);
        Details = (EditText) findViewById(R.id.admin_restaurant_details);
        MapCoordinates = (EditText) findViewById(R.id.admin_restaurant_map);
        Group = (Spinner) findViewById(R.id.admin_restaurant_spinner);
        imageView = (ImageView) findViewById(R.id.admin_restaurant_image_view);
        imageContainer = (FrameLayout) findViewById(R.id.admin_restaurant_imageContainer);
        parentview = (ConstraintLayout) findViewById(R.id.admin_restaurant_parentview);
        progressBar = (ProgressBar) findViewById(R.id.admin_restaurant_progress_bar);
        addRestaurantBtn = (Button) findViewById(R.id.admin_restaurant_add_btn);


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

            String path = "Restaurant/";

            path += Name.getText().toString() +".png";
            StorageReference fireReferenceStorage = storage.getReference(path);

            progressBar.setVisibility(View.VISIBLE);
            addRestaurantBtn.setEnabled(false);

            final Restaurant tempRestaurant = new Restaurant();
            tempRestaurant.setName(Name.getText().toString());
            tempRestaurant.setCompany(Company.getText().toString());
            tempRestaurant.setDetails(Details.getText().toString());
            tempRestaurant.setGroup(Group.getSelectedItem().toString());
            tempRestaurant.setLocation1(MapCoordinates.getText().toString());

            tempRestaurant.setPhotoUrl(path);


            final UploadTask uploadTask = fireReferenceStorage.putBytes(data);

            uploadTask.addOnSuccessListener(AdminRestaurant.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //TODO Create an Upload Success Fragment and Destory session
                    PostToDatabase(tempRestaurant);
                    progressBar.setVisibility(View.GONE);
                    addRestaurantBtn.setEnabled(true);
                    Snackbar.make(parentview,"Added "+tempRestaurant.getName()+" Complete",Snackbar.LENGTH_SHORT).show();

                }
            });


        }
    }

    private void PostToDatabase(Restaurant value){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String key = firebaseDatabase.getReference().push().getKey();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Restaurant").child(key);

        databaseReference.setValue(value);

        PostToGroup(value);
    }

    private void PostToGroup(Restaurant value){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String myGroup = Group.getSelectedItem().toString();
        String key = firebaseDatabase.getReference().push().getKey();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Restaurant").child(myGroup).child(key);

        databaseReference.setValue(value);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //NO NEED TO GET THE BITMAP. WE GET THE URI AND LET PICASSO HANDLE IT.

            Uri tempUri = getImageUri(AdminRestaurant.this, imageBitmap);
            Log.e("Camera", data.getData().toString());
            Picasso.with(AdminRestaurant.this).load(tempUri).noPlaceholder().fit()
                    .into(imageView);


        }

        if (resultCode == Activity.RESULT_OK){
            if (null == data) return;
            Uri originalUri = null;
            if (requestCode == GALLERY_INTENT_CALLED) {
                originalUri = data.getData();
                Log.e("Gallery",data.getData().toString());
                Picasso.with(AdminRestaurant.this).load(originalUri).noPlaceholder().fit()
                        .into(imageView);

            } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                Log.e("Gallery",data.getData().toString());
                Picasso.with(AdminRestaurant.this).load(originalUri).noPlaceholder().fit()
                        .into(imageView);
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "tempOLB", null);
        return Uri.parse(path);
    }
}
