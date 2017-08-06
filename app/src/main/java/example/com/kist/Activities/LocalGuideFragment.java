package example.com.kist.Activities;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.Constant.LocalGuideListAdapter;
import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.R;

/**
 * Created by pr0 on 5/8/17.
 */

public class LocalGuideFragment extends Fragment implements View.OnClickListener {

    TextView eats, drinks, attr;
    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";

    SQLiteDatabase db;

    List<LocalGuideListItem> eatsList = new ArrayList<>();
    List<LocalGuideListItem> drinksList = new ArrayList<>();
    List<LocalGuideListItem> attrItems = new ArrayList<>();

    ListView guideList;
    LocalGuideListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.guide_fragment, viewGroup, false);
        initialize(v);
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

            queryDetails("Food");
            queryDetails("Drink");
            queryDetails("Attraction");

            adapter = new LocalGuideListAdapter(getActivity(), eatsList);
            guideList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(guideList);
            eats.setBackgroundColor(Color.parseColor("#8b8e8e"));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initialize(View v) {
        eats = (TextView) v.findViewById(R.id.eats);
        drinks = (TextView) v.findViewById(R.id.drinks);
        attr = (TextView) v.findViewById(R.id.attractions);

        guideList = (ListView) v.findViewById(R.id.guideList);
    }

    private void setOnClickListeners() {
        eats.setOnClickListener(this);
        drinks.setOnClickListener(this);
        attr.setOnClickListener(this);
    }

    public int getResourceId(String name, String from) {
        Resources resources = getActivity().getResources();
        final int resourceId = resources.getIdentifier(name, from,
                getActivity().getPackageName());
        return resourceId;
    }

    private void queryDetails(String table) {
        Cursor cursor = db.query(table, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                LocalGuideListItem item = new LocalGuideListItem();

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

                if(table.contains("Drink") || table.contains("Attraction"))
                    item.setClosest(cursor.getString(cursor.getColumnIndexOrThrow("ClosestPublicTransport")));
                else if(table.contains("Food"))
                    item.setClosest(cursor.getString(cursor.getColumnIndexOrThrow("ClosestStop")));

                item.setResourceId(queryForImage(table, item.getName()));

                item.setType(table);

                if(table.contains("Drink"))
                    drinksList.add(item);
                else if(table.contains("Food"))
                    eatsList.add(item);
                else if(table.contains("Attraction"))
                    attrItems.add(item);
            } while (cursor.moveToNext());
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private int queryForImage(String type, String id) {
        int t = 0;

        Cursor cursor = db.query("ThumbnailLookup", new String[]{"PhotoName"},
                "Area = " + "\"" + type + "\"" + " AND " + "ID = " + "\"" + id + "\"", null, null, null, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("PhotoName"));
            name = name.toLowerCase();

            name = name.replace(".jpg", "");
            t = getResourceId(name, "drawable");
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return t;
    }

    @Override
    public void onClick(View view) {
        if(view == eats) {
            eats.setBackgroundColor(Color.parseColor("#8b8e8e"));
            drinks.setBackgroundResource(R.drawable.stroke_rect);
            attr.setBackgroundResource(R.drawable.stroke_rect);

            adapter = new LocalGuideListAdapter(getActivity(), eatsList);
            guideList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(guideList);
        } else if(view == drinks) {
            drinks.setBackgroundColor(Color.parseColor("#8b8e8e"));
            eats.setBackgroundResource(R.drawable.stroke_rect);
            attr.setBackgroundResource(R.drawable.stroke_rect);
            adapter = new LocalGuideListAdapter(getActivity(), drinksList);
            guideList.setAdapter(adapter);

            setListViewHeightBasedOnChildren(guideList);
        } else if(view == attr) {
            attr.setBackgroundColor(Color.parseColor("#8b8e8e"));
            drinks.setBackgroundResource(R.drawable.stroke_rect);
            eats.setBackgroundResource(R.drawable.stroke_rect);
            adapter = new LocalGuideListAdapter(getActivity(), attrItems);
            guideList.setAdapter(adapter);

            setListViewHeightBasedOnChildren(guideList);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;

        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
