package com.example.memoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_settings);
        initSettings();
        initSortByClick();
        initSortOrderClick();
        initListButton();
        initSettingsButton();
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MyMemoListPreferences",
                Context.MODE_PRIVATE).getString("sortfield","subject");
        String sortOrder = getSharedPreferences("MyMemoListPreferences",
                Context.MODE_PRIVATE).getString("sortorder","ASC");

        RadioButton rbSubject = findViewById(R.id.radioSubject);
        RadioButton rbDate = findViewById(R.id.radioDate);
        RadioButton rbPriority = findViewById(R.id.radioPriority);
        if (sortBy.equalsIgnoreCase("subject")) {
            rbSubject.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("date")) {
            rbDate.setChecked(true);
        }
        else {
            rbPriority.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ASC")){
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }
    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbSubject = findViewById(R.id.radioSubject);
                RadioButton rbDate = findViewById(R.id.radioDate);
                if (rbSubject.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield","subject").apply();
                }
                else if (rbDate.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield","date").apply();
                }
                else {
                    getSharedPreferences("MyMemoListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield","priority_level").apply();
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder","ASC").apply();
                }
                else {
                    getSharedPreferences("MyMemoListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder","DESC").apply();
                }
            }
        });
    }

    private void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MemoListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton(){
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setEnabled(false);
    }
}