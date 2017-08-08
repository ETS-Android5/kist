package example.com.kist.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

import example.com.kist.R;

/**
 * Created by pr0 on 8/8/17.
 */

public class FeedbackFragment extends Fragment {
    EditText feedback;
    ImageView submit;

    SQLiteDatabase db;
    String DATABASE_PATH = "",
            DATABASE_NAME = "hostel.db";

    String email = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.feedback_fragment, viewGroup, false);

        feedback = (EditText) v.findViewById(R.id.feedback);
        submit = (ImageView) v.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!feedback.getText().toString().isEmpty()) {
                    makeEmail();
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle onSavedInstanceState) {
        super.onActivityCreated(onSavedInstanceState);

        try {
            DATABASE_PATH = getActivity().getPackageManager().
                    getPackageInfo(getActivity().getPackageName(), 0).applicationInfo.dataDir + "/";

            db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, 0);
            queryForDetails();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void queryForDetails() {
        Cursor cursor = db.query("HostelDetails", null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private void makeEmail() {
        Uri uri = Uri.parse("mailto:" + email)
                .buildUpon()
                .build();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback.getText().toString());
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

}
