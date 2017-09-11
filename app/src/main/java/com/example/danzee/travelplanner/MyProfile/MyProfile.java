package com.example.danzee.travelplanner.MyProfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danzee.travelplanner.R;
import com.example.danzee.travelplanner.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfile extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private TextView email;
    private TextView username;
    private TextView firstname;
    private TextView lastname;
    private EditText budget;
    private User user;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        email = (TextView) findViewById(R.id.myprofile_email);
        username = (TextView) findViewById(R.id.myprofile_username);
        firstname = (TextView) findViewById(R.id.myprofile_firstname);
        lastname = (TextView) findViewById(R.id.myprofile_lastname);
        budget = (EditText) findViewById(R.id.myprofile_budget);
        updateBtn = (Button) findViewById(R.id.myprofile_update);

        final String myUser = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(myUser);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                email.setText(user.getEmail());
                username.setText(user.getUserName());
                firstname.setText(user.getFirstName());
                lastname.setText(user.getLastName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReferenceUpdate = firebaseDatabase.getReference().child("users").child(myUser).child("budget");
                databaseReferenceUpdate.setValue(Double.valueOf(budget.getText().toString()));
            }
        });


    }
}