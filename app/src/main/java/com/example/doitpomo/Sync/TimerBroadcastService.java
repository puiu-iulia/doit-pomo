package com.example.doitpomo.Sync;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.doitpomo.Activities.DetailsPomoActivity;
import com.example.doitpomo.R;
import com.example.doitpomo.Utils.Notifications;
import com.example.doitpomo.Utils.PrefUtils;

public class TimerBroadcastService extends Service {

    public static final String COUNTDOWN_BROADCAST = "do_it_with_pomo_broadcast";
    private final static String TAG = "BroadcastService";

    Intent intent = new Intent(COUNTDOWN_BROADCAST);

    CountDownTimer workTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();

//        Log.i(TAG, "Starting timer...");

        int time = 0;
        final Context context = getApplicationContext();

        if (PrefUtils.getIsResumed(context)) {
            time = PrefUtils.getRemainingTime(context);
        } else {
            if (PrefUtils.getIsWorkModeOn(context)) {
                time = PrefUtils.getWorkTime(context);
            } else if (PrefUtils.getIsBreakModeOn(context)) {
                if (PrefUtils.getWorkSessions(context) == PrefUtils.getCurrentWorkSession(context)) {
                    time = PrefUtils.getLongBreakTime(context);
                    PrefUtils.setCurrentWorkSession(context, 0);
                } else {
                    time = PrefUtils.getBreakTime(context);
                }
            }
        }
//        Log.i(TAG, String.valueOf(time));

        workTimer = new CountDownTimer(time * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra("countdown", millisUntilFinished);
                Log.i(TAG, Long.toString(millisUntilFinished));
                sendBroadcast(intent);
                PrefUtils.setRemainingTime(getApplicationContext(), (int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                PrefUtils.setCurrentWorkSession(context, PrefUtils.getCurrentWorkSession(context)+ 1);
                stopSelf();
            }
        }.start();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (PrefUtils.getIsStopped(getApplicationContext() )) {
            workTimer.cancel();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
