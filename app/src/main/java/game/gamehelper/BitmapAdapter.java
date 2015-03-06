package game.gamehelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/* Class for handling bitmap images in an Array
 */

public class BitmapAdapter extends BaseAdapter {
    private Context context;
    private int[] imageIds;
    private int imageSize = 200;

    public BitmapAdapter(Context c, int[] data) {
        context = c;
        imageIds = data;
    }

    public int getCount() {
        return imageIds.length;
    }

    public Object getItem(int position) {
        return imageIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setImageSize(int size){
        imageSize = size;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ImageView iview;
        if (view == null) {
            iview = new ImageView(context);
            iview.setLayoutParams(new GridView.LayoutParams(imageSize,imageSize));
            iview.setPadding(0, 0, 0, 0);
        } else {
            iview = (ImageView) view;
        }
        iview.setImageResource(imageIds[position]);
        return iview;
    }
}