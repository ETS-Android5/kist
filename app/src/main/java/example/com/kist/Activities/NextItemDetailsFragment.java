package example.com.kist.Activities;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import example.com.kist.Objects.NextItem;

import example.com.kist.R;

/**
 * Created by pr0 on 6/8/17.
 */

public class NextItemDetailsFragment extends Fragment {

    SliderLayout slidingImages;
    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";
    SQLiteDatabase db;

    TextView title, des, there1, there2, there3;

    NextItem item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.next_item_detials_frag, viewGroup, false);

        slidingImages = (SliderLayout) v.findViewById(R.id.image_pager);
        title = (TextView) v.findViewById(R.id.title);

        des = (TextView) v.findViewById(R.id.desc);
        there1 = (TextView) v.findViewById(R.id.there1);
        there2 = (TextView) v.findViewById(R.id.there2);
        there3 = (TextView) v.findViewById(R.id.there3);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        item = new Gson().fromJson(getArguments().getString("item", ""), NextItem.class);

        try {
            DATABASE_PATH = getActivity().getPackageManager().
                    getPackageInfo(getActivity().getPackageName(), 0).applicationInfo.dataDir + "/";

            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, 0);

            List<Integer> res = queryForImages();

            if (res != null && res.size() != 0) {
                for (int i = 0; i < res.size(); ++i) {
                    DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                    sliderView.image(res.get(i)).setScaleType(BaseSliderView.ScaleType.Fit);

                    slidingImages.addSlider(sliderView);
                }
            }

            slidingImages.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slidingImages.stopAutoCycle();

            title.setText(item.getName());

            des.setText(item.getDescription());
            there1.setText(item.getGetting1());
            there2.setText(item.getGettng2());
            there3.setText(item.getGetting3());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getResourceId(String name) {
        Resources resources = getActivity().getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                getActivity().getPackageName());
        return resourceId;
    }

    private List<Integer> queryForImages() {
        List<String> names = new ArrayList<>();

        Cursor cursor = db.query("PhotoLookup", new String[]{"PhotoName"},
                "Area = " + "\"" + "Next" + "\"" + " AND " + "ID = " + "\"" + item.getName() + "\""
                , null, null, null, null);
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
            s = s.toLowerCase();
            s = s.replace(".jpg", "");

            arr.add(getResourceId(s));
        }

        return arr;
    }
}
