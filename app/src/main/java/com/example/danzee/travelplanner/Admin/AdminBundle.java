package com.example.danzee.travelplanner.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.danzee.travelplanner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminBundle extends AppCompatActivity {

    private EditText amount;
    private MultiAutoCompleteTextView details;
    private Spinner hotelSpinner;
    private Spinner roomSpinner;
    private Spinner restaurantSpinner;
    private Spinner activitySpinner;
    private Button bundleAddBtn;

    private ArrayList<String> hotelList = new ArrayList<String>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bundle);

        amount = (EditText) findViewById(R.id.bundle_price);
        details = (MultiAutoCompleteTextView) findViewById(R.id.bundle_details);
        hotelSpinner = (Spinner) findViewById(R.id.bundle_hotel_spinner);
        roomSpinner = (Spinner) findViewById(R.id.bundle_room_spinner);
        restaurantSpinner = (Spinner) findViewById(R.id.bundle_room_spinner);
        activitySpinner = (Spinner) findViewById(R.id.bundle_activity_spinner);
        bundleAddBtn = (Button) findViewById(R.id.bundle_add_btn);

//        DatabaseReference getHotels = firebaseDatabase.getReference().child("")

    }



}
