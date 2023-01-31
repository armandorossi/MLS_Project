package com.example.mls_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mls_project.databinding.ActivityRegisterBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

        private ActivityRegisterBinding binding;
    private boolean Cancel = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnConfirmRegister2.setOnClickListener((View v) -> {
            confirmRegisterAction();
        });

        binding.btnCancelRegister.setOnClickListener((View v) -> {
            Cancel = false;
            finish();
        });
    }

    //Method to validate all information inserted during registration
    private void confirmRegisterAction () {
        if (binding.edtFirstName.getText().toString().isEmpty()){ //Checking first name
            binding.edtFirstName.setError(getString(R.string.first_name_empty));
        }
        else if (binding.edtLastName.getText().toString().isEmpty()){ //Checking last name
            binding.edtLastName.setError(getString(R.string.last_name_empty));
        }
        else if (binding.edtEmailRegister.getText().toString().isEmpty()){ //Checking email
            binding.edtEmailRegister.setError(getString(R.string.email_empty));
        }
        else if (binding.edtPasswordRegister.getText().toString().isEmpty()){ //Checking password
            binding.edtPasswordRegister.setError(getString(R.string.password_empty));
        }
        else if (binding.edtPasswordConfirmRegister.getText().toString().isEmpty()){ //Checking password confirmation
            binding.edtPasswordConfirmRegister.setError(getString(R.string.password_confirm_empty));
        }
        else if (!binding.edtPasswordRegister.getText().toString().equals(binding.edtPasswordConfirmRegister.getText().toString())) { //Checking if both passwords are equal
            binding.edtPasswordConfirmRegister.setError(getString(R.string.password_not_equal));
        }
        else if (binding.edtPasswordRegister.getText().toString().length() < 8 || !isValidPassword(binding.edtPasswordRegister.getText().toString())) { //Checking if password meet password requirements
            binding.edtPasswordRegister.setError(getString(R.string.password_requirements));
        }
        else {
            ConnectionSQL con = new ConnectionSQL();
            HashPassword hashPassword = new HashPassword();

            String firstName, lastName, email, password;
            firstName = binding.edtFirstName.getText().toString();
            lastName = binding.edtLastName.getText().toString();
            email = binding.edtEmailRegister.getText().toString();

            try {
                password = hashPassword.hashAPassword(binding.edtPasswordRegister.getText().toString());
                if (con.registerConnection(this, firstName, lastName, email, password)){
                    Toast.makeText(this, "Registered. Redirecting to login page.", Toast.LENGTH_LONG).show();
                    (new Handler()).postDelayed(this::finish, 3000);
                }
                else {
                    Toast.makeText(this, "Failed to register, try again.", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                Toast.makeText(this, "Failed to register, try again. " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    //Overrinding finish method to return username to the previous activity
    @Override
    public void finish() {
        if (Cancel) {
            Intent data = new Intent();
            String returnEmail = binding.edtEmailRegister.getText().toString();
            data.putExtra("returnEmail", returnEmail);
            setResult(RESULT_OK, data);
        }
        super.finish();
    }

    //Method to check if password meet requirements
    private static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}