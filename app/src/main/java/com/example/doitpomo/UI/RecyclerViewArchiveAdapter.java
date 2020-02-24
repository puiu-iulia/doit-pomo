package com.example.doitpomo.UI;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.doitpomo.Data.DatabaseHandler;
import com.example.doitpomo.Model.TodoItem;
import com.example.doitpomo.R;

import java.util.List;

public class RecyclerViewArchiveAdapter extends RecyclerView.Adapter<RecyclerViewArchiveAdapter.ViewHolder> {

    private Context context;
    int itemId;
    private List <TodoItem> todoItems;
    private LayoutInflater inflater;

    public RecyclerViewArchiveAdapter(Context context, List <TodoItem> todoItems) {
        this.context = context;
        this.todoItems = todoItems;

    }

    @NonNull
    @Override
    public RecyclerViewArchiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_archive, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TodoItem todoItem = todoItems.get(i);

        if (todoItem.getDone() == 1) {
            viewHolder.todoItemName.setText(todoItem.getName());
            viewHolder.dateFinished.setText(todoItem.getCompletionDate());
            viewHolder.dateAdded.setText(todoItem.getDateItemAdded());


            Log.d("time on task archive", String.valueOf(todoItem.getTimeSpent()));

                int hours = todoItem.getTimeSpent() / 3600;
                int minutes = todoItem.getTimeSpent() / 60 - (hours * 60);

                if (hours <= 0) {
                    viewHolder.timeSpent.setText(minutes + " min");
                } else {
                    viewHolder.timeSpent.setText(hours + " h" + minutes + " min");
                }

        }
    }

    @Override
    public int getItemCount() {
        return todoItems.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button deleteButton;
        public TextView todoItemName;
        public TextView dateAdded, dateFinished, timeSpent;
        public int id;



        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

            deleteButton = view.findViewById(R.id.deleteButtonArchive);
            todoItemName = view.findViewById(R.id.nameArchive);
            dateAdded = view.findViewById(R.id.dateStarted);
            dateFinished = view.findViewById(R.id.dateFinished);
            timeSpent = view.findViewById(R.id.timeSpentOnProject);

            todoItemName.setPaintFlags(todoItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    TodoItem todoItem = todoItems.get(position);
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteTodoItem(todoItem.getId());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            todoItems.remove(position);
                            notifyItemChanged(position);
                            notifyItemRemoved(getAdapterPosition());
                        }
                    }, 1200); //  1 second.


                    Snackbar.make(v, "Deleted!", Snackbar.LENGTH_SHORT).show();


                }
            });
        }

    }


//        public void deleteItem(final int id) {
//
//            //create an alert dialog
//
//            alertDialogBuilder = new android.app.AlertDialog.Builder(context);
//
//            inflater = LayoutInflater.from(context);
//            View view = inflater.inflate(R.layout.confirmation_dialog, null);
//
//            Button noButton = (Button) view.findViewById(R.id.noButton);
//            Button yesButton = (Button) view.findViewById(R.id.yesButton);
//
//            alertDialogBuilder.setView(view);
//            dialog = alertDialogBuilder.create();
//            dialog.show();
//
//            noButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            yesButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //delete the item
//                    DatabaseHandler db = new DatabaseHandler(context);
//
//                    //delete item
//                    db.deleteTodoItem(id);
//                    todoItems.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
//
//                    dialog.dismiss();
//                }
//            });
//        }
}
