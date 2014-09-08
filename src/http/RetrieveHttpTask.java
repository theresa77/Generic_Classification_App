/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.genericclassificationapp.R;

import activity.UserScribbleMainActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import dialog.TransmissionToServerDialog;
import domain.ForeBackGround;
import domain.MinBoundingBox;
import domain.ObjectContour;
import domain.Picture;
import domain.Scribble;
 
/**
 * Task for sending picture and scribbles to a server.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class RetrieveHttpTask extends AsyncTask<Scribble[], Integer, String> {
 
	
	private static final String TAG = RetrieveHttpTask.class.getSimpleName();
	private TransmissionToServerDialog dialog;
	private UserScribbleMainActivity activity;
	private Boolean tranmissionComplete = false;
	
	public RetrieveHttpTask(UserScribbleMainActivity activity, TransmissionToServerDialog dialog){
		this.dialog = dialog;
		this.activity = activity;
	}
 
	/**
	 * Called when an instance of the class gets executed.
	 * Send picture and scribbles to a server and receive the response.
	 */
	@Override
		protected String doInBackground(Scribble[]... scribbles) {
		Log.d(TAG, "doInBackground() called");
		
		String result = "Error!";
		
		HttpClient httpclient = new DefaultHttpClient();
		
		// create httpPost object with IP and port for the server
		// TODO: set correct IP address and port in server.xml file
		String server = "http://" + R.string.server_ip + ":" + R.string.server_port;
		HttpPost httppost = new HttpPost(server);
		HttpResponse response;
	    
	    try {
	    	
	    	Picture mPicture = Picture.getInstance();
	    	Bitmap bitmap = mPicture.getBitmap();
	    	
	    	// get height and width of display
	        WindowManager winMan = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
			Display display = winMan.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			int displayHeight = metrics.heightPixels;
			int displayWidth = metrics.widthPixels;
	    	
	    	double width = 0.0;
	    	double height = 0.0;
	    	LinearLayout.LayoutParams pictureParams;
	    	
	    	if (!mPicture.isLandscape()) {
				height = (int) displayWidth * 1.33;
		    	width = displayWidth;
	    	} else {
	    		width = (int) displayHeight * 1.33;
		    	height = displayHeight;
	    	}
			
			pictureParams = new LinearLayout.LayoutParams((int)width, (int)height);
		    bitmap = Bitmap.createScaledBitmap(bitmap, pictureParams.width, pictureParams.height, true);
	    	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
	    	byte[] bitmapByteArray = byteStream.toByteArray(); // convert bitmap of picture to byte-array
	    	
	    	// create new bitmap for Foreground-Background scribbles only
			Bitmap foreBackScribble = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888); 
			Canvas foreBackCanvas = new Canvas(foreBackScribble);
			
			// create new bitmap for Object-Contour scribbles only
			Bitmap contourScribble = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas contourCanvas = new Canvas(contourScribble);
			
			// create list for min-bounding-rectangle scribbles
			List<Integer> MBRlist = new ArrayList<Integer>();
			if (mPicture.getScribbles() != null && !mPicture.getScribbles().isEmpty()) {
				for (Scribble s : mPicture.getScribbles()) {
					if(s instanceof ForeBackGround){
						s.drawScribble(foreBackCanvas); 
					} else if (s instanceof ObjectContour){
						s.drawScribble(contourCanvas);
					} else if (s instanceof MinBoundingBox){
						RectF rect = ((MinBoundingBox)s).getBoundingBoxOfScribble();
						MBRlist.add((int)rect.left);
						MBRlist.add((int)rect.top);
						MBRlist.add((int)rect.right);
						MBRlist.add((int)rect.bottom);
						Log.d(TAG, "add new coordinates from RectF object to list: left: "+rect.left+", top: "+rect.top+", right: "+rect.right+", bottom: "+rect.bottom);
					}
				}
			}
			
			ByteArrayOutputStream foreBackByteStream = new ByteArrayOutputStream();
			foreBackScribble.compress(Bitmap.CompressFormat.PNG, 100, foreBackByteStream);
			byte[] foreBackByteArray = foreBackByteStream.toByteArray(); // convert layer for foreground-background-scribbles to byte array
			
			ByteArrayOutputStream contourByteStream = new ByteArrayOutputStream();
			contourScribble.compress(Bitmap.CompressFormat.PNG, 100, contourByteStream);
			byte[] contourByteArray = contourByteStream.toByteArray(); // convert layer for object-contour-scribbles to byte array
			
			int[] minBoundRectArray = new int[MBRlist.size()];
			for(int i=0; i<MBRlist.size(); i++){
				minBoundRectArray[i] = MBRlist.get(i);
			}
			ByteBuffer byteBuffer = ByteBuffer.allocate(minBoundRectArray.length * 4);        
	        IntBuffer intBuffer = byteBuffer.asIntBuffer();
	        intBuffer.put(minBoundRectArray);
	        byte[] minBoundRectByteArray = byteBuffer.array(); // convert list of minimum-bounding-rectangle coordinates to byte array
	        
	    	// create JSONObject 
	        JSONObject jsonObject = new JSONObject();
	        try {
	        	bitmapByteArray = Base64.encode(bitmapByteArray, Base64.DEFAULT);
	        	foreBackByteArray = Base64.encode(foreBackByteArray, Base64.DEFAULT);
	        	contourByteArray = Base64.encode(contourByteArray, Base64.DEFAULT);	
	        	minBoundRectByteArray = Base64.encode(minBoundRectByteArray, Base64.DEFAULT);
	        	
	        	// add byte arrays of picture and scribbles to JSONObject
	        	jsonObject.put("picture", bitmapByteArray);
	        	jsonObject.put("landscape", mPicture.isLandscape());
	        	jsonObject.put("foreground-background", foreBackByteArray);
	        	jsonObject.put("object-contour", contourByteArray);
	        	jsonObject.put("min-bounding-rectangle", minBoundRectByteArray);
	            
	          } catch (JSONException e) {
	            e.printStackTrace();
	          }
	        
	        Log.d(TAG, "JSONObject length: "+jsonObject.length());
	        
	        final int totalProgress = 100;

	        // create Thread for updating the progress bar in transmission dialog
			Thread progressBarThread = new Thread() {
				@Override
				public void run() {
					int progress = 0;
					while (progress < totalProgress) {
						try {
							sleep(150);
							progress += 5;
							publishProgress(progress);
							if(progress == 100)
								tranmissionComplete = true;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					interrupt();
				}
			};

			progressBarThread.start();

			// add JsonObject to httpPost object
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("json", jsonObject.toString()));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        // Execute HTTP Post Request
	        response = httpclient.execute(httppost);
	        
	        // get response from server
	        HttpEntity entity = response.getEntity();

	        
	        if (entity != null) {
 
	            InputStream instream = entity.getContent();
	            BufferedReader r = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
 
	            StringBuilder total = new StringBuilder();
 
	            String line;
	            while ((line = r.readLine()) != null) {
	                total.append(line);
	            }
 
	            instream.close();
 
	            result = total.toString();
	            Log.d(TAG, "Response vom Server: " + result);
	            
	            while(!tranmissionComplete){
	            	// wait until Thread for ProgressBar is finished
	            }
	            Log.d(TAG, "tranmission complete: "+ tranmissionComplete);
	           
	        }
 
	        
	    } catch (ClientProtocolException e) {
	        Log.e(TAG, "ClientProtocolException");
	    	e.printStackTrace();
	    	
	    } catch (IOException e) {
	        Log.e(TAG, "IOException");
	    	e.printStackTrace();
	    }

		return result;
	}
 
	/**
	 * Called after doInBackground method.
	 * Set response message from server to transmission dialog.
	 */
	@Override
	protected void onPostExecute(String result) {
		Log.d(TAG, "onPostExecute() called");
		dialog.setResponseText(result);
		super.onPostExecute(result);
	}
	
	/**
	 * Called to update progress bar in transmission dialog.
	 */
	@Override
	protected void onProgressUpdate(Integer...progress){
		Log.d(TAG, "onProgressUpdate() called: "+progress[0]);
		dialog.updateProgressBar(progress[0]);
	}
	
}