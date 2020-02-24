package com.example.doitpomo.UI;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.doitpomo.Data.DatabaseHandler;
import com.example.doitpomo.Model.Subtask;
import com.example.doitpomo.R;

import java.util.List;

public class RecyclerViewSubtaksAdapter extends RecyclerView.Adapter<RecyclerViewSubtaksAdapter.ViewHolder> {

    private Context context;
    int subtaskId;
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button completeSubtaskButton;
        public TextView subtaskName;
        public int id;

        public ViewHolder(@NonNull View view, final Context context) {
            super(view);

            completeSubtaskButton = view.findViewById(R.id.checkboxSubtask);
            subtaskName = view.findViewById(R.id.subtaskName);

            completeSubtaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    Subtask subtask = subtasks.get(position);
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.updateSubtask(subtask);
                    db.close();

                    completeSubtaskButton.setVisibility(View.GONE);
                    subtaskName.setPaintFlags(subtaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    Snackbar.make(v, "Completed!", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}
