package com.example.danzee.travelplanner.Rooms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.MainActivity;
import com.example.danzee.travelplanner.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

/**
 * Created by DanZee on 08/08/2017.
 */


public class RoomDescription extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView hotelAddImageView;
    private TextView hotelAddName;
    private TextView hotelAddDescription;
    private TextView hotelAddCompany;
    private Context context;
    private Button hotelBtn;
    private String roomName;
    private TextView hotelPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.hotel_add);
        initPage();
    }

    private void initPage(){
        final Rooms hotel = RoomList.selection;
        roomName = hotel.getName();
        hotelAddImageView = (ImageView) findViewById(R.id.hotel_add_imageview);
        hotelAddDescription = (TextView) findViewById(R.id.hotel_add_details);
        hotelAddName = (TextView) findViewById(R.id.hotel_add_name);
        hotelAddCompany = (TextView) findViewById(R.id.hotel_add_company);
        hotelPrice = (TextView) findViewById(R.id.hotel_description_price);
        hotelPrice.setVisibility(View.GONE);
        hotelAddDescription.setText(hotel.getDetails());
        hotelAddName.setText(hotel.getName());
        hotelAddCompany.setText(hotel.getsTotalCost());
        hotelBtn = (Button) findViewById(R.id.hotel_add_roombtn);
        hotelBtn.setText("Book");
        hotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(RoomDescription.this, RoomList.class));
                MainActivity.theChosenRoom = hotel;
                MainActivity.theChosenHotel = HotelList.selection;


                Toast.makeText(getApplicationContext(),HotelList.selection.getName() + " " + roomName + "Booked", Toast.LENGTH_LONG).show();
            }
        });
        //Glide.with(this).load(hotel.myUri).into(hotelAddImageView);
        Picasso.with(this)
                        .load(hotel.myUri)

                        .into(hotelAddImageView);

    }
}
