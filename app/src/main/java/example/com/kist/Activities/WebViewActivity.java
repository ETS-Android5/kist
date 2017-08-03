package example.com.kist.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import example.com.kist.R;

/**
 * Created by pr0 on 3/8/17.
 */

public class WebViewActivity extends AppCompatActivity {
    LinearLayout back;
    WebView webView;
    String url = "";

    @Override
    protected void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.webview_activity);

        back = (LinearLayout) findViewById(R.id.back);
        webView = (WebView) findViewById(R.id.webview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(getIntent() != null) {
            url = getIntent().getStringExtra("url");
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
