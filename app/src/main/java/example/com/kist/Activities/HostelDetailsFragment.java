package example.com.kist.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.Constant.ExpandableHeightGridView;
import example.com.kist.Constant.ImageAdapter;
import example.com.kist.R;

/**
 * Created by pr0 on 3/8/17.
 */

public class HostelDetailsFragment extends Fragment implements View.OnClickListener {

    TextView desc1, desc2, title;
    ExpandableHeightGridView rooms, facilities;
    SliderLayout images;

    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";

    ImageView bookBed, callUs, directions, emailUs, showMap, contact, fb, insta, tripAdvisor;

    String name, description1, description2, phone, address, email, webPage, longt, lat, instaUrl, fbUrl, tripUrl;

    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.details_fragment, viewGroup, false);

        intialize(v);
        setOnClickListeners();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        try {
            DATABASE_PATH = getActivity().getPackageManager().
                    getPackageInfo(getActivity().getPackageName(), 0).applicationInfo.dataDir + "/";

            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, 0);

            List<Integer> res = queryForImages();
            Log.e("res images", res.size() + "");

            if(res != null && res.size() != 0) {
                for(int i =0; i < res.size(); ++i) {
                    DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                    sliderView.image(res.get(i)).setScaleType(BaseSliderView.ScaleType.Fit);

                    images.addSlider(sliderView);
                }
            }

            images.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            images.stopAutoCycle();
            queryForDetails();

            title.setText(name);
            desc1.setText(description1);
            desc2.setText(description2);

            setFacilitiesGrid(queryForFacilities());

            List<Integer> thumbs = queryForThumb();

            ImageAdapter adapter = new ImageAdapter(getActivity(), thumbs);
            rooms.setAdapter(adapter);

            rooms.setExpanded(true);

            rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void intialize(View v) {
        bookBed = (ImageView) v.findViewById(R.id.book_bed);
        callUs = (ImageView) v.findViewById(R.id.call_us);
        directions = (ImageView) v.findViewById(R.id.directions);
        emailUs = (ImageView) v.findViewById(R.id.mail_us);
        showMap = (ImageView) v.findViewById(R.id.show_map);
        contact = (ImageView) v.findViewById(R.id.contact_us);
        fb = (ImageView) v.findViewById(R.id.fb);
        insta = (ImageView) v.findViewById(R.id.insta);
        tripAdvisor = (ImageView) v.findViewById(R.id.tripAdviser);


        desc1 = (TextView) v.findViewById(R.id.desc1);
        desc2 = (TextView) v.findViewById(R.id.desc2);

        title = (TextView) v.findViewById(R.id.title);

        images = (SliderLayout) v.findViewById(R.id.image_pager);
        rooms = (ExpandableHeightGridView) v.findViewById(R.id.room_types);
        facilities = (ExpandableHeightGridView) v.findViewById(R.id.facilities);
    }

    public int getResourceId(String name, String from) {
        Resources resources = getActivity().getResources();
        final int resourceId = resources.getIdentifier(name, from,
                getActivity().getPackageName());
        return resourceId;
    }

    private List<Integer> queryForImages() {
        List<String> names = new ArrayList<>();

        Cursor cursor = db.query("PhotoLookup", new String[]{"PhotoName"},
                "Area = " + "\'" + "Details" + "\'" , null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(cursor.getColumnIndexOrThrow("PhotoName")));
            } while (cursor.moveToNext());
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        List<Integer> arr = new ArrayList<>();

        for(String s:names) {
            s = s.replace(".jpg", "");
            int t = getResourceId(s, "drawable");

            arr.add(t);
            Log.e(s, t + "");
        }

        return arr;
    }

    private List<Integer> queryForThumb() {
        List<String> names = new ArrayList<>();

        Cursor cursor = db.query("ThumbnailLookup", new String[]{"PhotoName"},
                "Area = " + "\'" + "Beds" + "\'" , null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String temp = cursor.getString(cursor.getColumnIndexOrThrow("PhotoName")).toLowerCase();
                Log.e("temp", temp);

                if(Character.isDigit(temp.charAt(0)))
                        names.add("_" + temp);
                else
                    names.add(temp);
                Log.e("name", names.get(0));
            } while (cursor.moveToNext());
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        List<Integer> arr = new ArrayList<>();

        for(String s:names) {
            s = s.replace(".png", "");
            int t = getResourceId(s, "mipmap");

            arr.add(t);
            Log.e(s, t + "");
        }

        return arr;
    }

    private void queryForDetails() {
        Cursor cursor = db.query("HostelDetails", null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            webPage = cursor.getString(cursor.getColumnIndexOrThrow("webPage"));
            description1 = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            description2 = cursor.getString(cursor.getColumnIndexOrThrow("description2"));
            longt = cursor.getString(cursor.getColumnIndexOrThrow("longitude"));
            lat = cursor.getString(cursor.getColumnIndexOrThrow("latitude"));
            instaUrl = cursor.getString(cursor.getColumnIndexOrThrow("InstagramURL"));
            fbUrl = cursor.getString(cursor.getColumnIndexOrThrow("FacebookURL"));
            tripUrl = cursor.getString(cursor.getColumnIndexOrThrow("TripAdvisorURL"));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private List<String> queryForFacilities() {
        Cursor cursor = db.query("Facilities", null, null, null, null, null, null);
        List<String> facilities = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                facilities.add(cursor.getString(cursor.getColumnIndexOrThrow("FacilityName")));
            } while (cursor.moveToNext());
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return facilities;
    }

    private void setOnClickListeners() {
        bookBed.setOnClickListener(this);
        callUs.setOnClickListener(this);
        directions.setOnClickListener(this);
        emailUs.setOnClickListener(this);
        showMap.setOnClickListener(this);
        contact.setOnClickListener(this);
        fb.setOnClickListener(this);
        insta.setOnClickListener(this);
        tripAdvisor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == bookBed) {

        } else if(v == callUs) {
            makeCall();
        } else if(v == directions) {

        } else if(v == emailUs) {
            makeEmail();
        } else if(v == showMap) {

        } else if(v == contact) {

        } else if(v == fb) {
            openWebView(fbUrl);
        } else if(v == insta) {
            openWebView(instaUrl);
        } else if(v == tripAdvisor) {
            openWebView(tripUrl);
        }
    }

    private void setFacilitiesGrid(final List<String> facilities) {

        this.facilities.setAdapter(new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, facilities){
            public View getView(int position, View convertView, ViewGroup parent) {

                // Return the GridView current item as a View
                View view = super.getView(position,convertView,parent);

                // Convert the view as a TextView widget
                TextView tv = (TextView) view;

                //tv.setTextColor(Color.DKGRAY);

                // Set the layout parameters for TextView widget
                RelativeLayout.LayoutParams lp =  new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
                );
                tv.setLayoutParams(lp);

                // Display TextView text in center position
                tv.setGravity(Gravity.CENTER);

                // Set the TextView text font family and text size
                tv.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                // Set the TextView text (GridView item text)
                tv.setText(facilities.get(position));

                // Return the TextView widget as GridView item
                return tv;
            }
        });

        this.facilities.setExpanded(true);
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    5);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            try {
                startActivity(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivity(intent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void makeEmail() {
        Uri uri = Uri.parse("mailto:" + email)
                .buildUpon()
                .build();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    private void openWebView(String url) {
        Intent i = new Intent(getActivity(), WebViewActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }
}
