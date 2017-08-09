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
import com.squareup.picasso.Picasso;

import java.util.List;

import example.com.kist.Activities.ImageScreen;
import example.com.kist.Activities.MainActivity;
import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.Objects.NotificationItem;
import example.com.kist.R;

/**
 * Created by pr0 on 5/8/17.
 */

public class NotificationAdapter extends BaseAdapter {

    List<NotificationItem> list;
    Context context;

    public NotificationAdapter(Context context, List<NotificationItem> list) {
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
            v = vi.inflate(R.layout.notification_item, null);
        }

        ImageView thumb = (ImageView) v.findViewById(R.id.photo);
        TextView message = (TextView) v.findViewById(R.id.message);
        TextView time = (TextView) v.findViewById(R.id.time);

        message.setText(list.get(position).getMessage());

        if(list.get(position).getType().equals("Message")) {
            thumb.setVisibility(View.GONE);
        } else if(list.get(position).getType().equals("Image")) {
            Picasso.with(context).load(list.get(position).getImage()).into(thumb);
        }

        time.setText(list.get(position).getTime());

        thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ImageScreen.class);
                i.putExtra("image", list.get(position).getImage());
                context.startActivity(i);
            }
        });

        return v;
    }
}
