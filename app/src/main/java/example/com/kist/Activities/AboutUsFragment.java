package example.com.kist.Activities;

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
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.Objects.TransportItem;
import example.com.kist.R;

/**
 * Created by pr0 on 5/8/17.
 */

public class AboutUsFragment extends Fragment {

    SliderLayout slidingImages;
    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";
    SQLiteDatabase db;

    FragmentTransaction ft;
    String address = "",  phone = "", email = "", web = "", name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.about_us_fragment, viewGroup, false);

        slidingImages = (SliderLayout) v.findViewById(R.id.image_pager);

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

            if (res != null && res.size() != 0) {
                for (int i = 0; i < res.size(); ++i) {
                    DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                    sliderView.image(res.get(i)).setScaleType(BaseSliderView.ScaleType.Fit);

                    slidingImages.addSlider(sliderView);
                }
            }

            slidingImages.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slidingImages.stopAutoCycle();

            queryDetails("HostelDetails");
            setFrag();
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
                "Area = " + "\"Details\""
                , null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(cursor.getColumnIndexOrThrow("PhotoName")));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        List<Integer> arr = new ArrayList<>();

        for (String s : names) {
            s = s.toLowerCase();
            s = s.replace(".jpg", "");
            arr.add(getResourceId(s));
        }


        return arr;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.about_us, fragment);
        transaction.commit();
    }

    public void setFrag() {

        ContactFragment fragment = new ContactFragment();
        Bundle b = new Bundle();

        b.putString("title", name);
        if(!address.isEmpty())
            b.putString("address", address);

        if(!phone.isEmpty())
            b.putString("phone", phone);
        if(!web.isEmpty())
            b.putString("website", web);

        if(!email.isEmpty())
            b.putString("email", email);
        fragment.setArguments(b);
        replaceFragment(fragment);

    }

    private void queryDetails(String table) {
        Cursor cursor = db.query(table, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            web = cursor.getString(cursor.getColumnIndexOrThrow("webPage"));
            phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
