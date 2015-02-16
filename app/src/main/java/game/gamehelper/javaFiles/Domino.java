package game.gamehelper.javaFiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

import game.gamehelper.R;

/**
 * Created by Jacob on 2/11/2015.
 * A domino: defined as value 1, value 2, and sum of values.
 * TODO add more functionality.
 */
public class Domino implements Parcelable{
    private final int val1;
    private final int val2;
    private final int sum;
    private Bitmap domino;

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Constructor. Creates the Domino.
     * @param value1 Pair value 1.
     * @param value2 Pair value 2.
     */
    public Domino(int value1, int value2, Context context) {
        val1 = value1;
        val2 = value2;
        sum = getVal1() + getVal2();

        //buildDomino(context);
    }

    public int getVal1() {
        return val1;
    }

    public int getVal2() {
        return val2;
    }

    public int getSum() {
        return sum;
    }

    public void setDomino(Bitmap a){
        domino = a;
    }

    //Load background and write each side on top
    public void buildDomino(Context context){

        Bitmap side1;
        Bitmap side2;
        domino = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_bg);
        //domino = BitmapFactory.decodeFile("dom_bg.png");
        side1 = getSide(val1, context);
        side2 = getSide(val2, context);

        Canvas canvas = new Canvas(domino);
        canvas.drawBitmap(side1, 0, 0, null);
        canvas.drawBitmap(side2, 200, 0, null);


    }

    //Load image for domino side value
    private Bitmap getSide(int value, Context context){

        Bitmap side;

        switch(value){
            case 1:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_one);
                break;
            case 2:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_two);
                break;
            case 3:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_three);
                break;
            case 4:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_four);
                break;
            case 5:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_five);
                break;
            case 6:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_six);
                break;
            case 7:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_seven);
                break;
            case 8:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_eight);
                break;
            case 9:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_nine);
                break;
            case 10:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_ten);
                break;
            case 11:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_eleven);
                break;
            case 12:
                side = BitmapFactory.decodeResource(context.getResources(), R.drawable.dom_twelve);
                break;
            case 0:
            default:
                side = Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_8888);
                break;
        }
        return side;
    }

    //return domino bitmap scaled by argument as a percent
    public Bitmap getDomino(double scale){

        int width = (int) (domino.getWidth() * scale/100);
        int height = (int) (domino.getHeight() * scale/100);

        Bitmap scaledDomino = Bitmap.createScaledBitmap(domino, width, height, true);

        return scaledDomino;
    }

    public Boolean compareTo(Domino a){
        if(val1 == a.getVal1() && val2 == a.getVal2())
            return true;
        if(val1 == a.getVal2() && val2 == a.getVal1())
            return true;
        return false;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[]{val1, val2, sum});
    }
}
