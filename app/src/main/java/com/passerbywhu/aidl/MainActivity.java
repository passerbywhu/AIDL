package com.passerbywhu.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mMainProcess;
    private TextView mOtherProcess;
    private Button mButton;
    private IValueService.Stub.Proxy valueServiceProxy;
    private ServiceConnection mConn;
    private static final String TAG = "AIDL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainProcess = (TextView) findViewById(R.id.mainProcess);
        mOtherProcess = (TextView) findViewById(R.id.otherProcess);
        mButton = (Button) findViewById(R.id.refresh);
        Intent intent = new Intent(this, ServiceContainer.class);

        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (name.getClassName().equals(ServiceContainer.class.getName())) {
                    Log.d(TAG, "ServiceConnected " + name.getShortClassName());
                    valueServiceProxy = (IValueService.Stub.Proxy) IValueService.Stub.asInterface(service);
                    try {
                        valueServiceProxy.setVal(3);
                    } catch (RemoteException mE) {
                        mE.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (name.getClassName().equals(ServiceContainer.class.getName())) {
                    Log.d(TAG, "ServiceDisconnected " + name.getShortClassName());
                    valueServiceProxy = null;
                }
            }
        };

        bindService(intent, mConn, BIND_AUTO_CREATE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainProcess.setText(ValueService.value + "");
                if (valueServiceProxy != null) {
                    try {
                        mOtherProcess.setText(valueServiceProxy.getVal() + "");
                    } catch (RemoteException mE) {
                        mE.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mConn != null) {
            unbindService(mConn);
        }
        super.onDestroy();
    }
}
