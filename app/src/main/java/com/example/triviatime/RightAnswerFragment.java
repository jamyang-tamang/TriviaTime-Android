package com.example.triviatime;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class RightAnswerFragment extends Fragment {
    private int numQuestions;
    private int questionIndex;
    private int score;
    private String question;
    private String answer;
    private MediaPlayer player;

    public RightAnswerFragment() { super(R.layout.fragment_right_answer); }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        player = MediaPlayer.create(this.getContext(), R.raw.correct);
        super.onViewCreated(v,savedInstanceState);

        //gets shared preferences data
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        numQuestions = sharedPref.getInt(getResources().getString(R.string.numQuestions), 3);
        questionIndex = sharedPref.getInt(getResources().getString(R.string.questionIndex), 0);
        score = sharedPref.getInt(getResources().getString(R.string.score), 0);
        if(sharedPref.getBoolean(getResources().getString(R.string.sound),true))
            player.start();

        //gets data from bundle
        question = getArguments().getString(getResources().getString(R.string.question));
        answer = getArguments().getString(getResources().getString(R.string.answer));

        //sets xml to code
        TextView Score = v.findViewById(R.id.right_score_display);
        TextView Question = v.findViewById(R.id.right_question_view);
        TextView Answer = v.findViewById(R.id.right_answer_display);
        Button Next = v.findViewById(R.id.right_next_button);

        //sets the right text and trigger
        Question.setText(question);
        Score.setText(getResources().getString(R.string.score_lbl)+ score);
        Answer.setText("You were right the correct answer is '" + answer.toUpperCase() + "'");
        Next.setOnClickListener(view -> onClickNext(view));
    }

    private void onClickNext(View v){
        Bundle bundle = new Bundle();
        player.release();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(sharedPref.getBoolean(getString(R.string.timedMode), false)){
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, QuestionFragment.class, bundle).commit();
        }
        else {
            if (questionIndex < numQuestions - 1) {
                editor.putInt(getResources().getString(R.string.questionIndex), questionIndex + 1);
                editor.apply();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, QuestionFragment.class, bundle).commit();
            } else {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, EndScreenFragment.class, bundle).commit();
            }
        }
    }
}
