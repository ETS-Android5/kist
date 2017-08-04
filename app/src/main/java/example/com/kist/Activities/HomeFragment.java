package example.com.kist.Activities;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.R;

/**
 * Created by pr0 on 2/8/17.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";

    SliderLayout slidingImages;
    CardView aboutHostel, guide, getAround, nexxt, noticeBoard;

    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.main_fragment, viewGroup, false);
        slidingImages = (SliderLayout) v.findViewById(R.id.image_pager);

        aboutHostel = (CardView) v.findViewById(R.id.about_hostel);
        guide = (CardView) v.findViewById(R.id.guide);
        getAround = (CardView) v.findViewById(R.id.get_around);
        nexxt = (CardView) v.findViewById(R.id.next);
        noticeBoard = (CardView) v.findViewById(R.id.notice_board);

        setOnclickListeners();

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

            if(res != null && res.size() != 0) {
                for(int i =0; i < res.size(); ++i) {
                    DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                    sliderView.image(res.get(i)).setScaleType(BaseSliderView.ScaleType.Fit);

                    slidingImages.addSlider(sliderView);
                }
            }

            slidingImages.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slidingImages.stopAutoCycle();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getResourceId(String name) {
        Resources resources = getActivity().getResources();
        final int resourceId = resources.getIdentifier(name, "mipmap",
                getActivity().getPackageName());
        return resourceId;
    }

    private List<Integer> queryForImages() {
        List<String> names = new ArrayList<>();

        Cursor cursor = db.query("PhotoLookup", new String[]{"PhotoName"},
                "Area = " + "\'" + "Home" + "\'" , null, null, null, null);
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
            s.replace(".png", "");
            arr.add(getResourceId(s));
        }

        return arr;
    }

    private void setOnclickListeners() {
        aboutHostel.setOnClickListener(this);
        guide.setOnClickListener(this);
        getAround.setOnClickListener(this);
        nexxt.setOnClickListener(this);
        noticeBoard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == aboutHostel) {
            ((MainActivity) getActivity()).setPage(1);
        } else if(v == guide) {

        } else if(v == getAround) {

        } else if(v == nexxt) {

        } else if(v == noticeBoard) {

        }
    }
}
