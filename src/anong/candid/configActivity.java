package anong.candid;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class configActivity extends ListActivity {
	

	
	private static Context context = null;
	public static void writeConfigure(Configure conf){
    	File configFile = new File(context.getFilesDir().getPath()+"config.cfg");
    	if(configFile.exists())
    		configFile.delete();
    	
    	try {
			configFile.createNewFile();
	    	ObjectOutputStream  objoutput = new ObjectOutputStream (new FileOutputStream(configFile));
	    	objoutput.writeObject(conf);
	    	objoutput.flush();
	    	objoutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Configure getConfigure(){
        Configure conf = null;
    	File configFile = new File(context.getFilesDir().getPath()+"config.cfg");
    	if(!configFile.exists()){
	        return new Configure();
    	}else{
    		try {
				ObjectInputStream objinput = new ObjectInputStream(new FileInputStream(configFile));
				conf = (Configure)objinput.readObject();
				objinput.close();
    		} catch (Exception e) {
    			Log.e("Error",e.getMessage());
				e.printStackTrace();
			} 
    	}
    	return conf;
	}
	public void onDestroy(){
		configActivity.writeConfigure(configActivity.cnf);
		CameraServer.StartCameraPerview();
		super.onDestroy();
	}
	public void onPause(){
		super.onPause();
		this.finish();
	}
	public static Configure cnf = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        cnf = getConfigure();
        setTitle("Setting");
        CreateConfigurationView();

    }
    private List<List<Object>> items =new ArrayList<List<Object>>();
	private void CreateConfigurationView(){
	   	items.add(newSpinner(this,"≈ƒ’’«∞∂‘Ωπ",Configure.focus,(cnf.autoFocus?"Yes":"No"),new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				((TextView)items.get(0).get(1)).setText(Configure.focus[position]);
				cnf.autoFocus = Configure.focus[position].equals("Yes");
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
    	}));
    	Log.i("size",cnf.psize.getText());
    	items.add(newSpinner(this,"’’∆¨≥ﬂ¥Á",cnf.sizes,cnf.psize.getText(),new AdapterView.OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				((TextView)items.get(1).get(1)).setText(cnf.sizes[position]);
				cnf.psize.setSize(cnf.sizes[position]);
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
    	}));
    	
    	
    	
    	this.setListAdapter (new MyAdapter(this));
    	
    }
	private ArrayList<Object> newSpinner(Context context,String text,String[] data,String defaultvalue,AdapterView.OnItemSelectedListener listener){
    	TextView txtview = null;
    	Spinner spinner = null;

    	
    	ArrayList<Object> row = new ArrayList<Object>();
    	
    	txtview = new TextView(context);
    	txtview.setText(text);
    	txtview.setTextSize(20);
    	txtview.setPadding(20, 20,0, 25);
    	TextPaint paint = txtview.getPaint();
    	paint.setFakeBoldText(true); 
    	row.add(txtview);
    	txtview = new TextView(context);
    	txtview.setText(defaultvalue);
    	txtview.setPadding(20, 65, 0, 5);
    	row.add(txtview);
    	
    	spinner = new Spinner(configActivity.this);
    	
    	ArrayAdapter<String> adt = 	new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, data);
    	adt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    	spinner.setAdapter(adt);
    	spinner.setVisibility(View.INVISIBLE);
    	spinner.setOnItemSelectedListener(listener);
    	for(int i=0;i<data.length;i++){
    		Log.i(data[i],defaultvalue);
    		if(data[i].equals(defaultvalue))
    			spinner.setSelection(i);
    	}
    	row.add(spinner);
    	return row;
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) { 

		Spinner sp = (Spinner) (((List<Object>)items.get(position)).get(2));
		sp.performClick();
	}
	public class MyAdapter extends BaseAdapter{

		private boolean[] isloaded = null;
		private LayoutInflater mInflater;
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
			isloaded = new boolean[items.size()];
		}
		public int getCount() {
			return items.size();
		}
		public Object getItem(int arg0) {
			return null;
		}
		public long getItemId(int arg0) {
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {			
			if(convertView==null){
					convertView = mInflater.inflate(R.layout.confview,null);
					RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.rlayout);
					List<Object> row = (List<Object>) items.get(position);
					if(!isloaded[position]){
						for(int i = row.size()-1; i >= 0;i--){
							View v = (View)row.get(i);
							layout.addView(v);
						}
					}
					convertView.setTag(layout);
			}
			if(!isloaded[position])
				isloaded[position]= true;
			return convertView;
		}
	}
}
