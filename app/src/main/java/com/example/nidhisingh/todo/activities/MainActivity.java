package com.example.nidhisingh.todo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.nidhisingh.todo.R;
import com.example.nidhisingh.todo.model.Task;
import com.example.nidhisingh.todo.adapter.TaskRecyclerAdapter;
import com.example.nidhisingh.todo.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private AppCompatActivity activity = MainActivity.this;
    Context context = MainActivity.this;
    private RecyclerView recyclerViewTasks;
    private ArrayList<Task> listTasks;
    private TaskRecyclerAdapter taskRecyclerAdapter;
    private DatabaseHandler databaseHelper;
    private ArrayList<Task> filteredList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initObjects();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initObjects() {
        listTasks = new ArrayList<>();
        databaseHelper = new DatabaseHandler(activity);
        taskRecyclerAdapter = new TaskRecyclerAdapter(listTasks, this, databaseHelper);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTasks.setLayoutManager(mLayoutManager);
        recyclerViewTasks.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTasks.setHasFixedSize(true);
        recyclerViewTasks.setAdapter(taskRecyclerAdapter);
        Log.d("main:", "calling get data from sqlite");
        getDataFromSQLite();

    }

    private void initViews() {
        recyclerViewTasks = (RecyclerView) findViewById(R.id.recyclerViewTask);
//        RecyclerView.ItemDecoration itemDecoration = new
//                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerViewTasks.addItemDecoration(itemDecoration);

    }

    @Override
    public void onResume(){
        super.onResume();
       getDataFromSQLite();

    }

    private void getDataFromSQLite() {
        Log.d("Main:", "Fetch tasks!!!");
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                listTasks.clear();
                if(databaseHelper.getAllTasks1() == null) {
                    Log.d("Main:", "######Not getting any tasks!!!");
                }else {
                    Log.d("Size of list", Integer.toString(listTasks.size()));
                    Log.d("Main:", "######Not getting any tasks!!!");
                }
                listTasks.addAll(databaseHelper.getAllTasks());

//                listTasks.add(0, new Task("Laundry", "23sept2017", "do it!!!", "TODO", "High"));
//                listTasks.add(1, new Task("CodepathApp", "20sept2017", "make it work", "TODO", "High"));
//                listTasks.add(2, new Task( "Gym", "20sept2017", "work out", "TODO", "low"));
//                listTasks.add(3, new Task("movie", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(4, new Task("fun", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(5, new Task("assignment", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(6, new Task("learning android", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(7, new Task("Visit shoreline lake", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(8, new Task( "visit xyz", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(9, new Task("Do this that", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(10, new Task( "Forget", "15sept2017", "watch it", "TODO", "high"));
//                listTasks.add(11, new Task( "movie date", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(12, new Task( "Eat out", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(13, new Task("Relax", "15sept2017", "watch it", "TODO", "high"));
//                listTasks.add(14, new Task("fun", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(15, new Task("assignment", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(16, new Task("learning android", "15sept2017", "watch it", "TODO", "low"));
//                listTasks.add(17, new Task("Visit shoreline lake", "15sept2017", "watch it", "TODO", "high"));
//                listTasks.add(18, new Task("visit xyz", "15sept2017", "watch it", "TODO", "high"));

                Collections.sort(listTasks, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                            if (o1.getPriority().equalsIgnoreCase("high")) return -1;
                            else return 1;
                        }

                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                taskRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
