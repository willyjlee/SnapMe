package course.labs.SnapMe;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by william_lee on 6/20/16.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> strData;
    private ArrayList<Bitmap> picData;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context context, ArrayList<String> strings, ArrayList<Bitmap> pics) {
        this.context = context;
        this.strData = strings;
        this.picData = pics;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return strData.size();
    }

    @Override
    public Object getItem(int position) {
        return strData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View view = layoutInflater.inflate(R.layout.listview_child, null);
        TextView textView = (TextView) view.findViewById(R.id.childTextView);
        textView.setText(strData.get(position) + "");

        ImageView imageView = (ImageView) view.findViewById(R.id.childImageView);
        imageView.setImageBitmap(picData.get(position));

        return view;
    }
}
