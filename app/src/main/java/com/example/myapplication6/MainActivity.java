package com.example.myapplication6;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvReminders;
    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminderList;
    private MyDbHelper dbHelper;

    private ActivityResultLauncher<String> requestNotificationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvReminders = findViewById(R.id.rvReminders);
        Button btnAddReminder = findViewById(R.id.btnAddReminder);

        dbHelper = new MyDbHelper(this);
        reminderList = dbHelper.getAllReminders();

        reminderAdapter = new ReminderAdapter(reminderList, reminder -> {
            dbHelper.deleteReminder(reminder.getId());
            reminderList.remove(reminder);
            reminderAdapter.notifyDataSetChanged();
        });

        rvReminders.setLayoutManager(new LinearLayoutManager(this));
        rvReminders.setAdapter(reminderAdapter);

        btnAddReminder.setOnClickListener(v -> openAddReminderActivity());

        requestNotificationPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show();
                    }
                });

        checkNotificationPermission();
    }

    private void openAddReminderActivity() {
        Intent intent = new Intent(this, AddReminderActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reminderList.clear();
        reminderList.addAll(dbHelper.getAllReminders());
        reminderAdapter.notifyDataSetChanged();
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}
