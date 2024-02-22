package com.example.memoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.Date;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    private Memo currentMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToggleButton();
        setForEditing(false);
        initSettingsButton();
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
        TextView dateButton = findViewById(R.id.btnCalendar);
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
        TextView editDate = findViewById(R.id.calendarView);
        RadioButton low = findViewById(R.id.radioButtonLow);
        RadioButton medium = findViewById(R.id.radioButtonMed);
        RadioButton high = findViewById(R.id.radioButtonHigh);

        editSubject.setText(currentMemo.getSubject());
        editDetails.setText(currentMemo.getDetails());
        editDate.setText(currentMemo.getDate().toString());
        low.setText(currentMemo.getPriority_level());
        medium.setText(currentMemo.getPriority_level());
        high.setText(currentMemo.getPriority_level());
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

    private void initSaveButton() {
        Button saveButtom = findViewById(R.id.buttonSave);

        saveButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                //  hideKeyboard();
                DataSource ds = new DataSource(MainActivity.this);
                try {
                    ds.open();

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}