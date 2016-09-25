package course.labs.SnapMe;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by william_lee on 6/20/16.
 */
public class HomeActivity extends AppCompatActivity {

    private Button cameraButton;

    private ListView listView;

    private ArrayList<String> text;
    private ArrayList<Bitmap> pics;

    private ListAdapter listAdapter;

    private AlarmManager alarmManager;
    private Intent notificationIntent;
    private PendingIntent notificationPendingIntent;

    private static final String TAG = "HomeActivity";
    private static final int BUTTON_DIMS = 80;
    private static final int CAMERA_REQ_CODE = 50;
    private static final String timeFileName = "timeFile";
    private static final String picsFileName = "picsFile";

    private static final String PIC_KEY = "key";

    private static long START_TIME = 10 * 1000L;
    private static long REPEAT_TIME = 24 * 60 * 60 * 1000L;

    private static File detect;
    private static final String detectName = "detect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepButton();

        toolbar.addView(cameraButton);

        listView = (ListView) findViewById(R.id.listView);

        loadData();

        updateList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bitmap bitmap = pics.get(position);
                Intent intent = new Intent(HomeActivity.this, FullPicActivity.class);
                intent.putExtra(PIC_KEY, bitmap);
                startActivity(intent);
            }
        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        notificationIntent = new Intent(HomeActivity.this, AlarmReceiver.class);
        notificationPendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        detect = new File(getApplicationContext().getFilesDir(), detectName);
        if (!detect.exists()) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + START_TIME, REPEAT_TIME, notificationPendingIntent);
            Log.i("tag", "alarm set!");
            try {
                detect.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData() {

        text = new ArrayList<String>();

        File ft = new File(getApplicationContext().getFilesDir(), timeFileName);

        if (!ft.exists()) {
            try {
                ft.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(ft));
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    text.add(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        pics = new ArrayList<Bitmap>();

        File fp = new File(getApplicationContext().getFilesDir(), picsFileName);
        if (!fp.exists()) {
            fp.mkdir();
        } else {
            File[] files = fp.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    if (lhs.lastModified() < rhs.lastModified()) {
                        return -1;
                    } else if (lhs.lastModified() > rhs.lastModified()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            Bitmap bmp;
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
                pics.add(bmp);
            }
        }

    }

    private void saveData() {

        File ft = new File(getApplicationContext().getFilesDir(), timeFileName);

        if (!ft.exists()) {
            try {
                ft.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ft)));
            for (String s : text) {
                out.println(s);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File fp = new File(getApplicationContext().getFilesDir(), picsFileName);
        if (!fp.exists()) {
            fp.mkdir();
        }

        clearFiles(fp);

        Bitmap bmp;
        String name;
        FileOutputStream fout;

        for (int i = 0; i < pics.size(); i++) {
            bmp = pics.get(i);
            name = text.get(i);
            File pic = new File(fp, name + ".png");
            Log.i("tag", pic.getAbsolutePath());

            try {
                pic.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fout = null;
            try {
                fout = new FileOutputStream(pic);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fout);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fout != null) {
                        fout.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearFiles(File dir) {

        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {
            File f;
            for (int i = 0; i < files.length; i++) {
                f = files[i];
                f.delete();
            }
        }
    }

    protected void prepButton() {
        cameraButton = new Button(this);
        cameraButton.setLayoutParams(new LinearLayout.LayoutParams(BUTTON_DIMS, BUTTON_DIMS));
        cameraButton.setBackgroundResource(R.drawable.camera_icon);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQ_CODE);
            }
        });
    }

    private void updateList() {
        listAdapter = new course.labs.SnapMe.ListAdapter(HomeActivity.this, text, pics);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQ_CODE) {
            Bitmap pic = (Bitmap) data.getExtras().get("data");
            pics.add(pic);

            Calendar c = Calendar.getInstance();
            String newText = (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY)
                    + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);

            text.add(newText);

            saveData();
            updateList();
        }
    }

}
