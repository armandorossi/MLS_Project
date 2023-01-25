package com.example.mls_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.example.mls_project.databinding.ActivityScheduleBinding;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private ActivityScheduleBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<String> list;
    private Date today = new Date();
    private Calendar cal = Calendar.getInstance();
    private ConnectionSQL con = new ConnectionSQL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        String admin = extras.getString("Admin");
        if (admin.equals("1")){
            binding.imgUser.setVisibility(View.VISIBLE);
        }

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

        layoutManager = new LinearLayoutManager(this);
        binding.rvScheduleItems.setLayoutManager(layoutManager);
        adapter = new ScheduleAdapter(this, list);
        binding.rvScheduleItems.setAdapter(adapter);

        binding.spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                list = con.scheduleList(Integer.parseInt(binding.spnYear.getSelectedItem().toString()), Integer.parseInt(binding.spnMonth.getSelectedItem().toString()));
                adapter = new ScheduleAdapter(ScheduleActivity.this, list);
                binding.rvScheduleItems.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                list = con.scheduleList(Integer.parseInt(binding.spnYear.getSelectedItem().toString()), Integer.parseInt(binding.spnMonth.getSelectedItem().toString()));
                adapter = new ScheduleAdapter(ScheduleActivity.this, list);
                binding.rvScheduleItems.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.imgUser.setOnClickListener((View v) -> {
            Intent userIntent = new Intent(this, UserActivity.class);
            userIntent.putExtra("Admin", admin);
            startActivity(userIntent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}