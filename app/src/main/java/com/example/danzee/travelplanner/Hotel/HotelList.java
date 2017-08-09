package com.example.danzee.travelplanner.Hotel;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.danzee.travelplanner.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HotelList extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    RecyclerView hotelRecyclerView;
    Context context;
    HotelAdapter hotelAdapter;
    public static Hotel selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.booking_add_hotel_list);
        initPage();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    private void initPage() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hotel List");
        final ArrayList<Hotel> myList = new ArrayList<Hotel>();


        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Hotels");
        final TextView hotelText = (TextView) findViewById(R.id.booking_add_hotel_list_Hotel);
        hotelRecyclerView = (RecyclerView) findViewById(R.id.hotel_recycler_view);
        hotelAdapter = new HotelAdapter(myList,context);
        hotelRecyclerView.setAdapter(hotelAdapter);
        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Hotel tempHotel = dataSnapshot.getValue(Hotel.class);
                myList.add(tempHotel);
                Log.e("HotelList :" ,dataSnapshot.toString());

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
                hotelAdapter = new HotelAdapter(myList,context);
                hotelRecyclerView.setAdapter(hotelAdapter);
                Log.e("Adapter :", "Called 1 :"+myList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void hey(){
        startActivity(new Intent(HotelList.this,HotelDescription.class));
    }

}
