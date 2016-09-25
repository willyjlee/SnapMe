package course.labs.SnapMe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

/**
 * Created by william_lee on 6/22/16.
 */
public class FullPicActivity extends AppCompatActivity {

    private static final String PIC_KEY = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_pic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.fullPicImageView);

        Intent intent = getIntent();

        Bitmap orig = (Bitmap) (intent.getParcelableExtra(PIC_KEY));

        imageView.setImageBitmap(orig);

    }
}
