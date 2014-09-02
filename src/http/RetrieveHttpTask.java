/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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
//	private String tag;
	private Boolean tranmissionComplete = false;
	
//	public RetrieveHttpTask(String tag, TransmissionToServerDialog dialog){
//		this.dialog = dialog;
//		this.tag = tag;
//	}
	
	public RetrieveHttpTask(UserScribbleMainActivity activity, TransmissionToServerDialog dialog){ //TODO: activity variable vll wieder entfernen
		this.dialog = dialog;
		this.activity = activity;
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
			
			// create new layout parameter object with calculated width and height for the bitmap
		    pictureParams = new LinearLayout.LayoutParams((int)width, (int)height);
		    
		    // creates scaled bitmap and adds it to view
		    bitmap = Bitmap.createScaledBitmap(bitmap, pictureParams.width, pictureParams.height, true);
	    	
	    	
//		    Bitmap testBitmap1 = bitmap;					//################# start
//	    	Bitmap testBitmap2 = bitmap;
//	    	Canvas testCanvas1 = new Canvas(testBitmap1);
//	    	Canvas testCanvas2 = new Canvas(testBitmap2);	//################# end
	    	
	    	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	    	
	    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
	    	byte[] bitmapByteArray = byteStream.toByteArray();
	    	
	    	// create new bitmap for Foreground-Background scribbles only
//			Bitmap foreBackScribble = Bitmap.createBitmap( ((UserScribbleMainActivity)dialog.getActivity()).getDisplayWidth(), ((UserScribbleMainActivity)dialog.getActivity()).getDisplayHeight(), 
//										Bitmap.Config.ARGB_8888); 
	    	Bitmap foreBackScribble = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888); 
			Canvas foreBackCanvas = new Canvas(foreBackScribble);
			
			
			// create new bitmap for Object-Contour scribbles only
