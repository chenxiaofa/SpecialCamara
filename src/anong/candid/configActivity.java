package anong.candid;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

public class configActivity extends Activity {
	

	
	private static Context context = null;

	
	public void onDestroy(){
		super.onDestroy();
		CameraServer.StartCameraPerview();
	}
	
	public void onPause(){
		super.onPause();
		this.finish();
	}
	
	public static Configure configure = null;
	SharedPreferences preferences = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this.getApplicationContext();
        configure = new Configure();
        preferences = context.getApplicationContext().getSharedPreferences("setting",0);
        
        configure.psize.width = preferences.getInt("picture_width", 0);
        configure.psize.height = preferences.getInt("picture_height", 0);
        configure.autoFocus = preferences.getBoolean("autofocus", true);
        this.setContentView(R.layout.new_setting_layout);
        
        
        final Spinner photo_size_spinner = new Spinner(configActivity.this);
        ArrayAdapter<String> photo_size_adt = 	new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, configure.sizes);
        photo_size_adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	photo_size_spinner.setAdapter(photo_size_adt);
    	//photo_size_spinner.setVisibility(View.INVISIBLE);
    	photo_size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				configure.psize.setSize(configure.sizes[position]);
				Editor editor = preferences.edit();
				editor.putInt("picture_width", configure.psize.width);
				editor.putInt("picture_height", configure.psize.height);
				editor.commit();
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
    	});
    	//photo_size_spinner.setSelection(0);
    	for(int i=0;i<configure.sizes.length;i++){
    		if(configure.sizes[i].equals(configure.psize.getText()))
    			photo_size_spinner.setSelection(i);
    	}
    	
    	final Spinner autofocus_spinner = new Spinner(configActivity.this);
        ArrayAdapter<String> autofocus_adt = 	new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, Configure.focus);
        autofocus_adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autofocus_spinner.setAdapter(autofocus_adt);

    	autofocus_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
    		public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				configure.autoFocus = Configure.focus[position].equals("Yes");
				preferences.edit().putBoolean("autofocus", configure.autoFocus).commit();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
    		
    	});
    	//autofocus_spinner.setSelection(0);
    	for(int i=0;i<Configure.focus.length;i++){
    		if(Configure.focus[i].equals(configure.autoFocus?"Yes":"No"))
    			autofocus_spinner.setSelection(i);
    	}
    	
    	
    	TableRow clickable_photosize = (TableRow)this.findViewById(R.id.row_photosize);
    	clickable_photosize.addView(photo_size_spinner);

    	TableRow clickable_autofocus = (TableRow)this.findViewById(R.id.row_autofocus);
        clickable_autofocus.addView(autofocus_spinner);
    }
}
