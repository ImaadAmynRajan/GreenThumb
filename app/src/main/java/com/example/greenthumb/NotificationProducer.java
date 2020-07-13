package com.example.greenthumb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Map;

public class NotificationProducer extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        // retrieve tasks, then the function will check for overdue tasks
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
                // send our tasks to be added to our recycle viewer
                checkForOverdue(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Database Error","There was an error connecting to the database");
            }
        });
    }

    /**
     * This method cycles through all the tasks in the database searching for tasks
     * that match the current user. If it finds that the user has an overdue task, it will notify the user
     * with a push notification
     * Reference: https://stackoverflow.com/questions/32886546/how-to-get-all-child-list-from-firebase-android
     * @param dataSnapshot A snapshot of the tasks branch of the database
     */
    private void checkForOverdue(DataSnapshot dataSnapshot) {
        String userId = FirebaseAuth.getInstance().getUid();
        for (DataSnapshot dp: dataSnapshot.getChildren()) {
            Map<String, Object> info = (Map<String, Object>) dp.getValue();
            String assigneeLabel, assigneeId;
            Date date;
            boolean isFinished;

            // set all our values
            TaskTitle title = (TaskTitle) info.get("title");
            String id = (String) dp.getKey();

            if (info.get("dueDate") != null) {
                date = new Date((Long) info.get("dueDate"));
            } else {
                date = null;
            }

            if (info.get("assigneeId") != null) {
                assigneeId = (String) info.get("assigneeId");
            } else {
                assigneeId = null;
            }

            if (info.get("assigneeLabel") != null) {
                assigneeLabel = (String) info.get("assigneeLabel");
            } else {
                assigneeLabel = null;
            }

            if (info.get("finished") != null) {
                isFinished = (boolean) info.get("finished");
            } else {
                isFinished = false;
            }

            Task task = new Task(id, title, date, new User(assigneeId, assigneeLabel), isFinished);

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
    }
}