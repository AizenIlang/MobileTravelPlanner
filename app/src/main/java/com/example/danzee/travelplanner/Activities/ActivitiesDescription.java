package com.example.danzee.travelplanner.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.MainActivity;
import com.example.danzee.travelplanner.R;
import com.example.danzee.travelplanner.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

/**
 * Created by DanZee on 08/08/2017.
 */


public class ActivitiesDescription extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView hotelAddImageView;
    private TextView hotelAddName;
    private TextView hotelAddDescription;
    private TextView hotelAddCompany;
    private Context context;
    private Button hotelBtn;
    private Button hotelRateUs;
    private TextView hotelAddPrice;
    private RatingBar rateusRatingBar;
    private RatingBar ratingBar;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.hotel_add);
        initPage();
    }

    private void initPage(){
        final Activities hotel = ActivitiesList.selection;

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final View bottomSheetView = getLayoutInflater().inflate(R.layout.bootom_persistent_rate_us,null);
        bottomSheetDialog.setContentView(bottomSheetView);

        rateusRatingBar = (RatingBar) bottomSheetView.findViewById(R.id.bottom_rate_us);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(hotel.getRating() / hotel.getNumberOfRates());

        hotelRateUs = (Button) findViewById(R.id.hotel_add_rate_us);

        roomName = hotel.getName();
        hotelAddImageView = (ImageView) findViewById(R.id.hotel_add_imageview);
        hotelAddDescription = (TextView) findViewById(R.id.hotel_add_details);
        hotelAddName = (TextView) findViewById(R.id.hotel_add_name);
        hotelAddCompany = (TextView) findViewById(R.id.hotel_add_company);
        hotelAddDescription.setText(hotel.getDetails());
        hotelAddName.setText(hotel.getName());
        hotelAddCompany.setText(hotel.getsTotalCost());
        hotelAddPrice = (TextView) findViewById(R.id.hotel_description_price);
        hotelAddPrice.setText(hotel.getsTotalCost());
        hotelBtn = (Button) findViewById(R.id.hotel_add_roombtn);
        hotelBtn.setText("Add Activity");
        hotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(RoomDescription.this, RoomList.class));
                MainActivity.theChosenActivitiesarray.add(hotel);

                Toast.makeText(getApplicationContext(),roomName + "Resgistered", Toast.LENGTH_LONG).show();
            }
        });
        Glide.with(this).load(hotel.myUri).into(hotelAddImageView);

        if(MainActivity.myUser.getType().equals(User.TYPE_USER)){
            hotelRateUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.show();
                }
            });

        }

        rateusRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float v, boolean b) {
                DatabaseReference getHotel = firebaseDatabase.getReference().child("Activity").child(hotel.getID());
                getHotel.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Hotel myTempHotel = dataSnapshot.getValue(Hotel.class);
                        float myRating = myTempHotel.getRating();
                        myRating += v;
                        int numberOfRates = myTempHotel.getNumberOfRates() + 1;
                        DatabaseReference dbrefnumRating = firebaseDatabase.getReference().child("Activity").child(hotel.getID()).child("numberOfRates");
                        DatabaseReference dbref2numRating = firebaseDatabase.getReference().child("Group").child("Activity").child(hotel.getGroup()).child(hotel.getID()).child("numberOfRates");
                        dbrefnumRating.setValue(numberOfRates);
                        dbref2numRating.setValue(numberOfRates);
                        DatabaseReference dbref = firebaseDatabase.getReference().child("Activity").child(hotel.getID()).child("rating");
                        DatabaseReference dbref2 = firebaseDatabase.getReference().child("Group").child("Activity").child(hotel.getGroup()).child(hotel.getID()).child("rating");
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

//        Picasso.with(this)
//                .load(hotel.myUri)
//
//                .into(hotelAddImageView);

    }
}
