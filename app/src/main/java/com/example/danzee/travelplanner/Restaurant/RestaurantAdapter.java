package com.example.danzee.travelplanner.Restaurant;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelDescription;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.MainActivity;
import com.example.danzee.travelplanner.MarkerDemoActivity;
import com.example.danzee.travelplanner.R;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.example.danzee.travelplanner.Restaurant.RestaurantList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Context mContext;
    private List<Restaurant> hotelList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, company;
        public ImageView imageView, overflow;
        public RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.booking_add_hotel_name);
            company = (TextView) view.findViewById(R.id.booking_add_hotel_company);
            imageView = (ImageView) view.findViewById(R.id.booking_add_hotel_imageview);
            overflow = (ImageView) view.findViewById(R.id.booking_add_hotel_overflow);
            ratingBar = (RatingBar) view.findViewById(R.id.booking_add_hotel_rating);
        }
    }


    public RestaurantAdapter(Context mContext, List<Restaurant> albumList,Activity mActivity) {
        this.mContext = mContext;
        this.hotelList = albumList;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_add_hotel_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Restaurant hotel = hotelList.get(position);
        holder.name.setText(hotel.getName());
        holder.company.setText(hotel.getCompany());

        holder.ratingBar.setRating(hotel.getRating());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestaurantList.selection = hotel;
                MainActivity.MapPick = MainActivity.RESTAURANT;
                View mySharedElement = view.findViewById(R.id.booking_add_hotel_imageview);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity,mySharedElement,"hotelImage");
                Intent i = new Intent(mContext, RestaurantDescription.class);
                mContext.startActivity(i,options.toBundle());

            }
        });

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                hotel.setRating(v);
                hotelList.get(position).setRating(v);
                DatabaseReference databaseReference = database.getReference().child("Restaurant").child(hotel.getID());
                databaseReference.setValue(hotel);

                DatabaseReference databaseReference2 = database.getReference().child("Group").child("Restaurant").child(hotel.getGroup()).child(hotel.getID());
                databaseReference2.setValue(hotel);
                Toast.makeText(mContext,"Thank you for Rating.",Toast.LENGTH_SHORT).show();
            }
        });

        // Create a storage reference from our app

        StorageReference storageRef = storage.getReference();

// Create a reference with an initial file path and name
        String path = hotel.getPhotoUrl();
//        StorageReference pathReference = storageRef.child(path);

        storageRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                try{
                    Glide.with(mContext).load(uri).into(holder.imageView);
                    hotel.myUri = uri;
                }catch (Exception e){
                    Log.e("Logger",e.getMessage());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        // loading album cover using Glide library


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow,position);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int MyPosition) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(MyPosition));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        Restaurant myHotelClicked;

        public MyMenuItemClickListener() {
        }

        public MyMenuItemClickListener(int MyPosition){
            myHotelClicked = hotelList.get(MyPosition);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_item_append:
                    MainActivity.MapPick = MainActivity.RESTAURANT;
                    RestaurantList.selection = myHotelClicked;
                    mContext.startActivity(new Intent(mContext,MarkerDemoActivity.class));

                    return true;
                case R.id.menu_item_details:

                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    private void SharedElementTransition(){


    }
}
