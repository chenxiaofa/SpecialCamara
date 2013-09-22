package anong.candid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class MediaButtonReceiver extends BroadcastReceiver{
	private CallBack callback = null;
	private Context context = null;
	public MediaButtonReceiver(Context c){
		context = c;
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);  
		intentFilter.setPriority(2147483647);  
		context.registerReceiver(this, intentFilter);  
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("Media","botton pressed");
		if(callback!=null)
			callback.pressed();
	}
	public interface CallBack{
		public void pressed();
	}
	public void addCallBack(CallBack cb){
		callback = cb;
	}
	public void Destroy(){
		Log.i("","unregisterReceiver");
		context.unregisterReceiver(this);
		callback = null;
	}
}
