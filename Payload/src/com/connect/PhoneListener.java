package com.connect;

import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;

public class PhoneListener extends PhoneStateListener
{
    private Context context;

    public PhoneListener(Context c) {
        context = c;
    }

    public void onCallStateChanged (int state, String incomingNumber)
    {
        switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
            Boolean stopped = context.stopService(new Intent(context, RecordService.class));
            
            break;
        case TelephonyManager.CALL_STATE_RINGING:
            break;
        case TelephonyManager.CALL_STATE_OFFHOOK:
	        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("RecordCalls", false))
	        {
	            Intent callIntent = new Intent(context, RecordService.class);
	            ComponentName name = context.startService(callIntent);
	            if (null == name) {
	            } else {
	            }
	        }
            break;
        }
    }
}
