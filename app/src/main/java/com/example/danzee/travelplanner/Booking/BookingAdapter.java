package com.example.danzee.travelplanner.Booking;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danzee.travelplanner.Activities.Activities;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.ListItemAdapter;
import com.example.danzee.travelplanner.POJOstringitem;
import com.example.danzee.travelplanner.R;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DanZee on 18/08/2017.
 */

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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

        holder.hotelName.setText(myTempBooking.getHotel().getName());
        holder.roomName.setText(myTempBooking.getRooms().getName());
        ArrayList<POJOstringitem> pojOstringitem = new ArrayList<POJOstringitem>();

        if(myTempBooking.getActivitiesArrayList() != null){
            for(Activities activities : myTempBooking.getActivitiesArrayList()){
                POJOstringitem temp = new POJOstringitem();
                temp.name = activities.getName();
                temp.price = activities.getTotalCost();
                pojOstringitem.add(temp);
            }
        }


        if(myTempBooking.getRestaurantArrayList() != null){
            for(Restaurant restaurant : myTempBooking.getRestaurantArrayList()){
                POJOstringitem temp = new POJOstringitem();
                temp.name = restaurant.getName();
                temp.price = restaurant.getPrice();
                pojOstringitem.add(temp);
            }
        }

        holder.date.setText(myTempBooking.getDate());
        holder.total.setText(myTempBooking.getsTotalCost());
        holder.roomPrice.setText(myTempBooking.getRooms().getsTotalCost());
        BookingItemAdapter bookingItemAdapter = new BookingItemAdapter(mContext,pojOstringitem,mActivity);
        holder.recyclerView.setAdapter(bookingItemAdapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public int getItemCount() {
        Log.e("Booking","Count that we HAve" + String.valueOf(hotelList.size()));
        return hotelList.size();


    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView hotelName, roomName, date ,total, roomPrice;
        public RecyclerView recyclerView;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            roomPrice = itemView.findViewById(R.id.booking_item_price);
            date = itemView.findViewById(R.id.booking_item_date);
            total = itemView.findViewById(R.id.booking_item_total);
            hotelName = itemView.findViewById(R.id.booking_item_hotel_name);
            roomName = itemView.findViewById(R.id.booking_item_room);
            recyclerView = itemView.findViewById(R.id.booking_recycler_adapter);
        }
    }
}
