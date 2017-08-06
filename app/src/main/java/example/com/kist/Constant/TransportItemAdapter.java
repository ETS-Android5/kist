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
import example.com.kist.Objects.TransportItem;
import example.com.kist.R;

/**
 * Created by pr0 on 5/8/17.
 */

public class TransportItemAdapter extends BaseAdapter {

    List<TransportItem> list;
    Context context;

    public TransportItemAdapter(Context context, List<TransportItem> list) {
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

        thumb.setImageResource(list.get(position).getResourceId());
        name.setText(list.get(position).getName());
        blurb.setText(list.get(position).getBlurb());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = new Gson().toJson(list.get(position));
                ((MainActivity) context).setTransportItemFrag(8, s);
            }
        });

        return v;
    }
}
