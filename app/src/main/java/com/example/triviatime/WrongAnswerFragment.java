package com.example.triviatime;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class WrongAnswerFragment extends Fragment {
    private int numQuestions;
    private int questionIndex;
    private MediaPlayer player;

    public WrongAnswerFragment (){
        super(R.layout.fragment_wrong_answer);
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        //gets data from shared preferences
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        numQuestions = sharedPref.getInt(getResources().getString(R.string.numQuestions), 3);
        questionIndex = sharedPref.getInt(getResources().getString(R.string.questionIndex), 0);
        int score = sharedPref.getInt(getResources().getString(R.string.score), 0);

        //gets data from bundle
        String question = getArguments().getString(getResources().getString(R.string.question));
        String answer = getArguments().getString(getResources().getString(R.string.answer));
        Boolean timeUp = false;
        if(getArguments().containsKey(getResources().getString(R.string.timesUp)))
            timeUp = getArguments().getBoolean(getResources().getString(R.string.timesUp));

        //matches xml to code
        TextView Score = v.findViewById(R.id.wrong_score_display);
        TextView Question = v.findViewById(R.id.wrong_question_view);
        TextView Answer = v.findViewById(R.id.answer_display);
        Button Next = v.findViewById(R.id.next_btn);

        // checks if game is in timed mode it creates specific audio type
        if(sharedPref.getBoolean(getString(R.string.timedMode), false)){
            player = MediaPlayer.create(this.getContext(), R.raw.fail);
        }
        else{
            player = MediaPlayer.create(this.getContext(), R.raw.wrong);
        }
        if(sharedPref.getBoolean(getResources().getString(R.string.sound),true))
            player.start();


        //sets the right text and listener
        Question.setText(question);
        Score.setText(getResources().getString(R.string.score_lbl) + score);
        if(timeUp)
            Answer.setText("You ran out of time the answer is '" + answer.toUpperCase() + "'");
        else
            Answer.setText("You were wrong the answer is '" + answer.toUpperCase() + "'");
        Next.setOnClickListener(view -> onClickNext());
    }

    private void onClickNext(){
        Bundle bundle = new Bundle();
        player.release();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(sharedPref.getBoolean(getString(R.string.timedMode), false)){
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, EndScreenFragment.class, bundle).commit();
        }
        else {
            if (questionIndex < numQuestions - 1) {
                editor.putInt(getResources().getString(R.string.questionIndex), questionIndex + 1);
                editor.apply();
                Log.println(Log.INFO, "QuestionIndex", String.valueOf(questionIndex));
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, QuestionFragment.class, bundle).commit();
            } else {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, EndScreenFragment.class, bundle).commit();
            }
        }
    }
}
