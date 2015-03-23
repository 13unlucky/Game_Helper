package game.gamehelper.DominoMT;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import game.gamehelper.BitmapAdapter;
import game.gamehelper.R;

/**
 * Created by Mark Andrews on 2/23/2015.
 * Fragment for selecting the end piece to calculate runs
 */
public class EndSelectFragment extends DialogFragment {

    public interface EndListener {
        public void onClose(int var1);
    }

    private static final float TARGET_BITMAP_SIZE = 80.0f;
    int bitmapSize;
    int numColumns;
    int deckMax;
    EndListener mListener;
    GridView gridView;
    View drawView;
    Display display;
    Point size = new Point();
    BitmapAdapter bitmapAdapter;


    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null)
            return;

        //TODO adjust size calculations
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = size.x;
        getDialog().getWindow().setAttributes(params);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (EndListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement interface EndListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle b = getArguments();
        if (b != null)
            deckMax = b.getInt("maxDouble");

        //retrieve draw_layout view
        drawView = View.inflate(getActivity(), R.layout.end_select_layout, null);

        //get the size of the display
        display = getActivity().getWindowManager().getDefaultDisplay();
        display.getSize(size);

        //set bitmap size
        //TODO make size relative to screen width
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        bitmapSize = (int) (TARGET_BITMAP_SIZE * metrics.density + .5f);
        numColumns = size.x / bitmapSize;

        final int[] mList = new int[]{
                R.drawable.side_border,
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
                R.drawable.dom_twelve,
                R.drawable.dom_thirteen,
                R.drawable.dom_fourteen,
                R.drawable.dom_fifteen,
                R.drawable.dom_sixteen,
                R.drawable.dom_seventeen,
                R.drawable.dom_eighteen
        };

        //retrieve gridview from layout, set adapter
        gridView = (GridView) drawView.findViewById(R.id.gridView);
        bitmapAdapter = new BitmapAdapter(getActivity(), mList, deckMax + 1);
        bitmapAdapter.setImageSize(bitmapSize);
        gridView.setAdapter(bitmapAdapter);
        gridView.setNumColumns(numColumns);

        //return the selected tile
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mark value
                mListener.onClose(position);
                dismiss();
            }
        });

        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(drawView);
        builder.setTitle(R.string.endSelectTitle);

        return builder.create();
    }
}