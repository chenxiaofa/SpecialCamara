package anong.candid;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatWindow {
	/**
	 * 创建悬浮窗
	 */
	private static WindowManager wm;
	private boolean ending = false;
	private static WindowManager.LayoutParams params;
	private Context context = null;
	private View view = null;
	public FloatWindow(Context c,View v) {
		context = c;
        view = v;
        ending = false;
        wm = (WindowManager) context.getApplicationContext()
        	.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        
        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
         * 那么优先级会降低一些, 即拉下通知栏不可见
         */
        
        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        
        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
         */
        
        // 设置悬浮窗的长得宽
        params.width = configActivity.configure.previewSize.width;
        params.height = configActivity.configure.previewSize.height;
        // 设置悬浮窗的Touch监听
        Init();
        v.setOnTouchListener(new View.OnTouchListener() {
        	int lastX, lastY;
        	int paramX, paramY;
        	
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				if(!ending){
					switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						lastX = (int) event.getRawX();
						lastY = (int) event.getRawY();
						paramX = params.x;
						paramY = params.y;
						break;
					case MotionEvent.ACTION_MOVE:
						if(removed)
							break;
						int dx = (int) event.getRawX() - lastX;
						int dy = (int) event.getRawY() - lastY;
						params.x = paramX + dx;
						params.y = paramY + dy;
						// 更新悬浮窗位置
				        wm.updateViewLayout(v, params);
						break;
					}
				}
				return true;
			}

		});

        
	}
	public void start(){
		wm.addView(view, params);
	}
	private GestureDetector mGestureDetector;
	private void Init(){
		mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
	    public void onLongPress(MotionEvent e) {
	    	CameraServer.StopCameraServer();
	    }
	    public boolean onSingleTapUp(MotionEvent e){
	    	Log.i("","i'm here");
	    	CameraServer.StartConfigActivity();
			return false;
	    }
	});
	}
	
	private boolean removed = false;
	public void close(){
		if(!removed){
			removed = true;
			wm.removeView(view);
		}
	}
}
