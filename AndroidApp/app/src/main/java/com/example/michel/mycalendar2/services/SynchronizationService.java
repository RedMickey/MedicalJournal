package com.example.michel.mycalendar2.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SynchronizationService extends Service {
    public SynchronizationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
