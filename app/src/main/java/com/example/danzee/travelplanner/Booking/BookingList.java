package com.example.danzee.travelplanner.Booking;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.danzee.travelplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingList extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private ArrayList<Booking> bookingArrayList = new ArrayList<Booking>();
    private Activity myActivity;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);
        myActivity = BookingList.this;
        myContext = BookingList.this;

        myRecyclerView = (RecyclerView) findViewById(R.id.booking_list_recycler_view);
        BookingAdapter bookingAdapter = new BookingAdapter(myContext,bookingArrayList,myActivity);
        myRecyclerView.setAdapter(bookingAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Booking").child(firebaseAuth.getCurrentUser().getUid());

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Booking tempBooking = dataSnapshot.getValue(Booking.class);
                bookingArrayList.add(tempBooking);
                Log.e("Booking","Jump on my Pony added");
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
                BookingAdapter bookingAdapter = new BookingAdapter(myContext,bookingArrayList,myActivity);
                myRecyclerView.setAdapter(bookingAdapter);
                Log.e("Booking","Comming");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
