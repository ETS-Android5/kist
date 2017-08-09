package example.com.kist.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
    String selected = "";

    int which = 0;
    String[] codes, currencies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.currency_convertor_fragment, viewGroup, false);
        curr1 = (TextView) v.findViewById(R.id.first);
        curr2 = (TextView) v.findViewById(R.id.sec);

        currAmnt1 = (EditText) v.findViewById(R.id.first_amount);
        currAmnt2 = (EditText) v.findViewById(R.id.sec_amount);

        codes = getResources().getStringArray(R.array.currcode);
        currencies = getResources().getStringArray(R.array.currencies);

        curr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrencyList(true);
            }
        });

        curr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrencyList(false);
            }
        });

        currAmnt1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i == EditorInfo.IME_ACTION_DONE) {
                    toConvert = Float.parseFloat(currAmnt1.getText().toString());
                    which = 1;

                    code1 = getCode(curr1.getText().toString());
                    code2 = getCode(curr2.getText().toString());

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

                    code1 = getCode(curr2.getText().toString());
                    code2 = getCode(curr1.getText().toString());

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

        Log.e("url", url);

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

    private void showCurrencyList(final boolean first) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.currency_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ListView listView = (ListView) dialog.findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item, currencies);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.notifyDataSetChanged();
                view.setAlpha(1f);

                selected = currencies[i];
            }
        });

        final TextView select = (TextView) dialog.findViewById(R.id.select);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(first) {
                    curr1.setText(selected);
                } else {
                    curr2.setText(selected);
                }

                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String getCode(String cur) {
        for(int i = 0; i < currencies.length; ++i) {
            if(currencies[i].contains(cur))
                return codes[i];
        }

        return "";
    }
}

