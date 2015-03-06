package game.gamehelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Mark Andrews on 2/23/2015.
 * Fragment class for handling draw operations
 *
 * TODO make parent class to minimize duplicate code in DrawFragment and EndSelectFragment
 */
public class DrawFragment extends DialogFragment {

    public interface DrawListener {
        public void onClose(int var1, int var2);
    }

    int bitmapSize = 250;
    int deckMax = 12;
    DrawListener mListener;
    GridView gridView;
    View drawView;
    ImageView imageView;
    ImageView leftSide;
    ImageView rightSide;
    BitmapAdapter bitmapAdapter;
    int width = 0;
    int height = 0;
    int var1 = 0;
    int var2 = 0;
    int currentSide = 0;
    Display display;
    Point size = new Point();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mListener = (DrawListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
            + " must implement interface DrawListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        int windowX;
        int windowY;

        if (getDialog() == null)
            return;

        display.getSize(size);
        windowX = size.x - 50;
        windowY = ( deckMax / (size.x / bitmapSize) ) * bitmapSize + 680;
        //TODO get height of title, domino, and buttons dynamically and replace 680

        getDialog().getWindow().setLayout(windowX,windowY);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Clicker clickListener = new Clicker();

        //retrieve draw_layout view
        drawView = View.inflate(getActivity(), R.layout.draw_layout, null);

        display = getActivity().getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int[] mList = new int[] {
                R.drawable.dom_one,
                R.drawable.dom_two,
                R.drawable.dom_three,
                R.drawable.dom_four,
                R.drawable.dom_five,
                R.drawable.dom_six,
                R.drawable.dom_seven,
                R.drawable.dom_eight,
                R.drawable.dom_nine,
                R.drawable.dom_ten,
                R.drawable.dom_eleven,
                R.drawable.dom_twelve

        };

        //get imageview from top left of layout and place the domino background
        imageView = (ImageView)drawView.findViewById(R.id.imageViewBG);
        imageView.setImageResource(R.drawable.dom_bg);

        //get sides
        leftSide = (ImageView)drawView.findViewById(R.id.leftSide);
        rightSide = (ImageView)drawView.findViewById(R.id.rightSide);

        leftSide.setOnClickListener(clickListener);
        rightSide.setOnClickListener(clickListener);

        //retrieve gridview from layout, set adapter
        gridView = (GridView)drawView.findViewById(R.id.gridView);
        bitmapAdapter = new BitmapAdapter(getActivity(), mList);
        bitmapAdapter.setImageSize(bitmapSize);
        gridView.setAdapter(bitmapAdapter);
        gridView.setNumColumns(size.x / bitmapSize);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mark piece, toggle side of preview domino
                position++;

                switch(currentSide) {
                    default:
                    case 0:
                        var1 = position;
                        leftSide.setImageDrawable(getSide(position));
                        break;
                    case 1:
                        var2 = position;
                        rightSide.setImageDrawable(getSide(position));
                }

                currentSide ^= 1;
            }
        });

        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(drawView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Add domino to hand
                mListener.onClose(var1,var2);

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close window

                    }
                });

        return builder.create();
    }

    //Load image for domino side value
    private Drawable getSide(int value){


        switch(value){
            case 1:
                return getResources().getDrawable(R.drawable.dom_one);
            case 2:
                return getResources().getDrawable(R.drawable.dom_two);
            case 3:
                return getResources().getDrawable(R.drawable.dom_three);
            case 4:
                return getResources().getDrawable( R.drawable.dom_four);
            case 5:
                return getResources().getDrawable(R.drawable.dom_five);
            case 6:
                return getResources().getDrawable(R.drawable.dom_six);
            case 7:
                return getResources().getDrawable(R.drawable.dom_seven);
            case 8:
                return getResources().getDrawable(R.drawable.dom_eight);
            case 9:
                return getResources().getDrawable(R.drawable.dom_nine);
            case 10:
                return getResources().getDrawable(R.drawable.dom_ten);
            case 11:
                return getResources().getDrawable(R.drawable.dom_eleven);
            case 12:
                return getResources().getDrawable(R.drawable.dom_twelve);
            case 0:
            default:
                return null;
        }
    }
    public class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if(v == leftSide ) {
                var1 = 0;
                leftSide.setImageDrawable(null);
                currentSide = 0;
            }
            else {
                var2 = 0;
                rightSide.setImageDrawable(null);
                currentSide = 1;
            }
        }
    }
}
