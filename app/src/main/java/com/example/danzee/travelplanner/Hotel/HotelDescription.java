package com.example.danzee.travelplanner.Hotel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.danzee.travelplanner.R;
import com.example.danzee.travelplanner.Rooms.RoomList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Created by DanZee on 08/08/2017.
 */


public class HotelDescription extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView hotelAddImageView;
    private TextView hotelAddName;
    private TextView hotelAddDescription;
    private TextView hotelAddCompany;
    private TextView hotelPrice;
    private Context context;
    private Button hotelBtn;
    private Button hotelRateUs;
    private RatingBar rateusRatingBar;
    private RatingBar ratingBar;
    private Toolbar hotelToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.hotel_add);
        initPage();
    }

    private void initPage(){
        final Hotel hotel = HotelList.selection;


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final View bottomSheetView = getLayoutInflater().inflate(R.layout.bootom_persistent_rate_us,null);
        bottomSheetDialog.setContentView(bottomSheetView);

        rateusRatingBar = (RatingBar) bottomSheetView.findViewById(R.id.bottom_rate_us);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);


        ratingBar.setRating(hotel.getRating() / hotel.getNumberOfRates());
        hotelAddImageView = (ImageView) findViewById(R.id.hotel_add_imageview);
        hotelAddDescription = (TextView) findViewById(R.id.hotel_add_details);
        hotelAddName = (TextView) findViewById(R.id.hotel_add_name);
        hotelAddCompany = (TextView) findViewById(R.id.hotel_add_company);
        hotelAddDescription.setText(hotel.getDetails());
        hotelPrice = (TextView) findViewById(R.id.hotel_description_price);
        hotelToolBar = (Toolbar) findViewById(R.id.hotel_description_toolbar);
        hotelRateUs = (Button) findViewById(R.id.hotel_add_rate_us);
        hotelPrice.setText(YouwillBedeductedStringBuilder(hotel.getAveragePrice()));
        hotelAddName.setText(hotel.getName());
        hotelAddCompany.setText(hotel.getCompany());
        hotelRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });


        rateusRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float v, boolean b) {

                DatabaseReference getHotel = firebaseDatabase.getReference().child("Hotels").child(hotel.getID());
                getHotel.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Hotel myTempHotel = dataSnapshot.getValue(Hotel.class);
                        float myRating = myTempHotel.getRating();
                        myRating += v;
                        int numberOfRates = myTempHotel.getNumberOfRates() + 1;
                        DatabaseReference dbrefnumRating = firebaseDatabase.getReference().child("Hotels").child(hotel.getID()).child("numberOfRates");
                        DatabaseReference dbref2numRating = firebaseDatabase.getReference().child("Group").child("Hotels").child(hotel.getGroup()).child(hotel.getID()).child("numberOfRates");
                        dbrefnumRating.setValue(numberOfRates);
                        dbref2numRating.setValue(numberOfRates);
                        DatabaseReference dbref = firebaseDatabase.getReference().child("Hotels").child(hotel.getID()).child("rating");
                        DatabaseReference dbref2 = firebaseDatabase.getReference().child("Group").child("Hotels").child(hotel.getGroup()).child(hotel.getID()).child("rating");
                        dbref.setValue(myRating);
                        dbref2.setValue(myRating);
                        ratingBar.setRating(myRating / numberOfRates);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






                bottomSheetDialog.hide();
            }
        });

        hotelToolBar.setTitle(hotel.getName());
        hotelBtn = (Button) findViewById(R.id.hotel_add_roombtn);
        hotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("The Hotel ID", HotelList.selection.getID());
                startActivity(new Intent(HotelDescription.this, RoomList.class));
            }
        });
        Glide.with(this).load(hotel.myUri).into(hotelAddImageView);
//        Picasso.with(this)
//                        .load(hotel.myUri)
//
//                        .into(hotelAddImageView);

    }

    private String YouwillBedeductedStringBuilder(Double value){
        String returnValue = new DecimalFormat("P#,###.##").format(value) + ".00";
        return returnValue;
    }
}
