package example.com.kist.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import example.com.kist.R;

/**
 * Created by pr0 on 3/8/17.
 */

public class WebViewFragment extends Fragment {
    WebView webView;
    String url = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.booking_fragment, viewGroup, false);
        webView = (WebView) v.findViewById(R.id.web);

        url = getArguments().getString("url", "");

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
