package com.example.danzee.travelplanner.Mapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.danzee.travelplanner.R;

public class Mapper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapper);
        Bundle bundle = getIntent().getExtras();

        String theChosen = bundle.getString("chosen");

    }
}
