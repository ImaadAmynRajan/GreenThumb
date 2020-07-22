package com.example.greenthumb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.greenthumb.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class DatabaseService extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        // retrieve tasks, then the function will check for overdue tasks or recurring tasks
        getTasks();
    }

    /**
     * This will build and send a notification to the user for an overdue task
     * @param content the text inside the notification
     */
    public void sendNotification(String content) {
        // first setup the intent that we want to be taken to when the notification is clicked
        Intent intent = new Intent(context, ViewTasks.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // now we build the notification
        // referenced https://stackoverflow.com/questions/43038361/how-to-make-notification-clickable/43038389
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, NOTIFICATION_ID)
                .setContentTitle("You have overdue tasks!")
                .setContentText(content)
                .setSmallIcon(R.drawable.heavy_check_mark)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(100, notification.build());
    }

    /**
     * This function retrieves all the tasks from the data base.
     * Once it takes a snapshot of the database,
     * it sends them to the checkForOverdue function where they are scanned for overdue tasks
     * Reference: https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     */
    private void getTasks() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tasks");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Task task = item.getValue(Task.class);
                    checkForOverdue(task);
                    checkForRecurringTask(task);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Database Error","There was an error connecting to the database");
            }
        });
    }

    /**
     * Checks whether a task is overdue. If so, notify the user with a push notification
     * Reference: https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     * @param task task to check for being overdue
     */
    private void checkForOverdue(Task task) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            return;
        }
        // check if the task isn't finished, and it belongs to the current user, and is overdue
        if (
                userId.equals(task.getAssigneeId()) &&
                        task.isOverdue()
        ) {
            // build the content of the notification on the title of the task
            String content = task.getTitle() + " is overdue!";
            sendNotification(content);
        }
    }

    /**
     * This will check for recurring tasks and change the due date to the interval if needed
     * @param task current task we are checking
     */
    private void checkForRecurringTask(Task task) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            return;
        }
        // time at midnight of current day
        // referenced: https://stackoverflow.com/questions/38754490/get-current-day-in-milliseconds-in-java#:~:text=long%20time%20%3D%20System.,millisOfDay().
        Calendar cal = Calendar.getInstance();
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date  = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        long curDate = cal.getTimeInMillis();

        // if the task belongs to the current user, the due date is today, and its a recurring task
        // we change the due date to the set interval

        if (userId.equals(task.getAssigneeId()) && task.getDueDate() != null && task.getDueDate() == curDate && task.getInterval() != -1) {
            // 1 day in milliseconds
            long day = 1000 * 60 * 60 * 24;
            // new date sets to the current date + the interval in milliseconds (interval * day)
            long nextDate = (new Date(task.getDueDate() + task.getInterval() * day).getTime());
            task.setDueDate(nextDate);

            // update the database
            DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference("tasks");
            if (task.getId() == null) task.setId(firebaseReference.push().getKey());
            firebaseReference.child(task.getId()).setValue(task);
        }
    }
}