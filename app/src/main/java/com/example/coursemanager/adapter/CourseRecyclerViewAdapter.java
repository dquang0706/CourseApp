package com.example.coursemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanager.R;
import com.example.coursemanager.model.Course;

import java.text.SimpleDateFormat;
import java.util.List;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder> {
    @NonNull
    private List<Course> list;
    private static onCallBack mListener;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public CourseRecyclerViewAdapter(List<Course> list,onCallBack mListener){
        this.list = list;
        this.mListener = mListener;
    }
    @Override
    public CourseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_one_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.txtName.setText(list.get(position).getName());
        holder.txtStartDay.setText(simpleDateFormat.format(list.get(position).getStartDay()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtStartDay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCourseName);
            txtStartDay = itemView.findViewById(R.id.tvStartDayItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClickListener(getPosition());
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public interface onCallBack{
        void onItemClickListener(int position);
    }
}
