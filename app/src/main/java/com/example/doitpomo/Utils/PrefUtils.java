package com.example.doitpomo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class PrefUtils {

    public synchronized static void setWorkTime(Context context, int workTime) {

        SharedPreferences mPrefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt("Work", workTime);
        editor.apply();
        Log.d("Work", String.valueOf(workTime));
    }

    public static int getWorkTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int workTime = prefs.getInt("Work", 1500);
        return workTime;
    }

    public synchronized static void setBreakTime(Context context, int breakTime) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Break", breakTime);
        editor.apply();
    }

    public static int getBreakTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int workTime = prefs.getInt("Break", 300);
        return workTime;
    }

    public synchronized static void setLongBreakTime(Context context, int longBreakTime) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Long", longBreakTime);
        editor.apply();
    }

    public static int getLongBreakTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int workTime = prefs.getInt("Long", 900);
        return workTime;
    }


    public synchronized static void setWorkSessions(Context context, int workSessions) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Sessions", workSessions);
        editor.apply();
    }

    public static int getWorkSessions(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int workSessions = prefs.getInt("Sessions", 4);
        return workSessions;
    }

    public synchronized static void setCurrentWorkSession(Context context, int currentworkSession) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Current", currentworkSession);
        editor.apply();
    }

    public static int getCurrentWorkSession(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int currentworkSession = prefs.getInt("Current", 1);
        return currentworkSession;
    }


    public synchronized static void setIsWorkModeOn(Context context, boolean isWorkModeOn) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("workTimerRunning", isWorkModeOn);
        editor.apply();
    }

    public static boolean getIsWorkModeOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        Boolean isWorkModeOn = prefs.getBoolean("workTimerRunning", false);
        return isWorkModeOn;
    }


    public synchronized static void setIsResumed(Context context, boolean isResumed) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isResumed", isResumed);
        editor.apply();
    }

    public static boolean getIsResumed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        Boolean isResumed = prefs.getBoolean("isResumed", false);
        return isResumed;
    }

    public synchronized static void setIsBreakModeOn(Context context, boolean isBreakModeOn) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("breakTimerRunning", isBreakModeOn);
        editor.apply();
    }

    public static boolean getIsBreakModeOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        Boolean isBreakModeOn = prefs.getBoolean("breakTimerRunning", false);
        return isBreakModeOn;
    }


    public synchronized static void setRemaningTime(Context context, int endTimeWork) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("endTimeWork", endTimeWork);
        editor.apply();
    }

    public static int getRemindingTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int endTimeWork = prefs.getInt("endTimeWork", 0);
        return endTimeWork;
    }


    public synchronized static void setEndTimeBreak(Context context, long endTimeBreak) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("endTimeBreak", endTimeBreak);
        editor.apply();
    }

    public static long getEndTimeBreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        long endTimeBreak = prefs.getLong("endTimeBreak", 0);
        return endTimeBreak;
    }

    public static long getEndTimeLongBreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        long endTimeLongBreak = prefs.getLong("endTimeLongBreak", 0);
        return endTimeLongBreak;
    }

    public synchronized static void setEndTimeLongBreak(Context context, long endTimeLongBreak) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("endTimeLongBreak", endTimeLongBreak);
        editor.apply();
    }

    public static long getTotalWork(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        long totalWork = prefs.getLong("totalWork", PrefUtils.getWorkTime(context) * 1000);
        return totalWork;
    }

    public synchronized static void setTotalWork(Context context, long totalWork) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("totalWork", totalWork);
        editor.apply();
    }


    public static long getTotalBreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        long totalBreak = prefs.getLong("totalBreak", PrefUtils.getBreakTime(context) * 1000);
        return totalBreak;
    }

    public synchronized static void setTotalBreak(Context context, long totalBreak) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("totalBreak", totalBreak);
        editor.apply();
    }

    public static long getTotalLongBreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        long totalLongBreak = prefs.getLong("totalLongBreak", PrefUtils.getLongBreakTime(context) * 1000);
        return totalLongBreak;
    }

    public synchronized static void setTotalLongBreak(Context context, long totalLongBreak) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("totalLongBreak", totalLongBreak);
        editor.apply();
    }


//    synchronized public void work(Context context) {
//        int workSessions = PrefUtils.getWorkSessions(context);
//        PrefUtils.setEndTimeWork(context, PrefUtils.getEndTimeWork(context));
//    }

    synchronized public void takeAbreak(Context context) {

    }

}
