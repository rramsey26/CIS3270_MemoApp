package com.example.memoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MemoListActivity extends AppCompatActivity {

    private ArrayList<Memo> memos;
    private MemoAdapter memoAdapter;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int memoID = memos.get(position).getMemoID();
            Intent intent = new Intent(MemoListActivity.this, MainActivity.class);
            intent.putExtra("memoID", memoID);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        initListButton();
        initSettingsButton();
        initAddMemoButton();
        initDeleteSwitch();
    }

    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MyMemoListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("MyMemoListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");
        DataSource ds = new DataSource(this);
        try {
            ds.open();
            memos = ds.getMemos(sortBy, sortOrder);
            ds.close();
            if (memos.size() > 0) {
                RecyclerView memoList = findViewById(R.id.rvMemos);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                memoList.setLayoutManager(layoutManager);
                memoList.addItemDecoration(new DividerItemDecoration(memoList.getContext(), DividerItemDecoration.VERTICAL));
                memoAdapter = new MemoAdapter(memos, this);
                memoList.setAdapter(memoAdapter);
                memoAdapter.setOnItemClickListener(onItemClickListener);
            }
            else {
                Intent intent = new Intent(MemoListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }

    private void initAddMemoButton(){
        Button newmemo = findViewById(R.id.buttonAddMemo);
        newmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean status = buttonView.isChecked();
                memoAdapter.setDelete(status);
                memoAdapter.notifyDataSetChanged();
            }
        });
    }

    // Bottom Navigation
    private void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setEnabled(false);
    }

    private void initSettingsButton(){
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoListActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}