package com.example.mls_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    private final Context context;
    private final List<String> list;

    public ScheduleAdapter(Context current, List<String> list) {
        this.context = current;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] scheduleList = list.get(position).split(";");
        holder.date.setText(scheduleList[1]); //Date
        holder.teamOne.setText(scheduleList[2]); //First team
        holder.teamTwo.setText(scheduleList[3]); //Second team
        if (scheduleList[4].equals("null")){ //Schedule time
            holder.time.setText("-");
        }
        else {
            holder.time.setText(scheduleList[4]);
        }
        String text;
        if (scheduleList[5].equals("null")) { //Real score
            text = context.getResources().getString(R.string.txt_score_real) + " -";
        }
        else {
            text = context.getResources().getString(R.string.txt_score_real) + " " + scheduleList[5];
        }
        holder.score.setText(text);
        //Removing accents from team names to match image names
        String t1 = Normalizer.normalize(scheduleList[6].toLowerCase(Locale.ROOT).replace(" ", "_").replace(".", ""), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String t2 = Normalizer.normalize(scheduleList[7].toLowerCase(Locale.ROOT).replace(" ", "_").replace(".", ""), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        holder.imgOne.setImageResource(context.getResources().getIdentifier(t1, "drawable", context.getPackageName()));
        holder.imgTwo.setImageResource(context.getResources().getIdentifier(t2, "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, teamOne, teamTwo, time, score, predicted;
        ImageView imgOne, imgTwo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_schedule_date);
            teamOne = itemView.findViewById(R.id.txt_f_team_name);
            teamTwo = itemView.findViewById(R.id.txt_s_team_name);
            time = itemView.findViewById(R.id.txt_schedule_time);
            score = itemView.findViewById(R.id.txt_score_real);
            predicted = itemView.findViewById(R.id.txt_score_predicted);
            imgOne = itemView.findViewById(R.id.img_f_team);
            imgTwo = itemView.findViewById(R.id.img_s_team);
        }
    }
}
