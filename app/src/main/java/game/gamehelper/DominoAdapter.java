package game.gamehelper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import game.gamehelper.javaFiles.Domino;

/**
 * Created by Mark Andrews on 2/14/2015.
 * Adapter for image lists
 */
public class DominoAdapter extends ArrayAdapter {

    private Context context;
    private Domino[] data;
    int layoutResourceId;

    public DominoAdapter(Context context, int layoutResourceId, Domino[] data){

        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    //Updates view for list
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DominoHolder holder = null;
        Domino piece;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DominoHolder();
            holder.domino = (ImageView) row.findViewById(R.id.domList);
            row.setTag(holder);
        }
        else
        {
            holder = (DominoHolder) row.getTag();
        }

        if(data == null)
            return row;


        piece = data[position];
        holder.domino.setImageBitmap(piece.getDomino(50));
        return row;

    }

    private class DominoHolder{
        ImageView domino;
    }


}
