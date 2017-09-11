package com.example.danzee.travelplanner.Booking;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.ListItemAdapter;
import com.example.danzee.travelplanner.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by DanZee on 18/08/2017.
 */

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private Context mContext;
    private List<Booking> hotelList;
    private Activity mActivity;

    public BookingAdapter(Context mContext, List<Booking> hotelList, Activity mActivity){
        this.mContext = mContext;
        this.hotelList = hotelList;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Booking myTempBooking = hotelList.get(position);
        holder.activityName.setText(myTempBooking.getHotel().getName());
        holder.hotelName.setText(myTempBooking.getRooms().getName());
    }

    @Override
    public int getItemCount() {
        Log.e("Booking","Count that we HAve" + String.valueOf(hotelList.size()));
        return 2;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView hotelName, restaurantName, activityName;

        public MyViewHolder(View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.booking_list_item_HotelName);
            restaurantName = itemView.findViewById(R.id.booking_list_item_RestaurantName);
            activityName = itemView.findViewById(R.id.booking_list_item_ActivityName);
        }
    }
}
