package example.com.kist.Constant;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import example.com.kist.Activities.MainActivity;
import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.R;

/**
 * Created by pr0 on 5/8/17.
 */

public class LocalGuideListAdapter extends BaseAdapter {

    List<LocalGuideListItem> list;
    Context context;

    public LocalGuideListAdapter(Context context, List<LocalGuideListItem> list) {
        this.context = context;
        this.list = list;

        Log.e("lsit size", list.size() + "");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.local_guide_item, null);
        }

        ImageView thumb = (ImageView) v.findViewById(R.id.thumb);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView blurb = (TextView) v.findViewById(R.id.blurb);

        Log.e("postion : res", position + ":" + list.get(position).getResourceId() + "");

        thumb.setImageResource(list.get(position).getResourceId());
        name.setText(list.get(position).getName());
        blurb.setText(list.get(position).getBlurb());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position).getType().contains("Attraction")) {
                    String s = new Gson().toJson(list.get(position));
                    ((MainActivity) context).setItemFrag(6, s, "Attraction");
                } else if(list.get(position).getType().contains("Food")) {
                    String s = new Gson().toJson(list.get(position));
                    ((MainActivity) context).setItemFrag(6, s, "Food");
                } else if(list.get(position).getType().contains("Drink")) {
                    String s = new Gson().toJson(list.get(position));
                    ((MainActivity) context).setItemFrag(6, s, "Drink");
                }
            }
        });

        return v;
    }
}
