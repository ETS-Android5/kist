package example.com.kist.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import example.com.kist.R;

/**
 * Created by pr0 on 4/8/17.
 */

public class BookingFragment extends Fragment {
    WebView webView;
    String url = " http://www.axisrooms.com/beV2/displaySearchResultV3.html?\n" +
            "room_number=0&searcherId=30601908&applicableDealId=0&searchNumber=1&allHotels=true";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.booking_fragment, viewGroup, false);
        webView = (WebView) v.findViewById(R.id.web);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
