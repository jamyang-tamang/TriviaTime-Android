package com.example.triviatime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import org.apache.commons.text.StringEscapeUtils;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment {
    private ArrayList<String> questions= new ArrayList<String>();
    private ArrayList<String> correctAnswer = new ArrayList<String>();
    private ArrayList<List<String>> incorrectAnswers = new ArrayList<List<String>>();
    private Call<TBResponse> call;
    private CountDownTimer timer;
    private int difficulty;
    private int questionIndex;
    private int score;
    private int numQuestions;
    private int counter;
    private MediaPlayer player;
    public QuestionFragment(){
        super(R.layout.fragment_question);
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v,savedInstanceState);
        counter = 100;

        //sets up shared preferences and gets data or sets the default
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        numQuestions = sharedPref.getInt(getResources().getString(R.string.numQuestions), 3);
        questionIndex = sharedPref.getInt(getResources().getString(R.string.questionIndex), 0);
        score = sharedPref.getInt(getResources().getString(R.string.score), 0);
        difficulty = sharedPref.getInt(getResources().getString(R.string.difficulty), 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(getResources().getString(R.string.fetchingData))
                .setTitle(getResources().getString(R.string.waitMessage));
        AlertDialog waitDialog = builder.create();
        waitDialog.show();

        TextView Question = v.findViewById(R.id.question_view);
        TextView Answer = v.findViewById(R.id.option_a);
        Answer.setOnClickListener(view -> onWrongAnswerClicked());

        TextView Answer1 = v.findViewById(R.id.option_b);
        Answer1.setOnClickListener(view -> onWrongAnswerClicked());

        TextView Answer2 = v.findViewById(R.id.option_c);
        Answer2.setOnClickListener(view -> onWrongAnswerClicked());

        TextView Answer3 = v.findViewById(R.id.option_d);
        Answer3.setOnClickListener(view -> onWrongAnswerClicked());

        TextView Score = v.findViewById(R.id.score_display);

        GetOpenTBResponse service = RetrofitClientInstance.getRetrofitInstance().create(GetOpenTBResponse.class);
        switch(difficulty){
            case 0:
                call = service.getResponse();
            case 1:
                call = service.getEasyResponse();
            case 2:
                call = service.getMediumResponse();
            case 3:
                call = service.getHardResponse();
        }

        call.enqueue(new Callback<TBResponse>() {
            @Override
            public void onResponse(Call<TBResponse> call, Response<TBResponse> response) {
                waitDialog.dismiss();
                TBResponse tbResponse = response.body();
                for(int i = 0; i < numQuestions; i++) {
                    questions.add(tbResponse.getResults().get(i).question);
                    correctAnswer.add(tbResponse.getResults().get(i).correctAnswer);
                    incorrectAnswers.add(tbResponse.getResults().get(i).incorrect_answers);
                }

                //causes crashes if if statement is missing
                if(getActivity() != null) {
                    Score.setText(getActivity().getResources().getString(R.string.score_lbl) + score);
                    Question.setText(StringEscapeUtils.unescapeHtml4(questions.get(questionIndex)));
                }
                ProgressBar timeDisplay = v.findViewById(R.id.timerBar);
                TextView timerDisplay = v.findViewById(R.id.timerView);
                timeDisplay.setVisibility(View.INVISIBLE);
                timerDisplay.setVisibility(View.INVISIBLE);
                int randomInt = (int) (Math.random()*3);
                //Display question
                if(incorrectAnswers.get(questionIndex).size() > 2) {
                    Answer2.setVisibility(View.VISIBLE);
                    Answer3.setVisibility(View.VISIBLE);
                    if (randomInt == 0) {
                        Answer.setText(StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
                        Answer.setOnClickListener(view -> onCorrectAnswerClicked());
                        Answer1.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(0)));
                        Answer2.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(1)));
                        Answer3.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(2)));
                    } else if (randomInt == 1) {
                        Answer.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(0)));
                        Answer1.setText(StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
                        Answer1.setOnClickListener(view -> onCorrectAnswerClicked());
                        Answer2.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(1)));
                        Answer3.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(2)));
                    } else if (randomInt == 2) {
                        Answer.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(0)));
                        Answer1.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(1)));
                        Answer2.setText(StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
                        Answer2.setOnClickListener(view -> onCorrectAnswerClicked());
                        Answer3.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(2)));
                    } else if (randomInt == 3) {
                        Answer.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(0)));
                        Answer1.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(1)));
                        Answer2.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(2)));
                        Answer3.setText(StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
                        Answer3.setOnClickListener(view -> onCorrectAnswerClicked());
                    }
                }
                else{
                    if (randomInt % 2 == 0) {
                        Answer.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(0)));
                        Answer1.setText(StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
                        Answer1.setOnClickListener(view -> onCorrectAnswerClicked());
                    }
                    else{
                        Answer.setText(StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
                        Answer.setOnClickListener(view -> onCorrectAnswerClicked());
                        Answer1.setText(StringEscapeUtils.unescapeHtml4(incorrectAnswers.get(questionIndex).get(0)));
                    }
                    Answer2.setVisibility(View.INVISIBLE);
                    Answer3.setVisibility(View.INVISIBLE);
                }

                timeDisplay.setProgress(100);
                //sets up audio
                player = MediaPlayer.create(getContext(), R.raw.timer);
                if(getActivity()!= null) {
                    if (sharedPref.getBoolean(getActivity().getResources().getString(R.string.timedMode), false)) {
                        timeDisplay.setVisibility(View.VISIBLE);
                        timerDisplay.setVisibility(View.VISIBLE);
                        timer = new CountDownTimer(10000, 100) {

                            //triggers progressbar to drop every second, changes color and triggers audio
                            //if counter hits 4 seconds
                            @Override
                            public void onTick(long millisUntilFinished) {
                                timerDisplay.setText(getResources().getString(R.string.timerLbl) + (counter / 10));
                                timeDisplay.setProgress(counter);
                                counter--;
                                if (counter < 40) {
                                    timeDisplay.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                                    if(!player.isPlaying() && sharedPref.getBoolean(getResources().getString(R.string.sound),true))
                                        player.start();
                                }
                            }

                            @Override
                            public void onFinish() {
                                player.release();
                                Bundle bundle = new Bundle();
                                bundle.putString(getResources().getString(R.string.question), StringEscapeUtils.unescapeHtml4(questions.get(questionIndex)));
                                bundle.putString(getResources().getString(R.string.answer), StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
                                bundle.putBoolean(getResources().getString(R.string.timesUp), true);
                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, WrongAnswerFragment.class, bundle).commit();
                            }
                        }.start();
                    }
                }
            }

            @Override
            public void onFailure(Call<TBResponse> call, Throwable t) {
                waitDialog.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.failiureMsg),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onCorrectAnswerClicked(){
        Bundle bundle = new Bundle();
        if(player != null)
            player.release();
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getResources().getString(R.string.score), score+1);
        editor.apply();
        bundle.putString(getResources().getString(R.string.question), StringEscapeUtils.unescapeHtml4(questions.get(questionIndex)));
        bundle.putString(getResources().getString(R.string.answer), StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, RightAnswerFragment.class, bundle).commit();
        if(sharedPref.getBoolean(getString(R.string.timedMode), false))
            timer.cancel();
    }

    public void onWrongAnswerClicked(){
        Bundle bundle = new Bundle();
        if(player != null)
            player.release();
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        bundle.putString(getResources().getString(R.string.question), StringEscapeUtils.unescapeHtml4(questions.get(questionIndex)));
        bundle.putString(getResources().getString(R.string.answer), StringEscapeUtils.unescapeHtml4(correctAnswer.get(questionIndex)));
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, WrongAnswerFragment.class, bundle).commit();
        if(sharedPref.getBoolean(getString(R.string.timedMode), false))
            timer.cancel();
    }
}
