package example.com.kist.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import example.com.kist.R;

import static android.R.attr.angle;
import static android.R.attr.theme;

/**
 * Created by pr0 on 9/8/17.
 */

public class ImageScreen extends AppCompatActivity {
    PhotoView view;
    String image = "few", head;
    int resource_id = 0;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_lay);

        view = (PhotoView) findViewById(R.id.photo);
        image = getIntent().getStringExtra("image");

        heading = (TextView) findViewById(R.id.heading);

        head = getIntent().getStringExtra("head");
        resource_id = getIntent().getIntExtra("res", 0);

        Log.e("res id", resource_id + "");

        heading.setText(head);

        if (resource_id != 0)
            view.setImageBitmap(ImageUtils.getInstant().getCompressedBitmap(resource_id, this));
        else
            Picasso.with(this).load(image).into(view);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
