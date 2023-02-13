package com.example.mls_project.Classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.mls_project.Database.ConnectionSQL;
import com.example.mls_project.Entities.User;
import com.example.mls_project.R;
import com.example.mls_project.Models.UserAdapter;
import com.example.mls_project.databinding.ActivityUserBinding;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private RecyclerView.Adapter adapter;
    private List<User> userList;
    private final ConnectionSQL con = new ConnectionSQL();
    private static String admin = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        popUserList(1);

        binding.tabLayoutUser.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        popUserList(1);
                        break;
                    case 1:
                        popUserList(0);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        popUserList(1);
                        break;
                    case 1:
                        popUserList(0);
                        break;
                }
            }
        });

        //Method to find a specific user by name or email
        binding.edtUserSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<User> userSearch = new ArrayList<>();
                if (s.length() > 0) {
                    for (int i = 0; i < userList.size(); i++) {
                        User user = userList.get(i);
                        String data = user.getEmail().toLowerCase(Locale.ROOT);
                        if (data.contains(binding.edtUserSearch.getText().toString().toLowerCase(Locale.ROOT))) {
                            userSearch.add(user);
                        }
                    }
//                    if (!userSearch.isEmpty()) {
                        adapter = new UserAdapter(UserActivity.this, userSearch, binding.tabLayoutUser);
                        binding.rvUserItems.setAdapter(adapter);
//                    }
                }
                else {
                    adapter = new UserAdapter(UserActivity.this, userList, binding.tabLayoutUser);
                    binding.rvUserItems.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtUserSearch.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (binding.edtUserSearch.getRight() - binding.edtUserSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    binding.edtUserSearch.getText().clear();
                    adapter = new UserAdapter(UserActivity.this, userList, binding.tabLayoutUser);
                    binding.rvUserItems.setAdapter(adapter);
                    return true;
                }
            }
            return false;
        });
    }

    private void popUserList (int userStatus) {
        try {
            userList = con.userList(userStatus);
            binding.rvUserItems.setLayoutManager(new LinearLayoutManager(UserActivity.this));
            adapter = new UserAdapter(UserActivity.this, userList, binding.tabLayoutUser);
            binding.rvUserItems.setAdapter(adapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error getting user list", Toast.LENGTH_LONG).show();
            Log.e("Error", e.getMessage());
        }
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