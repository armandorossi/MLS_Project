package com.example.mls_project.Models;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mls_project.Entities.Schedule;
import com.example.mls_project.R;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    private final Context context;
    private final List<Schedule> scheduleList;

    public ScheduleAdapter(Context current, List<Schedule> scheduleList) {
        this.context = current;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.date.setText(schedule.getScheduleDate());
        holder.teamOne.setText(schedule.getHomeTeamName());
        holder.teamTwo.setText(schedule.getAwayTeamName());
        if (schedule.getScheduleTime() == null) {
            holder.time.setVisibility(View.GONE);
        }
        else {
            holder.time.setText(schedule.getScheduleTime().substring(0, 5));
        }
        if (schedule.getScore() == null) {
            holder.txt_score.setVisibility(View.GONE);
            holder.score.setVisibility(View.GONE);
        }
        else {
            holder.score.setText(schedule.getScore());
        }
        String t1 = Normalizer.normalize(schedule.getHomeClubName().toLowerCase(Locale.ROOT).replace(" ", "_").replace(".", ""), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String t2 = Normalizer.normalize(schedule.getAwayClubName().toLowerCase(Locale.ROOT).replace(" ", "_").replace(".", ""), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        holder.imgOne.setImageResource(context.getResources().getIdentifier(t1, "drawable", context.getPackageName()));
        holder.imgTwo.setImageResource(context.getResources().getIdentifier(t2, "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, teamOne, teamTwo, time, txt_score, score, txt_prediction, prediction;
        ImageView imgOne, imgTwo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_schedule_date);
            teamOne = itemView.findViewById(R.id.txt_f_team_name);
            teamTwo = itemView.findViewById(R.id.txt_s_team_name);
            time = itemView.findViewById(R.id.txt_schedule_time);
            txt_score = itemView.findViewById(R.id.txt_score);
            score = itemView.findViewById(R.id.txt_score_real);
            prediction = itemView.findViewById(R.id.txt_score_prediction);
            txt_prediction = itemView.findViewById(R.id.txt_prediction);
            imgOne = itemView.findViewById(R.id.img_f_team);
            imgTwo = itemView.findViewById(R.id.img_s_team);
        }
    }
}
