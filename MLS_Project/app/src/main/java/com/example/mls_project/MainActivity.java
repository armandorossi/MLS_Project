package com.example.mls_project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mls_project.databinding.ActivityMainBinding;

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
            if (con.loginConnection(binding.edtUsername.getText().toString(), hashPassword.hashAPassword(binding.edtPassword.getText().toString()))){
                Toast.makeText(this, "Login succeeded", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
        }
    }

    private void registerAction () {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startForResult.launch(registerIntent);
    }

//    private static String hashPassword(String password) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("SHA-512");
//        md.reset();
//        md.update(password.getBytes());
//        byte[] mdArray = md.digest();
//        StringBuilder sb = new StringBuilder(mdArray.length * 2);
//        for(byte b : mdArray) {
//            int v = b & 0xff;
//            if(v < 16)
//                sb.append('0');
//            sb.append(Integer.toHexString(v));
//        }
//        return sb.toString();
//    }
}