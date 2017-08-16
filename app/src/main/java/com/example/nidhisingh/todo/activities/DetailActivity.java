package com.example.nidhisingh.todo.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nidhisingh.todo.R;
import com.example.nidhisingh.todo.database.DatabaseHandler;
import com.example.nidhisingh.todo.model.Task;

import java.util.Calendar;

import info.hoang8f.android.segmented.SegmentedGroup;

public class DetailActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{
    private EditText taskNameEditText;
    private EditText taskNotesEditText;
    private Button saveButton;
    private RadioButton high;
    private RadioButton low;
    private Button dueDateButton;
    private EditText duedateText;
    private SegmentedGroup segmentedPriority;
    private SegmentedGroup segmentedStatus;


    DatabaseHandler db;
    String priority, status;
    int mYear, mMonth, mDay;

    String editname, editnotes, editDueDate, editPriority, editStatus, type;
    long editID;
    boolean isEditType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        taskNameEditText = (EditText) findViewById(R.id.taskname);
        taskNotesEditText = (EditText) findViewById(R.id.taskNotes);
        saveButton = (Button) findViewById(R.id.savebutton);
        dueDateButton = (Button)findViewById(R.id.dueDateButton);
        duedateText = (EditText) findViewById(R.id.dueDatetext);
        high = (RadioButton) findViewById(R.id.buttonhigh);
        low = (RadioButton) findViewById(R.id.buttonlow);

        segmentedPriority = (SegmentedGroup)findViewById(R.id.segmentedpriority);
        segmentedStatus = (SegmentedGroup)findViewById(R.id.segmentedstatus);

        segmentedPriority.setOnCheckedChangeListener(this);
        segmentedStatus.setOnCheckedChangeListener(this);

        Intent i = getIntent();

        editname = i.getStringExtra("taskname");
        editnotes = i.getStringExtra("tasknotes");
        editDueDate = i.getStringExtra("duedate");
        editPriority = i.getStringExtra("priority");
        editStatus = i.getStringExtra("status");
        editID = i.getLongExtra("id", -1);
        type = i.getStringExtra(type);
        if(editname != null) {
            isEditType = true;
        }


        if(editname != null) {
            taskNameEditText.setText(editname.toCharArray(), 0, editname.length());
        }
        if(editnotes != null) {
            taskNotesEditText.setText(editnotes.toCharArray(), 0, editnotes.length());
        }

        if(editDueDate != null) {
            duedateText.setText(editDueDate.toCharArray(), 0, editDueDate.length());
        }
        if(editPriority != null) {
            if(editPriority.equalsIgnoreCase("HIGH")) {
                segmentedPriority.check(R.id.buttonhigh);
                priority = "HIGH";

            } else  if(editPriority.equalsIgnoreCase("LOW")) {
                segmentedPriority.check(R.id.buttonlow);
                priority = "LOW";
            }

        }
        if(editStatus != null) {
            if(editStatus.equalsIgnoreCase("TODO")) {
                segmentedStatus.check(R.id.buttontodo);
                status = "TODO";

            } else if(editStatus.equalsIgnoreCase("DONE")) {
                segmentedStatus.check(R.id.buttondone);
                status = "DONE";
            }
        }


        ActionBar ab = this.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        //noinspection RestrictedApi
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);



        db = new DatabaseHandler(this);
        dueDateButton.setOnClickListener(new View.OnClickListener() {

                                             @Override
                                             public void onClick(View v) {
                                                 final Calendar c = Calendar.getInstance();
                                                 mYear = c.get(Calendar.YEAR);
                                                 mMonth = c.get(Calendar.MONTH);
                                                 mDay = c.get(Calendar.DAY_OF_MONTH);
                                                 DatePickerDialog datePickerDialog = new DatePickerDialog(DetailActivity.this,
                                                         new DatePickerDialog.OnDateSetListener() {
                                                             @Override
                                                             public void onDateSet(DatePicker view, int year,
                                                                                   int monthOfYear, int dayOfMonth) {

                                                                 duedateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                                             }
                                                         }, mYear, mMonth, mDay);
                                                 datePickerDialog.show();

                                             }
                                         });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = taskNameEditText.getText().toString();
                String notes = taskNotesEditText.getText().toString();
                String duedate = duedateText.getText().toString();
                Log.d(name, notes);

                if(!isEditType) {
                    db.addTasks(new Task(name, duedate, notes, status, priority));
                } else{
                    db.updateRow(editID, new Task(name, duedate, notes, status, priority));
                }

                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.buttonhigh:
                priority = "HIGH";
                break;

            case R.id.buttonlow:
                priority = "LOW";
                break;

            case R.id.buttontodo:
                status = "TODO";
                break;

            case R.id.buttondone:
                status = "Done";
                break;
        }
    }
}
