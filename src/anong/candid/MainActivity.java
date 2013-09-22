package anong.candid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
public class MainActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏           
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        
        (new Thread(){
        	public void run(){
                try {
        			Thread.sleep(1500);
        		} catch (InterruptedException e) {
        			e.printStackTrace();
        		}
                startService(new Intent(MainActivity.this, CameraServer.class));  
                finish();
        	}
        }).start();
    }    

}
