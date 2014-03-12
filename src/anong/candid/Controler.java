package anong.candid;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceView;
public class Controler implements Rock.CallBack,PhotoTaker.CallBack,MediaButtonReceiver.CallBack{
	private Context context = null;
	private boolean processing = false;
	private Vibrator vibrator;
	private Rock rock = null;
	private SurfaceView surface = null;
	private PhotoTaker taker = null;
	private FloatWindow fw = null;
	private MediaButtonReceiver buttonreveive = null;
	public Controler(Context c){
		context = c;
		vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);   
		
		rock = new Rock(context);
		rock.addCallBack(this);
		
		buttonreveive = new MediaButtonReceiver(context);
		buttonreveive.addCallBack(this);
		
		surface = new SurfaceView(context.getApplicationContext());
		taker = new PhotoTaker(context,surface,configActivity.configure);
		taker.addCallBack(this);
		fw = new FloatWindow(context,surface);
	}
	public void Start(){
		rock.start();
		fw.start();
	}
	public void rocked() {
		if(taker.Status==PhotoTaker.STATUS_READY){
			if(!processing){
				processing = true;
				vibr();
				taker.take();
			}
		}
	}
	
	public void registed() {
		
	}
	public void taked() {
		vibrlong();
		processing = false;
	}

	public void statusChanged(int status) {
	}
	
    private void vibr(){
		long[] pattern = {0, 200, 100, 200}; // OFF/ON/OFF/ON...   
		vibrator.vibrate(pattern, -1);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    private void vibrlong(){
		long[] pattern = {0,200}; // OFF/ON/OFF/ON...   
		vibrator.vibrate(pattern, -1);
		Log.i("Activity","vibrlong");
    }
    public void Destroy(){
    	if(vibrator!=null){
    		vibrator = null;
    	}
    	if(rock!=null){
    		Log.i("","UnRegister");
    		rock.unregister();
    		rock.addCallBack(null);
    		rock = null;
    	}
    	if(taker!=null){
    		taker.onDestroy();
    		taker = null;
    	}
    	if(fw!=null){
    		fw.close();
    		if(surface!=null){
    			surface = null;
    		}
    		fw = null;
    	}
    	if(buttonreveive!=null){
    		buttonreveive.Destroy();
    		buttonreveive=null;
    	}

    	
    }
	public void pressed() {
		// TODO Auto-generated method stub
		rocked();
	}
	
}
