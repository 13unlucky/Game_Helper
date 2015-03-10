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
public class ConfirmationFragment extends DialogFragment {

    public interface ConfirmationListener {
        public void onDialogPositiveClick(String tag);
    }

    String[] dialogText = new String[4];
    ConfirmationListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    //handle button click
        String tag = this.getTag();
        Bundle b = getArguments();
        String argument= "";

        //create additional text for delete dialogs
        if(b != null)
            argument = " " + b.getString("field") + "?";

        //get text for dialog type
        if(tag.compareTo(getString(R.string.newGame)) == 0){
            dialogText[0] = getString(R.string.yes);
            dialogText[1] = getString(R.string.cancel);
            dialogText[2] = getString(R.string.newGameText);
            dialogText[3] = getString(R.string.newGame);
        }
        else if(tag.compareTo(getString(R.string.endSet)) == 0){
            dialogText[0] = getString(R.string.yes);
            dialogText[1] = getString(R.string.cancel);
            dialogText[2] = getString(R.string.endSetText);
            dialogText[3] = getString(R.string.endSet);
        }
        else if(tag.compareTo(getString(R.string.deleteRow)) == 0){
            dialogText[0] = getString(R.string.remove);
            dialogText[1] = getString(R.string.cancel);
            dialogText[2] = getString(R.string.deleteRowText);
            dialogText[3] = getString(R.string.deleteRow);
        }
        else if(tag.compareTo(getString(R.string.deleteColumn)) == 0){
            dialogText[0] = getString(R.string.remove);
            dialogText[1] = getString(R.string.cancel);
            dialogText[2] = getString(R.string.deleteColumnText);
            dialogText[3] = getString(R.string.deleteColumn);
        }
        else if(tag.compareTo(getString(R.string.addColumn)) == 0){
            dialogText[0] = getString(R.string.add);
            dialogText[1] = getString(R.string.cancel);
            dialogText[2] = getString(R.string.addColumnText);
            dialogText[3] = getString(R.string.addColumn);
            argument = "";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogText[2] + argument)
                .setPositiveButton(dialogText[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //return dialog name on positive click
                        mListener.onDialogPositiveClick(dialogText[3]);
                    }
                })
                .setNegativeButton(dialogText[1], new DialogInterface.OnClickListener() {
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
            mListener = (ConfirmationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + " must implement ConfirmationListener");
        }
    }
}
