package example.com.kist.Constant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import example.com.kist.Objects.DirectionItem;
import example.com.kist.Objects.LocalGuideListItem;
import example.com.kist.R;

/**
 * Created by pr0 on 6/8/17.
 */

public class DirectionDescAdapter extends BaseAdapter {
    List<DirectionItem> list;
    Context context;

    public DirectionDescAdapter(Context context, List<DirectionItem> list) {
        this.context = context;
        this.list = list;
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
            v = vi.inflate(R.layout.direction_item, null);
        }

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView desc1 = (TextView) v.findViewById(R.id.desc1);
        TextView desc2 = (TextView) v.findViewById(R.id.desc2);
        TextView desc3 = (TextView) v.findViewById(R.id.desc3);

        title.setText(list.get(position).getTitle());
        desc1.setText(list.get(position).getDesc1());
        desc2.setText(list.get(position).getDesc2());
        desc3.setText(list.get(position).getDesc3());

        return v;
    }
}
