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
import android.widget.TextView;
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
//        initSettingsButton();
        initChangeDateButton();
        initTextChangedEvents();
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


//    private void initSettingsButton() {
//        ImageButton ibList = findViewById(R.id.imageButtonSettings);
//        ibList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}