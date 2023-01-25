package com.example.mls_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.mls_project.databinding.ActivityScheduleBinding;
import com.example.mls_project.databinding.ActivityUserBinding;

import java.util.List;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<String> list;
    private ConnectionSQL con = new ConnectionSQL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        String admin = extras.getString("Admin");
        if (admin.equals("1")){
            binding.imgSchedule.setVisibility(View.VISIBLE);
        }

        try {
            list = con.userList(1);
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        layoutManager = new LinearLayoutManager(this);
        binding.rvUserItems.setLayoutManager(layoutManager);
        adapter = new UserAdapter(this, list, binding.spnStatus);
        binding.rvUserItems.setAdapter(adapter);

        binding.spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                if (binding.spnStatus.getSelectedItemPosition() == 0) {
                    list = con.userList(1); //Active
                }
                else {
                    list = con.userList(0); //Active
                }
                adapter = new UserAdapter(UserActivity.this, list, binding.spnStatus);
                binding.rvUserItems.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.imgSchedule.setOnClickListener((View v) -> {
            Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
            scheduleIntent.putExtra("Admin", admin);
            startActivity(scheduleIntent);
            finish();
        });
    }
}