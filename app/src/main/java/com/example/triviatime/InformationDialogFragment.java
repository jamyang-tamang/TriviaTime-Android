package com.example.triviatime;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class InformationDialogFragment extends DialogFragment {
    @Override
     public Dialog onCreateDialog(Bundle savedInstanceState){
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setTitle("Trivia Time")
                 .setMessage(R.string.tutorial)
                 .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                        //closes this
                     }
                 });
         return builder.create();
     }
}
