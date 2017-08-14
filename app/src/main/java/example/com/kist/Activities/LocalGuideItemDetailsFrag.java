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
import example.com.kist.R;

/**
 * Created by pr0 on 5/8/17.
 */

public class LocalGuideItemDetailsFrag extends Fragment implements View.OnClickListener {

    SliderLayout slidingImages;
    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";
    SQLiteDatabase db;

    TextView about, contact;
    FrameLayout layout;
    FragmentTransaction ft;

    LocalGuideListItem item;
    boolean first = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.local_guide_item_detail_frag, viewGroup, false);

        about = (TextView) v.findViewById(R.id.about);
        contact = (TextView) v.findViewById(R.id.contact);
        layout = (FrameLayout) v.findViewById(R.id.layout);
        slidingImages = (SliderLayout) v.findViewById(R.id.image_pager);


        about.setOnClickListener(this);
        contact.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        item = new Gson().fromJson(getArguments().getString("item", ""), LocalGuideListItem.class);

        if(item != null) {
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

                setAboutFrag();
                about.setBackgroundColor(Color.parseColor("#8b8e8e"));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
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

        if(item != null) {
            Log.e("area", item.getType());
            Log.e("id", item.getName());

            Cursor cursor = db.query("PhotoLookup", new String[]{"PhotoName"},
                    "Area = " + "\"" + item.getType() + "\"" + " AND " + "ID = " + "\"" + item.getName() + "\""
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
        } else
            return new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if(v == about) {
            about.setBackgroundColor(Color.parseColor("#8b8e8e"));
            contact.setBackgroundResource(R.drawable.stroke_rect);

            setAboutFrag();
        } else if(v == contact) {
            contact.setBackgroundColor(Color.parseColor("#8b8e8e"));
            about.setBackgroundResource(R.drawable.stroke_rect);

            ContactFragment fragment = new ContactFragment();
            Bundle b = new Bundle();
            b.putString("title", item.getName());
            if(item.getAddress() != null)
                b.putString("address", item.getAddress());

            if(item.getPhone() != null)
                b.putString("phone", item.getPhone());
            if(item.getWebsite() != null)
                b.putString("website", item.getWebsite());

            fragment.setArguments(b);
            replaceFragment(fragment);
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if(first) {
            transaction.add(R.id.layout, fragment);
            first = false;
        }
        else
            transaction.replace(R.id.layout, fragment);
        transaction.commit();
    }

    public void setAboutFrag() {
        AboutFragment fragment = new AboutFragment();
        Bundle b = new Bundle();
        b.putString("title", item.getName());
        b.putString("type", item.getType());
        if(item.getDes1() != null)
            b.putString("desc1", item.getDes1());

        if(item.getDes2() != null)
            b.putString("desc2", item.getDes2());
        if(item.getDes3() != null)
            b.putString("desc3", item.getDes3());
        if(item.getClosest() != null)
            b.putString("closestStop", item.getClosest());
        if(item.getGettingThere() != null)
            b.putString("gettingThere", item.getGettingThere());
        if(item.getLongitude() != null)
            b.putString("long", item.getLongitude());
        if(item.getLatitude() != null)
            b.putString("lat", item.getLatitude());
        if(item.getFbLink() != null)
            b.putString("fb", item.getFbLink());
        if(item.getInstaLink() != null)
            b.putString("insta", item.getInstaLink());
        if(item.getDes3() != null)
            b.putString("ta", item.getTaurl());
        if(item.getDes3() != null)
            b.putString("opening", item.getOpeningHrs());


        fragment.setArguments(b);
        replaceFragment(fragment);
    }
}
