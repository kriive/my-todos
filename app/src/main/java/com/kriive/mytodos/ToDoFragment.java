package com.kriive.mytodos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

/**
 * A fragment representing a list of Items.
 */
public class ToDoFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";

    private ArrayList<ToDo> mDataset;
    private MyAdapterSortedList mAdapter;
    private RecyclerView recyclerView;

    public ToDoFragment() {
    }

    public void orderByCategory() {
        mAdapter.orderByCategory();
    }

    public void orderByCreatedOn() {
        mAdapter.orderByCreatedOn();
    }

    public void orderByDueDate() {
        mAdapter.orderByDueDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (!(view instanceof RecyclerView)) {
            return view;
        }

        Context context = view.getContext();
        recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new MyAdapterSortedList(context);

        recyclerView.setAdapter(mAdapter);
        mAdapter.add(ToDoManager.getInstance().getList());

        ToDoManager.getInstance().setOnAddListener(new ToDoManager.OnAddListener() {
            @Override
            public void onAdd(final ToDo pos) {
                mAdapter.add(pos);
                recyclerView.scrollToPosition(0);
                if (pos.getDueDate() != null && (pos.getDueDate().compareTo(new Date()) > 0)){
                    ((MainActivity) getActivity()).scheduleNewNotification(pos.getName(), pos.getDueDate());
                }
            }
        });

        ToDoManager.getInstance().setOnDeleteListener(new ToDoManager.OnDeleteListener() {
            @Override
            public void onDelete(final ToDo pos) {
                mAdapter.remove(pos);
            }
        });

        ToDoManager.getInstance().setOnEditListener(new ToDoManager.OnEditListener() {
            @Override
            public void onEdit(ToDo todo) {
                mAdapter.remove(todo);
                mAdapter.add(todo);
            }
        });

        mAdapter.setOnEntryClickListener(new MyAdapterSortedList.OnEntryClickListener() {
            @Override
            public void onEntryClick(ToDo todo) {
                ToDoManager.getInstance().markToDo(todo, !todo.getCompleted());
            }
        });

        deleteTodoItem();

        return view;
    }

    private void deleteTodoItem() {
        //Swipe to delete currentTodo Item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            private ColorDrawable background = new ColorDrawable(Color.RED);


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ToDoManager.getInstance().removeToDo(((MyAdapterSortedList.ToDoViewHolder) viewHolder).todo);
                Activity act = getActivity();
                if (act instanceof MainActivity) {
                    ((MainActivity) act).checkEmpty();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;

                if (viewHolder instanceof MyAdapterSortedList.HeaderViewHolder) {
                    itemView = itemView.findViewById(R.id.data_control);
                }

                int backgroundCornerOffset = 60;

                if (dX > 0) { // Swiping to the right
                    background.setBounds(itemView.getLeft() - 60, itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                            itemView.getBottom());

                } else if (dX < 0) { // Swiping to the left
                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight() + 60, itemView.getBottom());
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
            }
        }).attachToRecyclerView(recyclerView);
    }
}