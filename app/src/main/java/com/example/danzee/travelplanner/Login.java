package com.example.danzee.travelplanner;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danzee.travelplanner.User.Registration;
import com.example.danzee.travelplanner.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements Registration.OnFragmentInteractionListener{

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    public static String TAG_LOGIN = "LOGIN-DEBUG";
    public static String TAG_REGISTER = "REGISTER-FRAGMENT";

    private EditText loginText;
    private EditText passwordText;
    private Button loginBtn;
    private Button registerBtn;
    SharedPreferences mySharedpref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        loginText = (EditText) findViewById(R.id.login_email);
        passwordText = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_button);
        registerBtn = (Button) findViewById(R.id.login_register);

        loginBtn.setOnClickListener(new onLoginClick());
        registerBtn.setOnClickListener(new onRegistrationClick());
         mySharedpref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       editor = mySharedpref.edit();
        loginText.setText(mySharedpref.getString("username",""));
        passwordText.setText(mySharedpref.getString("password",""));

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);


    }

    public void AttemptLogin(final String Email, final String Password){
        Context c = Login.this;
        mAuth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_LOGIN, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            editor.putString("username",Email);
                            editor.putString("password",Password);
                            editor.commit();

                            if(mAuth.getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(Login.this,MainActivity.class));
                                finish();
                            }else{
                               SendEmail();
                            }

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_LOGIN, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void SendEmail(){
    final Context c = Login.this;
        final FirebaseUser user3 = mAuth.getCurrentUser();
        user3.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
//                                                findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(c,
                                    "Verification email sent to " + user3.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG_LOGIN, "sendEmailVerification", task.getException());
                            Toast.makeText(c,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void SignUp(final User user1){
        mAuth.createUserWithEmailAndPassword(user1.getEmail(), user1.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_LOGIN, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Account Creation Success",
                                    Toast.LENGTH_SHORT).show();


                            CheckUserExist(user1);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_LOGIN, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void CheckUserExist(final User user){

        DatabaseReference userReference1 = db.getReference();
        final DatabaseReference userReference = userReference1.child("users");

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                    User newUser = dataSnapshot.getValue(User.class);
//                    startActivity(new Intent(Login.this,MainActivity.class));
//                    finish();
                    //finish the Intent here
                    SendEmail();

                }else{
                    userReference.child(mAuth.getCurrentUser().getUid()).setValue(user,
                            new DatabaseReference.CompletionListener(){

                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                    startActivity(new Intent(Login.this,MainActivity.class));
                                    SendEmail();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCancelRegistration() {
        getSupportFragmentManager().popBackStack(TAG_REGISTER,FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    private class onLoginClick implements View.OnClickListener{


        @Override
        public void onClick(View view) {
            if(EmailVerification(loginText.getText().toString())){
                AttemptLogin(loginText.getText().toString(), passwordText.getText().toString());
            }

        }
    }

    private class onRegistrationClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Registration registrationfragment = new Registration();
            CreateFragment(registrationfragment,TAG_REGISTER);
        }
    }

    private boolean EmailVerification(String email){

        if(!email.contains("@") || !email.contains(".")){
            Toast.makeText(Login.this,"INVALID EMAIL",Toast.LENGTH_LONG).show();

        }else{
            return true;
        }
        return false;
    }

    private void CreateFragment(Fragment fragment,String tag){
        if(getFragmentManager().findFragmentByTag(tag) != null){

        }else{

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.login_constraintlayout,fragment,tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

        }
    }

    @Override
    public void onSignUp(User user) {
        SignUp(user);
    }

    @Override
    public void onCreateToast(String message) {
        Toast.makeText(Login.this,message,Toast.LENGTH_LONG).show();
    }
}
