package example.com.kist.Activities;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.Constant.DirectionDescAdapter;
import example.com.kist.Constant.LocalGuideListAdapter;
import example.com.kist.Constant.TravelTipAdapter;
import example.com.kist.Objects.DirectionItem;
import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.Objects.TravelTIpItem;
import example.com.kist.R;

/**
 * Created by pr0 on 6/8/17.
 */

public class TravelTipFragment extends Fragment {
    ListView tipsList;
    List<TravelTIpItem> items = new ArrayList<>();

    TravelTipAdapter adapter;
    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";

    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.travel_tips_fragment, viewGroup, false);
        tipsList = (ListView) v.findViewById(R.id.tipsList);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);
        try {
            DATABASE_PATH = getActivity().getPackageManager().
                    getPackageInfo(getActivity().getPackageName(), 0).applicationInfo.dataDir + "/";

            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, 0);

            queryDetails("TravelTips");

            adapter = new TravelTipAdapter(getActivity(), items);
            tipsList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(tipsList);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void queryDetails(String table) {
        Cursor cursor = db.query(table, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                TravelTIpItem item = new TravelTIpItem();

                item.setName(cursor.getString(cursor.getColumnIndexOrThrow("tipName")));
                item.setDesc(cursor.getString(cursor.getColumnIndexOrThrow("description")));

                items.add(item);
            } while (cursor.moveToNext());
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
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
