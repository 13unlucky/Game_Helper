package game.gamehelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Mark Andrews on 2/23/2015.
 * Fragment for handling confirmation dialog on action bar new game button
 */
public class NewGameAlertFragment extends DialogFragment {

    public interface NewGameAlertListener {
        public void onDialogPositiveClick();
    }

    NewGameAlertListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    //handle button click

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.newGameButton)
                .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (NewGameAlertListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + " must implement NewGameAlertListener");
        }
    }
}
