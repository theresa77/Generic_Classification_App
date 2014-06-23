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

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import dialog.TransmissionToServerDialog;
import domain.Picture;
 
/**
 * Task class for sending picture and scribbles to a server.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class RetrieveHttpTask extends AsyncTask<byte[], Integer, String> {
 
	
	private static final String TAG = RetrieveHttpTask.class.getSimpleName();
	private TransmissionToServerDialog dialog;
	private String tag;
	private Boolean tranmissionComplete = false;
	
	public RetrieveHttpTask(String tag, TransmissionToServerDialog dialog){
		this.dialog = dialog;
		this.tag = tag;
	}
 
	/**
	 * Called when an instance of the class gets executed.
	 * Send picture and scribbles to a server and receive the response.
	 */
	@Override
	protected String doInBackground(byte[]... scribbles) {
		Log.d(TAG, "doInBackground() called");
		
		String result = "Error!";
		
		HttpClient httpclient = new DefaultHttpClient();
		
		// create httpPost object with ip and port for the server
	    HttpPost httppost = new HttpPost("http://192.168.43.29:8000");    
	    HttpResponse response;
	    
	    try {
	    	
	    	Picture mPicture = Picture.getInstance();
	    	Bitmap bitmap = mPicture.getBitmap();
	    	ByteArrayOutputStream stream = new ByteArrayOutputStream();	        	
	    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    	
	    	// create JSONObject 
	        JSONObject jsonObject = new JSONObject();
	        try {
	        	byte[] pictureEncoded = Base64.encode(stream.toByteArray(), Base64.DEFAULT);
	        	byte[] scribblesEncoded = Base64.encode(scribbles[0], Base64.DEFAULT);
	        	byte[] tagEncoded = Base64.encode(tag.getBytes(), Base64.DEFAULT);
	        	
	        	// add byte array of picture and scribble to JSONObject
	        	jsonObject.put("picture", pictureEncoded);
	        	jsonObject.put("landscape", mPicture.isLandscape());
	        	jsonObject.put("scribbles", scribblesEncoded);
	        	jsonObject.put("tag",tagEncoded); 
	            
	          } catch (JSONException e) {
	            e.printStackTrace();
	          }
	        
	        Log.d(TAG, "JSONObject lenght: "+jsonObject.length());
	        
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