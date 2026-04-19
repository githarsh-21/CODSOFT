package com.example.todoapp;

import android.graphics.Paint;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onDelete(int position);
        void onToggle(int position, boolean checked);
        void onEdit(int position);
    }

    public TaskAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvTitle, tvDesc, tvDelete;

        public TaskViewHolder(@NonNull View v) {
            super(v);
            checkBox = v.findViewById(R.id.checkBox);
            tvTitle  = v.findViewById(R.id.tvTaskTitle);
            tvDesc   = v.findViewById(R.id.tvTaskDesc);
            tvDelete = v.findViewById(R.id.tvDelete);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvDesc.setText(task.getDescription());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isCompleted());

        if (task.isCompleted()) {
            holder.tvTitle.setPaintFlags(
                    holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTitle.setPaintFlags(
                    holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.checkBox.setOnCheckedChangeListener((b, isChecked) ->
                listener.onToggle(holder.getAdapterPosition(), isChecked));

        holder.tvDelete.setOnClickListener(v ->
                listener.onDelete(holder.getAdapterPosition()));

        holder.itemView.setOnLongClickListener(v -> {
            listener.onEdit(holder.getAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}