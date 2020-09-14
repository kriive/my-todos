package com.kriive.mytodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private final Integer SECOND_ACTIVITY_REQUEST_CODE = 1337;

    private Context mContext;
    private ToDoFragment fragment = new ToDoFragment();
    private IllustrationFragment illustration = new IllustrationFragment();

    public void showIllustration() {
        FrameLayout frag = (FrameLayout) findViewById (R.id.fragment);
        frag.setVisibility (View.GONE); // or View.INVISIBLE, depending on what you exactly want

        FrameLayout ill = (FrameLayout) findViewById (R.id.illustration);
        ill.setVisibility (View.VISIBLE); // or View.INVISIBLE, depending on what you exactly want

        getSupportFragmentManager().beginTransaction().replace(R.id.illustration, illustration).addToBackStack(null).commit();
    }

    public void showList() {
        FrameLayout frag = (FrameLayout) findViewById (R.id.fragment);
        frag.setVisibility (View.VISIBLE); // or View.INVISIBLE, depending on what you exactly want

        FrameLayout ill = (FrameLayout) findViewById (R.id.illustration);
        ill.setVisibility (View.GONE); // or View.INVISIBLE, depending on what you exactly want

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        ExtendedFloatingActionButton fabExt = (ExtendedFloatingActionButton) findViewById(R.id.create_task_fab);
        fabExt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateTaskActivity.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        checkEmpty();
    }

    public void checkEmpty() {
        if (!ToDoManager.getInstance().isEmpty()) {
            showList();
        } else {
            showIllustration();
        }
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // get String data from Intent
                ToDo todo = (ToDo) data.getSerializableExtra(Intent.EXTRA_TEXT);
                if (todo != null) {
                    ToDoManager.getInstance().addToDo(todo);
                    checkEmpty();
                }
            }
        }
    }
}