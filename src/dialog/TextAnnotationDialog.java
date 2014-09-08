/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package dialog;

import java.util.ArrayList;
import java.util.List;

import activity.UserScribbleMainActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.genericclassificationapp.R;

import domain.Picture;

/**
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class TextAnnotationDialog extends DialogFragment {
	
	private static final String TAG = TextAnnotationDialog.class.getSimpleName();
	private ListView listView;
	private SparseArray<String> textAnnotations;
	private EditTextAdapter adapter;
	
	/**
	 * Called when Dialog gets created.
	 */
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final UserScribbleMainActivity activity = (UserScribbleMainActivity) getActivity();
		final Dialog dialog = new Dialog(activity);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.text_annotation_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
		textAnnotations = new SparseArray<String>();
		List<String> annotations = Picture.getInstance().getAnnotations();
		if(annotations != null && !annotations.isEmpty()) {
			int id = 0;
			for (String a: annotations){
				Log.d(TAG, "add old annotation to list: "+a);
				addTextAnnotation(id, a);
				id++;
			}
		}
		
		listView = (ListView) dialog.findViewById(R.id.text_annotation_list);
        adapter = new EditTextAdapter(this); 
        listView.setAdapter(adapter);
        
        // set height for list view of edit-text-fields
        if(calculateListViewHeight() > activity.getDisplayHeight()*0.5){
        	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
					(int)(activity.getDisplayHeight()*0.5));
        	listView.setLayoutParams(params);
        }
        
       
       // get ok button and set a click listener 
       ImageButton mPickButton = (ImageButton) dialog.findViewById(R.id.text_annotation_ok_button);
       mPickButton.setOnClickListener(
	            new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                	List<String> annotatonsList = new  ArrayList<String>();
	            		for(int i=0; i<textAnnotations.size(); i++){
	            			if(!textAnnotations.valueAt(i).equals(""))
	            				annotatonsList.add(textAnnotations.valueAt(i));
	            		}
	            		Picture.getInstance().setAnnotations(annotatonsList);
//	                	activity.setTextAnnotations(textAnnotations);
	                	dialog.dismiss();
	                	
	                	Toast.makeText(activity, R.string.annotation_saved, Toast.LENGTH_SHORT).show();
	                }
	            }
	        );
       
       // button for adding new EditText field to the list
       ImageButton mNewButton = (ImageButton) dialog.findViewById(R.id.new_text_annotation_button);
       mNewButton.setOnClickListener(
    		   new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                	adapter.addNewListItem();
	                	if(calculateListViewHeight() > activity.getDisplayHeight()*0.5){
	                    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(activity.getDisplayHeight()*0.5));
	                    	listView.setLayoutParams(params);
	                    }	
	                }
	            }
	        );
		
		return dialog;
	}
	
	/**
	 * Adds a new text annotation to the list.
	 * @param id position of EditText-field in the list
	 * @param annotation string value of the annotation
	 */
	public void addTextAnnotation(int id, String annotation){
		Log.d(TAG, "addTextAnnotation( id: "+id+", annotation: "+annotation+" ) called");
		textAnnotations.put(id, annotation);
		Log.d(TAG, textAnnotations.size()+"");
	}
	
	/**
	 * Get all text annotations.
	 * @return list of text annotations
	 */
	public SparseArray<String> getTextAnnotations(){
		return textAnnotations;
	}
	
	/**
	 * Calculates the current height of the ListView which contains the text annotations. 
	 * @return height of list of annotations
	 */
	public int calculateListViewHeight(){
		int height = 0;
        EditText item;
        for(int i=0; i<adapter.getList().size(); i++){
        	item = adapter.getItem(i);
        	item.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        	height += item.getMeasuredHeight();
        }
        return height;
	}

}
