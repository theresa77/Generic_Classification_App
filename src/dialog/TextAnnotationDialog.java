package dialog;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
	
	private static final String TAG = TextAnnotationDialog.class.getSimpleName();
	private ArrayList<EditText> editTextItems;
	private ListView listView;
	private SparseArray<String> textAnnotations;
	
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
		
		listView = (ListView) dialog.findViewById(R.id.text_annotation_list);
        EditTextAdapter adapter = new EditTextAdapter(this); 
        listView.setAdapter(adapter);
       
       // get ok button and set a click listener 
       ImageButton mPickButton = (ImageButton) dialog.findViewById(R.id.text_annotation_ok_button);
       mPickButton.setOnClickListener(
	            new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                	List<String> annotations = new ArrayList<String>();
	                	
	                	for(int i=0; i<textAnnotations.size(); i++){
	                		annotations.add(textAnnotations.get(i));
	                	}
	                	
	                	activity.setTextAnnotations(annotations);
	                	
	                	dialog.dismiss();
	                	//TODO: make correct Toast Text
	                	Toast.makeText(activity, "Text Annotation saved", Toast.LENGTH_SHORT).show();
	                }
	            }
	        );
		
		return dialog;
	}
	
	public void addTextAnnotation(int id, String annotation){
		textAnnotations.put(id, annotation);
	}

}
