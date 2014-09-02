/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package dialog;

import com.genericclassificationapp.R;
import http.RetrieveHttpTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import view.UserScribbleView;
import activity.CameraActivity;
import activity.PictureActivity;
import activity.UserScribbleMainActivity;
import activity.ViewObjectActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import domain.Picture;
import domain.Scribble;

/**
 * Dialog for the option menu.
 * Shows management functions save, view and delete
 * and send picture to server function. 
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class OptionMenuDialog extends DialogFragment {

	private static final String TAG = OptionMenuDialog.class.getSimpleName();
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	/**
	 * Called when Dialog gets created.
	 */
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Activity activity = getActivity();
		final Dialog dialog = new Dialog(activity);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.option_menu_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Log.d(TAG, "show OptionMenuDialog");
		
		ListView listView = (ListView) dialog.findViewById(R.id.option_menu_list);

		String[] list;
		if (activity instanceof PictureActivity) {
			 list = this.getResources().getStringArray(
					R.array.option_menu_picture_activity);
		} else {
			list = this.getResources().getStringArray(
					R.array.option_menu_user_scribble);
		}
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                 R.layout.dialog_list_text_view, list);
        listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent newIntent;
				Picture mPicture = Picture.getInstance();
				
				switch (position) {
				
				//save picture (+ scribbles) to Gallery
				case 0:
					Log.d(TAG, "save to Gallery selected");
					
					// get storage directory
					File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				              Environment.DIRECTORY_PICTURES), "GenericClassificationApp");
				    
				    // Create the storage directory if it does not exist
				    if (! mediaStorageDir.exists()){
				        if (! mediaStorageDir.mkdirs()){
				            Log.d("GenericClassificationApp", "failed to create directory");
				        }
				    }
				    
				    // Create a media file name
				    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
				    File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
				        "IMG_"+ timeStamp + ".jpg");
				    
				    Bitmap bitmap = mPicture.getBitmap();;		                       
				    
				    // add scribbles to the bitmap
				    if(activity instanceof UserScribbleMainActivity){
				    	
//				    	bitmap = mPicture.getBitmap();
				    	
				    	double width = 0.0;
				    	double height = 0.0;
					    
					    LinearLayout.LayoutParams pictureParams;
					
						if (!mPicture.isLandscape()) {
							height = ((UserScribbleMainActivity)activity).getDisplayWidth() * 1.33;
					    	width = ((UserScribbleMainActivity)activity).getDisplayWidth();
				    	} else {
				    		width = ((UserScribbleMainActivity)activity).getDisplayHeight() * 1.33;
					    	height = ((UserScribbleMainActivity)activity).getDisplayHeight();				
				    	}
						
					    pictureParams = new LinearLayout.LayoutParams((int)width, (int)height);
						bitmap = Bitmap.createScaledBitmap(bitmap, pictureParams.width, pictureParams.height, false);

						// draw picture and scribbles to new canvas
						Canvas canvas = new Canvas(bitmap);
						UserScribbleView currView = ((UserScribbleMainActivity) activity).getView();
						currView.draw(canvas);
					
					} 
//				    else {
//				    	bitmap = mPicture.getBitmap();
//				    }
				    
				    ByteArrayOutputStream stream = new ByteArrayOutputStream();	        	
				    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				    
				    try {
						FileOutputStream fos = new FileOutputStream(mediaFile);
						fos.write(stream.toByteArray());
						fos.flush();
						fos.close();
						Log.d(TAG, "Picture saved");
					} catch (FileNotFoundException e) {
						Log.d(TAG, "File not found: " + e.getMessage());
					} catch (IOException e) {
						Log.d(TAG, "Error accessing file: " + e.getMessage());
					}
			    
				    // save file in gallery
				    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				    File f = new File(mediaFile.getAbsolutePath());
				    Uri contentUri = Uri.fromFile(f);
				    mediaScanIntent.setData(contentUri);
				    activity.sendBroadcast(mediaScanIntent);
					
					dialog.dismiss();
					Toast.makeText(activity, R.string.save_picture, Toast.LENGTH_SHORT).show();
					break;
				
				//delete picture, go back to camera activity
				case 1:
					newIntent = new Intent(activity, CameraActivity.class);
					startActivity(newIntent);
					activity.finish();
					Toast.makeText(activity, R.string.delete_picture, Toast.LENGTH_SHORT).show();
					break;
					
				//view picture, go to Picture Activity
				case 2:
					newIntent = new Intent(activity, PictureActivity.class);
					startActivity(newIntent);
					activity.finish();
					break;
				
				//view object with scribbles
				case 3:
					((UserScribbleMainActivity)activity).addFurtherScribble(null);
					
					if(Picture.getInstance().getScribbles() != null && !Picture.getInstance().getScribbles().isEmpty()){
						newIntent = new Intent(activity, ViewObjectActivity.class);
						startActivity(newIntent);
						activity.finish();
					} else {
						Toast.makeText(activity, R.string.mark_object, Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
					
					break;
				
				//take new photo, go to CameraActivity
				case 4:
					newIntent = new Intent(activity, CameraActivity.class);
					startActivity(newIntent);
					activity.finish();
					break;
				
				//add Text Annotation	
				case 5:
					dialog.dismiss();
					((UserScribbleMainActivity)activity).openTextAnnotationDialog();
					break;
					
				//delete last drawing
				case 6:
					((UserScribbleMainActivity)activity).removeLastScribble(null);
					dialog.dismiss();
					Toast.makeText(activity, R.string.delete_drawing, Toast.LENGTH_SHORT).show();
					break;
					
				//delete all drawings	
				case 7:
					((UserScribbleMainActivity)activity).removeAllScribbles();
					dialog.dismiss();
					Toast.makeText(activity, R.string.delete_drawing, Toast.LENGTH_SHORT).show();
					break;
				
				//send picture and scribbles to server
				case 8:
					Log.d(TAG, "sendToServer called");
//					// create new bitmap for scribbles only
//					Bitmap userScribble = Bitmap.createBitmap( ((UserScribbleMainActivity)activity).getDisplayWidth(),
//												((UserScribbleMainActivity)activity).getDisplayHeight(), 
//												Bitmap.Config.ARGB_8888); 
//					Canvas canvas = new Canvas(userScribble);
//					UserScribbleView currView = ((UserScribbleMainActivity) activity).getView();
//					
//					// draw scribbles at new canvas object
//					currView.drawUserScribble(canvas);
//					
//					ByteArrayOutputStream byteStream = new ByteArrayOutputStream();	        	
//					userScribble.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
					
					dialog.dismiss();
					
					// open Dialog to show progress of the transmission from picture and scribbles to a server
					TransmissionToServerDialog transDialog = new TransmissionToServerDialog();
					transDialog.show(getActivity().getSupportFragmentManager(), "TransmissionToServerDialog");
					
//					Scribble[] scribbles = (Scribble[]) ((UserScribbleMainActivity)activity).getScribbles().toArray();
					Scribble[] scribbles = new Scribble[mPicture.getScribbles().size()];
					mPicture.getScribbles().toArray(scribbles);
					
					// send picture and scribbles to a server
//					new RetrieveHttpTask(((UserScribbleMainActivity)activity).getCurrentScribble().name(), transDialog).execute(byteStream.toByteArray());
					new RetrieveHttpTask((UserScribbleMainActivity)activity, transDialog).execute(scribbles);	
					break;
				}
			}	
		});

		return dialog;
	}
	
}
