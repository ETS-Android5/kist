package example.com.kist.Activities;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.R;

/**
 * Created by pr0 on 4/8/17.
 */

public class RoomFragment extends Fragment {

    SliderLayout images;
    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";
    String type = "";


    TextView title, price, desc;

    SQLiteDatabase db;
    ImageView book;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.room_fragment, viewGroup, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);
        initialize(getView());
        type = getArguments().getString("type", "4 Bed Dorm");

        try {
            DATABASE_PATH = getActivity().getPackageManager().
                    getPackageInfo(getActivity().getPackageName(), 0).applicationInfo.dataDir + "/";

            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, 0);

            List<Integer> res = queryForImages();
            Log.e("res images", res.size() + "");

            if (res != null && res.size() != 0) {
                for (int i = 0; i < res.size(); ++i) {
                    DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                    sliderView.image(res.get(i)).setScaleType(BaseSliderView.ScaleType.Fit);

                    images.addSlider(sliderView);
                }
            }

            images.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            images.stopAutoCycle();

            queryForDetails();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initialize(View v) {
        title = (TextView) v.findViewById(R.id.title);
        price = (TextView) v.findViewById(R.id.price);
        desc = (TextView) v.findViewById(R.id.desc);

        book = (ImageView) v.findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).setPage(3);
            }
        });

        images = (SliderLayout) v.findViewById(R.id.image_pager);
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
                "Area = " + "\'" + "Beds" + "\'" + " AND " +  "ID = " + "\'" + type + "\'", null, null, null, null);
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
            int t = getResourceId(s, "mipmap");

            arr.add(t);
            Log.e(s, t + "");
        }

        return arr;
    }

    private void queryForDetails() {
        Cursor cursor = db.query("Rooms", null, "roomType = " + "\'" + type + "\'", null, null, null, null);

        if(cursor.moveToFirst()) {
            if(desc != null) {
                desc.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                price.setText(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                title.setText(type);
            }
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
