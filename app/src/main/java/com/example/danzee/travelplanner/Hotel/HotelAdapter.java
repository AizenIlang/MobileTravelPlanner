package com.example.danzee.travelplanner.Hotel;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danzee.travelplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DanZee on 08/08/2017.
 */

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private ArrayList<Hotel> hotelList;
    private Context context;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public HotelAdapter(ArrayList<Hotel> hotelList, Context context) {
        this.hotelList = hotelList;
        this.context = context;
        Log.e("Logging for Hotel:", "Hotel list Number : "+this.hotelList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Hotel tempHotel = hotelList.get(position);

        holder.nameView.setText(tempHotel.getName());
        holder.descriptionView.setText(tempHotel.getDetails());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelList.selection = tempHotel;
                view.getContext().startActivity(new Intent(view.getContext(),HotelDescription.class));
            }
        });
        // Create a storage reference from our app

        StorageReference storageRef = storage.getReference();

// Create a reference with an initial file path and name
        String path = tempHotel.getPhotoURL();
//        StorageReference pathReference = storageRef.child(path);

        storageRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.with(context)
                        .load(uri)
                        .resize(50, 50)
                        .centerCrop()
                        .into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public ImageView imageView;
        public TextView nameView;
        public TextView descriptionView;


        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.hotel_item_cardview);
            imageView = itemView.findViewById(R.id.hotel_item_imageview);
            nameView = itemView.findViewById(R.id.hotel_item_name);
            descriptionView = itemView.findViewById(R.id.hotel_item_description);

        }
    }
}
