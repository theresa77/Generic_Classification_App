package dialog;

import java.util.ArrayList;
import java.util.List;

import activity.UserScribbleMainActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.genericclassificationapp.R;

/**
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class TextAnnotationDialog extends DialogFragment {
	
	private ArrayList<EditText> editTextItems;
	private ListView listView;
	
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
		
		listView = (ListView) dialog.findViewById(R.id.text_annotation_list);
		editTextItems = new ArrayList<EditText>();
        for (int i = 0; i <= activity.getTextAnnotations().size(); i++) {
        	EditText item = (EditText) dialog.findViewById(R.id.edit_text_item_annotation_dialog);
        	if(i < activity.getTextAnnotations().size())
        		item.setText(activity.getTextAnnotations().get(i));
            editTextItems.add(item);
        }
        
        EditTextArrayAdapter adapter = new EditTextArrayAdapter(activity, R.drawable.edit_text_item_annotation_dialog, editTextItems); 
        listView.setAdapter(adapter);
		
//		String[] list = new String[] {"First Annotation", "Second Annotation"};
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
//                R.layout.dialog_list_text_view, list);
//       listView.setAdapter(adapter);
       
       // get ok button and set a click listener 
       ImageButton mPickButton = (ImageButton) dialog.findViewById(R.id.text_annotation_ok_button);
       mPickButton.setOnClickListener(
	            new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                	//TODO:save new Text Annotation
//	                	List<String> annotations = new ArrayList<String>();
//	                	EditTextArrayAdapter adapter = (EditTextArrayAdapter)listView.getAdapter();
//	                	
////	                	for(int i=0; i < adapter.getCount(); i++){
////	                		if(adapter.getView(i, listView, listView).getText() != null){
////	                			annotations.add(adapter.getView(i).getText().toString());
////	                		}
////	                	}
//	                	
//	                	for(EditText item : editTextItems){
//	                		String text = item.getText().toString();
//	                	}
//	                	activity.setTextAnnotations(annotations);
	                	dialog.dismiss();
	                	//TODO: make correct Toast Text
	                	Toast.makeText(activity, "Text Annotation saved", Toast.LENGTH_SHORT).show();
	                }
	            }
	        );
		
		return dialog;
	}

}
