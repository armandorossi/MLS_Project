package com.example.mls_project.Models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mls_project.Classes.TeamScheduleActivity;
import com.example.mls_project.R;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private final Context context;
    private final List<String> list;

    public TeamAdapter(Context current, List<String> list) {
        this.context = current;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ViewHolder holder, int position) {
        holder.txt_team_name.setText(list.get(position));
        String t1 = Normalizer.normalize(list.get(position).toLowerCase(Locale.ROOT).replace(" ", "_").replace(".", ""), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        holder.img_team.setImageResource(context.getResources().getIdentifier(t1, "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_team_name;
        ImageView img_team;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_team_name = itemView.findViewById(R.id.txt_team_name);
            img_team = itemView.findViewById(R.id.img_team);

            itemView.setOnClickListener((View v) -> {
                Intent teamScheduleIntent = new Intent(itemView.getContext(), TeamScheduleActivity.class);
//                teamScheduleIntent.putExtra("Admin", admin);
                teamScheduleIntent.putExtra("TeamName", txt_team_name.getText());
                itemView.getContext().startActivity(teamScheduleIntent);
            });
        }
    }
}
