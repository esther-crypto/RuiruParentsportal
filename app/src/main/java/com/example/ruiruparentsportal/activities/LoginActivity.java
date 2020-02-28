package com.example.ruiruparentsportal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ruiruparentsportal.R;
import com.example.ruiruparentsportal.interfaces.ApiService;
import com.example.ruiruparentsportal.model.Parent;
import com.example.ruiruparentsportal.response.ParentResponse;
import com.example.ruiruparentsportal.utils.AppUtils;
import com.example.ruiruparentsportal.utils.SharedPrefsManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText phone, password;
    Button btnLogin;
    ApiService service;
    ProgressBar loginProgressBar;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkIfIsLoggedIn();

        service = AppUtils.getApiService();

        phone = findViewById(R.id.edtPhoneLogin);
        password = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(phone.getText().toString().trim(),
                        password.getText().toString().trim());
            }
        });

    }

    private void checkIfIsLoggedIn() {
        if (SharedPrefsManager.getInstance(LoginActivity.this).loginCheck()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void loginUser(String phone, final String password) {
        AppUtils.showView(loginProgressBar);
        //Send login request to server - DONE
        //TODO: Remember to use md5 for password
        service.loginParent(AppUtils.LOGIN_TOKEN, phone, password).enqueue(new Callback<ParentResponse>() {
            @Override
            public void onResponse(Call<ParentResponse> call, Response<ParentResponse> response) {
                AppUtils.hideView(loginProgressBar);
                if (response.isSuccessful()) {
                    try {
                        Parent parent = response.body().getParent();
                        if (parent == null) {
                            Toast.makeText(LoginActivity.this, "data: " + response.body().getParent(), Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPrefsManager.getInstance(LoginActivity.this).userLogin(parent.getName(),
                                    parent.getEmail(), parent.getPhone(), true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "An error has occurred!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ParentResponse> call, Throwable t) {
                AppUtils.hideView(loginProgressBar);
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}