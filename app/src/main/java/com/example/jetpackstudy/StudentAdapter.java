package com.example.jetpackstudy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter {

    List<StudentEntity> students;

    public StudentAdapter(List<StudentEntity> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new MyViewHoder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StudentEntity student = students.get(position);
        TextView tvId = holder.itemView.findViewById(R.id.tvId);
        tvId.setText(String.valueOf(student.getId()));

        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(student.getName());

        TextView tvAge = holder.itemView.findViewById(R.id.tvAge);
        tvAge.setText(String.valueOf(student.getAge()));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class MyViewHoder extends RecyclerView.ViewHolder {

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setStudents(List<StudentEntity> students) {
        this.students = students;
    }
}
