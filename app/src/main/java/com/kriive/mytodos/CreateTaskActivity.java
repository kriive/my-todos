package com.kriive.mytodos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity {
    private Date dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_todo);

        final FloatingActionButton fab = findViewById(R.id.fabAdd);
        final TextInputLayout name = findViewById(R.id.taskName);
        final TextInputLayout detail = findViewById(R.id.taskDetail);
        final TextInputLayout category = findViewById(R.id.category);
        String[] COUNTRIES = new String[] {"Work", "Sport", "Home", "School"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_menu_item,
                        COUNTRIES);

        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getEditText().getText().toString().equals("")) {
                    finish();
                    return;
                }

                if (category.getEditText().getText().toString().equals("")) {
                    category.getEditText().setText("No Category");
                }

                ToDo newTodo = new ToDo(name.getEditText().getText().toString(), detail.getEditText().getText().toString(), category.getEditText().getText().toString(), dueDate);
                ToDoManager.getInstance().addToDo(newTodo);

                finish();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private Context mCtx;

        public DatePickerFragment(final Context ctx) {
            mCtx = ctx;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Log.d("DKJ", String.valueOf(year));
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);

            if (mCtx instanceof CreateTaskActivity) {
                ((CreateTaskActivity) mCtx).dueDate = cal.getTime();
            }

            dismiss();
        }
    }

}
