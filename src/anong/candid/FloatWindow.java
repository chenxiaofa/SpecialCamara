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
	 * ����������
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
        
        // ����window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * �������Ϊparams.type = WindowManager.LayoutParams.TYPE_PHONE;
         * ��ô���ȼ��ή��һЩ, ������֪ͨ�����ɼ�
         */
        
        params.format = PixelFormat.RGBA_8888; // ����ͼƬ��ʽ��Ч��Ϊ����͸��
        
        // ����Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * �����flags���Ե�Ч����ͬ����������
         * ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
         */
        
        // �����������ĳ��ÿ�
        params.width = configActivity.configure.previewSize.width;
        params.height = configActivity.configure.previewSize.height;
        // ������������Touch����
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
						// ����������λ��
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
