package com.example.mls_project.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mls_project.Entities.Schedule;
import com.example.mls_project.R;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{
    private final Context context;
    private List<Schedule> scheduleList;

    public ScheduleAdapter(Context current, List<Schedule> scheduleList) {
        this.context = current;
        this.scheduleList = scheduleList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setScheduleList (List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false));
    }

    @SuppressLint("DiscouragedApi")
    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);

        try {
            holder.date.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy").format(
                    new SimpleDateFormat("yyyy-MM-dd").parse(schedule.getScheduleDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        holder.date.setText(schedule.getScheduleDate());
        holder.teamOne.setText(schedule.getHomeTeamName());
        holder.teamTwo.setText(schedule.getAwayTeamName());
        //Checking if the field result is not null to set winner as green and loser as red
        if (schedule.getResult() != null) {
            if (schedule.getHomeTeamName().equals(schedule.getResult())){
                holder.teamOne.setTextColor(Color.parseColor("#0AAE51"));
            }
            else {
                holder.teamOne.setTextColor(Color.parseColor("#AE0A0A"));
            }
            if (schedule.getAwayTeamName().equals(schedule.getResult())){
                holder.teamTwo.setTextColor(Color.parseColor("#0AAE51"));
            }
            else {
                holder.teamTwo.setTextColor(Color.parseColor("#AE0A0A"));
            }
        }
        else {
            holder.teamOne.setTextColor(Color.parseColor("#757575"));
            holder.teamTwo.setTextColor(Color.parseColor("#757575"));
        }
        //Checking if scheduleTime is available to display, otherwise the field will not be displayed
        if (schedule.getScheduleTime() == null) {
            holder.time.setVisibility(View.GONE);
        }
        else {
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(schedule.getScheduleTime().substring(0, 5));
        }
        //Checking if Score is available to display, otherwise the field will not be displayed
        if (schedule.getScore() != null) {
            String[] scoreSplit = schedule.getScore().split(":");

            holder.teamOne.setText(schedule.getHomeTeamName() + " " + scoreSplit[0]);
            holder.teamTwo.setText(scoreSplit[1] + " " + schedule.getAwayTeamName());
        }
        String t1 = Normalizer.normalize(schedule.getHomeClubName().toLowerCase(Locale.ROOT).replace(" ", "_").replace(".", ""), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String t2 = Normalizer.normalize(schedule.getAwayClubName().toLowerCase(Locale.ROOT).replace(" ", "_").replace(".", ""), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        holder.imgOne.setImageResource(context.getResources().getIdentifier(t1, "drawable", context.getPackageName()));
        holder.imgTwo.setImageResource(context.getResources().getIdentifier(t2, "drawable", context.getPackageName()));

        //String pred = "Home " + schedule.getHome_pct() + "\nAway  " + schedule.getAway_pct() + "\nDraw   " + schedule.getDraw_pct();
        String pred = "Home " + schedule.getHome_pct() + " Away " + schedule.getAway_pct() + " Draw " + schedule.getDraw_pct();
        holder.prediction.setText(pred);
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView date, teamOne, teamTwo, time, txt_prediction, prediction;
        ImageView imgOne, imgTwo;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_schedule_date);
            teamOne = itemView.findViewById(R.id.txt_f_team_name);
            teamTwo = itemView.findViewById(R.id.txt_s_team_name);
            time = itemView.findViewById(R.id.txt_schedule_time);
            prediction = itemView.findViewById(R.id.txt_score_prediction);
            txt_prediction = itemView.findViewById(R.id.txt_prediction);
            imgOne = itemView.findViewById(R.id.img_f_team);
            imgTwo = itemView.findViewById(R.id.img_s_team);
        }
    }
}
