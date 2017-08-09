package example.com.kist.Activities;

import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.com.kist.Constant.ImageAdapter;
import example.com.kist.Constant.NotificationAdapter;
import example.com.kist.Objects.NotificationItem;
import example.com.kist.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pr0 on 9/8/17.
 */

public class NotificationFrament extends Fragment {
    ListView notificationList;
    List<NotificationItem> list = new ArrayList<>();
    NotificationAdapter adapter;

    ImageView send, gallery;
    EditText msg;


    String encodedImage = "";

    boolean selectedImage = false;

  //  String DATABASE_PATH = "",
//            DATABASE_NAME = "hostel.db";

//    SQLiteDatabase db;

    private final String KEY = "5a14ec5b310164f2dfe49e86b06124a";
    private final int SELECT_PICTURE = 100;

    String requestURL = "http://kistchatstorage.com/HostelBlocksDB/" +
            "getNB.php?%20userID=0&senderID=%@&key=5a14ec5b310164f2dfe49e86b06124a";
    String base = "http://kistchatstorage.com/HostelBlocksDB/sendMsg.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.notification_fragment, viewGroup, false);
        notificationList = (ListView) v.findViewById(R.id.notify_list);
        send = getActivity().findViewById(R.id.post);
        gallery = getActivity().findViewById(R.id.gallery);
        msg = (EditText) getActivity().findViewById(R.id.message);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);
        load();

        getActivity().findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogin();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImage) {
                    postMessage(1, 2, msg.getText().toString(), "Image");
                } else {
                    if(!msg.getText().toString().isEmpty())
                        postMessage(1, 2, msg.getText().toString(), "Message");
                    else
                        Toast.makeText(getActivity(), "Please select an Image or a Message to post.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void load() {
        String url = requestURL;

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
                try {
                    String _list = new JSONObject(result).optJSONArray("msg_data").toString();
                    Type listType = new TypeToken<List<NotificationItem>>(){}.getType();

                    list = new Gson().fromJson(_list, listType);
                    adapter = new NotificationAdapter(getActivity(), list);
                    notificationList.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute(new String[] {url});
    }

    private void showLogin() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText pass = (EditText) dialog.findViewById(R.id.password);

        final TextView select = (TextView) dialog.findViewById(R.id.login);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPass(pass.getText().toString())) {
                    getActivity().findViewById(R.id.home_lay).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.message_post_lay).setVisibility(View.VISIBLE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_PICTURE) {

            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                Bitmap selectedImageBitmap = null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().
                            getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

                selectedImage = true;
            }
        }
    }

    private boolean checkPass(String pass) {
        if(pass.equals("666666"))
            return true;
        else
            return false;
    }

    private void postMessage(final int userId, final int senderId,
                             final String msg, final String type) {

        gallery.setEnabled(false);
        send.setEnabled(false);

        new AsyncTask<Void, Void ,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    base += "?userId=" + userId + "&senderId=" + senderId + "&msg=" + msg +
                            "&image=" + encodedImage + "&type=" + type + "&key=" + KEY;


                    URL url = new URL(base);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.connect();

                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            in, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    in.close();
                    String result = sb.toString();

                    Log.e("result", result);

                    connection.disconnect();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void args) {
                gallery.setEnabled(true);
                send.setEnabled(true);

                NotificationFrament.this.msg.setText("");
            }

        }.execute();
    }
}
