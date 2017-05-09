package com.passerbywhu.aidl;

import android.os.RemoteException;

/**
 * Created by passe on 2017/4/5.
 */

public class ValueService extends IValueService.Stub {
    public static int value;

    @Override
    public void setVal(int val) throws RemoteException {
        value = val;
    }

    @Override
    public int getVal() throws RemoteException {
        return value;
    }
}
