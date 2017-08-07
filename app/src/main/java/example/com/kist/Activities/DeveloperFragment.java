package example.com.kist.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.com.kist.R;

/**
 * Created by pr0 on 8/8/17.
 */

public class DeveloperFragment extends Fragment {
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.developer_fragment, viewGroup, false);
        textView = (TextView) v.findViewById(R.id.desc4);

        SpannableString ss = new SpannableString(getResources().getString(R.string.dev_desc4));
        int i = getResources().getString(R.string.dev_desc4).indexOf("Info@keepitsimpletravel.com");

        int j = getResources().getString(R.string.dev_desc4).length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                makeEmail("Info@keepitsimpletravel.com");
            }
        };

        ss.setSpan(new UnderlineSpan(),i,j,0);

        //For Bold
        ss.setSpan(new ForegroundColorSpan(Color.BLUE),i,j,0);

        ss.setSpan(clickableSpan, i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

    }

    private void makeEmail(String email) {
        Uri uri = Uri.parse("mailto:" + email)
                .buildUpon()
                .build();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }
}
