package tech.terrastorm.doitpomo.UI;

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

import tech.terrastorm.doitpomo.Data.DatabaseHandler;
import tech.terrastorm.doitpomo.Model.Subtask;
import tech.terrastorm.doitpomo.R;

import java.util.List;

public class RecyclerViewSubtaksAdapter extends RecyclerView.Adapter<RecyclerViewSubtaksAdapter.ViewHolder> {

    private Context context;
    private int subtaskId;
    private List<Subtask> subtasks;
    private LayoutInflater inflater;

    public RecyclerViewSubtaksAdapter(Context context, List <Subtask> subtasks) {
        this.context = context;
        this.subtasks = subtasks;
    }


    @NonNull
    @Override
    public RecyclerViewSubtaksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_subtask, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewSubtaksAdapter.ViewHolder viewHolder, int i) {

        Subtask subtask = subtasks.get(i);
        viewHolder.subtaskName.setText(subtask.getName());
//        if (subtask.getDone() == 1) {
//            viewHolder.completeSubtaskButton.setBackgroundResource(R.drawable.ic_check_black_24dp);
////            viewHolder.completeSubtaskButton.setBackgroundTintMode((R.color.lightGrey));
//            viewHolder.subtaskName.setPaintFlags(viewHolder.subtaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }
        Log.d("Subtasks viewHolder", subtask.getName());
    }

    @Override
    public int getItemCount() {
        return subtasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button completeSubtaskButton;
        private TextView subtaskName;
        private int id;

        private ViewHolder(@NonNull View view, final Context ctx) {
            super(view);

            context = ctx;

            completeSubtaskButton = view.findViewById(R.id.checkboxSubtask);
            subtaskName = view.findViewById(R.id.subtaskName);

            completeSubtaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    Subtask subtask = subtasks.get(position);

                    DatabaseHandler db = new DatabaseHandler(context);
                    subtask.setDone(1);
                    db.updateSubtask(subtask);
                    db.close();

                    completeSubtaskButton.setBackgroundResource(R.drawable.ic_check_black_24dp);
                    subtaskName.setPaintFlags(subtaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            subtasks.remove(position);
                            notifyItemChanged(position);
                            notifyItemRemoved(getAdapterPosition());
                        }
                    }, 1200); //  1 second.

                    Snackbar.make(v, "Completed!", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}
