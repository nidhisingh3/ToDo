package com.example.nidhisingh.todo.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nidhisingh.todo.R;
import com.example.nidhisingh.todo.database.DatabaseHandler;
import com.example.nidhisingh.todo.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class DetailActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static String EMPTY_DATE = "MM-DD-YYYY";
    private static String EMPTY_TIME = "HH:MM";
    private EditText taskNameEditText;
    private EditText taskNotesEditText;
    private TextInputLayout tasknameInputLayout;
    private Button saveButton;
    private RadioButton high;
    private RadioButton low;
    private ImageView dueDateImage;
    private ImageView dueTimeButton;
    private TextView duedateText;
    private TextView duetimeText;
    private RadioButton radiobuttonTodo;
    private Spinner spinner;
    private List<String> categories;

    private SegmentedGroup segmentedPriority;
    private SegmentedGroup segmentedStatus;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    private DatabaseHandler db;
    private String priority, status;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String month;
    private String editname, editnotes, editDueDate, editPriority, editStatus, type, editCategory, editDueTime;
    private long editID;
    private boolean isEditType;
    private String categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        taskNameEditText = (EditText) findViewById(R.id.taskname);
        taskNotesEditText = (EditText) findViewById(R.id.taskNotes);
        saveButton = (Button) findViewById(R.id.savebutton);
        dueDateImage = (ImageView) findViewById(R.id.dueDateButton);
        dueTimeButton = (ImageView) findViewById(R.id.dueTimeButton);
        duedateText = (TextView) findViewById(R.id.dueDatetext);
        duetimeText = (TextView) findViewById(R.id.dueTimetext);
        high = (RadioButton) findViewById(R.id.buttonhigh);
        low = (RadioButton) findViewById(R.id.buttonlow);
        radiobuttonTodo = (RadioButton) findViewById(R.id.buttontodo);
        tasknameInputLayout = (TextInputLayout) findViewById(R.id.tasknameInputLayout);

        segmentedPriority = (SegmentedGroup) findViewById(R.id.segmentedpriority);
        segmentedStatus = (SegmentedGroup) findViewById(R.id.segmentedstatus);

        segmentedPriority.setOnCheckedChangeListener(this);
        segmentedStatus.setOnCheckedChangeListener(this);

        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        categories = new ArrayList<String>();
        categories.add("Personal");
        categories.add("Office Work");
        categories.add("Event");
        categories.add("Shopping");
        categories.add("Bills");
        categories.add("Health");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        Intent i = getIntent();

        editname = i.getStringExtra("taskname");
        editnotes = i.getStringExtra("tasknotes");
        editDueDate = i.getStringExtra("duedate");
        editPriority = i.getStringExtra("priority");
        editStatus = i.getStringExtra("status");
        editDueTime = i.getStringExtra("duetime");
        editID = i.getLongExtra("id", -1);
        type = i.getStringExtra(type);
        editCategory = i.getStringExtra("category");
        if (editname != null) {
            isEditType = true;
        }

        if (editname != null) {
            taskNameEditText.setText(editname.toCharArray(), 0, editname.length());
        }
        if (editnotes != null) {
            taskNotesEditText.setText(editnotes.toCharArray(), 0, editnotes.length());
        }

        if (editDueDate != null) {
            duedateText.setText(editDueDate.toCharArray(), 0, editDueDate.length());
        }
        if (editDueTime != null) {
            duetimeText.setText(editDueTime.toCharArray(), 0, editDueTime.length());
        }
        if (editPriority != null) {
            if (editPriority.equalsIgnoreCase("HIGH")) {
                segmentedPriority.check(R.id.buttonhigh);
                priority = "HIGH";

            } else if (editPriority.equalsIgnoreCase("LOW")) {
                segmentedPriority.check(R.id.buttonlow);
                priority = "LOW";
            }

        }
        if (editStatus != null) {
            if (editStatus.equalsIgnoreCase("TODO")) {
                segmentedStatus.check(R.id.buttontodo);
                status = "TODO";

            } else if (editStatus.equalsIgnoreCase("DONE")) {
                segmentedStatus.check(R.id.buttondone);
                status = "DONE";
            }
        }

        if (editCategory != null) {
            spinner.setSelection(categories.indexOf(editCategory));
        }

        ActionBar ab = this.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        //noinspection RestrictedApi
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        db = new DatabaseHandler(this);
        dueDateImage.setOnClickListener(new View.OnClickListener() {

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

        duedateText.setOnClickListener(new View.OnClickListener() {

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
                                month = MONTHS[monthOfYear];
                                duedateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        dueTimeButton.setOnClickListener(this);
        duetimeText.setOnClickListener(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = taskNameEditText.getText().toString();
                String notes = taskNotesEditText.getText().toString();
                String duedate = duedateText.getText().toString();
                String dueTime = duetimeText.getText().toString();
                if (name == null || name.length() == 0) {
                    showAlertDialog("Task name should not be empty.");
                    return;
                }
                if (EMPTY_DATE.equals(duedate)) {
                    showAlertDialog("Please select valid Due date.");
                    return;
                }
                if (EMPTY_TIME.equals(dueTime)) {
                    showAlertDialog("Please select valid Due time.");
                    return;
                }
                if (TextUtils.isEmpty(priority)) {
                    showAlertDialog("Please select priority!");
                    return;
                }
                if (TextUtils.isEmpty(status)) {
                    showAlertDialog("Please select status!");
                    return;
                }
                if (TextUtils.isEmpty(categoryType)) {
                    showAlertDialog("Please select categoryType!");
                    return;
                }

                if (!isEditType) {
                    db.addTasks(new Task(name, duedate, notes, status, priority, categoryType, dueTime));
                } else {
                    db.updateRow(editID, new Task(name, duedate, notes, status, priority, categoryType, dueTime));
                }

                finish();

            }
        });
    }

    private void showAlertDialog(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Invalid input");
        alertDialog.setMessage(errorMessage);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

        if (v == dueTimeButton || v == duetimeText) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            duetimeText.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryType = parent.getItemAtPosition(position).toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
