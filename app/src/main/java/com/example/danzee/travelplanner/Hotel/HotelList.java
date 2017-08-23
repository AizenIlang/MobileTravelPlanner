package com.example.danzee.travelplanner.Hotel;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.example.danzee.travelplanner.ListItemAdapter;
import com.example.danzee.travelplanner.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HotelList extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private RecyclerView recyclerView;
    private ListItemAdapter adapter;
    private List<Hotel> hotelList;
    public static Hotel selection;
    public static String selectionID;
    public LinearLayout bottomSheet;
    public FloatingActionButton fab;
    public LinearLayout sortAtoZ;
    public LinearLayout sortPrice;
    public LinearLayout sortRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_add_hotel_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.booking_add_hotel_toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Hotels");
        initCollapsingToolbar();

        fab = (FloatingActionButton) findViewById(R.id.hotel_fab);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bootom_persistent_sheet,null);
        bottomSheetDialog.setContentView(bottomSheetView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        hotelList = new ArrayList<>();
        adapter = new ListItemAdapter(this, hotelList,this);

        sortAtoZ = bottomSheetView.findViewById(R.id.bottom_sheet_sort_az);
        sortPrice = bottomSheetView.findViewById(R.id.bottom_sheet_sort_price);
        sortRating = bottomSheetView.findViewById(R.id.bottom_sheet_sort_rating);

        sortAtoZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SortByAZ();
            }
        });

        sortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortByPrice();
            }
        });

        sortRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortByRating();
            }
        });
        Button coron = (Button) findViewById(R.id.booking_add_hotel_coron_btn);
        Button elnido = (Button) findViewById(R.id.booking_add_hotel_elnido_btn);
        Button puerto = (Button) findViewById(R.id.booking_add_hotel_puerto_btn);

        coron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateListCoron();
            }
        });

        elnido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateListElNido();
            }
        });

        puerto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateListPuerto();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        populateList();

        try {
            Glide.with(this).load(R.drawable.hotelsample).into((ImageView) findViewById(R.id.booking_add_hotel_list_backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Hotels");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void populateList()
    {
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Hotels");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Hotel tempHotel = dataSnapshot.getValue(Hotel.class);
                if (tempHotel != null) {
                    tempHotel.setID(dataSnapshot.getKey());

                    hotelList.add(tempHotel);
                    Log.e("Map :",dataSnapshot.getValue().toString());
                    Log.e("Map :",tempHotel.getLocation1() + " " + tempHotel.getName());
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void populateListCoron()
    {
        hotelList.clear();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Hotels").child("CORON");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Hotel tempHotel = dataSnapshot.getValue(Hotel.class);
                if (tempHotel != null) {
                    tempHotel.setID(dataSnapshot.getKey());

                    hotelList.add(tempHotel);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void populateListElNido()
    {
        hotelList.clear();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Hotels").child("EL NIDO");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Hotel tempHotel = dataSnapshot.getValue(Hotel.class);
                if (tempHotel != null) {
                    tempHotel.setID(dataSnapshot.getKey());
                    hotelList.add(tempHotel);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void populateListPuerto()
    {
        hotelList.clear();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Hotels").child("PUERTO PRINCESSA");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Hotel tempHotel = dataSnapshot.getValue(Hotel.class);
                if (tempHotel != null) {
                    tempHotel.setID(dataSnapshot.getKey());
                    hotelList.add(tempHotel);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //For sorting Purposes

    private void SortByAZ(){
//        Collections.sort(hotelList, new Comparator<Hotel>() {
//            @Override
//            public int compare(Hotel hotel, Hotel t1) {
//                return hotel.getName().compareTo(t1.getName());
//            }
//
//
//        });
        Collections.reverse(hotelList);
        recyclerView.setAdapter(adapter);
        Log.e("Hotel","Sort by AZ");
    }

    private void SortByPrice(){
        Collections.sort(hotelList, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel hotel, Hotel t1) {
                return Double.compare(hotel.getAveragePrice(),t1.getAveragePrice());
            }


        });

        recyclerView.setAdapter(adapter);
    }

    private void SortByRating(){
        Collections.sort(hotelList, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel hotel, Hotel t1) {
                return Float.compare(hotel.getRating(),t1.getRating());
            }


        });
        Collections.reverse(hotelList);
        recyclerView.setAdapter(adapter);
    }
}
