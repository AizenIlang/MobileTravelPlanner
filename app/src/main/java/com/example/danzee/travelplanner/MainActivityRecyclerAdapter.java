package com.example.danzee.travelplanner;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.example.danzee.travelplanner.Restaurant.RestaurantAdapter;

import java.util.List;

/**
 * Created by DanZee on 14/09/2017.
 */

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<POJOstringitem> hotelList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName;
        public TextView itemPrice;
        public LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.main_activity_list_item_name);
            itemPrice = itemView.findViewById(R.id.main_activity_list_item_price);
            linearLayout = itemView.findViewById(R.id.main_activity_item_ll);
        }
    }

    public MainActivityRecyclerAdapter(Context mContext, List<POJOstringitem> albumList,Activity mActivity) {
        this.mContext = mContext;
        this.hotelList = albumList;
        this.mActivity = mActivity;
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
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return hotelList.size();
    }




}
