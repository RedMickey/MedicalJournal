package com.example.michel.mycalendar2.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MedicalJournalAuthenticatorService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MedicalJournalAuthenticator authenticator = new MedicalJournalAuthenticator(this);
        return authenticator.getIBinder();
    }
}
