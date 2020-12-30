package com.example.coursemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coursemanager.database.DbHelper;
import com.example.coursemanager.model.User;

import java.util.ArrayList;

public class UserDAO {
    private DbHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    public boolean insert(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", user.getUsername());
        values.put("passWord", user.getPassword());
        long ins = db.insert("User", null, values);
        if (ins == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean chkID(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from User where id=?", new String[]{id});
        if (cursor.getCount() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean chkAccount(String id, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from User where id=? and passWord=?", new String[]{id, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean changePassword(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("passWord", user.getPassword());
        int r = db.update("User", values, "id=?", new String[]{user.getUsername()});
        if (r <= 0) {
            return false;
        }
        return true;
    }
    public ArrayList<User> getALl() {
        ArrayList<User> listTK = new ArrayList<>();
        SQLiteDatabase dtb = dbHelper.getReadableDatabase();
        Cursor cs = dtb.rawQuery("select * from User", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            try {
                String tk = cs.getString(0);
                String mk = cs.getString(1);
                User user = new User(tk, mk);
                listTK.add(user);
                cs.moveToNext();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }

        cs.close();
        return listTK;
    }
}
