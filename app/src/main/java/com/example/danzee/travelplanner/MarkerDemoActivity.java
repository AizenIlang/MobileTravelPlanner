/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.danzee.travelplanner;

import com.example.danzee.travelplanner.Activities.Activities;
import com.example.danzee.travelplanner.Activities.ActivitiesList;
import com.example.danzee.travelplanner.Hotel.Hotel;
import com.example.danzee.travelplanner.Hotel.HotelList;
import com.example.danzee.travelplanner.Restaurant.Restaurant;
import com.example.danzee.travelplanner.Restaurant.RestaurantList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This shows how to place markers on a map.
 */
public class MarkerDemoActivity extends AppCompatActivity implements
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnMarkerDragListener,
        OnSeekBarChangeListener,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);

    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);

    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);

    private static final LatLng ALICE_SPRINGS = new LatLng(-24.6980, 133.8807);

    private static final LatLng MANDALUYONG_CITYHALL = new LatLng(14.5777334, 121.0314702);

    private static final LatLng CORON = new LatLng(12.1207327, 120.10005);
    private static final LatLng EL_NIDO = new LatLng(11.202146, 119.4164515);
    private static final LatLng PUERTO = new LatLng(9.9672163, 118.78551);
    private static final int MY_LOCATION_REQUEST_CODE = 30;


    ArrayList<Marker> myMarkerList = new ArrayList<Marker>();


    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
                // This means that getInfoContents will be called.
                return null;
            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
                // This means that the default info contents will be used.
                return null;
            }
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            if (marker.equals(mBrisbane)) {
                badge = R.drawable.badge_qld;
            } else if (marker.equals(mAdelaide)) {
                badge = R.drawable.badge_sa;
            } else if (marker.equals(mSydney)) {
                badge = R.drawable.badge_nsw;
            } else if (marker.equals(mMelbourne)) {
                badge = R.drawable.badge_victoria;
            } else if (marker.equals(mPerth)) {
                badge = R.drawable.badge_wa;
            } else {
                // Passing 0 to setImageResource will clear the image view.
                badge = 0;
            }
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }

    private GoogleMap mMap;

    private Marker mPerth;

    private Marker mSydney;

    private Marker mBrisbane;

    private Marker mAdelaide;

    private Marker mMelbourne;

    private Marker mCoron;
    private Marker mElNido;
    private Marker mPuerto;


    /**
     * Keeps track of the last selected marker (though it may no longer be selected).  This is
     * useful for refreshing the info window.
     */
    private Marker mLastSelectedMarker;

    private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();

    private final ArrayList<Marker> mMarkerWholeThing = new ArrayList<Marker>();

    private TextView mTopText;

    private SeekBar mRotationBar;

    private CheckBox mFlatBox;

    private RadioGroup mOptions;

    private final Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_demo);

        mTopText = (TextView) findViewById(R.id.top_text);

        mRotationBar = (SeekBar) findViewById(R.id.rotationSeekBar);
        mRotationBar.setMax(360);
        mRotationBar.setOnSeekBarChangeListener(this);

        mFlatBox = (CheckBox) findViewById(R.id.flat);

        mOptions = (RadioGroup) findViewById(R.id.custom_info_window_options);
        mOptions.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mLastSelectedMarker != null && mLastSelectedMarker.isInfoWindowShown()) {
                    // Refresh the info window when the info window's content has changed.
                    mLastSelectedMarker.showInfoWindow();
                }
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();
        makeMyList();
        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");


        //WE DOUBLE CHECK IF THE MY CURRENT LOCATION IS SUPPORTED
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("Map", "Setting my Location Enabled");
            mMap.setMyLocationEnabled(true);

            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            // Show rationale and request permission.
            Log.e("Map", "Requesting permission");
            ActivityCompat.requestPermissions(MarkerDemoActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);


        }

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(PUERTO)
                .include(CORON)
                .include(EL_NIDO)
                .include(EL_NIDO)
