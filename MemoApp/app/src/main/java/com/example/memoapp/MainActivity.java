package com.example.memoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    private Memo currentMemo;

    private RadioGroup radioGroupPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        TextView calendarDate = findViewById(R.id.textMemoDate);

        radioGroupPriority = findViewById(R.id.radioGroupPriority);

        calendarDate.setText(currentDate);
        initToggleButton();
        setForEditing(false);
        initSettingsButton();
        initMemoListButton();
        initSaveButton();
        initChangeDateButton();
        initTextChangedEvents();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initMemo(extras.getInt("memoID"));
        } else {
            currentMemo = new Memo();
        }



    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editSubject = findViewById(R.id.editSubject);
        EditText editDetails = findViewById(R.id.editDetails);
        RadioButton lowLevel = findViewById(R.id.radioButtonLow);
        RadioButton mediumLevel = findViewById(R.id.radioButtonMed);
        RadioButton highLevel = findViewById(R.id.radioButtonHigh);
        Button saveButton = findViewById(R.id.buttonSave);
        Button dataButton = findViewById(R.id.btnCalendar);

        editSubject.setEnabled(enabled);
        editDetails.setEnabled(enabled);
        lowLevel.setEnabled(enabled);
        mediumLevel.setEnabled(enabled);
        highLevel.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        dataButton.setEnabled(enabled);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView dateButton = findViewById(R.id.textMemoDate);
        dateButton.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
        currentMemo.setDate(selectedTime);
    }

    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.btnCalendar);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    private void initMemo(int id) {
        DataSource ds = new DataSource(MainActivity.this);
        try {
            ds.open();
            currentMemo = ds.getSpecificMemo(id);
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Load Memo Failed", Toast.LENGTH_LONG).show();
        }

        EditText editSubject = findViewById(R.id.editSubject);
        EditText editDetails = findViewById(R.id.editDetails);
        TextView editDate = findViewById(R.id.textMemoDate);
        RadioButton low = findViewById(R.id.radioButtonLow);
        RadioButton medium = findViewById(R.id.radioButtonMed);
        RadioButton high = findViewById(R.id.radioButtonHigh);

        editSubject.setText(currentMemo.getSubject());
        editDetails.setText(currentMemo.getDetails());
        // add this if statement for the app not to crash, but the date is not being retrieved
//        if (editDate != null) {editDate.setText(currentMemo.getDate().toString());}
        editDate.setText(DateFormat.format("MM/dd/yyyy", currentMemo.getDate().getTimeInMillis()).toString());

        String priorityLevel = currentMemo.getPriority();
        if (priorityLevel.equals("Low")) {
            low.setChecked(true);
        } else if (priorityLevel.equals("Medium")) {
            medium.setChecked(true);
        } else if (priorityLevel.equals("High")) {
            high.setChecked(true);
        }
    }

    private void initTextChangedEvents() {
        final EditText etSubject = findViewById(R.id.editSubject);

        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setSubject(etSubject.getText().toString());

            }
        });

        final EditText etDetails = findViewById(R.id.editDetails);
        etDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setDetails(etDetails.getText().toString());

            }
        });

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editSubject = findViewById(R.id.editSubject);
        imm.hideSoftInputFromWindow(editSubject.getWindowToken(), 0);

        EditText editMemo = findViewById(R.id.editDetails);
        imm.hideSoftInputFromWindow(editMemo.getWindowToken(),0);

    }

    private void initSaveButton() {
        Button saveButtom = findViewById(R.id.buttonSave);

        saveButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                hideKeyboard();
                DataSource ds = new DataSource(MainActivity.this);
                try {
                    ds.open();


                    String priority;
                    int priorityLevel;
                    int selectedRadioButtonId = radioGroupPriority.getCheckedRadioButtonId();
                    if (selectedRadioButtonId == R.id.radioButtonHigh) {
                        priority = "High";
                        priorityLevel = 1;
                    } else if (selectedRadioButtonId == R.id.radioButtonMed) {
                        priority = "Medium";
                        priorityLevel = 2;
                    } else {
                        priority = "Low";
                        priorityLevel = 3;
                    }


                    currentMemo.setPriority(priority);
                    currentMemo.setPriority_level(priorityLevel);

                    if (currentMemo.getMemoID() == -1) {
                        wasSuccessful = ds.insertMemo(currentMemo);
                        if (wasSuccessful) {
                            int newID = ds.getLastMemoID();
                            currentMemo.setMemoID(newID);}
                        } else {
                            wasSuccessful = ds.updateMemo(currentMemo);
                        }
                        ds.open();
                    } catch(Exception e){
                        wasSuccessful = false;
                    }
                    if (wasSuccessful) {
                        ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                        editToggle.toggle();
                        setForEditing(false);
                    }
                }

        });
    }


    private void initSettingsButton() {
        ImageButton ibList = findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMemoListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MemoListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}