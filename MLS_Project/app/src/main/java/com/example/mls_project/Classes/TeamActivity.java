package com.example.mls_project.Classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.example.mls_project.Database.ConnectionSQL;
import com.example.mls_project.R;
import com.example.mls_project.Models.TeamAdapter;
import com.example.mls_project.databinding.ActivityTeamBinding;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    private final ConnectionSQL con = new ConnectionSQL();
    private static String admin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTeamBinding binding = ActivityTeamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        List<String> list = con.teamList();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvTeamItems.setLayoutManager(layoutManager);
        RecyclerView.Adapter<TeamAdapter.ViewHolder> adapter = new TeamAdapter(this, list);
        binding.rvTeamItems.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);

        //Getting info about user privileges
        Bundle extras = getIntent().getExtras();
        admin = extras.getString("Admin");
        if (!admin.equals("1")){
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