package com.example.coursemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursemanager.fragment.AboutFragment;
import com.example.coursemanager.fragment.ChangePasswordFragment;
import com.example.coursemanager.fragment.CourseFragment;
import com.example.coursemanager.fragment.MapsFragment;
import com.example.coursemanager.fragment.NewsFragment;
import com.example.coursemanager.rss.PicassoClient;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class ManagerActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private View view;
    //    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    private TextView tvUser, tvEmail;
    private ImageView ivAva;
//    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build();
//    // Build a GoogleSignInClient with the options specified by gso.
//    final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    //private GoogleSignIn mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        setColorStatusBar();
        init();
        setToolbarToActionBar();
        setDrawerLayout(savedInstanceState);
        isPermissionGranted();
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbars);

    }

    // Gắn toolbar
    private void setToolbarToActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setDrawerLayout(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            AboutFragment aboutFragment = new AboutFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.frame, aboutFragment)
                    .commit();
        }
        NavigationView navigationView = findViewById(R.id.navView);
        view = navigationView.inflateHeaderView(R.layout.nav_header);
        tvUser = view.findViewById(R.id.txt_User);
        tvEmail = view.findViewById(R.id.txtEmailheader);
        ivAva = view.findViewById(R.id.ivAva);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String imageUrl = intent.getStringExtra("image");
        String emailGoogle = intent.getStringExtra("emailGoogle");
        if (name == null) {
            tvUser.setText("Đồng Phương Quang");
        } else {
            tvUser.setText(name);
            tvEmail.setText(email);
            Picasso.get().load(imageUrl).into(ivAva);
        }
        if (email == null) {
            tvEmail.setText(email);
        }
        tvEmail.setText(emailGoogle);
        //Thực hiện các chức năng cho menu, khi click vào icon menu sẽ show ra
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                String title = "";
                Fragment fragment;
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (menuItem.getItemId()) {
                    case R.id.khoaHoc:
                        title = "KHOÁ HỌC";
                        fragment = new CourseFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        break;
                    case R.id.gioiThieu:
                        title = "GIỚI THIỆU";
                        fragment = new AboutFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        break;
                    case R.id.doimatkhau:
                        title = "ĐỔI MẬT KHẨU";
                        fragment = new ChangePasswordFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        break;
                    case R.id.tintuc:
                        title = "TIN TỨC GIÁO DỤC";
                        fragment = new NewsFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        break;
                    case R.id.ggMap:
                        title = "BẢN ĐỒ";
                        fragment = new MapsFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                        break;
                    case R.id.thoat:
                        if (AccessToken.getCurrentAccessToken() == null) {
                            Toast.makeText(ManagerActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                        }

                        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                                .Callback() {
                            @Override
                            public void onCompleted(GraphResponse graphResponse) {

                                LoginManager.getInstance().logOut();

                            }
                        }).executeAsync();
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();

                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(ManagerActivity.this, gso);
                        googleSignInClient.signOut();
//                        mGoogleSignInClient.signOut();
                        ManagerActivity.this.finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle(title);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setColorStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(ManagerActivity.this, R.color.color1));
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 2: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    //do your specific task after read phone state granted
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}