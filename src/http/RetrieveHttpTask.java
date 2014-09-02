/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import view.UserScribbleView;

import activity.UserScribbleMainActivity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import dialog.TransmissionToServerDialog;
import domain.ForeBackGround;
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
//	private String tag;
	private Boolean tranmissionComplete = false;
	
//	public RetrieveHttpTask(String tag, TransmissionToServerDialog dialog){
//		this.dialog = dialog;
//		this.tag = tag;
//	}
	
	public RetrieveHttpTask(TransmissionToServerDialog dialog){
		this.dialog = dialog;
	}
 
	/**
	 * Called when an instance of the class gets executed.
	 * Send picture and scribbles to a server and receive the response.
	 */
	@Override
//	protected String doInBackground(byte[]... scribbles) {
		protected String doInBackground(Scribble[]... scribbles) {
		Log.d(TAG, "doInBackground() called");
		
		String result = "Error!";
		
		HttpClient httpclient = new DefaultHttpClient();
		
		// create httpPost object with ip and port for the server
	    HttpPost httppost = new HttpPost("http://192.168.43.29:8000");    
	    HttpResponse response;
	    
	    try {
	    	
	    	Picture mPicture = Picture.getInstance();
	    	Bitmap bitmap = mPicture.getBitmap();
	    	
	    	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	    	
	    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
	    	byte[] bitmapByteArray = byteStream.toByteArray();
	    	
	    	// create new bitmap for Foreground-Background scribbles only
			Bitmap foreBackScribble = Bitmap.createBitmap( ((UserScribbleMainActivity)dialog.getActivity()).getDisplayWidth(),
										((UserScribbleMainActivity)dialog.getActivity()).getDisplayHeight(), 
										Bitmap.Config.ARGB_8888); 
			Canvas foreBackCanvas = new Canvas(foreBackScribble);
			
			// create new bitmap for Object-Contour scribbles only
			Bitmap contourScribble = Bitmap.createBitmap(
					((UserScribbleMainActivity) dialog.getActivity())
							.getDisplayWidth(),
					((UserScribbleMainActivity) dialog.getActivity())
							.getDisplayHeight(), Bitmap.Config.ARGB_8888);
			Canvas contourCanvas = new Canvas(contourScribble);
			
			//TODO: create array for minimum bounding box coordinates and fill with coordinates (left, top, right, bottom)
			int[] minBoundRectArray = new int[]{};
			
			// draw scribbles at new canvas objects 
			if (mPicture.getScribbles() != null && !mPicture.getScribbles().isEmpty()) {
				for (Scribble s : mPicture.getScribbles()) {
					if(s instanceof ForeBackGround){
						s.drawScribble(foreBackCanvas);
					} else if (s instanceof ObjectContour){
						s.drawScribble(contourCanvas);
					}
				}
			}
			
//			ByteArrayOutputStream scribbbleByteStream = new ByteArrayOutputStream();
			
			foreBackScribble.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
			byte[] foreBackByteArray = byteStream.toByteArray();
			
			contourScribble.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
			byte[] contourByteArray = byteStream.toByteArray();
			
			byte[] minBoundRectByteArray = new byte[minBoundRectArray.length];
			byteStream.write(minBoundRectByteArray);
			minBoundRectByteArray = byteStream.toByteArray();
	    	
	    	// create JSONObject 
	        JSONObject jsonObject = new JSONObject();
	        try {
	        	bitmapByteArray = Base64.encode(bitmapByteArray, Base64.DEFAULT);
	        	foreBackByteArray = Base64.encode(foreBackByteArray, Base64.DEFAULT);
	        	contourByteArray = Base64.encode(contourByteArray, Base64.DEFAULT);	
	        	minBoundRectByteArray = Base64.encode(minBoundRectByteArray, Base64.DEFAULT);
	        	
	        	// add byte array of picture and scribble to JSONObject
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