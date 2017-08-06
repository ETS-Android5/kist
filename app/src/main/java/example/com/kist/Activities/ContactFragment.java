package example.com.kist.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.com.kist.R;

/**
 * Created by pr0 on 5/8/17.
 */

public class ContactFragment extends Fragment {
    String heading, address, phone, website, email;
    TextView titleV, addV, phoneV, webV, emailV;
    LinearLayout addL, phoneL, webL, emailL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.contact_fragment, viewGroup, false);

        initialize(v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        if(getArguments() != null) {
            heading = getArguments().getString("title", "");
            address = getArguments().getString("address", "");
            phone = getArguments().getString("phone", "");
            website = getArguments().getString("website", "");
            email = getArguments().getString("email", "");
        }

        titleV.setText(heading);

        if(address.isEmpty()) {
            addL.setVisibility(View.GONE);
        } else {
            addV.setText(address);
        }

        if(phone.isEmpty()) {
            phoneL.setVisibility(View.GONE);
        } else {
            phoneV.setText(phone);
        }

        if(email.isEmpty()) {
            emailL.setVisibility(View.GONE);
        } else {
            emailV.setText(email);
        }

        if(website.isEmpty()) {
            webL.setVisibility(View.GONE);
        } else {
            webV.setText(website);
        }
    }

    private void initialize(View v) {
        titleV = (TextView) v.findViewById(R.id.title);
        addV = (TextView) v.findViewById(R.id.address);
        phoneV = (TextView) v.findViewById(R.id.phone);
        webV = (TextView) v.findViewById(R.id.url);
        emailV = (TextView) v.findViewById(R.id.email);

        addL = (LinearLayout) v.findViewById(R.id.addLay);
        phoneL = (LinearLayout) v.findViewById(R.id.phoneLay);
        webL = (LinearLayout) v.findViewById(R.id.webLay);
        emailL = (LinearLayout) v.findViewById(R.id.emailLay);
    }
}
