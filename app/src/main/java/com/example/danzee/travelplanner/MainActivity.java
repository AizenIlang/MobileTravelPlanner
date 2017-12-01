package com.example.danzee.travelplanner;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danzee.travelplanner.Activities.Activities;
import com.example.danzee.travelplanner.Activities.ActivitiesList;
import com.example.danzee.travelplanner.Admin.Admin;
import com.example.danzee.travelplanner.Booking.Booking;
import com.example.danzee.travelplanner.Booking.BookingList;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.Mapper.Mapper;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.example.danzee.travelplanner.Restaurant.RestaurantList;
import com.example.danzee.travelplanner.Rooms.Rooms;
import com.example.danzee.travelplanner.User.User;
import com.example.danzee.travelplanner.User.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser fbUser;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private TextView UserName;
    private TextView Email;
    private ImageView ProfilePic;
    public static User myUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Menu navMenu;
    private MenuItem menuItemAdmin;
    public Context mContext = MainActivity.this;

    public static String MapPick;
    public static final String HOTEL = "HOTEL";
    public static final String RESTAURANT ="RESTAURANT";
    public static final String ACTIVITY = "ACTIVITY";
    public static final String NONE = "NONE";


    //FOR BOOKINGS

    public TextView mChosen_Hotel_Name;
    public TextView mChosen_Hotel_Price;
    public TextView mChosen_Activity_Name;
    public TextView mChosen_Activity_Price;
    public TextView mChosen_Dining_Name;
    public TextView mChosen_Dining_Price;
    public TextView mChosen_Total_Price;
    public RecyclerView mRecyclerView;
    public Button mRerserve_booking;
    public TextView mChosen_Calender;
    public TextView mChosen_Calender2;
    public DatePicker mCalendarView;
    public TextView mBudget;

    public boolean fromto = false;

    public static Hotel theChosenHotel;
    public static Restaurant theChosenRestaurant;
    public static Rooms theChosenRoom;
    public static Activities theChosenActivities;
    public static String theChosenGroup = NONE;


    public static ArrayList<Restaurant> theChosenRestaurantarray = new ArrayList<Restaurant>();
    public static ArrayList<Activities> theChosenActivitiesarray = new ArrayList<Activities>();



    public void ResetText(){
        double myCost = 0;
        Log.e("Main","ResetText Called");
        ArrayList<POJOstringitem> myTempPojo = new ArrayList<POJOstringitem>();
        if(theChosenHotel != null){
            theChosenGroup = theChosenHotel.getGroup();
//            mChosen_Hotel_Name.setText(theChosenHotel.getName());
//            mChosen_Hotel_Price.setText(YouwillBedeductedStringBuilder(theChosenRoom.getTotalCost()));
            POJOstringitem temp = new POJOstringitem();
            temp.name = theChosenHotel.getName();
            temp.price = theChosenRoom.getTotalCost();
            myTempPojo.add(temp);
            myCost += theChosenRoom.getTotalCost();
            if(theChosenHotel != null && theChosenRoom != null){
                mRerserve_booking.setVisibility(View.VISIBLE);
            }

        }

        if(theChosenRestaurantarray.size() > 0){
            for(Restaurant myrest : theChosenRestaurantarray){
                theChosenGroup = myrest.getGroup();
                POJOstringitem temp = new POJOstringitem();
                temp.name = myrest.getName();
                temp.price = myrest.getPrice();
                myCost += temp.price;
                myTempPojo.add(temp);
            }

        }
        if(theChosenActivitiesarray.size() > 0){
            for(Activities myact : theChosenActivitiesarray){
                theChosenGroup = myact.getGroup();
                POJOstringitem temp = new POJOstringitem();
                temp.name = myact.getName();
                temp.price = myact.getTotalCost();
                myCost += temp.price;
                myTempPojo.add(temp);
            }

        }
        MainActivityRecyclerAdapter mainActivityRecyclerAdapter = new MainActivityRecyclerAdapter(MainActivity.this,myTempPojo,MainActivity.this);
        mRecyclerView.setAdapter(mainActivityRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TotalMake(myCost);
    }

    public void TotalMake(double val){
        mChosen_Total_Price.setText(YouwillBedeductedStringBuilder(val));
        if(myUser != null){
            if(val > myUser.getBudget()){
                mChosen_Total_Price.setTextColor(Color.parseColor("#000000"));
                Toast.makeText(MainActivity.this,"Budget too low...",Toast.LENGTH_LONG).show();
            }else{
                mChosen_Total_Price.setTextColor(Color.parseColor("#ff4081"));
            }
        }


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
        mBudget = navView.findViewById(R.id.nav_header_budget);

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

                mBudget.setText(String.valueOf(myUser.getBudget()));
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
            MainActivity.MapPick = MainActivity.HOTEL;
            startActivity(new Intent(MainActivity.this, Mapper.class));
        } else if (id == R.id.nav_activities) {
            MainActivity.MapPick = MainActivity.ACTIVITY;
            startActivity(new Intent(MainActivity.this, Mapper.class));
        } else if (id == R.id.nav_restaurant) {
            MainActivity.MapPick = MainActivity.RESTAURANT;
            startActivity(new Intent(MainActivity.this, Mapper.class));
        } else if (id == R.id.nav_my_profile) {
            startActivity(new Intent(MainActivity.this, UserProfile.class));
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

    public static boolean validatePastDate(Context mContext, int day, int month, int year){
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH)+1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        if (day > currentDay && year == currentYear && month == currentMonth) {
            Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (month > currentMonth && year == currentYear) {
            Toast.makeText(mContext, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (year > currentYear) {
            Toast.makeText(mContext, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static boolean validateFutureDate(Context mContext,int day,int month,int year){
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        if (day < currentDay && year == currentYear && month == currentMonth) {
            Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (month < currentMonth && year == currentYear) {
            Toast.makeText(mContext, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (year < currentYear) {
            Toast.makeText(mContext, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    private void initPage(){
        Button hotelAddButton = (Button) findViewById(R.id.booking_add_Hotel);
        Button activityAddButton = (Button) findViewById(R.id.booking_add_ActivityBtn);
        Button restaurantAddButton = (Button) findViewById(R.id.booking_add_RestaurantBtn);
        mChosen_Calender = (TextView) findViewById(R.id.main_activity_date);
        mChosen_Calender2 = (TextView) findViewById(R.id.main_activity_date2);


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final View bottomSheetView = getLayoutInflater().inflate(R.layout.calendar_view,null);
        bottomSheetDialog.setContentView(bottomSheetView);
        mCalendarView = (DatePicker) bottomSheetView.findViewById(R.id.datePicker);
        mCalendarView.setMinDate(System.currentTimeMillis() - 1000);
        mChosen_Calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromto = false;
                bottomSheetDialog.show();
            }
        });
        mChosen_Calender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromto = true;
                bottomSheetDialog.show();
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                int day = mCalendarView.getDayOfMonth();
                int month = mCalendarView.getMonth();
                int year = mCalendarView.getYear();

                if(validateFutureDate(mContext,day,month,year)){
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yy");
                    Date d = new Date(year, month, day);
                    String strDate = dateFormatter.format(d);
                    if(fromto){
                        mChosen_Calender2.setText(strDate);
                    }else{
                        mChosen_Calender.setText(strDate);
                    }
                }



            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.main_activity_recyclerview);
//        mChosen_Hotel_Name = (TextView) findViewById(R.id.chosen_hotel_name);
//        mChosen_Hotel_Price = (TextView) findViewById(R.id.chosen_hotel_price);
//        mChosen_Dining_Name = (TextView) findViewById(R.id.chosen_dining_name);
//        mChosen_Dining_Price = (TextView) findViewById(R.id.chosen_dining_price);
//        mChosen_Activity_Name = (TextView) findViewById(R.id.chosen_activity_name);
//        mChosen_Activity_Price = (TextView) findViewById(R.id.chosen_activity_price);
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
                if(theChosenActivitiesarray.size() > 0){

                    booking.setActivitiesArrayList(theChosenActivitiesarray);
                }
                if(theChosenRestaurantarray.size() > 0){

                    booking.setActivitiesArrayList(theChosenActivitiesarray);
                }

                if(!mChosen_Calender.equals("SET DATE")){
                    booking.setDate(mChosen_Calender.getText().toString());
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
                        theChosenActivitiesarray.clear();
                        theChosenRestaurantarray.clear();
                        ResetValues();
                    }
                });

            }
        });
    }

    private void ResetValues(){

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
