package com.example.mls_project.Classes;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.example.mls_project.Database.ConnectionSQL;
import com.example.mls_project.R;
import com.example.mls_project.Models.ScheduleAdapter;
import com.example.mls_project.databinding.ActivityScheduleBinding;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private ActivityScheduleBinding binding;
    private RecyclerView.Adapter<ScheduleAdapter.ViewHolder> adapter;
    private List<String> list = new ArrayList<>();
    private final Date today = new Date();
    private final Calendar cal = Calendar.getInstance();
    private final ConnectionSQL con = new ConnectionSQL();
    private static String admin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Populating Year and Month spinner and retrieving list of games
        try {
            cal.setTime(today);

            ArrayAdapter<String> yearList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, con.yearList());
            binding.spnYear.setAdapter(yearList);
            binding.spnYear.setSelection(0);

            String[] months = new String[12];
            for (int i = 1; i <= 12; i++) {
                months[i-1] = String.valueOf(i);
            }
            ArrayAdapter<String> monthList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, months);
            binding.spnMonth.setAdapter(monthList);
            binding.spnMonth.setSelection(((ArrayAdapter<String>)binding.spnMonth.getAdapter()).getPosition(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+2)));

            list = con.scheduleList(Integer.parseInt(binding.spnYear.getSelectedItem().toString()), Integer.parseInt(binding.spnMonth.getSelectedItem().toString()));
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvScheduleItems.setLayoutManager(layoutManager);
        adapter = new ScheduleAdapter(this, list);
        binding.rvScheduleItems.setAdapter(adapter);

        //Refreshing data when a different year is selected
        binding.spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                list = con.scheduleList(Integer.parseInt(binding.spnYear.getSelectedItem().toString()), Integer.parseInt(binding.spnMonth.getSelectedItem().toString()));
                adapter = new ScheduleAdapter(ScheduleActivity.this, list);
                binding.rvScheduleItems.setAdapter(adapter);
                if (list.size() == 0) {
                    Toast.makeText(ScheduleActivity.this, "No games found for the selected period", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Refreshing data when a different month is selected
        binding.spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                list = con.scheduleList(Integer.parseInt(binding.spnYear.getSelectedItem().toString()), Integer.parseInt(binding.spnMonth.getSelectedItem().toString()));
                adapter = new ScheduleAdapter(ScheduleActivity.this, list);
                binding.rvScheduleItems.setAdapter(adapter);
                if (list.size() == 0) {
                    Toast.makeText(ScheduleActivity.this, "No games found for the selected period", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Overriding method to display a confirmation message before exiting the app
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
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