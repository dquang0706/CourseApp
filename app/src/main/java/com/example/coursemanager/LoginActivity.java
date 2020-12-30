package com.example.coursemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursemanager.dao.UserDAO;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText etUser, etPwd;
    UserDAO userDAO;
    String state;
    CallbackManager callbackManager;
    LoginButton loginButton;
    SignInButton signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Code lay KeyHash chay 1 lan
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.example.coursemanager", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        setColorStatusBar();
        init();
        intentToRegister();
        intentToManager();
        loginWithFacebook();
        loginWithGoogle();
    }
    private void init() {
        etUser = findViewById(R.id.etUser);
        etPwd = findViewById(R.id.etPass);
        userDAO = new UserDAO(this);
        loginButton = findViewById(R.id.login_button);
        signInButton = findViewById(R.id.btn_signinGoogle);
    }

    private void intentToRegister() {
        findViewById(R.id.btnDangKy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void intentToManager() {
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUser.getText().toString();
                String pass = etPwd.getText().toString();
                if (username.length() == 0 || pass.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Không được bỏ trống tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    boolean chkAccount = userDAO.chkAccount(username, pass);
                    if (chkAccount) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, ManagerActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tên tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void setColorStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.color1));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        etUser.setText("");
        etPwd.setText("");
    }

    private void loginWithFacebook(){
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList( "public_profile", "email", "user_birthday", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                String userId=loginResult.getAccessToken().getUserId();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
//                                            String id=object.getString("id");
                                    String name=object.getString("name");
                                    String email=object.getString("email");
                                    String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    Intent intent=new Intent(LoginActivity.this,ManagerActivity.class);
//                                            intent.putExtra("id",id);
                                    intent.putExtra("name",name);
                                    intent.putExtra("email",email);
                                    intent.putExtra("image",profilePicUrl);
                                    startActivity(intent);
                                    finish();

                                    //do something with the data here
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,cover,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Loi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loginWithGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,0);
//                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 0){

                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);

        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // get info User
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this,ManagerActivity.class);
            i.putExtra("emailGoogle",personEmail);
            startActivity(i);
            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());

        }
    }
}