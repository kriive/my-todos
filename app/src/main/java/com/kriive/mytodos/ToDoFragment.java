package com.kriive.mytodos;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ToDoFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private ArrayList<ToDo> mDataset;
    private MyToDoRecyclerViewAdapter adapter;

    public ToDoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            adapter = new MyToDoRecyclerViewAdapter(ToDoManager.getInstance().getList());
            ToDoManager.getInstance().setOnAddListener(new ToDoManager.OnAddListener() {
                @Override
                public void onAdd(Integer pos) {
                    ((MainActivity) getActivity()).showList();
                    adapter.notifyItemInserted(pos);
                }
            });
            ToDoManager.getInstance().setOnDeleteListener(new ToDoManager.OnDeleteListener() {
                @Override
                public void onDelete(Integer pos) {
                    if (ToDoManager.getInstance().isEmpty()) {
                        ((MainActivity) getActivity()).showIllustration();
                    }
                    adapter.notifyItemRemoved(pos);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}