package com.example.triviatime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Collections;
import java.util.LinkedList;

public class EndScreenFragment extends Fragment implements HighScorePostFragment.OnScorePosted{
    private int score; // keeps track of current score
    private Button addScoreButton;
    private int currentHighScore;  // current highscore, gotten through Firebase
    private String highScoreHolder;  // name of current highscore holder
    private TextView announcement;
    private MediaPlayer player;

    public EndScreenFragment(){super(R.layout.fragment_end_screen);}

    public void onViewCreated(View v, Bundle savedInstanceState) {
        LinkedList<User> users = new LinkedList<>();
        RecyclerView recyclerView;
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentHighScore = 0;
        score = sharedPref.getInt(getResources().getString(R.string.score), 0);

        builder.setMessage(getResources().getString(R.string.fetchingScores))
                .setTitle(getResources().getString(R.string.waitMessage));
        AlertDialog waitDialog = builder.create();
        waitDialog.show();

        TextView Score = v.findViewById(R.id.end_score_lbl);
        addScoreButton = v.findViewById(R.id.addUser_btn);
        announcement = v.findViewById(R.id.highScoreAnouce);
        recyclerView = v.findViewById(R.id.scoresRecycler);
        addScoreButton.setVisibility(View.INVISIBLE);
        Button goHomeButton = v.findViewById(R.id.go_to_splash);
        // hides the post score button and announcement
        goHomeButton.setOnClickListener(view -> onHomeClicked(view));


        ScoreAdapter adapter = new ScoreAdapter(getActivity(), users);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
        player = MediaPlayer.create(getContext(), R.raw.highscore);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot userDataSnap : snapshot.getChildren()){
                    User user = userDataSnap.getValue(User.class);
                    if(user != null) {
                        users.add(user);
                        if (user.getScore() > currentHighScore) {
                            currentHighScore = user.getScore();
                            highScoreHolder = user.getName();
                        }
                        Collections.sort(users, (o1, o2) -> o2.getScore() - o1.getScore());
                    }
                }
                // reference to getResources directly caused crashes,
                // the line below make sure that activity is present while getting resouces.
                if(getActivity()!= null) {
                    Score.setText(getActivity().getResources().getString(R.string.score_lbl) + score);
                    if (highScoreHolder == null)
                        announcement.setText(getActivity().getResources().getString(R.string.noHighScore));
                    else if((!sharedPref.getBoolean(getActivity().getResources().getString(R.string.timedMode),false)) && currentHighScore < score) {
                        announcement.setText(getActivity().getResources().getString(R.string.notTimedMode));
                        if (!player.isPlaying() && sharedPref.getBoolean(getResources().getString(R.string.sound),true))
                            player.start();
                    }
                    else {
                        announcement.setText(getActivity().getResources().getString(R.string.currentHighScore) + currentHighScore + "\n" + getActivity().getResources().getString(R.string.heldBy) + highScoreHolder);
                    }
                    // Shows the announcement that current score is a new record and shows the post score button
                    if (score > currentHighScore && sharedPref.getBoolean(getActivity().getResources().getString(R.string.timedMode),false)) {
                        addScoreButton.setVisibility(View.VISIBLE);
                        announcement.setText(getActivity().getResources().getString(R.string.newHighScore));
                        if (!player.isPlaying() && sharedPref.getBoolean(getResources().getString(R.string.sound),true))
                            player.start();
                    }
                }
                addScoreButton.setOnClickListener(v -> {
                    onPostScoreClicked(score);
                });

                if(getContext() != null) {
                    ScoreAdapter adapter = new ScoreAdapter(getContext(), users);
                    recyclerView.setAdapter(adapter);
                    waitDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                Toast.makeText(v.getContext(), getResources().getString(R.string.failiureMsg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //takes user back to home screen
    public void onHomeClicked(View view){
        Bundle bundle = new Bundle();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, SplashScreenFragment.class, bundle).commit();
        player.release();
    }

    public void onPostScoreClicked(int score){
        Bundle bundle = new Bundle();
        bundle.putInt(getResources().getString(R.string.scoreToPost), score);
        HighScorePostFragment highScorePostFragment = new HighScorePostFragment();
        highScorePostFragment.setArguments(bundle);
        highScorePostFragment.setTargetFragment(this,1);
        highScorePostFragment.show(this.getActivity().getSupportFragmentManager(), "post score dialog");
        player.release();
    }

    @Override
    public void sendBool(Boolean input) {
        addScoreButton.setVisibility(View.INVISIBLE);
    }
}
