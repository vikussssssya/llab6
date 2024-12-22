package com.example.myapplication6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private List<Reminder> reminderList;
    private OnReminderDeleteListener deleteListener;

    public ReminderAdapter(List<Reminder> reminderList, OnReminderDeleteListener deleteListener) {
        this.reminderList = reminderList;
        this.deleteListener = deleteListener;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);

        holder.titleTextView.setText(reminder.getTitle());

        // Форматируем дату и время
        String formattedDate = formatDateTime(reminder.getDatetime());
        holder.dateTimeTextView.setText(formattedDate);

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onReminderDelete(reminder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    private String formatDateTime(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        try {
            Date date = sdf.parse(datetime);  // Преобразуем строку в объект Date
            if (date != null) {
                return sdf.format(date);  // Преобразуем объект Date обратно в строку
            }
        } catch (ParseException e) {
            e.printStackTrace();  // Обрабатываем исключение, если строка не соответствует формату
        }
        return datetime;  // Если произошла ошибка, возвращаем исходную строку
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView dateTimeTextView;
        public ImageButton deleteButton;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvReminderTitle);
            dateTimeTextView = itemView.findViewById(R.id.tvReminderDateTime);
            deleteButton = itemView.findViewById(R.id.btnDeleteReminder);
        }
    }

    public interface OnReminderDeleteListener {
        void onReminderDelete(Reminder reminder);
    }
}
