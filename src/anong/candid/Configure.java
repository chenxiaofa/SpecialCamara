package anong.candid;

import java.io.Serializable;

import android.hardware.Camera;
import android.util.Log;

public class Configure  implements Serializable{
	public class Size implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public int width=0;
		public int height=0;
		public Size(int w,int h){
			width = w;
			height = h;
		}
		public Size(String sSize){
			setSize(sSize);
		}
		public void setSize(String sSize){
			String[] s = sSize.split("x");
			width = Integer.valueOf(s[0]);
			height = Integer.valueOf(s[1]);;
		}
		public String getText(){
			return String.valueOf(width) + "x" + String.valueOf(height);
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String[] focus={"Yes","No"};
	public String sizes[] = null;
	public Size psize  = null;
	public Size previewSize = null;
	public boolean autoFocus = false;
	public Configure(){
		Camera camera = Camera.open();
		Camera.Parameters params = camera.getParameters();
		String picture_size_values[] = params.get("picture-size-values").split(",");
		String preview_size_values[] = params.get("preview-size-values").split(",");
		psize       = new Size(picture_size_values[0]);
		previewSize = new Size(preview_size_values[0]);
		sizes = new String[picture_size_values.length];
		int i = 0;
		for(String size:picture_size_values){
			sizes[i++] = size;
		}
		autoFocus = true;
		camera.release();
		camera = null;
	}
}
