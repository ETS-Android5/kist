package example.com.kist.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.Objects.MapObject;
import example.com.kist.R;

/**
 * Created by pr0 on 12/8/17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnMarkerClickListener {

    MapView mapView;
    GoogleMap googleMap = null;

    List<MapObject> pins = new ArrayList<>();
    MapObject selected;

    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";

    SQLiteDatabase db;

    ImageView showEats, showDrinks, showAttr, showHostel;
    LatLngBounds.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.map_fragment, viewGroup, false);

        builder = new LatLngBounds.Builder();

        try {
            DATABASE_PATH = getActivity().getPackageManager().
                    getPackageInfo(getActivity().getPackageName(), 0).applicationInfo.dataDir + "/";

            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, 0);

            mapView = (MapView) v.findViewById(R.id.map);

            showEats = (ImageView) getActivity().findViewById(R.id.food_map);
            showAttr = (ImageView) getActivity().findViewById(R.id.attraction_map);
            showDrinks = (ImageView) getActivity().findViewById(R.id.drinks_map);
            showHostel = (ImageView) getActivity().findViewById(R.id.hostel_map);

            showAttr.setOnClickListener(this);
            showHostel.setOnClickListener(this);
            showDrinks.setOnClickListener(this);
            showEats.setOnClickListener(this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
        @Override
    public void onMapReady(GoogleMap googleMap) {
            this.googleMap = googleMap;
            MapsInitializer.initialize(getActivity());

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

            queryLocations("Drink");
            queryLocations("Food");
            queryLocations("HostelDetails");
            queryLocations("Attraction");

            Log.e("reached callback", "map");

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            googleMap.setOnMarkerClickListener(this);
            googleMap.setMyLocationEnabled(true);

            ImageView btnMyLocation = (ImageView) ((View) mapView.findViewById(1).getParent()).findViewById(2);
            btnMyLocation.setImageResource(R.mipmap.location);
            btnMyLocation.getLayoutParams().height = 20;
            btnMyLocation.getLayoutParams().width = 20;

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    btnMyLocation.getLayoutParams();

            // position on left bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);

            mapView.setPadding(30, 0, 0, 30);

            if (getArguments().getBoolean("single", false)) {
                selected = new MapObject();

                selected.setLatitude(getArguments().getDouble("lat"));
                selected.setLongitude(getArguments().getDouble("long"));
                selected.setType(getArguments().getString("type", ""));
                selected.setName(getArguments().getString("name", ""));
                BitmapDescriptor ic = null;

                String type = selected.getType();

                if (type.contains("Drink"))
                    ic = BitmapDescriptorFactory.fromResource(R.mipmap.mapdrink);
                else if (type.contains("Food"))
                    ic = BitmapDescriptorFactory.fromResource(R.mipmap.mapeats);
                else if (type.contains("Attraction"))
                    ic = BitmapDescriptorFactory.fromResource(R.mipmap.mapattractions);
                else if (type.contains("HostelDetails"))
                    ic = BitmapDescriptorFactory.fromResource(R.mipmap.maphostelicon);


                drawMarker(new LatLng(selected.getLatitude(), selected.getLongitude()), ic,
                        selected.getName());
            } else {
                if (getArguments().getBoolean("directions", false)) {

                } else {
                    showHostel.setAlpha(0.5f);
                    showPins("HostelDetails");
                }
            }

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    if(!selected.getType().contains("HostelDetails")) {
                        LocalGuideListItem item = queryDetails(selected.getType(), selected.getName());
                        if (item.getType().contains("Attraction")) {
                            String s = new Gson().toJson(item);
                            ((MainActivity) getActivity()).setItemFrag(6, s, "Attraction");
                        } else if (item.getType().contains("Food")) {
                            String s = new Gson().toJson(item);
                            ((MainActivity) getActivity()).setItemFrag(6, s, "Food");
                        } else if (item.getType().contains("Drink")) {
                            String s = new Gson().toJson(item);
                            ((MainActivity) getActivity()).setItemFrag(6, s, "Drink");
                        }
                    } else {
                        Log.e("clicked", "info");
                        ((MainActivity) getActivity()).setPage(1);
                    }
                }
            });
        }


    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onPause() {
        mapView.onStop();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    protected void queryLocations(String table) {
        Cursor cursor = db.query(table, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                MapObject mapObject = new MapObject();
                mapObject.setType(table);

                mapObject.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
                mapObject.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
                mapObject.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));

                Log.e("lat long", mapObject.getLatitude() + "   " + mapObject.getLongitude() + "");
                pins.add(mapObject);
            } while (cursor.moveToNext());
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private void showPins(String type) {
        Log.e("showing pin", type);

        BitmapDescriptor ic = null;
        builder = new LatLngBounds.Builder();

        if (type.contains("Drink"))
            ic = BitmapDescriptorFactory.fromResource(R.mipmap.mapdrink);
        else if (type.contains("Food"))
            ic = BitmapDescriptorFactory.fromResource(R.mipmap.mapeats);
        else if (type.contains("Attraction"))
            ic = BitmapDescriptorFactory.fromResource(R.mipmap.mapattractions);
        else if (type.contains("HostelDetails"))
            ic = BitmapDescriptorFactory.fromResource(R.mipmap.maphostelicon);

        for (MapObject object : pins) {
            if (object.getType().contains(type)) {
                drawMarker(new LatLng(object.getLatitude(), object.getLongitude()), ic, object.getName());
            }
        }

        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        googleMap.animateCamera(cu);
    }

    private void drawMarker(LatLng point, BitmapDescriptor icon, String name){
        // Creating an instance of MarkerOptions

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(icon);
        markerOptions.title(name);

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);

        if (getArguments().getBoolean("single", false)) {
            builder = new LatLngBounds.Builder();
            builder.include(point);

            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
            googleMap.animateCamera(cu);
        }

        builder.include(point);
    }

    @Override
    public void onClick(View view) {
        if(view == showEats) {
            googleMap.clear();

            if(showEats.getAlpha() == 1f) {
                showPins("Food");
                changeAlphaAll();
                showEats.setAlpha(0.5f);
            } else
                showEats.setAlpha(1f);
        } else if(view == showDrinks) {
            googleMap.clear();

            if(showDrinks.getAlpha() == 1f) {
                showPins("Drink");
                changeAlphaAll();
                showDrinks.setAlpha(0.5f);
            } else
                showDrinks.setAlpha(1f);
        } else if(view == showAttr) {
            googleMap.clear();

            if(showAttr.getAlpha() == 1f) {
                showPins("Attraction");
                changeAlphaAll();
                showAttr.setAlpha(0.5f);
            } else
                showAttr.setAlpha(1f);
        } else if(view == showHostel) {
            googleMap.clear();

            if(showHostel.getAlpha() == 1f) {
                showPins("HostelDetails");
                changeAlphaAll();
                showHostel.setAlpha(0.5f);
            } else
                showHostel.setAlpha(1f);
        }
    }

    private void changeAlphaAll() {
        showAttr.setAlpha(1f);
        showHostel.setAlpha(1f);
        showEats.setAlpha(1f);
        showDrinks.setAlpha(1f);
    }

    private LocalGuideListItem queryDetails(String table, String name) {

        LocalGuideListItem item = new LocalGuideListItem();

        Cursor cursor = db.query(table, null, "name = " + "\"" + name + "\"", null, null, null, null);

        if (cursor.moveToFirst()) {

            item.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            item.setDes1(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            item.setDes2(cursor.getString(cursor.getColumnIndexOrThrow("description2")));
            item.setDes3(cursor.getString(cursor.getColumnIndexOrThrow("description3")));
            item.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow("longitude")));
            item.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow("latitude")));
            item.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            item.setWebsite(cursor.getString(cursor.getColumnIndexOrThrow("website")));
            item.setBlurb(cursor.getString(cursor.getColumnIndexOrThrow("blurb")));
            item.setTaurl(cursor.getString(cursor.getColumnIndexOrThrow("TAURL")));
            item.setFbLink(cursor.getString(cursor.getColumnIndexOrThrow("FacebookURL")));
            item.setInstaLink(cursor.getString(cursor.getColumnIndexOrThrow("InstagramURL")));
            item.setOpeningHrs(cursor.getString(cursor.getColumnIndexOrThrow("OpeningHours")));
            item.setGettingThere(cursor.getString(cursor.getColumnIndexOrThrow("GettingThere")));

            if (table.contains("Drink") || table.contains("Attraction"))
                item.setClosest(cursor.getString(cursor.getColumnIndexOrThrow("ClosestPublicTransport")));
            else if (table.contains("Food"))
                item.setClosest(cursor.getString(cursor.getColumnIndexOrThrow("ClosestStop")));

            item.setType(table);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return item;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(MapObject object: pins) {
            if(object.getName().equals(marker.getTitle()))
                selected = object;
        }

        marker.showInfoWindow();
        return true;
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.map_info_window, null);

            ImageView tvSnippet = ((ImageView)myContentsView.findViewById(R.id.info));
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
