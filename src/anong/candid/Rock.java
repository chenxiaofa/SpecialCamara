package anong.candid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;



public class Rock implements SensorEventListener{
	private static final String TAG = "Rock";
	private CallBack callback = null;
	private  SensorManager sm;
	private boolean started = false;
	private Context context = null;
	public Rock(Context context){
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        int sensorType = Sensor.TYPE_ACCELEROMETER;
        sm.registerListener(this,sm.getDefaultSensor(sensorType),SensorManager.SENSOR_DELAY_GAME);
	}
	public void addCallBack(CallBack c){
		callback = c;
	}
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	public void start(){
		started = true;
	}

	private float arrX[] = {0,0,0,0,0,0,0,0,0,0};
	private float arrY[] = {0,0,0,0,0,0,0,0,0,0};
	private float arrZ[] = {0,0,0,0,0,0,0,0,0,0};
	private int p = 0;

	private static final int n = 20;
	
	public void onSensorChanged(SensorEvent sensorEvent){
		if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

			arrX[p] = sensorEvent.values[0];
			arrY[p] = sensorEvent.values[1];
			arrZ[p] = sensorEvent.values[2];
			
			p = (++p)%10;
			
			if(cp(fmax(arrX),fmin(arrX))>n || cp(fmax(arrY),fmin(arrY))>n || cp(fmax(arrZ),fmin(arrZ))>n){
				if(callback!=null)
					if(started)
						Log.i("","Rocked");
					callback.rocked();
			}

		}
	}
    private float fmax(float[] v){
    	float vmax = v[0];
    	for(int i=1;i<v.length;i++){
    		if(vmax<v[i])
    			vmax = v[i];
    	}
    	return vmax;
    }
    private float fmin(float[] v){
    	float vmin = v[0];
    	for(int i=1;i<v.length;i++){
    		if(vmin>v[i])
    			vmin = v[i];
    	}
    	return vmin;
    }
    private float cp(float v1,float v2){
    	return Math.abs(v1-v2);
    }
    public void unregister(){
    	sm.unregisterListener(this);
		Log.i(TAG,"unregisterListener");
    }
    public interface CallBack{
    	public void rocked();
    	public void registed();
    }
}
