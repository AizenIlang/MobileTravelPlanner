package com.example.danzee.travelplanner.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danzee.travelplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private TextView Email;
    private EditText Password;
    private TextView FirstName;
    private TextView LastName;
    private EditText Budget;
    private Button update;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Email = (TextView) findViewById(R.id.user_profile_email);
        Password = (EditText) findViewById(R.id.user_profile_password);
        FirstName = (TextView) findViewById(R.id.user_profile_first_name);
        LastName = (TextView) findViewById(R.id.user_profile_last_name);
        Budget = (EditText) findViewById(R.id.user_profile_budget);
        update = (Button) findViewById(R.id.user_profile_update);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User myTemp = dataSnapshot.getValue(User.class);
                Email.setText(myTemp.getEmail());
                FirstName.setText(myTemp.getFirstName());
                LastName.setText(myTemp.getLastName());
                Password.setText(myTemp.getPassword());
                Budget.setText(String.valueOf(myTemp.getBudget()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("password");
                db.setValue(Password.getText().toString());
                DatabaseReference db2 = firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("budget");
                db2.setValue(Double.parseDouble(Budget.getText().toString()));
            }
        });
    }
}
