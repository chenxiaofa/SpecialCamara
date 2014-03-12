package anong.candid;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PhotoTaker implements SurfaceHolder.Callback{
	
	static final int STATUS_UNKNOWERROR = -1;
	static final int STATUS_SDCARDDNOTREADY = 0;
	static final int STATUS_READY = 1;
	static final int STATUS_TAKINGPHOTO = 2;
	
	public int Status = -1;
	private Camera mCamera = null;
	private Context context = null;
	private Configure conf=null;
	public PhotoTaker(Context c,SurfaceView surface,Configure cfg){
		context = c;
		conf = cfg;
		surface.getHolder().addCallback(this);
		surface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	
	
	/**************************
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 **************************/
	public void cameraInit(){
		mCamera = Camera.open();
		Camera.Parameters params = mCamera.getParameters();
		params.setPictureSize(conf.psize.width, conf.psize.height);
		params.setPreviewSize(conf.previewSize.width, conf.previewSize.height);
		if(Integer.valueOf(android.os.Build.VERSION.SDK)<=4)
			params.set("orientation", "portrait");
		else
			setDisplayOrientation(mCamera,90,Integer.valueOf(android.os.Build.VERSION.SDK));
		mCamera.setParameters(params);
		Log.i("CameraInfo",params.flatten());
	}
	
	private void setDisplayOrientation(Camera camera, int angle,int sdk) {
		Method downPolymorphic;
		
		if (sdk <= 4)
			return;

		try {
			if (sdk > 4 && sdk < 8) {

				// parameters for pictures created by a Camera service.
				Camera.Parameters parameters = mCamera.getParameters();

				// 2.0, 2.1
				downPolymorphic = parameters.getClass().getMethod(
						"setRotation", new Class[] { int.class });
				if (downPolymorphic != null)
					downPolymorphic.invoke(parameters, new Object[] { angle });

				// Sets the Parameters for pictures from this Camera
				// service.
				mCamera.setParameters(parameters);

			} else {

				downPolymorphic = camera.getClass().getMethod(
						"setDisplayOrientation", new Class[] { int.class });
				if (downPolymorphic != null)
					downPolymorphic.invoke(camera, new Object[] { angle });
			}
		} catch (Exception e) {
		}
	}
	
	
	
	
	/**************************
	 * 
	 * 
	 * 
	 * when the surface is loaded
	 * 	connect the camera.
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 **************************/
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i("PhotoTaker","surfaceCreated");
		cameraInit();
		
	}	
	
	
	
	/**************************
	 * 
	 * 
	 * 
	 * when the surface sizechange 
	 * change the camera accordingly
	 * 
	 * 
	 **************************/
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if(mCamera!=null){
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
				
			} catch (Exception e) {
				e.printStackTrace();
				statusChanged(STATUS_UNKNOWERROR);
			}
		}
		
			if(SDCard_is_ready()){
				statusChanged(STATUS_READY);
			}else{
				statusChanged(STATUS_SDCARDDNOTREADY);
			}
	}
	
	private void statusChanged(int status){
		Status = status;
		if(callback!=null)
			callback.statusChanged(status);
	}
	
	
	/***********************
	 * 
	 * destroy
	 * 
	 ***********************/
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.onDestroy();
	}
	
	/***********************
	 * 
	 * release resources
	 * 
	 ***********************/
	public void onDestroy(){
		if(mCamera!=null){
			mCamera.release();
			mCamera=null;
		}
	}
	
	
	/****************************************************
	 * 
	 *
	 * 
	 * 	autoFocus and Take a photo 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 ****************************************************/
	public void take(){
		Log.i("PhotoTaker","autoFocusing");
		if(Status==STATUS_READY){
			statusChanged(STATUS_TAKINGPHOTO);
			if(conf.autoFocus){
				mCamera.autoFocus(new Camera.AutoFocusCallback() {
					public void onAutoFocus(boolean success, Camera camera) {
						Log.i("PhotoTaker","autoFocused");
						Log.i("PhotoTaker","taking Photo");
						camera.takePicture(null, null, pictureCallBack);
						
					}
				});
			}else{
				mCamera.takePicture(null, null, pictureCallBack);
			}
			
		}
		
		
	}
	private Camera.PictureCallback pictureCallBack = new Camera.PictureCallback(){
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i("PhotoTaker","Saving");
			File file = new File(getOutputPath());
			if(file.exists())
				file.delete();
			try {
				RandomAccessFile rfile = new RandomAccessFile(file, "rw");
				rfile.write(data);
				rfile.close();
				Uri uri = Uri.parse("file://"+file.getPath());
				context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
				rfile = null;
				file  = null;
				camera.startPreview();
				if(callback!=null)
					callback.taked();
				statusChanged(STATUS_READY);								
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	};
	public void s(){
		Log.i("Status",String.valueOf(Status));
	}
	/***************************************
	 * 
	 * 
	 * 
	 * 	return a path for saving photo
	 * 
	 * 
	 * 
	 ************************************** */
	private static String getOutputPath(){ 
		if(!SDCard_is_ready())
			return null;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/CandidCamera/"); 
        Log.w("FileDir", mediaStorageDir.getPath());
        if (! mediaStorageDir.exists()){ 
            if (! mediaStorageDir.mkdirs()){ 
                Log.d("MyCameraApp", "failed to create directory"); 
                return null; 
            } 
        } 

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); 
        return mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg"; 

    }
	/***********************************
	 * 
	 * 	check if the SDCard is ready! 
	 * 
	 * 
	 * 
	 ***********************************/
    private static boolean SDCard_is_ready(){
    	if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
    	{
    		Log.e("Error", "SD Card not ready");
    		return false;
    	}
    	return true;
    }
    
    /*********************************
     * 
     * 
     * CallBack
     * 
     * 
     * 
     * 
     *********************************/
    public interface CallBack{
    	public void taked();
    	public void statusChanged(int status);
    }
    public CallBack callback = null;
    
    /********************
     * 
     * Link the callback
     * 
     * 
     * 
     **********************/
    public void addCallBack(CallBack cb){
    	callback = cb;
    }
	
}
