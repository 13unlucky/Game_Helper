package game.gamehelper.DominoMT;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import game.gamehelper.DominoMT.Domino;
import game.gamehelper.R;

/**
 * Created by Mark Andrews on 2/14/2015.
 * Adapter for image lists
 */
public class DominoAdapter extends ArrayAdapter<Domino> {

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
        holder.domino.setImageBitmap(buildDomino(piece, row, 50));
        return row;

    }

    //Load background and write each side on top
    public Bitmap buildDomino(Domino a, View view, double scale){

        Bitmap side1;
        Bitmap side2;
        Bitmap bg;
        Bitmap fullTile;

        bg = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_bg);
        side1 = getSide(a.getVal1(), view);
        side2 = getSide(a.getVal2(), view);

        //copy immutable bitmap generated previously to a mutable bitmap and impose the sides
        bg = bg.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bg);
        canvas.drawBitmap(side1, 0, 0, null);
        canvas.drawBitmap(side2, side2.getWidth(), 0, null);

        //scale image
        int width = (int) (bg.getWidth() * scale/100);
        int height = (int) (bg.getHeight() * scale/100);

        fullTile = Bitmap.createScaledBitmap(bg, width, height, true);

        //free space
        side1.recycle();
        side2.recycle();
        bg.recycle();

        return fullTile;
    }

    //Load image for domino side value
    private Bitmap getSide(int value, View view){

        Bitmap side;

        switch(value){
            case 1:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_one);
                break;
            case 2:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_two);
                break;
            case 3:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_three);
                break;
            case 4:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_four);
                break;
            case 5:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_five);
                break;
            case 6:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_six);
                break;
            case 7:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_seven);
                break;
            case 8:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_eight);
                break;
            case 9:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_nine);
                break;
            case 10:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_ten);
                break;
            case 11:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_eleven);
                break;
            case 12:
                side = BitmapFactory.decodeResource(view.getResources(), R.drawable.dom_twelve);
                break;
            case 0:
            default:
                side = Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_8888);
                break;
        }
        return side;
    }

    private class DominoHolder{
        ImageView domino;
    }


}
