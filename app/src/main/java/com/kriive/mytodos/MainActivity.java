package com.kriive.mytodos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Date;

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
        String[] choice = new String[]{"Due date", "Creation date", "Category"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Order by")
                .setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (fragment == null) {
                            dialog.dismiss();
                            return;
                        }
                        switch (which) {
                            case 0:
                                fragment.orderByDueDate();
                                break;
                            case 1:
                                fragment.orderByCreatedOn();
                                break;
                            case 2:
                                fragment.orderByCategory();
                                break;
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        mContext = this;

        JsonSaver saving = new JsonSaver(this);
        ToDoManager.getInstance().load(saving);
        checkEmpty();

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
    }

    @Override
    protected void onPause() {
        JsonSaver saving = new JsonSaver(this);
        ToDoManager.getInstance().save(saving);

        super.onPause();
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
        checkEmpty();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        CharSequence name = "MyTODOsReminderChannel";
        String description = "Channel for MyTODOs Reminders";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("mytodoReminder", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void scheduleNewNotification(final String eventString, final Date dueDate) {
        Intent intent = new Intent(MainActivity.this, ReminderBroadcastReceiver.class);
        intent.putExtra("Title", eventString);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long twentyFourHours = (60 * 1000 * 60 * 24);

        alarmManager.set(AlarmManager.RTC_WAKEUP, dueDate.getTime() - twentyFourHours, pendingIntent);
    }

}