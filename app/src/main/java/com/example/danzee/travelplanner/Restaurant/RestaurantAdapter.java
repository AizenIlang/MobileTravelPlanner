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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelDescription;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.R;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.example.danzee.travelplanner.Restaurant.RestaurantList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context mContext;
    private List<Restaurant> hotelList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, company;
        public ImageView imageView, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.booking_add_hotel_name);
            company = (TextView) view.findViewById(R.id.booking_add_hotel_company);
            imageView = (ImageView) view.findViewById(R.id.booking_add_hotel_imageview);
            overflow = (ImageView) view.findViewById(R.id.booking_add_hotel_overflow);
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Restaurant hotel = hotelList.get(position);
        holder.name.setText(hotel.getName());
        holder.company.setText(hotel.getCompany());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestaurantList.selection = hotel;
                View mySharedElement = view.findViewById(R.id.booking_add_hotel_imageview);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity,mySharedElement,"hotelImage");
                Intent i = new Intent(mContext, RestaurantDescription.class);
                mContext.startActivity(i,options.toBundle());

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
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_item_append:


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
