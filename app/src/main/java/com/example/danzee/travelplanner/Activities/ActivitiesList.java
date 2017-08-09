package com.example.danzee.travelplanner.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelAdapter;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by DanZee on 08/08/2017.
 */

public class ActivitiesList extends AppCompatActivity{
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    RecyclerView activitiesRecyclerView;
    Context context;
    ActivitiesAdapter activitiesAdapter;
    public static Activities selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.booking_add_acitivity_list);
        initPage();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    private void initPage() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Activity List");
        final ArrayList<Activities> myList = new ArrayList<Activities>();


        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Activity");
        final TextView hotelText = (TextView) findViewById(R.id.booking_add_hotel_list_Hotel);
        activitiesRecyclerView = (RecyclerView) findViewById(R.id.activity_recycler_view);
        activitiesAdapter = new ActivitiesAdapter(myList,context);
        activitiesRecyclerView.setAdapter(activitiesAdapter);
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Activities tempActivity = dataSnapshot.getValue(Activities.class);
                myList.add(tempActivity);
                Log.e("ActivityList :" ,dataSnapshot.toString());

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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activitiesAdapter = new ActivitiesAdapter(myList,context);
                activitiesRecyclerView.setAdapter(activitiesAdapter);
                Log.e("Adapter :", "Called 1 :"+myList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
