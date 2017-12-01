package com.example.danzee.travelplanner.Mapper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.danzee.travelplanner.MarkerDemoActivity;
import com.example.danzee.travelplanner.R;

public class Mapper extends AppCompatActivity {

    private RelativeLayout coron;
    private RelativeLayout elnido;
    private RelativeLayout puerto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapper);
        Bundle bundle = getIntent().getExtras();

//        String theChosen = bundle.getString("chosen");

        coron = (RelativeLayout) findViewById(R.id.mapper_coron);
        elnido = (RelativeLayout) findViewById(R.id.mapper_elnido);
        puerto = (RelativeLayout) findViewById(R.id.mapper_puerto);

        coron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Mapper.this,MarkerDemoActivity.class));
            }
        });

        elnido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Mapper.this,MarkerDemoActivity.class));
            }
        });


        puerto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Mapper.this,MarkerDemoActivity.class));
            }
        });

    }



}
