package com.example.mls_project.Classes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mls_project.Database.ConnectionSQL;
import com.example.mls_project.Database.HashPassword;
import com.example.mls_project.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin) {
            binding.edtUsername.setText(loginPreferences.getString("username", ""));
            binding.edtPassword.setText(loginPreferences.getString("password", ""));
            binding.chkRemember.setChecked(true);
        }

        binding.chkRemember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", binding.edtUsername.getText().toString());
                loginPrefsEditor.putString("password", binding.edtPassword.getText().toString());
                loginPrefsEditor.apply();
            }
            else {
                loginPrefsEditor.clear();
                loginPrefsEditor.apply();
            }
        });

        binding.btnLogin.setOnClickListener((View v) -> loginAction());

        binding.btnRegister.setOnClickListener((View v) -> {
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            startForResult.launch(registerIntent);
        });
    }

    //Method responsible for retrieving the username from RegisterActivity when a new user is registered and set the edtUsername with this information
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String returnString = null;
                if (data != null) {
                    returnString = data.getExtras().getString("returnEmail");
                }
                binding.edtUsername.setText(returnString);
                binding.edtPassword.setText("");
            }
        }
    });

    //Method for authenticating an existing user and setting the privilege mode (if the user is an admin)
    private void loginAction () {
        ConnectionSQL con = new ConnectionSQL();
//        HashPassword hashPassword = new HashPassword();
        try {
            String[] result = con.loginConnection(binding.edtUsername.getText().toString(), HashPassword.hashAPassword(binding.edtPassword.getText().toString())).split(";");
            if (result[0].equals("Yes")) {
                if (result[2].equals("Yes")){
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
}