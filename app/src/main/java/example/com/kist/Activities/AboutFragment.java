package example.com.kist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import example.com.kist.R;

/**
 * Created by pr0 on 5/8/17.
 */

public class AboutFragment extends Fragment implements View.OnClickListener {
    String closestStop, openingHrs, gettingThere, longt,
            lat, fbUrl, instaUrl, taUrl, title, desc1, desc2, desc3, type;

    TextView titleV, desc1V, desc2V, desc3V, closestH, closestC,
            getThereH, getThereC, openHrsV;
    ImageView showMap, fb, insta, ta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.about_frag, viewGroup, false);

        initialize(v);
        setOnclickListeners();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        if(getArguments() != null) {
            title = getArguments().getString("title", "");
            type = getArguments().getString("type", "");
            desc1 = getArguments().getString("desc1", "");
            desc2 = getArguments().getString("desc2", "");
            desc3 = getArguments().getString("desc3", "");
            closestStop = getArguments().getString("closestStop", "");
            gettingThere = getArguments().getString("gettingThere", "");
            longt = getArguments().getString("long", "");
            lat = getArguments().getString("lat", "");
            fbUrl = getArguments().getString("fb", "");
            instaUrl = getArguments().getString("insta", "");
            taUrl = getArguments().getString("ta", "");
            openingHrs = getArguments().getString("opening", "");
        }

        titleV.setText(title);
        desc1V.setText(desc1);
        desc2V.setText(desc2);
        desc3V.setText(desc3);

        if(closestStop.isEmpty()) {
            closestH.setVisibility(View.GONE);
            closestC.setVisibility(View.GONE);
        } else {
            closestC.setText(closestStop);
        }

        if(openingHrs.isEmpty()) {
            openHrsV.setVisibility(View.GONE);
        } else
            openHrsV.setText(openingHrs);

        if(gettingThere.isEmpty()) {
            getThereH.setVisibility(View.GONE);
            getThereC.setVisibility(View.GONE);
        } else {
            getThereC.setText(gettingThere);
        }

        if(lat.isEmpty() || longt.isEmpty()) {
            showMap.setVisibility(View.GONE);
        } else {
            showMap.setVisibility(View.VISIBLE);
        }

        if(fbUrl.isEmpty()) {
            fb.setAlpha(0.5f);
            fb.setEnabled(false);
        }
        if(taUrl.isEmpty()) {
            ta.setAlpha(0.5f);
            ta.setEnabled(false);
        }

        if (instaUrl.isEmpty()) {
            insta.setAlpha(0.5f);
            insta.setEnabled(false);
        }
    }

    private void initialize(View v) {
        titleV = (TextView) v.findViewById(R.id.title);
        desc1V = (TextView) v.findViewById(R.id.desc1);
        desc2V = (TextView) v.findViewById(R.id.desc2);
        desc3V = (TextView) v.findViewById(R.id.desc3);
        closestH = (TextView) v.findViewById(R.id.closestHead);
        closestC = (TextView) v.findViewById(R.id.closestContent);
        getThereH = (TextView) v.findViewById(R.id.getHead);
        getThereC = (TextView) v.findViewById(R.id.getContent);
        openHrsV = (TextView) v.findViewById(R.id.timing);

        showMap = (ImageView) v.findViewById(R.id.show_map);
        fb = (ImageView) v.findViewById(R.id.fb);
        insta = (ImageView) v.findViewById(R.id.insta);
        ta = (ImageView) v.findViewById(R.id.tripAdviser);
    }

    private void setOnclickListeners() {
        showMap.setOnClickListener(this);
        fb.setOnClickListener(this);
        insta.setOnClickListener(this);
        ta.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == showMap) {
            if(((MainActivity) getActivity()).checkMap())
                ((MainActivity) getActivity()).setMapdetails(true, false, title, type,
                       Double.parseDouble(longt), Double.parseDouble(lat));
        } else if(view == fb) {
            openWebView(fbUrl);
        } else if(view == insta) {
            openWebView(instaUrl);
        } else if(view == ta) {
            openWebView(taUrl);
        }
    }

    private void openWebView(String url) {
        ((MainActivity) getActivity()).setUrl(url);
    }

}
