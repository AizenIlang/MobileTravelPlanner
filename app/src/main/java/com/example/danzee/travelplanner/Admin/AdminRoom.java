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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.danzee.travelplanner.Booking.Booking;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelRooms;
import com.example.danzee.travelplanner.R;
import com.example.danzee.travelplanner.Rooms.Rooms;
import com.example.danzee.travelplanner.Rooms.SpinnerRoom;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by DanZee on 08/08/2017.
 */

public class AdminRoom extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 500;
    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private EditText Name;
    private EditText Details;
    private EditText TotalCost;
    private EditText PhotoURL;
    private Spinner Group;
    private EditText Size;

    private ProgressBar progressBar;
    private FrameLayout imageContainer;
    private ImageView imageAttach;
    private ConstraintLayout parentView;
    private Button addRoom;
    private String selectedHotelKey;

    ArrayList<SpinnerRoom> spinnerRooms = new ArrayList<SpinnerRoom>();
    ArrayAdapter<SpinnerRoom> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_rooms);
        initPage();
    }

    private void initPage() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ADMIN Room List");
        getHotelNames();
        imageAttach = (ImageView) findViewById(R.id.admin_room_image_view);
        imageContainer = (FrameLayout) findViewById(R.id.admin_room_imageContainer);

        // SETTING UP THE UI

        Name = (EditText) findViewById(R.id.admin_room_name);
        Details = (EditText) findViewById(R.id.admin_room_details);
        TotalCost = (EditText) findViewById(R.id.admin_room_price);
        Group = (Spinner) findViewById(R.id.admin_room_spinner);
        Size = (EditText) findViewById(R.id.admin_room_size);


        progressBar = (ProgressBar) findViewById(R.id.admin_room_progress_bar);
        parentView = (ConstraintLayout) findViewById(R.id.admin_room_parentView);

        imageAttach.setOnClickListener(new ImageUploadListener());

        addRoom = (Button) findViewById(R.id.admin_room_add_btn);
        addRoom.setOnClickListener(new UploadOnClickListener());
        TextView hotelText = (TextView) findViewById(R.id.booking_add_hotel_list_Hotel);

        //dummy
//        SpinnerRoom dummy = new SpinnerRoom("0","Pick Hotel");
//        spinnerRooms.add(dummy);
        spinnerAdapter = new ArrayAdapter<SpinnerRoom>(this, android.R.layout.simple_spinner_dropdown_item);
        Group.setAdapter(spinnerAdapter);
        Group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SpinnerRoom country = (SpinnerRoom) parent.getSelectedItem();
                selectedHotelKey = country.getId();
                Snackbar.make(parentView, "Hotel ID: "+country.getId()+",  Hotel Name: "+country.getName(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void getHotelNames(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Hotels");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Hotel myTemp = dataSnapshot.getValue(Hotel.class);
                SpinnerRoom spinnerTemp = new SpinnerRoom(dataSnapshot.getKey(),myTemp.getName());
                spinnerRooms.add(spinnerTemp);
                spinnerAdapter.clear();
                spinnerAdapter.addAll(spinnerRooms);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Group.setAdapter(spinnerAdapter);

                spinnerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

            String path = "Rooms/";

            path += Name.getText().toString() +".png";
            StorageReference fireReferenceStorage = storage.getReference(path);

            progressBar.setVisibility(View.VISIBLE);
            addRoom.setEnabled(false);

            final Rooms tempRooms = new Rooms();
            tempRooms.setName(Name.getText().toString());
            tempRooms.setDetails(Details.getText().toString());
            tempRooms.setPhotoURL(path);
            tempRooms.setSize(Size.getText().toString());
            try{
                Double myPrice = Double.parseDouble(TotalCost.getText().toString());
                tempRooms.setTotalCost(myPrice);
            }catch (Exception e){
                tempRooms.setTotalCost(0.00d);
                Log.e("AdminRoom : ", e.getMessage());
            }
            tempRooms.setsTotalCost(PriceStringBuilder(TotalCost.getText().toString()));


            final UploadTask uploadTask = fireReferenceStorage.putBytes(data);

            uploadTask.addOnSuccessListener(AdminRoom.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //TODO Create an Upload Success Fragment and Destory session
                    PostToDatabase(tempRooms,selectedHotelKey);
                    progressBar.setVisibility(View.GONE);
                    addRoom.setEnabled(true);
                    Snackbar.make(parentView,"Added "+tempRooms.getName()+" Complete",Snackbar.LENGTH_SHORT).show();

                }
            });


        }
    }

    private void PostToDatabase(Rooms value,String hotelkey){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String key = firebaseDatabase.getReference().push().getKey();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Rooms").child(key);

        databaseReference.setValue(value);

        DatabaseReference databaseReferenceForHotelRooms = firebaseDatabase.getReference().child("HotelRooms").child(hotelkey);
        HotelRooms tempHotelRooms = new HotelRooms();
        tempHotelRooms.setHotelKey(hotelkey);
        tempHotelRooms.setRoomKey(key);
        databaseReferenceForHotelRooms.setValue(tempHotelRooms);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //NO NEED TO GET THE BITMAP. WE GET THE URI AND LET PICASSO HANDLE IT.

            Uri tempUri = getImageUri(AdminRoom.this, imageBitmap);
            Log.e("Camera", data.getData().toString());
            Picasso.with(AdminRoom.this).load(tempUri).noPlaceholder().fit()
                    .into(imageAttach);


        }

        if (resultCode == Activity.RESULT_OK){
            if (null == data) return;
            Uri originalUri = null;
            if (requestCode == GALLERY_INTENT_CALLED) {
                originalUri = data.getData();
                Log.e("Gallery",data.getData().toString());
                Picasso.with(AdminRoom.this).load(originalUri).noPlaceholder().fit()
                        .into(imageAttach);

            } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                Log.e("Gallery",data.getData().toString());
                Picasso.with(AdminRoom.this).load(originalUri).noPlaceholder().fit()
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

    private String PriceStringBuilder(String value){
        String returnValue = new DecimalFormat("P#,###.##").format(Double.valueOf(value)) + ".00";
        return returnValue;
    }
}
