package com.example.triviatime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.UserViewHolder>{
    private LayoutInflater mInflater;
    private LinkedList<User> users;
    private Context context;

    public ScoreAdapter(Context context, LinkedList<User> users){
        mInflater = LayoutInflater.from(context);
        this.users = users;
        this.context = context;
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ScoreAdapter scoreAdapter;
        public final TextView userName;
        public final TextView userScore;

        public UserViewHolder(View itemView, ScoreAdapter adapter) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.userName);
            this.userScore = itemView.findViewById(R.id.userScore);
            this.scoreAdapter = adapter;
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_view, parent, false);
        return new UserViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User mCurrent = users.get(position);

        holder.userName.setText(String.valueOf(mCurrent.getName()));
        holder.userScore.setText(String.valueOf(mCurrent.getScore()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
