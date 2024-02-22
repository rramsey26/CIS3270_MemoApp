package com.example.memoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter {
    private ArrayList<Memo> memoData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class MemoViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewSubject;
        public TextView textDate;
        public TextView textPriority;
        public Button deleteButton;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textMemoListSubject);
            textDate = itemView.findViewById(R.id.textMemoListDate);
            textPriority = itemView.findViewById(R.id.textMemoListPriority);
            deleteButton = itemView.findViewById(R.id.buttonDeleteMemo);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getSubjectTextView() {
            return textViewSubject;
        }

        public TextView getDateTextView() {
            return textDate;
        }

        public TextView getPriorityTextView() {
            return textPriority;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public MemoAdapter(ArrayList<Memo> arrayList, Context context) {
        memoData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MemoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        MemoViewHolder cvh = (MemoViewHolder) holder;
        cvh.getSubjectTextView().setText(memoData.get(position).getSubject());
        cvh.getDateTextView().setText(DateFormat.format("MM/dd/yyyy",memoData.get(position).getDate().getTimeInMillis()).toString());
        cvh.getPriorityTextView().setText(memoData.get(position).getPriority());
        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void  deleteItem(int position) {
        Memo memo = memoData.get(position);
        DataSource ds = new DataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteMemo(memo.getMemoID());
            ds.close();
            if (didDelete) {
                memoData.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return memoData.size();
    }
}


