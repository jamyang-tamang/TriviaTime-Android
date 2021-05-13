package com.example.triviatime;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class SplashScreenFragment extends Fragment {
    private int difficulty;
    private int numQuestions;
    private boolean timedMode;
    private ImageButton audioButton;
    public SplashScreenFragment (){
        super(R.layout.fragment_splash_screen);
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {

        audioButton = v.findViewById(R.id.audioButton);
        audioButton.setOnClickListener(this::onAudioButtonClicked);

      //sets up shared preferences and gets data or sets the default
        SharedPreferences sharedPref;
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        numQuestions = sharedPref.getInt(getResources().getString(R.string.numQuestions), 3);
        difficulty = sharedPref.getInt(getResources().getString(R.string.difficulty), 0);
        timedMode = sharedPref.getBoolean(getString(R.string.timedMode), false);
        if(sharedPref.getBoolean(getResources().getString(R.string.sound),true))
            audioButton.setBackgroundResource(android.R.drawable.ic_lock_silent_mode_off);
        else
            audioButton.setBackgroundResource(android.R.drawable.ic_lock_silent_mode);

//      links xml to code
        Button difficultyMenu = v.findViewById(R.id.change_difficulty_btn);
        Button numQuestionsMenu = v.findViewById(R.id.change_num_questions_btn);
        Button startButton = v.findViewById(R.id.start_btn);
        ImageButton infoButton = v.findViewById(R.id.infoButton);
        SwitchCompat modeSwitch = v.findViewById(R.id.modeSwitch);
        modeSwitch.setChecked(timedMode);

        //enables or disables menu buttons
        if(timedMode){
            numQuestionsMenu.setBackgroundColor(getResources().getColor(R.color.silver));
            difficultyMenu.setBackgroundColor(getResources().getColor(R.color.silver));
            numQuestionsMenu.setEnabled(false);
            difficultyMenu.setEnabled(false);
        }
        else{
            numQuestionsMenu.setBackgroundColor(getResources().getColor(R.color.green));
            difficultyMenu.setBackgroundColor(getResources().getColor(R.color.red));
            numQuestionsMenu.setEnabled(true);
            difficultyMenu.setEnabled(true);
        }

        startButton.setOnClickListener(this::onStartButtonClick);
        infoButton.setOnClickListener(this::onInfoButtonClicked);

        // Displays menu, menu data gotten from res/menu/difficulty_meu
        difficultyMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this.getActivity(), v.findViewById(difficultyMenu.getId()));
            popupMenu.getMenuInflater().inflate(R.menu.difficulty_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch(item.getItemId()){
                    case R.id.random_diff:
                        difficulty = 0;
                        displayToast(getResources().getString(R.string.toastDiffRandom));
                        return true;
                    case R.id.easy_diff:
                        difficulty = 1;
                        displayToast(getResources().getString(R.string.toastDiffEz));
                        return true;
                    case R.id.medium_diff:
                        difficulty = 2;
                        displayToast(getResources().getString(R.string.toastDiffMed));
                        return true;
                    case R.id.hard_diff:
                        difficulty = 3;
                        displayToast(getResources().getString(R.string.toastDiffHard));
                        return true;
                }
                return false;
            });
            popupMenu.show();
        });

        // Displays menu, menu data gotten from res/menu/num_questions_menu
        numQuestionsMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this.getActivity(), v.findViewById(numQuestionsMenu.getId()));
            popupMenu.getMenuInflater().inflate(R.menu.num_questions_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch(item.getItemId()){
                    case R.id.three_questions:
                        displayToast(getResources().getString(R.string.toastNumQuestions3));
                        numQuestions = 3;
                        return true;
                    case R.id.five_questions:
                        numQuestions = 5;
                        displayToast(getResources().getString(R.string.toastNumQuestions5));
                        return true;
                    case R.id.ten_questions:
                        numQuestions = 10;
                        displayToast(getResources().getString(R.string.toastNumQuestions10));
                        return true;
                    case R.id.fifteen_questions:
                        numQuestions = 15;
                        displayToast(getResources().getString(R.string.toastNumQuestions15));
                        return true;
                    case R.id.twenty_questions:
                        numQuestions = 20;
                        displayToast(getResources().getString(R.string.toastNumQuestions20));
                        return true;
                }
                return false;
            });
            popupMenu.show();

        });

        //based on whether timed mode switch is on it sets Timed Mode shared Pref on or off.
        modeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                editor.putBoolean(getString(R.string.timedMode), true);
                //should be 0, 1 for testing purposes
//                difficulty = 0;
                difficulty = 1;
                numQuestionsMenu.setBackgroundColor(getResources().getColor(R.color.silver));
                difficultyMenu.setBackgroundColor(getResources().getColor(R.color.silver));
                numQuestionsMenu.setEnabled(false);
                difficultyMenu.setEnabled(false);
                editor.apply();
                timedMode = true;
                displayToast(getResources().getString(R.string.timedModeOn));
            }
            else{
                numQuestionsMenu.setBackgroundColor(getResources().getColor(R.color.green));
                difficultyMenu.setBackgroundColor(getResources().getColor(R.color.red));
                numQuestionsMenu.setEnabled(true);
                difficultyMenu.setEnabled(true);
                editor.putBoolean(getResources().getString(R.string.timedMode), false);
                editor.apply();
                timedMode = false;
                displayToast(getResources().getString(R.string.timedModeOff));
            }
        });
    }

    public void displayToast(String message) {
        Toast.makeText(this.getContext(), message,
                Toast.LENGTH_SHORT).show();
    }

//    Inflates info dialog
    public void onInfoButtonClicked(View view){
        InformationDialogFragment informationDialogFragment = new InformationDialogFragment();
        informationDialogFragment.show(this.requireActivity().getSupportFragmentManager(), "info dialog");
    }

    public void onAudioButtonClicked(View view){
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(sharedPref.getBoolean(getResources().getString(R.string.sound),true)) {
            editor.putBoolean(getResources().getString(R.string.sound), false);
            audioButton.setBackgroundResource(android.R.drawable.ic_lock_silent_mode);
        }
        else{
            editor.putBoolean(getResources().getString(R.string.sound), true);
            audioButton.setBackgroundResource(android.R.drawable.ic_lock_silent_mode_off);
        }
        editor.apply();
    }

    //sets the shared preferences and triggers a starts a quiz.
    public void onStartButtonClick(View view){
        Bundle bundle = new Bundle();
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getResources().getString(R.string.difficulty), difficulty);
        editor.putInt(getResources().getString(R.string.questionIndex), 0);
        editor.putInt(getResources().getString(R.string.score), 0);
        editor.putInt(getResources().getString(R.string.numQuestions), numQuestions);
        editor.putBoolean(getString(R.string.timedMode), timedMode);
        editor.apply();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, QuestionFragment.class,  bundle).commit();
    }
}
