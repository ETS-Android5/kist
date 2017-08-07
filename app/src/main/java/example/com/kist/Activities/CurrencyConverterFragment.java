package example.com.kist.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import example.com.kist.R;

/**
 * Created by pr0 on 7/8/17.
 */

public class CurrencyConverterFragment extends Fragment {

    TextView curr1, curr2;
    EditText currAmnt1, currAmnt2;
    String code1, code2, requestURL = "https://www.exchangerate-api.com/", key = "?k=0eaf1227513081af92575a45";

    float toConvert = 1.0f;

    int which = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.currency_convertor_fragment, viewGroup, false);
        curr1 = (TextView) v.findViewById(R.id.first);
        curr2 = (TextView) v.findViewById(R.id.sec_amount);

        currAmnt1 = (EditText) v.findViewById(R.id.first_amount);
        currAmnt2 = (EditText) v.findViewById(R.id.sec_amount);

        currAmnt1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i == EditorInfo.IME_ACTION_DONE) {
                    toConvert = Float.parseFloat(currAmnt1.getText().toString());
                    which = 1;

                    convert();
                }

                return false;
            }
        });

        currAmnt2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    toConvert = Float.parseFloat(currAmnt1.getText().toString());
                    which = 2;

                    convert();
                }

                return false;
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void convert() {
        String url = requestURL + code1 + "/" + code2 + "/" + toConvert + key;

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                URL obj = null;
                try {
                    obj = new URL(params[0]);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    int responseCode = con.getResponseCode();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();

                    Log.e("url", response.toString());

                    return response.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return  "";
            }

            @Override
            protected void onPostExecute(String result) {
                if(which == 1)
                    currAmnt2.setText(result);
                else
                    currAmnt1.setText(result);
            }

        }.execute(new String[] {url});
    }

    private void showCurrencyList() {

    }
}
