package com.example.triviatime;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class HighScorePostFragment extends DialogFragment {
    private DatabaseReference dbReference;
    private EditText nameEntry;

    public interface OnScorePosted{
        void sendBool(Boolean input);
    }
    public OnScorePosted onScorePosted;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_highscore, null);
        nameEntry = view.findViewById(R.id.enter_user_name);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int score = getArguments().getInt(getResources().getString(R.string.scoreToPost));
        builder.setTitle("Post Your Score")
                .setView(view)
                .setMessage("Leave your legacy!")
                .setPositiveButton("Yee", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        User user = new User(nameEntry.getText().toString(), score);
                        dbReference.push().setValue(user);
                        onScorePosted.sendBool(true);
                    }
                })
                .setNegativeButton("Nah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try{
            onScorePosted = (OnScorePosted) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("TAG", "onAttach: ClassCastException : " + e.getMessage());
        }
    }
}
