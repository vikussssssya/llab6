package com.example.myapplication6;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddReminderActivity extends AppCompatActivity {

    private EditText etTitle, etExecutor, etDateTime;
    private MyDbHelper dbHelper;
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        etTitle = findViewById(R.id.etTitle);
        etExecutor = findViewById(R.id.etExecutor);
        etDateTime = findViewById(R.id.etDateTime);
        Button btnSave = findViewById(R.id.btnSave);

        dbHelper = new MyDbHelper(this);
        selectedDateTime = Calendar.getInstance();

        etDateTime.setOnClickListener(v -> showDateTimePickerDialog());
        btnSave.setOnClickListener(v -> saveReminder());
    }

    private void showDateTimePickerDialog() {
        int year = selectedDateTime.get(Calendar.YEAR);
        int month = selectedDateTime.get(Calendar.MONTH);
        int dayOfMonth = selectedDateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth1) -> {
            selectedDateTime.set(year1, month1, dayOfMonth1);

            int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
            int minute = selectedDateTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute1) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute1);
                etDateTime.setText(formatDateTime(selectedDateTime));
            }, hour, minute, true);

            timePickerDialog.show();
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private String formatDateTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private void saveReminder() {
        String title = etTitle.getText().toString().trim();
        String executor = etExecutor.getText().toString().trim();
        String datetime = etDateTime.getText().toString().trim();

        if (title.isEmpty() || executor.isEmpty() || datetime.isEmpty()) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.insertReminder(new Reminder(0, title, executor, datetime));
        setReminderNotification(title, datetime);

        Toast.makeText(this, "Reminder added", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setReminderNotification(String title, String datetime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date date = sdf.parse(datetime);

            Intent intent = new Intent(this, ReminderNotificationReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("datetime", datetime);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    (int) System.currentTimeMillis(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            }

            Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show();
            finish();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in date/time format", Toast.LENGTH_SHORT).show();
        }
    }
}
