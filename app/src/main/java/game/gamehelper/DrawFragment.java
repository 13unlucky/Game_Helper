package game.gamehelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Mark Andrews on 2/23/2015.
 * Fragment class for handling draw operations
 */
public class DrawFragment extends DialogFragment {

    public interface DrawListener {
        public void onClose(Bundle savedInstanceState);
    }

    DrawListener mListener;
    GridView gridView;
    View drawView;
    ImageView imageView;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        //retrieve draw_layout view
        drawView = View.inflate(getActivity(), R.layout.draw_layout, null);

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

        //retrieve gridview from layout, set adapter
        gridView = (GridView)drawView.findViewById(R.id.gridView);
        gridView.setAdapter(new BitmapAdapter(getActivity(), mList));
        gridView.setNumColumns(3);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mark piece, toggle side of preview domino

            }
        });

        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(drawView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Add domino to hand

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
}
