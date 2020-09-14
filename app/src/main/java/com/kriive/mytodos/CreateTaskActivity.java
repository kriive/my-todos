package com.kriive.mytodos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_todo);

        final FloatingActionButton fab = findViewById(R.id.fabAdd);
        final TextInputLayout name = findViewById(R.id.taskName);
        final TextInputLayout detail = findViewById(R.id.taskDetail);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDo newTodo = new ToDo(name.getEditText().getText().toString(), detail.getEditText().getText().toString(), "GENERIC", new Date());
                final Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT, newTodo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
