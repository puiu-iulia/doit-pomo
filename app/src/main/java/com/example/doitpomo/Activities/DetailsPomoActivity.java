package com.example.doitpomo.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doitpomo.Data.DatabaseHandler;
import com.example.doitpomo.Model.TodoItem;
import com.example.doitpomo.R;
import com.example.doitpomo.Sync.TimerBroadcastService;
import com.example.doitpomo.Utils.Constants;
import com.example.doitpomo.Utils.Notifications;
import com.example.doitpomo.Utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsPomoActivity extends AppCompatActivity {

    private CheckedTextView itemName;
    public TextView dateAdded, timeSpent, dateFinish;
    public int itemId;
    private DatabaseHandler db;
    TextView timerTextView, workTimeStart, workTimeStop, breakTimeStart, breakTimeStop, breakTimeTextView, workTimeTextView, longBreakTextView, workSessionsTextView, descriptionTextView, priorityTextView, editPopupTextDate;
    EditText todoEditPopup, categroyEditPopup;
    SeekBar timerSeekbar, breakSeekbar, longBreakSeekbar, workSessionsSeekbar;
    Boolean workModeIsOn = false;
    Boolean breakModeIsOn = false;
    Button startButton, pauseButton, playButton, breakButton, stopButton, editButton,
            savaDateEditPopup, saveEditButton, todoItemDateFinishEditButton, settingsTimerButton,
            saveSettingsButton, checkboxButton, deleteButtonDetails;
    public RelativeLayout popupEditLayout, pomoLayout, calendarLayout;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Spinner spinner;
    CountDownTimer workCountDownTimer, breakCountDownTimer;
    private DatePicker toDoDateFinishEdit;
    

    private CardView seekbarCardView;

    private int workTime, breakTime, totalWorkOnTask, longBreakTime, workSessionsNumber, currentTimeSession;
    private long totalWork, totalBreak, endTimeWork, endTimeBreak, totalLongBreak, endTimeLongBreak;
    private String priority, itemFinishDAte;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_pomo);

        itemName = findViewById(R.id.itemNameDetails);
        dateAdded = findViewById(R.id.dateAddedDetails);
        timerSeekbar = findViewById(R.id.timerSeekbar);
        breakSeekbar = findViewById(R.id.breakSeekbar);
        timerTextView = findViewById(R.id.countdownChooseTime);
        startButton = findViewById(R.id.goButton);
        pauseButton = findViewById(R.id.pauseButton);
        playButton = findViewById(R.id.playButton);
        breakButton = findViewById(R.id.breakButton);
        workTimeStart = findViewById(R.id.workTimeStart);
        workTimeStop = findViewById(R.id.workTimeStop);
        breakTimeStart = findViewById(R.id.breakTimeStart);
        breakTimeStop = findViewById(R.id.breakTimeStop);
        breakTimeTextView = findViewById(R.id.breakTimeTextView);
        longBreakTextView = findViewById(R.id.longBreakTimeTextView);
        workSessionsTextView = findViewById(R.id.workSessionsTextView);
        workTimeTextView = findViewById(R.id.workTimeTextView);
        timeSpent = findViewById(R.id.timeSpentOnProjectDetails);
        dateFinish = findViewById(R.id.dateFinishDetails);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        editButton = findViewById(R.id.editButtonDetails);
        checkboxButton = findViewById(R.id.checkbox);
        stopButton = findViewById(R.id.stopButton);
        settingsTimerButton = findViewById(R.id.settingsButton);
        seekbarCardView = findViewById(R.id.seekBarCardView);
        pomoLayout = findViewById(R.id.pomodoroLayout);
        saveSettingsButton = findViewById(R.id.saveSettinsButton);
        priorityTextView = findViewById(R.id.priorityDetails);
        deleteButtonDetails = findViewById(R.id.deleteButtonDetails);
        longBreakSeekbar = findViewById(R.id.longBreakSeekbar);
        workSessionsSeekbar = findViewById(R.id.workSessionsSeekbar);

        getItemId();
        updateData();


