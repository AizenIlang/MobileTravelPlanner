package com.example.danzee.travelplanner.User;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danzee.travelplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Registration.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */

public class Registration extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button bCancel;
    private Button bSignup;


    private EditText mUserName;
    private EditText mEmail;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPassword;

    private Context c;
    public Registration() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bCancel = view.findViewById(R.id.registration_cancel);
        bSignup = view.findViewById(R.id.registration_signup);

        mEmail = view.findViewById(R.id.registration_email);
        mUserName = view.findViewById(R.id.registration_username);
        mPassword = view.findViewById(R.id.registration_password);
        mFirstName = view.findViewById(R.id.registration_firstname);
        mLastName = view.findViewById(R.id.registration_lastname);

        bCancel.setOnClickListener(new onCancelClickListener());
        bSignup.setOnClickListener(new onSignUpClickListener());

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            this.c = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onCancelRegistration();
        void onSignUp(User user);
        void onCreateToast(String message);
    }

    private class onCancelClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            mListener.onCancelRegistration();
        }
    }

    private class onSignUpClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(Validation()){
                User createUser = new User("",mUserName.getText().toString()
                        ,mFirstName.getText().toString()
                        ,mLastName.getText().toString()
                        ,mEmail.getText().toString()
                        ,mPassword.getText().toString()
                        ,User.TYPE_USER);

                mListener.onSignUp(createUser);
            }

        }
    }

    private boolean Validation(){
        if(mUserName.getText().toString().isEmpty()){
            mListener.onCreateToast("USER NAME REQUIRED");
            return false;

        }
        if(mEmail.getText().toString().isEmpty()){
            mListener.onCreateToast("EMAIL REQUIRED");
            return false;
        }
        if(mFirstName.getText().toString().isEmpty()){
            mListener.onCreateToast("FIRST NAME REQUIRED");
            return false;
        }
        if(mLastName.getText().toString().isEmpty()){
            mListener.onCreateToast("LAST NAME REQUIRED");
            return false;
        }
        if(mPassword.getText().toString().isEmpty()){
            mListener.onCreateToast("PASSWORD REQUIRED");
            return false;
        }
        if(!mEmail.getText().toString().contains("@") || !mEmail.getText().toString().contains(".")){
            mListener.onCreateToast("INVALID EMAIL");
            return false;
        }
        return true;
    }
}
