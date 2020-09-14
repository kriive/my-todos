package com.kriive.mytodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {
    private final Integer SECOND_ACTIVITY_REQUEST_CODE = 1337;

    private Context mContext;
    private ToDoFragment fragment = new ToDoFragment();
    private IllustrationFragment illustration = new IllustrationFragment();

    public void showIllustration() {
        FrameLayout frag = (FrameLayout) findViewById(R.id.fragment);
        frag.setVisibility(View.GONE);

        FrameLayout ill = (FrameLayout) findViewById(R.id.illustration);
        ill.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.illustration, illustration).addToBackStack(null).commit();
    }

    public void showList() {
        FrameLayout frag = (FrameLayout) findViewById(R.id.fragment);
        frag.setVisibility(View.VISIBLE);

        FrameLayout ill = (FrameLayout) findViewById(R.id.illustration);
        ill.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
    }

    protected void showOrderBy() {
        String[] singleItems = {"Creation time", "Category", "Due date"};

        new MaterialAlertDialogBuilder(this)
                .setTitle("Order by")
                .setSingleChoiceItems(singleItems, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Selected", String.valueOf(which));
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        MaterialToolbar myToolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.order:
                        showOrderBy();
                        return true;
                    default:
                        return false;
                }
            }
        });

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

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ToDo todo = (ToDo) data.getSerializableExtra(Intent.EXTRA_TEXT);
                if (todo != null) {
                    ToDoManager.getInstance().addToDo(todo);
                    checkEmpty();
                }
            }
        }
    }
}