//			Bitmap contourScribble = Bitmap.createBitmap( ((UserScribbleMainActivity) dialog.getActivity()).getDisplayWidth(), ((UserScribbleMainActivity) dialog.getActivity()) .getDisplayHeight(), Bitmap.Config.ARGB_8888);
			Bitmap contourScribble = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas contourCanvas = new Canvas(contourScribble);
			
			//TODO: folgende bitmap und canvas object wieder löschen - nur zum testen !!!
			Bitmap mbrScribble = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas mbrCanvas = new Canvas(mbrScribble);
			
			List<Integer> MBRlist = new ArrayList<Integer>();
			
			// draw scribbles at new canvas objects 
			if (mPicture.getScribbles() != null && !mPicture.getScribbles().isEmpty()) {
				for (Scribble s : mPicture.getScribbles()) {
					if(s instanceof ForeBackGround){
						s.drawScribble(foreBackCanvas); 
//						s.drawScribble(testCanvas1);//##########
					} else if (s instanceof ObjectContour){
						s.drawScribble(contourCanvas);
//						s.drawScribble(testCanvas2);//########
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
//			testBitmap1.compress(Bitmap.CompressFormat.PNG, 100, foreBackByteStream); ///######
			byte[] foreBackByteArray = foreBackByteStream.toByteArray();
			
			ByteArrayOutputStream contourByteStream = new ByteArrayOutputStream();
			contourScribble.compress(Bitmap.CompressFormat.PNG, 100, contourByteStream);
//			testBitmap2.compress(Bitmap.CompressFormat.PNG, 100, contourByteStream); //#######
			byte[] contourByteArray = contourByteStream.toByteArray();
			
			int[] minBoundRectArray = new int[MBRlist.size()];
			for(int i=0; i<MBRlist.size(); i++){
				minBoundRectArray[i] = MBRlist.get(i);
			}
			// int-array to byte-array	
			ByteBuffer byteBuffer = ByteBuffer.allocate(minBoundRectArray.length * 4);        
	        IntBuffer intBuffer = byteBuffer.asIntBuffer();
	        intBuffer.put(minBoundRectArray);
	        byte[] minBoundRectByteArray = byteBuffer.array();
	        
	        
			
//			####################### start
			
			
//			// get storage directory
//			File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//		              Environment.DIRECTORY_PICTURES), "GenericClassificationApp");
//		    // Create the storage directory if it does not exist
//		    if (! mediaStorageDir.exists()){
//		        if (! mediaStorageDir.mkdirs()){
//		            Log.d("GenericClassificationApp", "failed to create directory");
//		        }
//		    }
//		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		    
		    // FORE_BACK_GROUND
//		    File foreBackMediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_1"+ timeStamp + ".jpg");
//		    FileOutputStream fos1 = new FileOutputStream(foreBackMediaFile);
//		    
//		    try {
//				fos1.write(foreBackByteArray);
//				fos1.flush();
//				
//				// save file in gallery
//			    Intent mediaScanIntent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//			    File f = new File(foreBackMediaFile.getAbsolutePath());
//			    Uri contentUri = Uri.fromFile(f);
//			    mediaScanIntent1.setData(contentUri);
//			    activity.sendBroadcast(mediaScanIntent1);
//				
//				fos1.close();
//				Log.d(TAG, "Picture FORE-BACK-GROUND saved");
//			} catch (FileNotFoundException e) {
//				Log.d(TAG, "File not found: " + e.getMessage());
//			} catch (IOException e) {
//				Log.d(TAG, "Error accessing file: " + e.getMessage());
//			}
//			
//		    // OBJECT-CONTOUR
//		    File contourMediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_2"+ timeStamp + ".jpg");		    
//		    FileOutputStream fos2 = new FileOutputStream(contourMediaFile);
//		       
//		    try {
//				fos2.write(contourByteArray);
//				fos2.flush();
//				
//				// save file in gallery
//			    Intent mediaScanIntent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//			    File f = new File(contourMediaFile.getAbsolutePath());
//			    Uri contentUri = Uri.fromFile(f);
//			    mediaScanIntent2.setData(contentUri);
//			    activity.sendBroadcast(mediaScanIntent2);
//				
//				fos2.close();
//				Log.d(TAG, "Picture OBJECT-CONTOUR saved");
//			} catch (FileNotFoundException e) {
//				Log.d(TAG, "File not found: " + e.getMessage());
//			} catch (IOException e) {
//				Log.d(TAG, "Error accessing file: " + e.getMessage());
//			}
		    
		    
//		    // MIN-BOUND-BOX
//		    int countLeft = 0;
//		    int countTop = 1;
//		    int countRight = 2;
//		    int countBottom = 3;
//		    Paint mPaint = new Paint();
//			mPaint.setStyle(Paint.Style.STROKE);
//			mPaint.setColor(Color.BLUE);
//			mPaint.setStrokeWidth(10);
//			mPaint.setAntiAlias(true);
//			mPaint.setDither(true);
//			mPaint.setStrokeJoin(Paint.Join.ROUND);
//			mPaint.setStrokeCap(Paint.Cap.ROUND);
//			
//		    while(countBottom < minBoundRectArray.length){
//		    	RectF rectf = new RectF(minBoundRectArray[countLeft], minBoundRectArray[countTop], minBoundRectArray[countRight], minBoundRectArray[countBottom]);
//		    	mbrCanvas.drawRect(rectf, mPaint);
//		    	Log.d(TAG, "draw Rect to mbrCanvas: left: "+rectf.left+", top: "+rectf.top+", right: "+rectf.right+", bottom: "+rectf.bottom);
//		    	countLeft += 4;
//		    	countTop += 4;
//		    	countRight += 4;
//		    	countBottom += 4;
//		    }
//		    
//		    ByteArrayOutputStream MBRbyteStream2 = new ByteArrayOutputStream();
//		    mbrScribble.compress(Bitmap.CompressFormat.PNG, 100, MBRbyteStream2);
//		    byte[] MBRByteArray2 = MBRbyteStream2.toByteArray();
//		    
//		    File mbrMediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_3"+ timeStamp + ".jpg");
//		    FileOutputStream fos3 = new FileOutputStream(mbrMediaFile);
//		    
//		    try {
//		    	fos3.write(MBRByteArray2);
//		    	fos3.flush();
//				
//				// save file in gallery
//			    Intent mediaScanIntent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//			    File f = new File(mbrMediaFile.getAbsolutePath());
//			    Uri contentUri = Uri.fromFile(f);
//			    mediaScanIntent1.setData(contentUri);
//			    activity.sendBroadcast(mediaScanIntent1);
//				
//			    fos3.close();
//				Log.d(TAG, "Picture MIN-BOUND-BOX saved");
//			} catch (FileNotFoundException e) {
//				Log.d(TAG, "File not found: " + e.getMessage());
//			} catch (IOException e) {
//				Log.d(TAG, "Error accessing file: " + e.getMessage());
//			}
			
//			####################### end
	    	
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
	        
	        
//	        #################################################################################### start
	        
	        bitmapByteArray = Base64.decode(bitmapByteArray, Base64.DEFAULT);
        	foreBackByteArray = Base64.decode(foreBackByteArray, Base64.DEFAULT);
        	contourByteArray = Base64.decode(contourByteArray, Base64.DEFAULT);	
        	minBoundRectByteArray = Base64.decode(minBoundRectByteArray, Base64.DEFAULT);
	        
			// get storage directory
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"GenericClassificationApp");
			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					Log.d("GenericClassificationApp",
							"failed to create directory");
				}
			}
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
			
			
			// FORE_BACK_GROUND
		    File foreBackMediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_1_"+ timeStamp + ".jpg");
		    FileOutputStream fos1 = new FileOutputStream(foreBackMediaFile);
		    
		    try {
				fos1.write(foreBackByteArray);
				fos1.flush();
				
				// save file in gallery
			    Intent mediaScanIntent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			    File f = new File(foreBackMediaFile.getAbsolutePath());
			    Uri contentUri = Uri.fromFile(f);
			    mediaScanIntent1.setData(contentUri);
			    activity.sendBroadcast(mediaScanIntent1);
				
				fos1.close();
				Log.d(TAG, "Picture FORE-BACK-GROUND saved");
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
			
		    // OBJECT-CONTOUR
		    File contourMediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_2_"+ timeStamp + ".jpg");		    
		    FileOutputStream fos2 = new FileOutputStream(contourMediaFile);
		       
		    try {
				fos2.write(contourByteArray);
				fos2.flush();
				
				// save file in gallery
			    Intent mediaScanIntent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			    File f = new File(contourMediaFile.getAbsolutePath());
			    Uri contentUri = Uri.fromFile(f);
			    mediaScanIntent2.setData(contentUri);
			    activity.sendBroadcast(mediaScanIntent2);
				
				fos2.close();
				Log.d(TAG, "Picture OBJECT-CONTOUR saved");
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
		    
		    
		    // MIN-BOUND-BOX
		    
		    //byte array to int array
		    IntBuffer intBuf =ByteBuffer.wrap(minBoundRectByteArray).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
	        minBoundRectArray = new int[intBuf.remaining()];
	        intBuf.get(minBoundRectArray);
			
		    int countLeft = 0;
		    int countTop = 1;
		    int countRight = 2;
		    int countBottom = 3;
		    Paint mPaint = new Paint();
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(Color.BLUE);
			mPaint.setStrokeWidth(10);
			mPaint.setAntiAlias(true);
			mPaint.setDither(true);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			
		    while(countBottom < minBoundRectArray.length){
		    	RectF rectf = new RectF(minBoundRectArray[countLeft], minBoundRectArray[countTop], minBoundRectArray[countRight], minBoundRectArray[countBottom]);
		    	mbrCanvas.drawRect(rectf, mPaint);
		    	Log.d(TAG, "draw Rect to mbrCanvas: left: "+rectf.left+", top: "+rectf.top+", right: "+rectf.right+", bottom: "+rectf.bottom);
		    	countLeft += 4;
		    	countTop += 4;
		    	countRight += 4;
		    	countBottom += 4;
		    }
		    
		    ByteArrayOutputStream MBRbyteStream2 = new ByteArrayOutputStream();
		    mbrScribble.compress(Bitmap.CompressFormat.PNG, 100, MBRbyteStream2);
		    byte[] MBRByteArray2 = MBRbyteStream2.toByteArray();
		    
		    File mbrMediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_3_"+ timeStamp + ".jpg");
		    FileOutputStream fos3 = new FileOutputStream(mbrMediaFile);
		    
		    try {
		    	fos3.write(MBRByteArray2);
		    	fos3.flush();
				
				// save file in gallery
			    Intent mediaScanIntent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			    File f = new File(mbrMediaFile.getAbsolutePath());
			    Uri contentUri = Uri.fromFile(f);
			    mediaScanIntent1.setData(contentUri);
			    activity.sendBroadcast(mediaScanIntent1);
				
			    fos3.close();
				Log.d(TAG, "Picture MIN-BOUND-BOX saved");
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
        	
	        
//	        ##################################################################################### end
	        
	        
	        
	        
	        
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