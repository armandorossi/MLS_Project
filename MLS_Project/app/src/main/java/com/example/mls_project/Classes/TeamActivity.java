package com.example.mls_project.Classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.mls_project.Database.ConnectionSQL;
import com.example.mls_project.Entities.Team;
import com.example.mls_project.Entities.TeamStandings;
import com.example.mls_project.R;
import com.example.mls_project.Models.TeamAdapter;
import com.example.mls_project.databinding.ActivityTeamBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    private final ConnectionSQL con = new ConnectionSQL();
    private static String admin = "";
    private final Date today = new Date();
    private final Calendar cal = Calendar.getInstance();
    private List<Team> teamList;
    private List<TeamStandings> teamStandingsList;
    private RecyclerView.Adapter<TeamAdapter.ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTeamBinding binding = ActivityTeamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        cal.setTime(today);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        teamList = con.teamList();
        teamStandingsList = con.teamStandings(String.valueOf(year));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvTeamItems.setLayoutManager(layoutManager);
        adapter = new TeamAdapter(this, teamList, teamStandingsList);
        binding.rvTeamItems.setAdapter(adapter);

        ArrayAdapter<String> yearList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        while (year >= 1996) {
            yearList.add(String.valueOf(year));
            year--;
        }
        binding.spnTeamYear.setAdapter(yearList);

        binding.spnTeamYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamStandingsList.clear();
                teamStandingsList = con.teamStandings(binding.spnTeamYear.getSelectedItem().toString());
                adapter = new TeamAdapter(TeamActivity.this, teamList, teamStandingsList);
                binding.rvTeamItems.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);

        //Getting info about user privileges
        Bundle extras = getIntent().getExtras();
        admin = extras.getString("Admin");
        if (!admin.equals("Yes")){
            menu.findItem(R.id.menu_user).setVisible(false);
        }

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_user:
                Intent userIntent = new Intent(this, UserActivity.class);
                userIntent.putExtra("Admin", admin);
                startActivity(userIntent);
                finish();
                return true;

            case R.id.menu_schedule:
                Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
                scheduleIntent.putExtra("Admin", admin);
                startActivity(scheduleIntent);
                finish();
                return true;

            case R.id.menu_teams:
                Intent TeamIntent = new Intent(this, TeamActivity.class);
                TeamIntent.putExtra("Admin", admin);
                startActivity(TeamIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}