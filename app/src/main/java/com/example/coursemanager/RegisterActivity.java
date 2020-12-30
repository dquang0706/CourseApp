package com.example.coursemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursemanager.dao.UserDAO;
import com.example.coursemanager.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etUser,etPwd,etPwdRe;
    UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setColorStatusBar();
        init();
        registerAccount();
        intentToLogin();
        exitActivity();
    }

    private void init(){
        etUser = findViewById(R.id.etUserRegister);
        etPwd = findViewById(R.id.etPassRegister);
        etPwdRe = findViewById(R.id.etPassRegisterRe);
        userDAO = new UserDAO(this);
    }
    private void exitActivity(){
        findViewById(R.id.ivExitRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void intentToLogin(){
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registerAccount(){
        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUser.getText().toString().trim();
                String pass = etPwd.getText().toString().trim();
                String passRe = etPwdRe.getText().toString().trim();
                if (username.length() == 0 || pass.length() == 0 || passRe.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Không được bỏ trống các trường dữ liệu", Toast.LENGTH_LONG).show();
                } else {
                    if (pass.equals(passRe)) {
                        Boolean chkID = userDAO.chkID(username);
                        if (chkID) {
                            User user = new User(username, pass);
                            Boolean insert = userDAO.insert(user);
                            if (insert) {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Xác nhận mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void setColorStatusBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(RegisterActivity.this,R.color.color1));
        }
    }
}