package com.example.danzee.travelplanner.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.ListItemAdapter;
import com.example.danzee.travelplanner.MainActivity;
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

public class ActivitiesList extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private RecyclerView recyclerView;
    private ActivitiesAdapter adapter;
    private List<Activities> hotelList;
    public static Activities selection;
    public FloatingActionButton fab;
    public LinearLayout sortAtoZ;
    public LinearLayout sortPrice;
    public Context context = ActivitiesList.this;
    public Activity activity = ActivitiesList.this;

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
        adapter = new ActivitiesAdapter(this, hotelList,this);

        sortAtoZ = bottomSheetView.findViewById(R.id.bottom_sheet_sort_az);
        sortPrice = bottomSheetView.findViewById(R.id.bottom_sheet_sort_price);

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

        Button coron = (Button) findViewById(R.id.booking_add_hotel_coron_btn);
        Button elnido = (Button) findViewById(R.id.booking_add_hotel_elnido_btn);
        Button puerto = (Button) findViewById(R.id.booking_add_hotel_puerto_btn);

        if(MainActivity.theChosenGroup.equals(MainActivity.NONE)){
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
        }else if(MainActivity.theChosenGroup.equals("CORON")){
            coron.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    populateListCoron();
                }
            });
            populateListCoron();
            elnido.setVisibility(View.GONE);
            puerto.setVisibility(View.GONE);
        }else if(MainActivity.theChosenGroup.equals("EL NIDO")){
            elnido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    populateListElNido();
                }
            });
            populateListElNido();
            coron.setVisibility(View.GONE);
            puerto.setVisibility(View.GONE);
        }else if(MainActivity.theChosenGroup.equals("PUERTO PRINCESSA")){
            puerto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    populateListPuerto();
                }
            });
            populateListPuerto();
            coron.setVisibility(View.GONE);
            puerto.setVisibility(View.GONE);
        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        TextView title = (TextView) findViewById(R.id.booking_add_hotel_title);
        TextView header = (TextView) findViewById(R.id.booking_add_hotel_choose_header);
        title.setText("Activities");
        header.setText("What to do? What to do?");

        populateList();

        try {
            Glide.with(this).load(R.drawable.activitiessample).into((ImageView) findViewById(R.id.booking_add_hotel_list_backdrop));
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
                    collapsingToolbar.setTitle("Activities");
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
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Activity");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Activities tempHotel = dataSnapshot.getValue(Activities.class);
                tempHotel.setID(dataSnapshot.getKey());
                hotelList.add(tempHotel);
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
                adapter = new ActivitiesAdapter(context, hotelList,activity);
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
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Activity").child("CORON");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Activities tempHotel = dataSnapshot.getValue(Activities.class);
                tempHotel.setID(dataSnapshot.getKey());
                hotelList.add(tempHotel);
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
                adapter = new ActivitiesAdapter(context, hotelList,activity);
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
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Activity").child("EL NIDO");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Activities tempHotel = dataSnapshot.getValue(Activities.class);
                tempHotel.setID(dataSnapshot.getKey());
                hotelList.add(tempHotel);
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
                adapter = new ActivitiesAdapter(context, hotelList,activity);
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
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Group").child("Activity").child("PUERTO PRINCESSA");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Activities tempHotel = dataSnapshot.getValue(Activities.class);
                tempHotel.setID(dataSnapshot.getKey());
                hotelList.add(tempHotel);
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
                adapter = new ActivitiesAdapter(context, hotelList,activity);
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
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
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
        Collections.sort(hotelList, new Comparator<Activities>() {
            @Override
            public int compare(Activities activities, Activities t1) {
                return Double.compare(activities.getTotalCost(),t1.getTotalCost());
            }


        });

        recyclerView.setAdapter(adapter);
    }
}
