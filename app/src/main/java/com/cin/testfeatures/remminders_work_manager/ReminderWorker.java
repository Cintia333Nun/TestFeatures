package com.cin.testfeatures.remminders_work_manager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker extends Worker {
    public static final String KEY_TITLE  = "key_title";
    public static final String KEY_MESSAGE  = "key_message";
    private final Context context;

    public ReminderWorker(Context context, WorkerParameters params) {
        super(context,params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        new NotificationHelper(context).createNotification(
                getInputData().getString(KEY_TITLE),
                getInputData().getString(KEY_MESSAGE)
        );

        return Result.success();
    }
}
