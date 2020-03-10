package com.example.doitpomo.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.doitpomo.Activities.DetailsPomoActivity;
import com.example.doitpomo.R;


public class Notifications {

    private static final String FINISHED_TIMER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static final int TIMER_REMINDER_NOTIFICATION_ID = 1473;
    private static String textContent;

//    public static void clearAllNotifications(Context context) {
//        NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();
//    }

    public static void remindUserTimerFinished(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    FINISHED_TIMER_NOTIFICATION_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(context, DetailsPomoActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        if (Prefs.getIsWorkModeOn(context)) {
            textContent = "Take a break!";
        } else if (Prefs.getIsBreakModeOn(context)) {
            textContent = "Get back to work!";
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, FINISHED_TIMER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.pomodorodetails)
                .setContentTitle("Time is up.")
                .setContentText(textContent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setSound(Uri.parse("android.resource://"
//                        + context.getPackageName() + "/" + R.raw.sound))
                .setContentIntent(pendingIntent)
//                .addAction(takeABreakAction(context))
//                .addAction(ignoreReminderAction(context))
                .setAutoCancel(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(TIMER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

//    private static NotificationCompat.Action ignoreReminderAction(Context context) {
//        Intent ignoreReminderIntent = new Intent(context, TimerIntentService.class);
//        ignoreReminderIntent.setAction(TimerTasks.ACTION_DISMISS_NOTIFICATION);
//        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
//                context,
//                ACTION_BREAK_PENDING_INTENT_ID,
//                ignoreReminderIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.breakbutton,
//                "No, thanks.",
//                ignoreReminderPendingIntent);
//        return ignoreReminderAction;
//    }
//
//    private static NotificationCompat.Action takeABreakAction(Context context) {
//        Intent takeABreak = new Intent(context, TimerIntentService.class);
//        takeABreak.setAction(TimerTasks.ACTION_BREAK);
//        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
//                context,
//                ACTION_WORK_PENDING_INTENT_ID,
//                takeABreak,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        NotificationCompat.Action drinkWaterAction = new NotificationCompat.Action(R.drawable.pomobutton,
//                "I did it!",
//                incrementWaterPendingIntent);
//        return drinkWaterAction;
//    }

//    private static PendingIntent contentIntent(Context context) {
//        Intent startActivityIntent = new Intent(context, DetailsPomoActivity.class);
//        return PendingIntent.getActivity(
//                context,
//                PENDING_INTENT_ID,
//                startActivityIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT)
//    }
}
