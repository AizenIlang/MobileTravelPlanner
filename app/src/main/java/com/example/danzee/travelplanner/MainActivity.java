package com.example.danzee.travelplanner;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danzee.travelplanner.Activities.Activities;
import com.example.danzee.travelplanner.Activities.ActivitiesList;
import com.example.danzee.travelplanner.Admin.Admin;
import com.example.danzee.travelplanner.Booking.Booking;
import com.example.danzee.travelplanner.Booking.BookingList;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.example.danzee.travelplanner.Restaurant.RestaurantList;
import com.example.danzee.travelplanner.Rooms.Rooms;
import com.example.danzee.travelplanner.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser fbUser;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private TextView UserName;
    private TextView Email;
    private ImageView ProfilePic;
    private User myUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Menu navMenu;
    private MenuItem menuItemAdmin;

    public static String MapPick;
    public static final String HOTEL = "HOTEL";
    public static final String RESTAURANT ="RESTAURANT";
    public static final String ACTIVITY = "ACTIVITY";

    //FOR BOOKINGS

    public TextView mChosen_Hotel_Name;
    public TextView mChosen_Hotel_Price;
    public TextView mChosen_Activity_Name;
    public TextView mChosen_Activity_Price;
    public TextView mChosen_Dining_Name;
    public TextView mChosen_Dining_Price;
    public TextView mChosen_Total_Price;
    public Button mRerserve_booking;

    public static Hotel theChosenHotel;
    public static Restaurant theChosenRestaurant;
    public static Rooms theChosenRoom;
    public static Activities theChosenActivities;



    public void ResetText(){
        double myCost = 0;
        if(theChosenHotel != null){
            mChosen_Hotel_Name.setText(theChosenHotel.getName());
            mChosen_Hotel_Price.setText(YouwillBedeductedStringBuilder(theChosenRoom.getTotalCost()));
            myCost += theChosenRoom.getTotalCost();

            mRerserve_booking.setVisibility(View.VISIBLE);
        }

        if(theChosenRestaurant != null){
            mChosen_Dining_Name.setText(theChosenRestaurant.getName());
            mChosen_Dining_Price.setText(YouwillBedeductedStringBuilder(theChosenRestaurant.getPrice()));
            myCost += theChosenRestaurant.getPrice();

            mRerserve_booking.setVisibility(View.VISIBLE);
        }

        if(theChosenActivities != null){
            mChosen_Activity_Name.setText(theChosenActivities.getName());
            mChosen_Activity_Price.setText(YouwillBedeductedStringBuilder(theChosenActivities.getTotalCost()));
            myCost += theChosenActivities.getTotalCost();

            mRerserve_booking.setVisibility(View.VISIBLE);
        }
        TotalMake(myCost);
    }

    public void TotalMake(double val){
        mChosen_Total_Price.setText(YouwillBedeductedStringBuilder(val));

    }

    @Override
    protected void onResume() {
        super.onResume();
        ResetText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPage();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this,Login.class));
                }
            }
        };



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //START OF HEADER
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navView = navigationView.getHeaderView(0);
        UserName = navView.findViewById(R.id.nav_header_profile_name);
        Email = navView.findViewById(R.id.nav_header_email_address);
        ProfilePic = navView.findViewById(R.id.nav_header_profile_pic);

        navMenu = navigationView.getMenu();
        menuItemAdmin = navMenu.findItem(R.id.nav_admin);



        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dr_user = db.getReference().child("users").child(mAuth.getCurrentUser().getUid());
        dr_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myUser = dataSnapshot.getValue(User.class);
                UserName.setText(myUser.getUserName());
                Email.setText(myUser.getEmail());
                if(myUser.getType().equals("USER")){
                    menuItemAdmin.setVisible(false);
                }else if(myUser.getType().equals("ADMIN")){
                    menuItemAdmin.setVisible(true);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_travels) {
            startActivity(new Intent(MainActivity.this, BookingList.class));
        } else if (id == R.id.nav_admin) {
            startActivity(new Intent(MainActivity.this, Admin.class));

        } else if (id == R.id.nav_hotels) {

        } else if (id == R.id.nav_activities) {

        } else if (id == R.id.nav_restaurant) {

        } else if (id == R.id.nav_my_profile) {

        } else if(id == R.id.nav_log_out){
            mAuth.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void initPage(){
        Button hotelAddButton = (Button) findViewById(R.id.booking_add_Hotel);
        Button activityAddButton = (Button) findViewById(R.id.booking_add_ActivityBtn);
        Button restaurantAddButton = (Button) findViewById(R.id.booking_add_RestaurantBtn);

        mChosen_Hotel_Name = (TextView) findViewById(R.id.chosen_hotel_name);
        mChosen_Hotel_Price = (TextView) findViewById(R.id.chosen_hotel_price);
        mChosen_Dining_Name = (TextView) findViewById(R.id.chosen_dining_name);
        mChosen_Dining_Price = (TextView) findViewById(R.id.chosen_dining_price);
        mChosen_Activity_Name = (TextView) findViewById(R.id.chosen_activity_name);
        mChosen_Activity_Price = (TextView) findViewById(R.id.chosen_activity_price);
        mChosen_Total_Price = (TextView) findViewById(R.id.chosen_total_cost);
        mRerserve_booking = (Button) findViewById(R.id.booking_add_reserve);
        mRerserve_booking.setVisibility(View.GONE);

        hotelAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedElementTransition();
            }
        });

        activityAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ActivitiesList.class));
            }
        });
        restaurantAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RestaurantList.class));
            }
        });

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mRerserve_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = firebaseDatabase.getReference()
                        .child("Booking")
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .push();
                Booking booking = new Booking();

                if(theChosenHotel != null){
                    booking.setHotel(theChosenHotel);
                }
                if(theChosenRestaurant != null){
                    ArrayList<Restaurant> restaurantArrayList = new ArrayList<Restaurant>();
                    restaurantArrayList.add(theChosenRestaurant);
                    booking.setRestaurantArrayList(restaurantArrayList);
                }
                if(theChosenActivities != null){
                    ArrayList<Activities> activitiesArrayList = new ArrayList<Activities>();
                    activitiesArrayList.add(theChosenActivities);
                    booking.setActivitiesArrayList(activitiesArrayList);
                }
                if(theChosenRoom != null){
                    booking.setRooms(theChosenRoom);
                }
                booking.setsTotalCost(mChosen_Total_Price.getText().toString());

                databaseReference.setValue(booking, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        theChosenHotel = null;
                        theChosenRestaurant = null;
                        theChosenActivities = null;
                        theChosenRoom = null;
                        ResetValues();
                    }
                });

            }
        });
    }

    private void ResetValues(){
        mChosen_Total_Price.setText("");
        mChosen_Activity_Price.setText("");
        mChosen_Activity_Name.setText("");
        mChosen_Hotel_Name.setText("");
        mChosen_Dining_Name.setText("");
        mChosen_Hotel_Price.setText("");
        mChosen_Dining_Price.setText("");
        mRerserve_booking.setVisibility(View.GONE);
    }

    private void SharedElementTransition(){
        View mySharedElement = findViewById(R.id.booking_add_Hotel);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,mySharedElement,"booking_add_hotel_shared");
        Intent i = new Intent(MainActivity.this, HotelList.class);
        startActivity(i,options.toBundle());

    }

    private String YouwillBedeductedStringBuilder(Double value){
        String returnValue = new DecimalFormat("P#,###.##").format(value) + ".00";
        return returnValue;
    }
}
