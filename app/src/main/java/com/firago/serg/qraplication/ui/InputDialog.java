package com.firago.serg.qraplication.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.firago.serg.qraplication.R;

public class InputDialog extends DialogFragment {
    public interface InputDialogListener{
        void onDialogOkClick(String text);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText input = new EditText(getActivity());
        return new AlertDialog.Builder(getActivity())
                .setView(input)
                .setPositiveButton(R.string.dialog_load, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (getActivity() instanceof InputDialogListener){
                            ((InputDialogListener)getActivity())
                                    .onDialogOkClick(input.getText().toString());
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
    }
}