//        Bundle bundle = getIntent().getExtras();
//
//        db = new DatabaseHandler(this);
//
//        if (bundle!= null) {
//            itemName.setText(bundle.getString("name"));
//            dateAdded.setText("Start:        " + bundle.getString("date"));
//
//            itemId = bundle.getInt("id");
//            TodoItem todoItem = db.getTodoItem(itemId);
//
////            priorityTextView.setText("Priority:    " + bundle.getString("priority"));
//            priorityTextView.setText("Priority:    " + todoItem.getPriority());
//            if (todoItem.getTimeSpent() != 0) {
//                timeSpent.setVisibility(View.VISIBLE);
//                if (todoItem.getTimeSpent()/60 < 60){
//                    timeSpent.setText("Time:        " + todoItem.getTimeSpent() / 60 + " min");
//                } else {
//                    timeSpent.setText("Time:        " + todoItem.getTimeSpent()/3600 + " h " + todoItem.getTimeSpent() % 3600 + " min");
//                }
//            }
//
//            if (todoItem.getDescription() == null || todoItem.getDescription() == "") {
//                descriptionTextView.setVisibility(View.GONE);
//            } else {
//                descriptionTextView.setVisibility(View.VISIBLE);
//                descriptionTextView.setText("Notes:      " + todoItem.getDescription());
//            }
//
//            dateFinish.setText("Finish:      " + todoItem.getFinishDate());
//        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsTimerButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);

                PrefUtils.setIsWorkModeOn(getApplicationContext(), true);
                PrefUtils.setIsBreakModeOn(getApplicationContext(), false);
                PrefUtils.setIsResumed(getApplicationContext(), false);

                stopService(new Intent(getApplicationContext(), TimerBroadcastService.class));
                startService(new Intent(getApplicationContext(), TimerBroadcastService.class));
                registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
            }
        });

        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsTimerButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                breakButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.VISIBLE);

                PrefUtils.setIsWorkModeOn(getApplicationContext(), false);
                PrefUtils.setIsBreakModeOn(getApplicationContext(), true);

                stopService(new Intent(getApplicationContext(), TimerBroadcastService.class));
                startService(new Intent(getApplicationContext(), TimerBroadcastService.class));
                registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsTimerButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                breakButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                stopService(new Intent(getApplicationContext(), TimerBroadcastService.class));
                updateTotalSpent();
                timerTextView.setText((workTime / 60) + ":00");
                timerSeekbar.setProgress(workTime / 60);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupEditDialog();
            }
        });

        deleteButtonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        checkboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDone(v);
            }
        });

        settingsTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarCardView.setVisibility(View.VISIBLE);
                pomoLayout.setVisibility(View.INVISIBLE);

            }
        });

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarCardView.setVisibility(View.GONE);
                pomoLayout.setVisibility(View.VISIBLE);
            }
        });


        timerSeekbar.setMax(90);
        timerSeekbar.setMin(1);
        breakSeekbar.setMax(20);
        breakSeekbar.setMin(1);
        longBreakSeekbar.setMin(1);
        longBreakSeekbar.setMax(30);
        workSessionsSeekbar.setMin(1);
        workSessionsSeekbar.setMax(10);

        workTime = PrefUtils.getWorkTime(getApplicationContext());
        Log.d("Work Time", String.valueOf(workTime));
        breakTime = PrefUtils.getBreakTime(getApplicationContext());
        longBreakTime = PrefUtils.getLongBreakTime(getApplicationContext());
        workSessionsNumber = PrefUtils.getLongBreakTime(getApplicationContext());

        timerSeekbar.setProgress(workTime / 60);
        breakSeekbar.setProgress(breakTime / 60);
        longBreakSeekbar.setProgress(longBreakTime / 60);
        workSessionsSeekbar.setProgress(workSessionsNumber);
        workTimeTextView.setText("Work Time: " + workTime / 60 +" min");
        breakTimeTextView.setText("Break Time: " + breakTime / 60 +" min");
        longBreakTextView.setText("Long Break Time: " + longBreakTime / 60 + " min");
        workSessionsTextView.setText("Work Sessions before Long Break: " + workSessionsNumber);


//        updateTimer(workTime);
        timerTextView.setText(workTime / 60 + ":00");


        timerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    workTime = progress * 60;
