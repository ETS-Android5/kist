package example.com.kist.Activities;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.Constant.LocalGuideListAdapter;
import example.com.kist.Constant.NextAdapter;
import example.com.kist.Constant.TransportItemAdapter;
import example.com.kist.Objects.DirectionItem;
import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.Objects.NextItem;
import example.com.kist.Objects.TransportItem;
import example.com.kist.R;

/**
 * Created by pr0 on 6/8/17.
 */

public class NextFragment extends Fragment {

    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";

    SQLiteDatabase db;

    ListView nextList;
    CardView guideOnly;

    List<NextItem> list = new ArrayList<>();
    NextAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.guide_fragment, viewGroup, false);
        initialize(v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);
        try {
            DATABASE_PATH = getActivity().getPackageManager().
                    getPackageInfo(getActivity().getPackageName(), 0).applicationInfo.dataDir + "/";

            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, 0);

            queryDetails("Next");

            adapter = new NextAdapter(getActivity(), list);
            nextList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(nextList);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initialize(View v) {

        nextList = (ListView) v.findViewById(R.id.guideList);
        guideOnly = (CardView) v.findViewById(R.id.guide_only);

        guideOnly.setVisibility(View.GONE);
    }

    private void queryDetails(String table) {
        Cursor cursor = db.query(table, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                NextItem item = new NextItem();

                item.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                item.setBlurb(cursor.getString(cursor.getColumnIndexOrThrow("blurb")));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                item.setGetting1(cursor.getString(cursor.getColumnIndexOrThrow("gettingThere")));
                item.setGettng2(cursor.getString(cursor.getColumnIndexOrThrow("gettingThere2")));
                item.setGetting3(cursor.getString(cursor.getColumnIndexOrThrow("gettingThere3")));

                item.setResourceId(queryForImage(table, item.getName()));

                list.add(item);
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

    public int getResourceId(String name, String from) {
        Resources resources = getActivity().getResources();
        final int resourceId = resources.getIdentifier(name, from,
                getActivity().getPackageName());
        return resourceId;
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