//                .include(PERTH)
//                .include(SYDNEY)
//                .include(ADELAIDE)
//                .include(BRISBANE)
//                .include(MELBOURNE)
                .build();

        if (MainActivity.MapPick.equals(MainActivity.HOTEL)) {
            if (HotelList.selection != null) {
                Hotel newMarked = HotelList.selection;
                double xAxis = 0.00;
                double yAxis = 0.00;
                LatLng myLocation = new LatLng(xAxis, yAxis);
                Log.e("Map :", "HotelName : " + newMarked.getName() + " " + newMarked.getLocation1() + " , " + newMarked.getLocation2());
                if (!newMarked.getLocation1().isEmpty() && !newMarked.getLocation2().isEmpty()) {
                    try {
                        xAxis = Double.parseDouble(newMarked.getLocation1());
                        yAxis = Double.parseDouble(newMarked.getLocation2());
                        myLocation = new LatLng(xAxis, yAxis);
                    } catch (NumberFormatException e) {
                        Log.e("Map :", "Incorrect format of location 1 " + e.getMessage());
                    }
                    Log.e("Map :", "SETTING THE MAP BOUND TO : " + myLocation.toString());
                    LatLngBounds makePickBounds = new LatLngBounds.Builder().include(myLocation).build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(makePickBounds, 90));
                } else {
                    Log.e("Map :", "The Location is empty, going to set the default bounds");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 90));
                }

            } else {
                Log.e("Map :", "The Location is empty, going to set the default bounds");
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 90));
            }
        } else if (MainActivity.MapPick.equals(MainActivity.RESTAURANT)) {
            if (RestaurantList.selection != null) {
                Restaurant newMarked = RestaurantList.selection;
                double xAxis = 0.00;
                double yAxis = 0.00;
                LatLng myLocation = new LatLng(xAxis, yAxis);
                Log.e("Map :", "HotelName : " + newMarked.getName() + " " + newMarked.getLocation1() + " , " + newMarked.getLocation2());
                if (!newMarked.getLocation1().isEmpty() && !newMarked.getLocation2().isEmpty()) {
                    try {
                        xAxis = Double.parseDouble(newMarked.getLocation1());
                        yAxis = Double.parseDouble(newMarked.getLocation2());
                        myLocation = new LatLng(xAxis, yAxis);
                    } catch (NumberFormatException e) {
                        Log.e("Map :", "Incorrect format of location 1 " + e.getMessage());
                    }
                    Log.e("Map :", "SETTING THE MAP BOUND TO : " + myLocation.toString());
                    LatLngBounds makePickBounds = new LatLngBounds.Builder().include(myLocation).build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(makePickBounds, 90));
                } else {
                    Log.e("Map :", "The Location is empty, going to set the default bounds");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 90));
                }

            } else {
                Log.e("Map :", "The Location is empty, going to set the default bounds");
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 90));
            }
        } else if (MainActivity.MapPick.equals(MainActivity.ACTIVITY)) {
            if (ActivitiesList.selection != null) {
                Activities newMarked = ActivitiesList.selection;
                double xAxis = 0.00;
                double yAxis = 0.00;
                LatLng myLocation = new LatLng(xAxis, yAxis);
                Log.e("Map :", "HotelName : " + newMarked.getName() + " " + newMarked.getLocation1() + " , " + newMarked.getLocation2());
                if (!newMarked.getLocation1().isEmpty() && !newMarked.getLocation2().isEmpty()) {
                    try {
                        xAxis = Double.parseDouble(newMarked.getLocation1());
                        yAxis = Double.parseDouble(newMarked.getLocation2());
                        myLocation = new LatLng(xAxis, yAxis);
                    } catch (NumberFormatException e) {
                        Log.e("Map :", "Incorrect format of location 1 " + e.getMessage());
                    }
                    Log.e("Map :", "SETTING THE MAP BOUND TO : " + myLocation.toString());
//                    LatLngBounds makePickBounds = new LatLngBounds.Builder().include(myLocation).build();
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(makePickBounds, 90));
                } else {
//                    Log.e("Map :", "The Location is empty, going to set the default bounds");
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 90));
                }

            } else {
//                Log.e("Map :", "The Location is empty, going to set the default bounds");
//                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 90));
            }
        }


    }

    private void addMarkersToMap() {

        //The Philipine 3 group
        mCoron = mMap.addMarker(new MarkerOptions()
                .position(CORON)
                .title("CORON")
                .snippet("Welcome to CORON")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mElNido = mMap.addMarker(new MarkerOptions()
                .position(EL_NIDO)
                .title("EL NIDO")
                .snippet("Welcome to EL NIDO")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mPuerto = mMap.addMarker(new MarkerOptions()
                .position(PUERTO)
                .title("PUERTO PRINCESSA")
                .snippet("Welcome to PUERTO PRINCESSA")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

////        // Uses a colored icon.
//        mBrisbane = mMap.addMarker(new MarkerOptions()
//                .position(BRISBANE)
//                .title("Brisbane")
//                .snippet("Population: 2,074,200")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//
//        // Uses a custom icon with the info window popping out of the center of the icon.
//        mSydney = mMap.addMarker(new MarkerOptions()
//                .position(SYDNEY)
//                .title("Sydney")
//                .snippet("Population: 4,627,300")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
//                .infoWindowAnchor(0.5f, 0.5f));
//
//        // Creates a draggable marker. Long press to drag.
//        mMelbourne = mMap.addMarker(new MarkerOptions()
//                .position(MELBOURNE)
//                .title("Melbourne")
//                .snippet("Population: 4,137,400")
//                .icon(vectorToBitmap(R.drawable.ic_hotel_black_24px,Color.parseColor("#A4C639")))
//                .draggable(true)
//        );
//
//        // A few more markers for good measure.
//        mPerth = mMap.addMarker(new MarkerOptions()
//                .position(PERTH)
//                .title("Perth")
//                .snippet("Population: 1,738,800"));
//        mAdelaide = mMap.addMarker(new MarkerOptions()
//                .position(ADELAIDE)
//                .title("Adelaide")
//                .snippet("Population: 1,213,000"));
//
//        // Vector drawable resource as a marker icon.
//        mMap.addMarker(new MarkerOptions()
//                .position(ALICE_SPRINGS)
//                .icon(vectorToBitmap(R.drawable.ic_android, Color.parseColor("#A4C639")))
//                .title("Alice Springs"));

        // Creates a marker rainbow demonstrating how to create default marker icons of different
        // hues (colors).
//        float rotation = mRotationBar.getProgress();
//        boolean flat = mFlatBox.isChecked();
//
//        int numMarkersInRainbow = 12;
//        for (int i = 0; i < numMarkersInRainbow; i++) {
//            Marker marker = mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(
//                            -30 + 10 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)),
//                            135 - 10 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
//                    .title("Marker " + i)
//                    .icon(BitmapDescriptorFactory.defaultMarker(i * 360 / numMarkersInRainbow))
//                    .flat(flat)
//                    .rotation(rotation));
//            mMarkerRainbow.add(marker);
//        }
    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     */
    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /** Called when the Clear button is clicked. */
    public void onClearMap(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }

    /** Called when the Reset button is clicked. */
    public void onResetMap(View view) {
        if (!checkReady()) {
            return;
        }
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
        addMarkersToMap();
    }

    /** Called when the Reset button is clicked. */
    public void onToggleFlat(View view) {
        if (!checkReady()) {
            return;
        }
        boolean flat = mFlatBox.isChecked();
        for (Marker marker : mMarkerRainbow) {
            marker.setFlat(flat);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!checkReady()) {
            return;
        }
        float rotation = seekBar.getProgress();
        for (Marker marker : mMarkerRainbow) {
            marker.setRotation(rotation);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Do nothing.
    }

    //
    // Marker related listeners.
    //

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(mCoron) || marker.equals(mElNido) || marker.equals(mPuerto)) {
            // This causes the marker at Perth to bounce into position when it is clicked.
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(
                            1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                    marker.setAnchor(0.5f, 1.0f + 2 * t);

                    if (t > 0.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        } else if (marker.equals(mAdelaide)) {
            // This causes the marker at Adelaide to change color and alpha.
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(mRandom.nextFloat() * 360));
            marker.setAlpha(mRandom.nextFloat());
        }

        // Markers have a z-index that is settable and gettable.
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(this, marker.getTitle() + " z-index set to " + zIndex,
                Toast.LENGTH_SHORT).show();

        mLastSelectedMarker = marker;
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        mTopText.setText("onMarkerDragStart");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mTopText.setText("onMarkerDragEnd");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
    }


    public void makeMyList() {

        if (MainActivity.MapPick.equals(MainActivity.HOTEL)) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Hotels");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Hotel tempHotel = dataSnapshot.getValue(Hotel.class);
                    Marker tempMarker;

                    if (tempHotel.getLocation1() != null && tempHotel.getLocation2() != null) {

                        LatLng myPosition = new LatLng(Double.parseDouble(tempHotel.getLocation1()), Double.parseDouble(tempHotel.getLocation2()));
                        tempMarker = mMap.addMarker(new MarkerOptions()
                                .position(myPosition)
                                .title(tempHotel.getName())
                                .icon(vectorToBitmap(R.drawable.ic_hotel_black_24px, Color.parseColor("#A4C639")))
                                .snippet("Price :" + String.valueOf(tempHotel.getAveragePrice())));
                        myMarkerList.add(tempMarker);

                    }
                    Log.e("Map :", "Location 1 is : " + tempHotel.getLocation1() + " " + "Location 2 is : " + tempHotel.getLocation2());

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


        } else if (MainActivity.MapPick.equals(MainActivity.RESTAURANT)) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Restaurant");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Restaurant tempHotel = dataSnapshot.getValue(Restaurant.class);
                    Marker tempMarker;

                    if (tempHotel.getLocation1() != null && tempHotel.getLocation2() != null) {

                        LatLng myPosition = new LatLng(Double.parseDouble(tempHotel.getLocation1()), Double.parseDouble(tempHotel.getLocation2()));
                        tempMarker = mMap.addMarker(new MarkerOptions()
                                .position(myPosition)
                                .title(tempHotel.getName())
                                .icon(vectorToBitmap(R.drawable.ic_hotel_black_24px, Color.parseColor("#DD0000")))
                                .snippet("Price :" + String.valueOf(tempHotel.getPrice())));
                        myMarkerList.add(tempMarker);

                    }
                    Log.e("Map :", "Location 1 is : " + tempHotel.getLocation1() + " " + "Location 2 is : " + tempHotel.getLocation2());

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


        } else if (MainActivity.MapPick.equals(MainActivity.ACTIVITY)) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Activity");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Activities tempHotel = dataSnapshot.getValue(Activities.class);
                    Marker tempMarker;

                    if (tempHotel.getLocation1() != null && tempHotel.getLocation2() != null) {

                        LatLng myPosition = new LatLng(Double.parseDouble(tempHotel.getLocation1()), Double.parseDouble(tempHotel.getLocation2()));
                        tempMarker = mMap.addMarker(new MarkerOptions()
                                .position(myPosition)
                                .title(tempHotel.getName())
                                .icon(vectorToBitmap(R.drawable.ic_directions_bike_black_24px, Color.parseColor("#CCCCCC")))
                                .snippet("Price :" + String.valueOf(tempHotel.getTotalCost())));
                        myMarkerList.add(tempMarker);

                    }
                    Log.e("Map :", "Location 1 is : " + tempHotel.getLocation1() + " " + "Location 2 is : " + tempHotel.getLocation2());

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


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                Log.e("Map", "Setting my Location Enabled");
            } else {
                // Permission was denied. Display an error message.
                Log.e("Map", "Setting my Location False");
            }
        }
    }
    public void ChangeTheList(){

    }
}
