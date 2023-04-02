package com.example.mls_project.Models;

import android.annotation.SuppressLint;
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
import com.example.mls_project.Entities.Team;
import com.example.mls_project.Entities.TeamStandings;
import com.example.mls_project.R;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private final Context context;
    private final List<Team> teamList;
    private List<TeamStandings> teamStandingsList;

    public TeamAdapter(Context current, List<Team> teamList, List<TeamStandings> teamStandingsList) {
        this.context = current;
        this.teamList = teamList;
        this.teamStandingsList = teamStandingsList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTeamStandingsList (List<TeamStandings> teamStandingsList) {
        this.teamStandingsList = teamStandingsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeamViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false));
    }

    @SuppressLint("DiscouragedApi")
    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teamList.get(position);
        holder.txt_team_name.setText(team.getTeamShortName());
        String t1 = Normalizer.normalize(team.getTeamName().toLowerCase(Locale.ROOT).replace(" ", "_").replace(".", ""), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        holder.img_team.setImageResource(context.getResources().getIdentifier(t1, "drawable", context.getPackageName()));

        TeamStandings teamStandings;
        for (int i = 0; i < teamStandingsList.size(); i ++) {
            teamStandings = teamStandingsList.get(i);
            if (teamStandings.getTeamName().equals(team.getTeamShortName())) {
                holder.txt_points1.setText(teamStandings.getTotalPoints());
                holder.txt_wins1.setText(teamStandings.getTotalWins());
                holder.txt_losses1.setText(teamStandings.getTotalLosses());
                holder.txt_draws1.setText(teamStandings.getTotalDraws());
            }
        }
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView txt_team_name, txt_points1, txt_wins1, txt_losses1, txt_draws1;
        ImageView img_team;
        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_team_name = itemView.findViewById(R.id.txt_team_name);
            txt_points1 = itemView.findViewById(R.id.txtPoints1);
            txt_wins1 = itemView.findViewById(R.id.txtWins1);
            txt_losses1 = itemView.findViewById(R.id.txtLosses1);
            txt_draws1 = itemView.findViewById(R.id.txtDraws1);
            img_team = itemView.findViewById(R.id.img_team);
            itemView.setOnClickListener((View v) -> {
                Intent teamScheduleIntent = new Intent(itemView.getContext(), TeamScheduleActivity.class);
                teamScheduleIntent.putExtra("TeamName", txt_team_name.getText());
                itemView.getContext().startActivity(teamScheduleIntent);
            });
        }
    }
}
