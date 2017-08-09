package com.example.danzee.travelplanner.Admin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.danzee.travelplanner.Activities.Activities;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.R;

public class Admin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initPage();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    private void initPage(){
        Button hotelBtn = (Button) findViewById(R.id.admin_hotels);
        Button roomBtn = (Button) findViewById(R.id.admin_rooms);
        Button activityBtn = (Button) findViewById(R.id.admin_activities);
        Button restaurant = (Button) findViewById(R.id.admin_restaurant);

        hotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelShared();
            }
        });
        roomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomShared();
            }
        });
        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activityshared();
            }
        });
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurantshared();
            }
        });

    }

    private void HotelShared(){
        View mySharedElement = findViewById(R.id.textView);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,mySharedElement,"admin_headerTitle");
        Intent i = new Intent(Admin.this, AdminHotel.class);

        startActivity(i,options.toBundle());

    }

    private void RoomShared(){
        View mySharedElement = findViewById(R.id.textView);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,mySharedElement,"admin_headerTitle");
        Intent i = new Intent(Admin.this, AdminRoom.class);

        startActivity(i,options.toBundle());
    }

    private void Restaurantshared(){
        View mySharedElement = findViewById(R.id.textView);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,mySharedElement,"admin_headerTitle");
        Intent i = new Intent(Admin.this, AdminRestaurant.class);

        startActivity(i,options.toBundle());
    }

    private void Activityshared(){
        View mySharedElement = findViewById(R.id.textView);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,mySharedElement,"admin_headerTitle");

        startActivity(new Intent(Admin.this,AdminActivities.class));
    }


}
