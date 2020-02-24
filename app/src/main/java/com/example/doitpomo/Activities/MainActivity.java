package com.example.doitpomo.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doitpomo.Data.DatabaseHandler;
import com.example.doitpomo.Model.TodoItem;
import com.example.doitpomo.R;
import com.example.doitpomo.UI.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<TodoItem> todoItemList;
    private List<TodoItem> todoItems;
    private DatabaseHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Spinner spinner;
    private RelativeLayout popupRelativeLayout, calendarLayout;
    String priority, itemFinishDAte;


    private TextView itemFinishText;
    private EditText toDoName, description;
    private DatePicker toDoDateFinish;
    private Button saveButton, todoItemFinish, saveDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        renderList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        renderList();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.home:
                return true;

            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_archive:

                startActivity(new Intent(MainActivity.this, ArchiveActivity.class));
                return true;

            case R.id.action_add:

                createPopDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();



        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                priority = parent.getItemAtPosition(position).toString();
                Log.d("priority_value", priority);
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

        popupRelativeLayout = view.findViewById(R.id.relativeLayoutPopup);
        toDoName = view.findViewById(R.id.todoItem);
        saveButton = view.findViewById(R.id.saveButton);
        todoItemFinish = view.findViewById(R.id.todoItemDateFinish);
        itemFinishText = view.findViewById(R.id.dateFinishText);
        description = view.findViewById(R.id.notesDescription);



        toDoName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        todoItemFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Date finishDate =  new Date(year - 1900, month, dayOfMonth);
                        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
                        itemFinishDAte = formatter.format(finishDate);
                        itemFinishText.setText(" Finish Date: " + itemFinishDAte);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String input = toDoName.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                    return;
                } else if (itemFinishDAte == null) {
                    Toast.makeText(MainActivity.this, "Please choose a finish date", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    saveTodoItemToDB(v);
                }
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveTodoItemToDB(View v) {

        TodoItem todoItem = new TodoItem();

        String itemName = toDoName.getText().toString().trim();
        String descriptionNotes = description.getText().toString().trim();


        todoItem.setName(itemName);
        todoItem.setPriority(priority);
        todoItem.setDescription(descriptionNotes);
        todoItem.setDone(0);
        todoItem.setFinishDate(itemFinishDAte);

        //Save to DB
        db.addTodoItem(todoItem);

//        Snackbar.make(v, "Saved!", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        }, 1200); //  1 second.

    }


    @Override
    public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView <?> parent) {

    }


    private void renderList() {

        db = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoItemList = new ArrayList<>();
        todoItems = new ArrayList<>();

        Log.d("date_finish", "finish date");

        // Get Items from BD
        todoItemList = db.getAllTodoItems();


        for (TodoItem c: todoItemList){
            TodoItem todoItem = new TodoItem();
            todoItem.setName(c.getName());
            todoItem.setId(c.getId());
            todoItem.setDateItemAdded( c.getDateItemAdded());
            todoItem.setFinishDate(c.getFinishDate());
            todoItem.setPriority(c.getPriority());
            todoItem.setDescription(c.getDescription());
            todoItem.setCompletionDate(c.getCompletionDate());
            todoItem.setDone(c.getDone());

            Log.d("done main", Integer.valueOf(todoItem.getDone()).toString());
            if (todoItem.getDone() == 0) {
                todoItems.add(todoItem);
            }

        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, todoItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }
}
