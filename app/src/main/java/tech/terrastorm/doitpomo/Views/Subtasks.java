package tech.terrastorm.doitpomo.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.terrastorm.doitpomo.Data.DatabaseHandler;
import tech.terrastorm.doitpomo.Model.Subtask;
import tech.terrastorm.doitpomo.R;
import tech.terrastorm.doitpomo.UI.RecyclerViewSubtaksAdapter;
import tech.terrastorm.doitpomo.Utils.Prefs;

import java.util.ArrayList;
import java.util.List;

public class Subtasks extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewSubtaksAdapter recyclerViewSubtaksAdapter;
    private List <Subtask> subtaskList, subtasks;
    private DatabaseHandler db;

    public Subtasks() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subtasks, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSubtasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        subtaskList = new ArrayList <>();
        subtasks = new ArrayList <>();

        //Get Subtasks from Db

        db = new DatabaseHandler(getContext());
        subtaskList = db.getAllSubtaks();
        db.close();

        Log.d("subtasks", subtaskList.toString());

        for (Subtask c: subtaskList) {

            Subtask subtask = new Subtask();
            subtask.setId(c.getId());
            subtask.setName(c.getName());
            subtask.setTaskId(c.getTaskId());
            subtask.setDone(c.getDone());

            if (subtask.getTaskId() == Prefs.getItemId(getContext()) && subtask.getDone() == 0) {
                subtasks.add(subtask);
            }
        }


        Log.d("subtasks list", subtasks.toString());

        recyclerViewSubtaksAdapter = new RecyclerViewSubtaksAdapter(getContext(), subtasks);
        recyclerView.setAdapter(recyclerViewSubtaksAdapter);
        recyclerViewSubtaksAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



    }

}
