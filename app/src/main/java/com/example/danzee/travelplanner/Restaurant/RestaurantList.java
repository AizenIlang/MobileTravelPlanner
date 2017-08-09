package com.example.danzee.travelplanner.Restaurant;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelAdapter;
import com.example.danzee.travelplanner.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantList extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    RecyclerView restuarantRecyclerView;
    Context context;
    RestaurantAdapter restaurantAdapter;
    public static Restaurant selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        context = this.getApplicationContext();
        initPage();


    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    private void initPage() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Restaurant List");
        final ArrayList<Restaurant> myList = new ArrayList<Restaurant>();


        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Restaurant");
        final TextView hotelText = (TextView) findViewById(R.id.booking_add_hotel_list_Hotel);
        restuarantRecyclerView = (RecyclerView) findViewById(R.id.restaurant_recycler_view);
        restaurantAdapter = new RestaurantAdapter(myList,context);
        restuarantRecyclerView.setAdapter(restaurantAdapter);
        restuarantRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Restaurant tempRestaurant = dataSnapshot.getValue(Restaurant.class);
                myList.add(tempRestaurant);
                Log.e("tempRestaurant :" ,dataSnapshot.toString());

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
                restaurantAdapter = new RestaurantAdapter(myList,context);
                restuarantRecyclerView.setAdapter(restaurantAdapter);
                Log.e("Adapter :", "Called 1 :"+myList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
