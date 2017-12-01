package com.example.danzee.travelplanner.Booking;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danzee.travelplanner.POJOstringitem;
import com.example.danzee.travelplanner.R;

import java.util.List;

/**
 * Created by DanZee on 15/09/2017.
 */

public class BookingItemAdapter extends RecyclerView.Adapter<BookingItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<POJOstringitem> hotelList;
    private Activity mActivity;

    public BookingItemAdapter(Context mContext, List<POJOstringitem> hotelList, Activity mActivity){
        this.mContext = mContext;
        this.hotelList = hotelList;
        this.mActivity = mActivity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName, itemPrice;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.main_activity_list_item_name);
            itemPrice = itemView.findViewById(R.id.main_activity_list_item_price);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_activity_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        POJOstringitem tempPojo = hotelList.get(position);
        holder.itemName.setText(tempPojo.name);
        holder.itemPrice.setText(String.valueOf(tempPojo.price));
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }
}
