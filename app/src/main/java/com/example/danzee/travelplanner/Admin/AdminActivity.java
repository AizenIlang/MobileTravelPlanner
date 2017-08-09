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
import android.support.design.widget.CoordinatorLayout;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

/**
 * Created by DanZee on 08/08/2017.
 */

public class AdminActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 500;
    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    private EditText Name;
    private EditText Price;
    private EditText CompanyName;
    private EditText Details;
    private EditText MapCoordinates;
    private FrameLayout imageContainer;
    private Spinner Group;
    private ImageView imageView;
    private Button activityaddBtn;
    private ProgressBar progressBar;
    private CoordinatorLayout parentview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_activities);
        initPage();
    }

    private void initPage(){
        Name = (EditText) findViewById(R.id.admin_activity_name);
        Price = (EditText) findViewById(R.id.admin_activity_price);
        CompanyName = (EditText) findViewById(R.id.admin_activity_company);
        Details = (EditText) findViewById(R.id.admin_activity_details);
        MapCoordinates = (EditText) findViewById(R.id.admin_activity_map);
        imageContainer = (FrameLayout) findViewById(R.id.admin_activity_imageContainer);
        imageView = (ImageView) findViewById(R.id.admin_activity_image_view);
        activityaddBtn = (Button) findViewById(R.id.admin_activity_add_btn);
        Group = (Spinner) findViewById(R.id.admin_activity_spinner);
        parentview = (CoordinatorLayout) findViewById(R.id.admin_activity_parentview);
        activityaddBtn.setOnClickListener(new UploadOnClickListener());
        imageContainer.setOnClickListener(new ImageUploadListener());
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

            String path = "Activity/";

            path += Name.getText().toString() +".png";
            StorageReference fireReferenceStorage = storage.getReference(path);

            progressBar.setVisibility(View.VISIBLE);
            activityaddBtn.setEnabled(false);

            final Activities tempActivities = new Activities();
            tempActivities.setName(Name.getText().toString());
            tempActivities.setCompany(CompanyName.getText().toString());
            tempActivities.setDetails(Details.getText().toString());
            tempActivities.setGroup(Group.getSelectedItem().toString());
            tempActivities.setsTotalCost(PriceStringBuilder(Price.getText().toString()));

            try{
                Double myPrice = Double.parseDouble(Price.getText().toString());
                tempActivities.setTotalCost(myPrice);
            }catch (Exception e){
                tempActivities.setTotalCost(0.00d);
                Log.e("AdminRoom : ", e.getMessage());
            }

            tempActivities.setMap(MapCoordinates.getText().toString());
            tempActivities.setPhotoURL(path);


            final UploadTask uploadTask = fireReferenceStorage.putBytes(data);

            uploadTask.addOnSuccessListener(AdminActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //TODO Create an Upload Success Fragment and Destory session
                    PostToDatabase(tempActivities);
                    progressBar.setVisibility(View.GONE);
                    activityaddBtn.setEnabled(true);
                    Snackbar.make(parentview,"Added "+tempActivities.getName()+" Complete",Snackbar.LENGTH_SHORT).show();

                }
            });


        }
    }

    private void PostToDatabase(Activities value){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String key = firebaseDatabase.getReference().push().getKey();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Activity").child(key);

        databaseReference.setValue(value);

        PostToGroup(value);
    }

    private void PostToGroup(Activities value){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String myGroup = Group.getSelectedItem().toString();
        String key = firebaseDatabase.getReference().push().getKey();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Activity").child(myGroup).child(key);

        databaseReference.setValue(value);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //NO NEED TO GET THE BITMAP. WE GET THE URI AND LET PICASSO HANDLE IT.

            Uri tempUri = getImageUri(AdminActivity.this, imageBitmap);
            Log.e("Camera", data.getData().toString());
            Picasso.with(AdminActivity.this).load(tempUri).noPlaceholder().fit()
                    .into(imageView);


        }

        if (resultCode == Activity.RESULT_OK){
            if (null == data) return;
            Uri originalUri = null;
            if (requestCode == GALLERY_INTENT_CALLED) {
                originalUri = data.getData();
                Log.e("Gallery",data.getData().toString());
                Picasso.with(AdminActivity.this).load(originalUri).noPlaceholder().fit()
                        .into(imageView);

            } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                Log.e("Gallery",data.getData().toString());
                Picasso.with(AdminActivity.this).load(originalUri).noPlaceholder().fit()
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
    private String PriceStringBuilder(String value){
        String returnValue = new DecimalFormat("P#,###.##").format(Double.valueOf(value)) + ".00";
        return returnValue;
    }


}
