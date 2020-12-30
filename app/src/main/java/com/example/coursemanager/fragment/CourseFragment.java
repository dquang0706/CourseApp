package com.example.coursemanager.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursemanager.R;
import com.example.coursemanager.adapter.CourseRecyclerViewAdapter;
import com.example.coursemanager.dao.CourseDAO;
import com.example.coursemanager.model.Course;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment implements CourseRecyclerViewAdapter.onCallBack {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private RecyclerView rcvCourse;
    private FloatingActionButton fbAddCourse;
    private CourseDAO courseDAO;
    private static List<Course> list = new ArrayList<>();
    private CourseRecyclerViewAdapter adapter;

    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        rcvCourse = view.findViewById(R.id.rcvCourse);
        fbAddCourse = view.findViewById(R.id.fbAddCourse);
        courseDAO = new CourseDAO(getActivity());


        // Thực hiện các chức năng
        fillDataToRecyclerView();
        addCourse();

        return view;
    }

    private void fillDataToRecyclerView() {
        list = courseDAO.getAllCourse();
        adapter = new CourseRecyclerViewAdapter(list, this);
        rcvCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvCourse.hasFixedSize();
        rcvCourse.setAdapter(adapter);
    }

    private void addCourse() {
        fbAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.add_course);
                final EditText etNameCourse = dialog.findViewById(R.id.etNameCourse);
                final TextView tvStartDay = dialog.findViewById(R.id.tvStartDay);
                final EditText etNameTutor = dialog.findViewById(R.id.etNameTutor);
                tvStartDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int d = calendar.get(Calendar.DAY_OF_MONTH);
                        int m = calendar.get(Calendar.MONTH);
                        int y = calendar.get(Calendar.YEAR);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final String startDay = dayOfMonth + "/" + (month + 1) + "/" + year;
                                tvStartDay.setText(startDay);
                            }
                        }, y, m, d);
                        datePickerDialog.show();
                    }
                });
                dialog.findViewById(R.id.btnConfirmAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nameCourse = etNameCourse.getText().toString().trim();
                        String startDay = tvStartDay.getText().toString();
                        String etNameTutorStr = etNameTutor.getText().toString().trim();
                        if (nameCourse.isEmpty() && startDay.isEmpty() && etNameTutorStr.isEmpty()) {
                            Toast.makeText(getActivity(), "Các trường không được để trống", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Course course = new Course(0, nameCourse, dfm.parse(startDay), etNameTutorStr);
                                if (courseDAO.insert(course)) {
                                    list.clear();
                                    list.addAll(courseDAO.getAllCourse());
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Thêm khoá học thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Thêm khoá học thất bại", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                dialog.findViewById(R.id.btnAddRe).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etNameCourse.setText("");
                        tvStartDay.setText("");
                        etNameTutor.setText("");
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onItemClickListener(final int position) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                getActivity(), R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(
                R.layout.item_click,
                (LinearLayout) bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
        );
        // Xem chi tiết khoá học
        bottomSheetView.findViewById(R.id.txt_XemChiTiet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getActivity(), R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.dialog_detail_course,
                        (RelativeLayout) bottomSheetDialog.findViewById(R.id.realative_chitiet)
                );
                TextView tvCourseName = bottomSheetView.findViewById(R.id.txt_TenKhoaHocChitiet);
                TextView tvTutorName = bottomSheetView.findViewById(R.id.txt_GiangVienChiTiet);
                TextView tvStartDay = bottomSheetView.findViewById(R.id.txt_NgayBatDauChitiet);
                Course course = list.get(position);
                tvCourseName.setText(course.getName());
                tvTutorName.setText(course.getTutorName());
                tvStartDay.setText(dfm.format(course.getStartDay()));
                bottomSheetView.findViewById(R.id.btn_HuyChiTiet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        // Edit khoá học
        bottomSheetView.findViewById(R.id.txt_SuaKhoahoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.edit_course);
                final EditText etNameCourseEdit = dialog.findViewById(R.id.etNameCourseEdit);
                final TextView tvStartDayEdit = dialog.findViewById(R.id.tvStartDayEdit);
                final EditText etNameTutorEdit = dialog.findViewById(R.id.etNameTutorEdit);
                final Button btnConfirmEdit = dialog.findViewById(R.id.btnConfirmEdit);
                final Course course = list.get(position);
                etNameCourseEdit.setText(course.getName());
                tvStartDayEdit.setText(dfm.format(course.getStartDay()));
                etNameTutorEdit.setText(course.getTutorName());
                tvStartDayEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int d = calendar.get(Calendar.DAY_OF_MONTH);
                        int m = calendar.get(Calendar.MONTH);
                        int y = calendar.get(Calendar.YEAR);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final String startDay = dayOfMonth + "/" + (month+1) + "/" + year;
                                tvStartDayEdit.setText(startDay);
                            }
                        }, y, m, d);
                        datePickerDialog.show();
                    }
                });
                btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String courseName = etNameCourseEdit.getText().toString().trim();
                        String startDay = tvStartDayEdit.getText().toString();
                        String tutorName = etNameTutorEdit.getText().toString().trim();
                        if (courseName.isEmpty() && startDay.isEmpty() && tutorName.isEmpty()) {
                            Toast.makeText(getActivity(), "Không được bỏ trống các trường", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Course course1 = new Course(list.get(position).getCourseCode(), courseName, dfm.parse(startDay), tutorName);
                                if (courseDAO.update(course1)) {
                                    list.clear();
                                    list.addAll(courseDAO.getAllCourse());
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Sửa khoá học thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Sửa thất bại", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                dialog.findViewById(R.id.btnExitEdit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        bottomSheetView.findViewById(R.id.txt_XoaKhoaHoc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Bạn có chắc chắn muốn xóa khoá học?")
                        .setCancelable(false)
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (courseDAO.delete(list.get(position))) {
                                    list.clear();
                                    list.addAll(courseDAO.getAllCourse());
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        bottomSheetView.findViewById(R.id.txt_Huy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}