package example.com.kist.Constant;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import example.com.kist.Activities.MainActivity;

/**
 * Created by pr0 on 3/8/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    List<Integer> mThumbIds = new ArrayList<>();
    List<String> types = new ArrayList<>();

    // Constructor
    public ImageAdapter(Context c, List<Integer> mThumbIds, List<String> types) {
        mContext = c;
        this.mThumbIds = mThumbIds;
        this.types = types;

        Log.e("thumbs", mThumbIds.size() + "");
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds.get(position));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("cliked0", "image");
                ((MainActivity) mContext).setDetailsPage(2, types.get(position));
            }
        });

        return imageView;
    }
}
