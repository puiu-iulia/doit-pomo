package com.example.doitpomo.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doitpomo.Data.DatabaseHandler;
import com.example.doitpomo.Model.Subtask;
import com.example.doitpomo.Model.TodoItem;
import com.example.doitpomo.R;
import com.example.doitpomo.UI.FragmentsAdapter;
import com.example.doitpomo.Utils.Prefs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsPomoActivity extends AppCompatActivity {

    private CheckedTextView itemName;
    private TextView dateAdded, timeSpent, dateFinish;
    private int itemId;
    private DatabaseHandler db;
    private TextView priorityTextView, editPopupTextDate;
    private EditText todoEditPopup, subtaskPopup;
    private Button editButton, savaDateEditPopup, saveEditButton, todoItemDateFinishEditButton, addSubtaskButton, saveSettingsButton, checkboxButton, deleteButtonDetails, saveSubtaskButton;
    public RelativeLayout popupEditLayout, pomoLayout, calendarLayout;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Spinner spinner;
    private DatePicker toDoDateFinishEdit;
    private int workTime, breakTime, longBreakTime, workSessionsNumber;
    private String priority, itemFinishDAte;
    private static ViewPager viewPager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_pomo);

        itemName = findViewById(R.id.itemNameDetails);
        dateAdded = findViewById(R.id.dateAddedDetails);
        timeSpent = findViewById(R.id.timeSpentOnProjectDetails);
        dateFinish = findViewById(R.id.dateFinishDetails);
        editButton = findViewById(R.id.editButtonDetails);
        checkboxButton = findViewById(R.id.checkbox);
        saveSettingsButton = findViewById(R.id.saveSettinsButton);
        priorityTextView = findViewById(R.id.priorityDetails);
        deleteButtonDetails = findViewById(R.id.deleteButtonDetails);
        viewPager = findViewById(R.id.viewPager);
        addSubtaskButton = findViewById(R.id.addSubtaskButton);

        getItemId();
        updateData();

        viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        TabLayout tabs = findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(R.color.colorPrimary, Color.parseColor("white"));


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

        addSubtaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupSubtaskDialog();
            }
        });


        workTime = Prefs.getWorkTime(getApplicationContext());
        Log.d("Work Time", String.valueOf(workTime));
        breakTime = Prefs.getBreakTime(getApplicationContext());
        longBreakTime = Prefs.getLongBreakTime(getApplicationContext());
        workSessionsNumber = Prefs.getLongBreakTime(getApplicationContext());



    }

    private void getItemId() {
        Bundle bundle = getIntent().getExtras();

        if (bundle!= null) {

            itemId = bundle.getInt("id");
            Prefs.setItemId(getApplicationContext(), itemId);

        }
    }

    private void updateData() {
        int id = Prefs.getItemId(getApplicationContext());

        db = new DatabaseHandler(this);

        TodoItem todoItem = db.getTodoItem(id);
        db.close();

        itemName.setText(todoItem.getName());
        dateAdded.setText(todoItem.getFinishDate());
        priorityTextView.setText(todoItem.getPriority());

        if (todoItem.getTimeSpent() == 0) {
            timeSpent.setText("0 min");
        } else {
            if (todoItem.getTimeSpent()/60 < 60){
                timeSpent.setText(todoItem.getTimeSpent() / 60 + " min");
            } else {
                timeSpent.setText(todoItem.getTimeSpent()/3600 + " h " + todoItem.getTimeSpent() % 3600 + " min");
            }
        }

        dateFinish.setText(todoItem.getFinishDate());

    }


    private void createPopupSubtaskDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_subtask, null);

        saveSubtaskButton = view.findViewById(R.id.subtaskSaveButton);
        subtaskPopup = view.findViewById(R.id.popup_subtask_name);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();


        subtaskPopup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        saveSubtaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subtaskName = subtaskPopup.getText().toString();

                //Save to DB

                if (subtaskName.length() == 0) {
                    Toast.makeText(DetailsPomoActivity.this, "Please enter a subtask", Toast.LENGTH_SHORT).show();
                } else {
                    db = new DatabaseHandler(getApplicationContext());
                    Subtask subtask = new Subtask();
                    subtask.setName(subtaskName);
                    subtask.setDone(0);
                    subtask.setTaskId(Prefs.getItemId(getApplicationContext()));
                    db.addSubtask(subtask);
                    db.close();


                    Log.d("subtask", subtask.getName() + subtask.getTaskId());
                    startActivity(new Intent(DetailsPomoActivity.this, DetailsPomoActivity.class));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            viewPager.setCurrentItem(1, true);
                            finish();
                        }
                    }, 1000); //  1 second.
                }
            }
        });
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
        saveEditButton = view.findViewById(R.id.saveEditButton);
        todoItemDateFinishEditButton = view.findViewById(R.id.todoItemDateFinishEdit);
        savaDateEditPopup = view.findViewById(R.id.saveDateButtonEdit);
        popupEditLayout = view.findViewById(R.id.relativeLayoutEditPopup);
        calendarLayout = view.findViewById(R.id.calendarLayoutEdit);
        editPopupTextDate = view.findViewById(R.id.dateFinishTextEdit);
        toDoDateFinishEdit = view.findViewById(R.id.popupDatePickerEdit);


        db = new DatabaseHandler(this);
        final TodoItem todoItem = db.getTodoItem(itemId);
        db.close();

        todoEditPopup.setText(todoItem.getName());
        editPopupTextDate.setText("Finish Date: " + todoItem.getFinishDate());

        todoEditPopup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                todoItem.setDescription("");
                todoItem.setPriority(priority);

                Date finishDate =  new Date(toDoDateFinishEdit.getYear() - 1900, toDoDateFinishEdit.getMonth(), toDoDateFinishEdit.getDayOfMonth());
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
                String itemFinishDAte = formatter.format(finishDate);
                todoItem.setFinishDate(itemFinishDAte);

                if (todoEditPopup.getText().length() == 0) {
                    Toast.makeText(DetailsPomoActivity.this, "Please enter a task", Toast.LENGTH_SHORT);
                    return;
                } else {
                    //Save to DB
                    db.updateTodoItem(todoItem);
                    db.close();
                }

                //Update in Details Activity
                itemName.setText(todoItem.getName());
                priorityTextView.setText(todoItem.getPriority());
                dateFinish.setText(todoItem.getFinishDate());
                dialog.dismiss();
            }
        });


    }

    private void deleteItem() {

        db = new DatabaseHandler(this);

        db.deleteTodoItem(itemId);
        db.close();

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


    private void updateDone(View v) {

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
                startActivity(new Intent(DetailsPomoActivity.this, DetailsPomoActivity.class));
                finish();
            }
        }, 1200); //  1 second.
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateData();
    }

}



