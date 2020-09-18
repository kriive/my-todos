package com.kriive.mytodos;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MyAdapterSortedList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final SortedList<ToDo> mSortedList = new SortedList<>(ToDo.class, new SortedList.Callback<ToDo>() {
        @Override
        public int compare(ToDo a, ToDo b) {
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(ToDo oldItem, ToDo newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(ToDo item1, ToDo item2) {
            return item1 == item2;
        }
    });

    private Comparator<ToDo> mComparator;
    private final LayoutInflater mInflater;
    private Order currentOrder = Order.BY_CREATION;

    private static final Comparator<ToDo> CATEGORY_ALPHABETICAL_COMPARATOR = new Comparator<ToDo>() {
        @Override
        public int compare(ToDo a, ToDo b) {
            return a.getCategory().compareTo(b.getCategory());
        }
    };

    private static final Comparator<ToDo> DUE_DATE_COMPARATOR = new Comparator<ToDo>() {
        @Override
        public int compare(ToDo a, ToDo b) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

            if (a.getDueDate() == null && b.getDueDate() != null) {
                return -1;
            }

            if (a.getDueDate() != null && b.getDueDate() == null) {
                return 1;
            }

            if (a.getDueDate() == null && b.getDueDate() == null) {
                return 0;
            }

            return fmt.format(a.getDueDate()).compareTo(fmt.format(b.getDueDate()));
        }
    };

    private static final Comparator<ToDo> CREATION_TIME_COMPARATOR = new Comparator<ToDo>() {
        @Override
        public int compare(ToDo a, ToDo b) {
            // Invert the compare to have dates first
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            return fmt.format(b.getCreatedOn()).compareTo(fmt.format(a.getCreatedOn()));
        }
    };

    private OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(final ToDo todo);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }

    public void orderByCategory() {
        mSortedList.clear();
        currentOrder = Order.BY_CATEGORY;
        mComparator = CATEGORY_ALPHABETICAL_COMPARATOR;
        this.add(ToDoManager.getInstance().getList());
    }

    public void orderByCreatedOn() {
        mSortedList.clear();
        currentOrder = Order.BY_CREATION;
        mComparator = CREATION_TIME_COMPARATOR;
        this.add(ToDoManager.getInstance().getList());
    }

    public void orderByDueDate() {
        mSortedList.clear();
        currentOrder = Order.BY_DUEDATE;
        mComparator = DUE_DATE_COMPARATOR;
        this.add(ToDoManager.getInstance().getList());
    }

    public MyAdapterSortedList(Context context) {
        mInflater = LayoutInflater.from(context);
        mComparator = CREATION_TIME_COMPARATOR;
    }

    class HeaderViewHolder extends ToDoViewHolder {
        public TextView data;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            View dataView = v.findViewById(R.id.data_control);
            dataView.setOnClickListener(this);

            data = v.findViewById(R.id.type);
        }

        @Override
        public void onClick(View v) {
            // The user may not set a click listener for list items, in which case our listener
            // will be null, so we need to check for this
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(mSortedList.get(getLayoutPosition()));
            }
        }
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View v;
        public TextView title;
        public TextView details;
        public ToDo todo;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            v = itemView;
            title = v.findViewById(R.id.todo_title);
            details = v.findViewById(R.id.todo_detail);
        }

        @Override
        public void onClick(View v) {
            // The user may not set a click listener for list items, in which case our listener
            // will be null, so we need to check for this
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(mSortedList.get(getLayoutPosition()));
            }
        }
    }

    class ElaborateDataViewHolder extends ToDoViewHolder {


        public ElaborateDataViewHolder(View itemView) {
            super(itemView);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Integer type = viewType;

        switch (type) {
            case 0: // HEADER
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_text_header_item, parent, false));
            case 1:
            default:
                return new ElaborateDataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (!(viewHolder instanceof ToDoViewHolder)) {
            super.bindViewHolder(viewHolder, position);
            return;
        }

        ToDoViewHolder holder = (ToDoViewHolder) viewHolder;
        DateFormat displayFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH);

        holder.todo = mSortedList.get(position);

        if (mSortedList.get(position).getCompleted()) {
            holder.details.setPaintFlags(holder.details.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG & 0xff);
            holder.details.setPaintFlags(holder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG & 0xff);
        }

        String order;
        switch (currentOrder) {
            case BY_DUEDATE:
                if (mSortedList.get(position).getDueDate() == null) {
                    order = "No due date set";
                    break;
                }
                order = "Due on " + displayFormat.format(mSortedList.get(position).getDueDate());
                break;
            case BY_CATEGORY:
                order = mSortedList.get(position).getCategory();
                break;
            case BY_CREATION:
            default:
                order = "Created on " + displayFormat.format(mSortedList.get(position).getCreatedOn());
                break;
        }

        holder.title.setText(mSortedList.get(position).getName());
        if (mSortedList.get(position).getDetails().equals("")) {
            holder.details.setVisibility(View.GONE);
        }

        holder.details.setText(mSortedList.get(position).getDetails());

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).data.setText(order);
        }
    }

    public void add(ToDo model) {
        mSortedList.add(model);
    }

    public void remove(final ToDo model) {
        mSortedList.remove(model);
    }

    public void remove(final int pos) {
        mSortedList.removeItemAt(pos);
    }

    public void add(List<ToDo> models) {
        mSortedList.addAll(models);
    }

    public void remove(List<ToDo> models) {
        mSortedList.beginBatchedUpdates();
        for (ToDo model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<ToDo> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final ToDo model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }

        if (mComparator.compare(mSortedList.get(position), mSortedList.get(position - 1)) != 0) {
            return 0;
        }

        return 1;
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}