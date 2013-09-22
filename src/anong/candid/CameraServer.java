package anong.candid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class CameraServer extends  Service {
	/*
	this.powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);  
    this.wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
    												this.getClass().getName());
    */
	private PowerManager powerManager = null;   
	private WakeLock wakeLock = null;
    private void screenoff(){
    	this.wakeLock.release();
        this.wakeLock = this.powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        this.wakeLock.acquire();
    }
	public static Controler controler = null;
	public static CameraServer context = null;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	public void onCreate(){
		context = this;
		StartConfigActivity();
		this.powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);  
	    this.wakeLock = this.powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
	    												this.getClass().getName());
	    this.wakeLock.acquire();
	}
	public void restart(){
		
	}
	public static void StartConfigActivity(){
		StopCameraPerview();
		Intent i = new Intent(context.getApplicationContext(),configActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.getApplication().startActivity(i);
	}
	public static void StartCameraPerview(){
		controler = new Controler(context.getApplicationContext());
		controler.Start();
	}
	public static void StopCameraPerview(){
		if(controler!=null)
			controler.Destroy();
	}
	public static void StopCameraServer(){
		StopCameraPerview();
		context.stopSelf();
		context.onDestroy();
		System.exit(0);
	}
	public void onDestroy(){
		
		StopCameraPerview();
		this.wakeLock.release();
		this.powerManager = null;
		this.wakeLock = null;
		controler = null;
		context = null;
	}

}
