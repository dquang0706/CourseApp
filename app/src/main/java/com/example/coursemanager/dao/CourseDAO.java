package com.example.coursemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coursemanager.database.DbHelper;
import com.example.coursemanager.model.Course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CourseDAO {
    private DbHelper dbHelper;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public CourseDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    public boolean insert(Course course) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", course.getName());
        values.put("startDay", simpleDateFormat.format(course.getStartDay()));
        values.put("tutorName", course.getTutorName());
        long ins = db.insert("Course", null, values);
        if (ins <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update(Course course) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", course.getName());
        values.put("startDay", simpleDateFormat.format(course.getStartDay()));
        values.put("tutorName", course.getTutorName());
        long ins = db.update("Course", values, "courseCode=?", new String[]{String.valueOf(course.getCourseCode())});
        if (ins <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean delete(Course course) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long chkResult = db.delete("Course", "courseCode=?", new String[]{String.valueOf(course.getCourseCode())});
        if (chkResult <= 0) {
            return false;
        }
        return true;
    }

    public ArrayList<Course> getCourse(String sql, String... a) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Course> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, a);
        cursor.moveToFirst(); // đưa con trỏ về dòng đầu tiên
        while (!cursor.isAfterLast()) {
            // duyệt từng dòng
            try {
                int courseCode = cursor.getInt(0);
                String name = cursor.getString(1);
                String startDay = cursor.getString(2);
                String tutorName = cursor.getString(3);
                list.add(new Course(courseCode, name, simpleDateFormat.parse(startDay), tutorName));
                cursor.moveToNext();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cursor.close();
        return list;
    }

    public ArrayList<Course> getAllCourse() {
        String sql = "select * from Course";
        return getCourse(sql);
    }
}
