package example.com.kist.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.Constant.LazyFragment;
import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.Objects.TransportItem;
import example.com.kist.R;

/**
 * Created by pr0 on 6/8/17.
 */

public class TransportItemDetailsFragment extends Fragment implements View.OnClickListener {

    SliderLayout slidingImages;
    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";
    SQLiteDatabase db;

    TextView title, des1, des2, des3, des4, des5, des6, des7, des8;

    TransportItem item;
    ImageView map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.transport_item_details_frag, viewGroup, false);

        slidingImages = (SliderLayout) v.findViewById(R.id.image_pager);
        title = (TextView) v.findViewById(R.id.title);

        des1 = (TextView) v.findViewById(R.id.desc1);
        des2 = (TextView) v.findViewById(R.id.desc2);
        des3 = (TextView) v.findViewById(R.id.desc3);
        des4 = (TextView) v.findViewById(R.id.desc4);
        des5 = (TextView) v.findViewById(R.id.desc5);
        des6 = (TextView) v.findViewById(R.id.desc6);
        des7 = (TextView) v.findViewById(R.id.desc7);
        des8 = (TextView) v.findViewById(R.id.desc8);

        map = (ImageView) v.findViewById(R.id.map);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        item = new Gson().fromJson(getArguments().getString("item", ""), TransportItem.class);
        Log.e("item val", getArguments().getString("item", ""));

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

            if (item.getMap() == null || item.getMap().isEmpty()) {
                map.setVisibility(View.GONE);
            }

            title.setText(item.getName());
            des1.setText(item.getDescription1());
            des2.setText(item.getDescription2());
            des3.setText(item.getDescription3());
            des4.setText(item.getDescription4());
            des5.setText(item.getDescription5());
            des6.setText(item.getDescription6());
            des7.setText(item.getDescription7());
            des8.setText(item.getDescription8());

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
                "Area = " + "\"" + "Transport" + "\"" + " AND " + "ID = " + "\"" + item.getName() + "\""
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

    @Override
    public void onClick(View v) {
        if(map == v) {
            Intent i = new Intent(getActivity(), ImageScreen.class);
            i.putExtra("res", getResourceId("bangkok_public_transport_map"));
            i.putExtra("head", "TRANSPORT");

            startActivity(i);
        }
    }
}
