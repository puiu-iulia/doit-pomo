package tech.terrastorm.doitpomo.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import tech.terrastorm.doitpomo.Activities.DetailsPomoActivity;
import tech.terrastorm.doitpomo.Data.DatabaseHandler;
import tech.terrastorm.doitpomo.Model.TodoItem;
import tech.terrastorm.doitpomo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List <TodoItem> todoItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List <TodoItem> todoItems) {
        this.context = context;
        this.todoItems = todoItems;

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {

        TodoItem todoItem = todoItems.get(i);

        if (todoItem.getDone() == 0) {
            viewHolder.todoItemName.setText(todoItem.getName());
            viewHolder.dateFinish.setText(todoItem.getFinishDate());


            switch (todoItem.getPriority()) {
                case 0 :
                    viewHolder.listRowCard.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.highPriority));
                    break;
                case 1:
                    viewHolder.listRowCard.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.mediumPriority));
                    break;
                case 2:
                    viewHolder.listRowCard.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lowPriority));
                    break;
                default:
                    viewHolder.listRowCard.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
            }
        }


    }

    @Override
    public int getItemCount() {
        return todoItems.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox priorityCircle;
        public TextView todoItemName;
        public TextView dateFinish;
        public CardView listRowCard;
        public int id;



        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

//            priorityCircle = view.findViewById(R.id.priorityCircle);
            todoItemName = view.findViewById(R.id.name);
            dateFinish = view.findViewById(R.id.dateFinish);
            listRowCard = view.findViewById(R.id.cardview);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //go to next screen - Details Activity
                    int position = getAdapterPosition();

                    TodoItem todoItem = todoItems.get(position);
                    Intent intent = new Intent(context, DetailsPomoActivity.class);
                    intent.putExtra("name", todoItem.getName());
                    intent.putExtra("id", todoItem.getId());
                    intent.putExtra("date", todoItem.getDateItemAdded());
                    intent.putExtra("description", todoItem.getDescription());
                    intent.putExtra("time_spent", todoItem.getTimeSpent());
                    intent.putExtra("date_finish", todoItem.getFinishDate());
                    intent.putExtra("priority", todoItem.getPriority());
                    intent.putExtra("id", todoItem.getId());
                    intent.putExtra("done", todoItem.getDone());

                    context.startActivity(intent);


                }
            });

//            priorityCircle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final int position = getAdapterPosition();
//                    TodoItem todoItem = todoItems.get(position);
//                    DatabaseHandler db = new DatabaseHandler(context);
//                    todoItem.setDone(1);
//                    todoItemName.setPaintFlags(todoItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//                    Date completionDate = Calendar.getInstance().getTime();
//                    SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
//                    String itemCompletionDate = formatter.format(completionDate);
//                    todoItem.setCompletionDate(itemCompletionDate);
//
//                    db.updateTodoItem(todoItem);
//                    db.close();
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            todoItems.remove(position);
//                            notifyItemChanged(position);
//                            notifyItemRemoved(getAdapterPosition());
//                        }
//                    }, 1200); //  1 second.
//
//
//                    Snackbar.make(v, "Completed!", Snackbar.LENGTH_SHORT).show();
//
//
//                }
//            });
        }

        @Override
        public void onClick(View v) {

        }

    }
}


