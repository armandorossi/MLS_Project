package com.example.mls_project.Classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import com.example.mls_project.Database.ConnectionSQL;
import com.example.mls_project.Models.ScheduleAdapter;
import com.example.mls_project.databinding.ActivityTeamScheduleBinding;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TeamScheduleActivity extends AppCompatActivity {

    private final ConnectionSQL con = new ConnectionSQL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTeamScheduleBinding binding = ActivityTeamScheduleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Bundle extras = getIntent().getExtras();
        String teamName = extras.getString("TeamName");

        List<String> list = con.teamSchedule(teamName, df.format(d));

        String text = teamName + " - Next games";
        binding.txtTeamSchedule.setText(text);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvTeamScheduleItems.setLayoutManager(layoutManager);
        RecyclerView.Adapter<ScheduleAdapter.ViewHolder> adapter = new ScheduleAdapter(this, list);
        binding.rvTeamScheduleItems.setAdapter(adapter);
    }
}