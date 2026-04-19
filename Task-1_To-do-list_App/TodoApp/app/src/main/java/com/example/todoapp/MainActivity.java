package com.example.todoapp;

import android.content.*;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.*;

public class MainActivity extends AppCompatActivity
        implements TaskAdapter.OnTaskActionListener {

    private List<Task> taskList = new ArrayList<>();
    private TaskAdapter adapter;
    private SharedPreferences prefs;
    private Gson gson = new Gson();
    private static final String KEY_TASKS = "tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("TodoPrefs", MODE_PRIVATE);
        loadTasks();

        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(taskList, this);
        rv.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.btnAddTask);
        btnAdd.setOnClickListener(v -> showAddDialog(false, -1));
    }

    private void showAddDialog(boolean isEdit, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Edit Task" : "Add New Task");

        View view = LayoutInflater.from(this)
                .inflate(android.R.layout.activity_list_item, null);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 16, 48, 16);

        final EditText etTitle = new EditText(this);
        etTitle.setHint("Task title (required)");
        layout.addView(etTitle);

        final EditText etDesc = new EditText(this);
        etDesc.setHint("Description (optional)");
        layout.addView(etDesc);

        if (isEdit) {
            Task t = taskList.get(position);
            etTitle.setText(t.getTitle());
            etDesc.setText(t.getDescription());
        }

        builder.setView(layout);
        builder.setPositiveButton(isEdit ? "Update" : "Add", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String desc  = etDesc.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isEdit) {
                taskList.get(position).setTitle(title);
                taskList.get(position).setDescription(desc);
                adapter.notifyItemChanged(position);
            } else {
                taskList.add(new Task(title, desc));
                adapter.notifyItemInserted(taskList.size() - 1);
            }
            saveTasks();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onDelete(int position) {
        taskList.remove(position);
        adapter.notifyItemRemoved(position);
        saveTasks();
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToggle(int position, boolean checked) {
        taskList.get(position).setCompleted(checked);
        adapter.notifyItemChanged(position);
        saveTasks();
    }

    @Override
    public void onEdit(int position) {
        showAddDialog(true, position);
    }

    private void saveTasks() {
        prefs.edit().putString(KEY_TASKS, gson.toJson(taskList)).apply();
    }

    private void loadTasks() {
        String json = prefs.getString(KEY_TASKS, null);
        if (json != null) {
            Type type = new TypeToken<List<Task>>(){}.getType();
            taskList = gson.fromJson(json, type);
        }
    }
}