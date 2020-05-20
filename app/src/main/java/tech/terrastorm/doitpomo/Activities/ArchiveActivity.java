package tech.terrastorm.doitpomo.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import tech.terrastorm.doitpomo.Data.DatabaseHandler;
import tech.terrastorm.doitpomo.Model.TodoItem;
import tech.terrastorm.doitpomo.R;
import tech.terrastorm.doitpomo.UI.RecyclerViewArchiveAdapter;

import java.util.ArrayList;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewArchiveAdapter recyclerViewAdapterArchive;
    private List <TodoItem> todoItemList;
    private List<TodoItem> todoItems;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        renderList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        renderList();
    }

    private void renderList() {

        db = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewArchive);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoItemList = new ArrayList <>();
        todoItems = new ArrayList<>();

        Log.d("date_finish", "finish date");

        // Get Items from BD
        todoItemList = db.getAllTodoItems();
        db.close();

        for (TodoItem c: todoItemList){
            TodoItem todoItem = new TodoItem();
            todoItem.setName(c.getName());
            todoItem.setId(c.getId());
            todoItem.setDateItemAdded(c.getDateItemAdded());
            todoItem.setFinishDate(c.getFinishDate());
            todoItem.setPriority(c.getPriority());
            todoItem.setDescription(c.getDescription());
            todoItem.setTimeSpent(c.getTimeSpent());
            todoItem.setCompletionDate(c.getCompletionDate());
            todoItem.setDone(c.getDone());

            Log.d("done main", Integer.valueOf(todoItem.getDone()).toString());
            if (todoItem.getDone() == 1) {
                todoItems.add(todoItem);
            }

        }

        recyclerViewAdapterArchive = new RecyclerViewArchiveAdapter(this, todoItems);
        recyclerView.setAdapter(recyclerViewAdapterArchive);
        recyclerViewAdapterArchive.notifyDataSetChanged();

    }
}
