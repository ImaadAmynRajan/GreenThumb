package com.example.greenthumb;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class NotificationProducer extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        getTasks();
        System.out.println("Checking for notification");
    }

    public void sendNotification(Context context) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, NOTIFICATION_ID)
                .setContentTitle("You have overdue tasks")
                .setContentText("Click here to see your tasks")
                .setSmallIcon(R.drawable.heavy_check_mark)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(100, notification.build());
    }

    /**
     * This function retrieves all the tasks from the data base.
     * Once it takes a snapshot of the database,
     * it  sends them to the collectTasks function where they are turned into task objects
     * Reference: https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     */
    private void getTasks() {
    }

    /**
     * This method cycles through all the tasks in the database searching for tasks
     * that match the current user. If it finds that the user has an overdue task, it will notify the user
     * with a push notification
     * Reference: https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     * @param dataSnapshot A snapshot of the tasks branch of the database
     */
    private void collectTasks(DataSnapshot dataSnapshot) {
    }
}