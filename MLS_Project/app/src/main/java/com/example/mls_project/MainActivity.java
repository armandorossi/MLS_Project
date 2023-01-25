package com.example.mls_project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mls_project.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAction();
            }
        });
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String returnString = data.getExtras().getString("returnEmail");
                binding.edtUsername.setText(returnString);
            }
        }
    });

    private void loginAction () {
        ConnectionSQL con = new ConnectionSQL();
        HashPassword hashPassword = new HashPassword();
        try {
            String[] result = con.loginConnection(binding.edtUsername.getText().toString(), hashPassword.hashAPassword(binding.edtPassword.getText().toString())).split(";");
//            if (con.loginConnection(binding.edtUsername.getText().toString(), hashPassword.hashAPassword(binding.edtPassword.getText().toString()))){
            if (result[0].equals("1")) {
                if (result[2].equals("1")){
                    Intent userIntent = new Intent(this, UserActivity.class);
                    userIntent.putExtra("Admin", result[2]);
                    startActivity(userIntent);
                    finish();
                }
                else {
                    Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
                    scheduleIntent.putExtra("Admin", result[2]);
                    startActivity(scheduleIntent);
                    finish();
                }
            }
            else {
                Toast.makeText(this, result[1], Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Login failed. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void registerAction () {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startForResult.launch(registerIntent);
    }
}