//                    updateTimer(workTime);
                    timerTextView.setText(workTime / 60 + ":00");
                    workTimeTextView.setText("Work Time: " + progress +" min");
                    PrefUtils.setWorkTime(getApplicationContext(), workTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        breakSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    breakTime = progress * 60;
                    breakTimeTextView.setText("Break Time: " + progress +" min");
                    PrefUtils.setBreakTime(getApplicationContext(), breakTime);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        longBreakSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    longBreakTime = progress * 60;
                    longBreakTextView.setText("Long Break Time: " + progress + " min");
                    PrefUtils.setLongBreakTime(getApplicationContext(), longBreakTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        workSessionsSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    workSessionsNumber = progress;
                    workSessionsTextView.setText("Work Sessions before Long Break: " + progress);
                    PrefUtils.setWorkSessions(getApplicationContext(), workSessionsNumber);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                pauseTimer();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeTimer();
                pauseButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                breakButton.setVisibility(View.VISIBLE);
            }
        });


    }

    public void getItemId() {
        Bundle bundle = getIntent().getExtras();

        if (bundle!= null) {

            itemId = bundle.getInt("id");
            PrefUtils.setItemId(getApplicationContext(), itemId);

        }
    }

    public void updateData() {
        int id = PrefUtils.getItemId(getApplicationContext());

        db = new DatabaseHandler(this);

        TodoItem todoItem = db.getTodoItem(id);

        itemName.setText(todoItem.getName());
        dateAdded.setText("Start:        " + todoItem.getFinishDate());
        priorityTextView.setText("Priority:    " + todoItem.getPriority());
        if (todoItem.getTimeSpent() != 0) {
            timeSpent.setVisibility(View.VISIBLE);
            if (todoItem.getTimeSpent()/60 < 60){
                timeSpent.setText("Time:        " + todoItem.getTimeSpent() / 60 + " min");
            } else {
                timeSpent.setText("Time:        " + todoItem.getTimeSpent()/3600 + " h " + todoItem.getTimeSpent() % 3600 + " min");
            }
        }

        if (todoItem.getDescription() == null || todoItem.getDescription() == "") {
            descriptionTextView.setVisibility(View.GONE);
        } else {
            descriptionTextView.setVisibility(View.VISIBLE);
            descriptionTextView.setText("Notes:      " + todoItem.getDescription());
        }

        dateFinish.setText("Finish:      " + todoItem.getFinishDate());

    }


    public void updateTimer(Intent intent) {

        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            int secondsLeft = (int) (millisUntilFinished / 1000);

            int minutes = secondsLeft / 60;
            int seconds = secondsLeft - (minutes * 60);
            String secondString = Integer.toString(seconds);
            if (seconds <= 9) {
                secondString = "0" + secondString;
            }

            timerTextView.setText(minutes + ":" + secondString);



            if (secondsLeft == 0) {
                Log.d("Stop", "millis is 0");
//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
//                mediaPlayer.start();
                Notifications.remindUserTimerFinished(getApplicationContext());
                stopTimer();
                updateTotalSpent();
            }
        }

    }


    @SuppressLint("ResourceAsColor")
    public void stopTimer() {

        totalWorkOnTask = totalWorkOnTask + (workTime - (int) totalWork/1000);
        settingsTimerButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
        breakButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        timerTextView.setText(String.valueOf(workTime / 60) + ":00");
        timerSeekbar.setProgress(workTime / 60);
        stopService(new Intent(getApplicationContext(), TimerBroadcastService.class));

    }

    public void pauseTimer() {
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.VISIBLE);
        PrefUtils.setIsResumed(getApplicationContext(), false);
        Log.d("time left when paused", String.valueOf(PrefUtils.getRemindingTime(getApplicationContext())));
        stopService(new Intent(getApplicationContext(), TimerBroadcastService.class));
    }

    public void resumeTimer() {
        pauseButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.GONE);
        PrefUtils.setIsResumed(getApplicationContext(), true);
        startService(new Intent(getApplicationContext(), TimerBroadcastService.class));
        registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
    }

    private void createPopupEditDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_edit_dialog, null);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        spinner = view.findViewById(R.id.spinnerEdit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                priority = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

        todoEditPopup = view.findViewById(R.id.todoItemEditPopup);
        categroyEditPopup = view.findViewById(R.id.categoryPopupEditText);
        saveEditButton = view.findViewById(R.id.saveEditButton);
        todoItemDateFinishEditButton = view.findViewById(R.id.todoItemDateFinishEdit);
        savaDateEditPopup = view.findViewById(R.id.saveDateButtonEdit);
        popupEditLayout = view.findViewById(R.id.relativeLayoutEditPopup);
        calendarLayout = view.findViewById(R.id.calendarLayoutEdit);
        editPopupTextDate = view.findViewById(R.id.dateFinishTextEdit);
        toDoDateFinishEdit = view.findViewById(R.id.popupDatePickerEdit);


        db = new DatabaseHandler(this);

        final TodoItem todoItem = db.getTodoItem(itemId);

        todoEditPopup.setText(todoItem.getName());
        editPopupTextDate.setText("Finish Date: " + todoItem.getFinishDate());

        if (todoItem.getDescription() != null || todoItem.getDescription() != "") {
            categroyEditPopup.setText(todoItem.getDescription());
        }

        todoEditPopup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        categroyEditPopup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });



        todoItemDateFinishEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsPomoActivity.this, R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Date finishDate =  new Date(year - 1900, month, dayOfMonth);
                        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
                        itemFinishDAte = formatter.format(finishDate);
                        editPopupTextDate.setText("Finish Date: " + itemFinishDAte);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });


        savaDateEditPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendarLayout.setVisibility(View.INVISIBLE);
                popupEditLayout.setVisibility(View.VISIBLE);

                Date finishDate =  new Date(toDoDateFinishEdit.getYear() - 1900, toDoDateFinishEdit.getMonth(), toDoDateFinishEdit.getDayOfMonth());
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
                String itemFinishDAte = formatter.format(finishDate);
                editPopupTextDate.setText("Finish Date: " + itemFinishDAte);
            }
        });


        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                todoItem.setName(todoEditPopup.getText().toString());
                todoItem.setDescription(categroyEditPopup.getText().toString());
                todoItem.setPriority(priority);

                Date finishDate =  new Date(toDoDateFinishEdit.getYear() - 1900, toDoDateFinishEdit.getMonth(), toDoDateFinishEdit.getDayOfMonth());
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
                String itemFinishDAte = formatter.format(finishDate);
                todoItem.setFinishDate(itemFinishDAte);

                if (todoEditPopup.getText().length() == 0) {
                    Toast.makeText(DetailsPomoActivity.this, "Please enter a task", Toast.LENGTH_SHORT);
                    return;
                } else {
                    //Save to DB
                    db.updateTodoItem(todoItem);
                }




                //Update in Details Activity
                itemName.setText(todoItem.getName());
                priorityTextView.setText("Priority:    " + todoItem.getPriority());

                if (todoItem.getDescription() == null || todoItem.getDescription() == "") {
                    descriptionTextView.setVisibility(View.GONE);
                } else {
                    descriptionTextView.setVisibility(View.VISIBLE);
                    descriptionTextView.setText("Notes:      " + todoItem.getDescription());
                }


                dateFinish.setText("Finish:      " + todoItem.getFinishDate());
                dialog.dismiss();
            }
        });


    }

    public void deleteItem() {

        db = new DatabaseHandler(this);

        db.deleteTodoItem(itemId);

        deleteButtonDetails.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.greyFont));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //start a new activity
                startActivity(new Intent(DetailsPomoActivity.this, MainActivity.class));
                finish();
            }
        }, 1200); //  1 second.



    }



    public void updateTotalSpent() {

        db = new DatabaseHandler(this);

        TodoItem todoItem = db.getTodoItem(PrefUtils.getItemId(getApplicationContext()));

        todoItem.setTimeSpent(todoItem.getTimeSpent()+ totalWorkOnTask);
        db.updateTodoItem(todoItem);
        db.close();
        Log.d("time spent item", String.valueOf(db.getTodoItem(itemId).getTimeSpent()));

        Log.d("time on task", String.valueOf(todoItem.getTimeSpent()));

        if (todoItem.getTimeSpent() != 0) {
            int hours = todoItem.getTimeSpent() / 3600;
            int minutes = todoItem.getTimeSpent() / 60 - (hours * 60);

            timeSpent.setVisibility(View.VISIBLE);
            if (hours <= 0) {
                timeSpent.setText("Time:        " + minutes + " min");
            } else {
                timeSpent.setText("Time:        " + hours + " h" + minutes + " min");
            }
        } else {
            timeSpent.setText("Time:    0 min");
        }
    }

    public void updateDone(View v) {

        db = new DatabaseHandler(this);

        TodoItem todoItem = db.getTodoItem(itemId);
        todoItem.setDone(1);

        Date completionDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
        String itemCompletionDate = formatter.format(completionDate);
        todoItem.setCompletionDate(itemCompletionDate);

        db.updateTodoItem(todoItem);
        db.close();

        checkboxButton.setBackgroundResource(R.drawable.ic_check_black_24dp);
        itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Snackbar.make(v, "Completed!", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //start a new activity
                startActivity(new Intent(DetailsPomoActivity.this, MainActivity.class));
                finish();
            }
        }, 1200); //  1 second.
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    public void saveToSharedPreferences() {
//
//        PrefUtils.setIsWorkModeOn(this, workModeIsOn);
//        PrefUtils.setIsBreakModeOn(this, breakModeIsOn);
//        PrefUtils.setEndTimeWork(this, endTimeWork);
//        PrefUtils.setEndTimeBreak(this, endTimeBreak);
//        PrefUtils.setEndTimeLongBreak(this, endTimeLongBreak);
//        PrefUtils.setTotalWork(this, totalWork);
//        PrefUtils.setTotalBreak(this, totalBreak);
//        PrefUtils.setTotalLongBreak(this, totalLongBreak);
//    }

    @Override
    protected void onStop() {
        super.onStop();
//        try {
//            unregisterReceiver(br);
//        } catch (Exception e) {
//            Log.d("reveiver stopped", e.toString());
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
//        registerReceiver(br, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
//        updateTimer(new Intent(this, TimerBroadcastService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(br);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(new Intent(this, TimerBroadcastService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateData();
//        registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BROADCAST));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTimer(intent);
        }
    };
}



