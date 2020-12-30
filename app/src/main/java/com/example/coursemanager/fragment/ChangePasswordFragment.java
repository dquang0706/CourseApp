package com.example.coursemanager.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coursemanager.ManagerActivity;
import com.example.coursemanager.R;
import com.example.coursemanager.dao.UserDAO;
import com.example.coursemanager.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {
    TextInputEditText etUser, etPwd, etPwdRe;
    Button btnChange;
    UserDAO daoUser;
    ArrayList<User> list = new ArrayList<>();

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        etUser = view.findViewById(R.id.etUserDoi);
        etPwd = view.findViewById(R.id.etPassDoi);
        etPwdRe = view.findViewById(R.id.etPassDoire);
        btnChange = view.findViewById(R.id.btnConfirmDoi);
        daoUser = new UserDAO(getActivity());
        changePassword();
        return view;
    }

    private void changePassword() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean xetTk = false, xetMk = true;
                String tk = etUser.getText().toString();
                String mk = etPwd.getText().toString();
                String mkk = etPwdRe.getText().toString();
                User tkmkMoi = new User(tk, mkk);
                list = daoUser.getALl();

                //Check tk, mk có khớp vs tk trong list k
                for (int i = 0; i < list.size(); i++) {
                    User tkx = list.get(i);
                    if (tkx.getUsername().matches(tk) && tkx.getPassword().matches(mk)) {
                        xetTk = true;
                        break;
                    }
                }

                //Check mk có trùng vs mk mới không
                if (mk.matches(mkk)) {
                    xetMk = false;
                } else {
                    xetMk = true;
                }

                //Đúng đk sẽ cho Đổi pass

                if (tk.isEmpty()) {
                    Toast.makeText(getActivity(), "Tên tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mk.isEmpty() || mkk.isEmpty()) {
                        Toast.makeText(getActivity(), "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (xetTk) {
                            if (xetMk) {
                                daoUser.changePassword(tkmkMoi);
                                Toast.makeText(getActivity(), "Đổi mật khẩu thành công! Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "Mật khẩu cũ không được trùng với mật khẩu mới!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Tên tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}