package com.example.nidhisingh.todo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nidhisingh.todo.R;
import com.example.nidhisingh.todo.activities.DetailActivity;
import com.example.nidhisingh.todo.database.DatabaseHandler;
import com.example.nidhisingh.todo.model.Task;

import java.util.ArrayList;

/**
 * Created by nidhisingh on 7/26/17.
 */

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private ArrayList<Task> listTask;
    private Context mContext;
    private ArrayList<Task> mFilteredList;
    private DatabaseHandler dbHandler;


    public TaskRecyclerAdapter(ArrayList<Task> listTask, Context mContext, DatabaseHandler dbhandler) {
        this.listTask = listTask;
        this.mContext = mContext;
        this.mFilteredList = listTask;
        this.dbHandler = dbhandler;


    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewName;
        public TextView textViewDate;
        public TextView textViewTime;
        private Context context;
        private ImageView deleteImage;
        private View priorityView;
        private TextView categoryTextView;
        private TextView notesTextView;

        public TaskViewHolder(Context context, View view) {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.taskname1);
            textViewDate = (TextView) view.findViewById(R.id.taskDate);
            textViewTime = (TextView) view.findViewById(R.id.taskTime);
            deleteImage = (ImageView) view.findViewById(R.id.deleteimage);
            priorityView = view.findViewById(R.id.taskprioritycolor);
            categoryTextView = (TextView) view.findViewById(R.id.categoryTextView);
            notesTextView = (TextView) view.findViewById(R.id.notestextView);
            this.context = context;
            view.setOnClickListener(this);
            deleteImage.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                Task t = listTask.get(position);
                String status = t.getStatus();
                long row = t.getId();
                if (v.getId() == R.id.deleteimage) {
                    //remove from recycler view list
                    listTask.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), listTask.size());
                    //remove from DB
                    dbHandler.deleteRow(row);

                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("taskname", t.getTaskName());
                    intent.putExtra("tasknotes", t.getTaskNotes());
                    intent.putExtra("duedate", t.getDueDate());
                    intent.putExtra("duetime", t.getDueTime());
                    intent.putExtra("priority", t.getPriority());
                    intent.putExtra("status", t.getStatus());
                    intent.putExtra("id", t.getId());
                    intent.putExtra("category", t.getCategory());
                    intent.putExtra("type", "Edittask");

                    context.startActivity(intent);

                }
            }
        }
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_data, parent, false);

        return new TaskViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {
        Task task = listTask.get(position);
        holder.textViewName.setText(task.getTaskName());
        holder.categoryTextView.setText("in " + task.getCategory());
        if (!TextUtils.isEmpty(task.getTaskNotes())) {
            holder.notesTextView.setText("Notes : " + task.getTaskNotes());
        }
        if (task.getPriority().equalsIgnoreCase("high")) {
            holder.priorityView.setBackgroundColor(Color.RED);
        } else {
            holder.priorityView.setBackgroundColor(Color.rgb(56, 142, 60));
            //388e3c
        }
        holder.textViewDate.setText(task.getDueDate());
        holder.textViewTime.setText(task.getDueTime());
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

}
