package com.passerbywhu.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by passe on 2017/4/5.
 */

public class ServiceContainer extends Service {
    private IValueService.Stub mBinder = new ValueService();
    private Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mHandler = new Handler();
        autoCreate();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        return super.onUnbind(intent);
    }

    private void autoCreate() {
        if (mBinder != null) {
            try {
                mBinder.setVal(mBinder.getVal() + 1);
            } catch (RemoteException mE) {
                mE.printStackTrace();
            }
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoCreate();
            }
        }, 1000);
    }
}
